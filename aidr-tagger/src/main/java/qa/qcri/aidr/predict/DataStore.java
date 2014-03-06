package qa.qcri.aidr.predict;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONObject;

import qa.qcri.aidr.predict.classification.nominal.Model;
import qa.qcri.aidr.predict.classification.nominal.NominalLabelBC;
import qa.qcri.aidr.predict.classification.nominal.ModelNominalLabelPerformance;
import qa.qcri.aidr.predict.common.Config;
import qa.qcri.aidr.predict.common.Helpers;
import qa.qcri.aidr.predict.common.Loggable;
import qa.qcri.aidr.predict.data.DocumentJSONConverter;
import qa.qcri.aidr.predict.data.Document;
import qa.qcri.aidr.predict.dbentities.ModelFamilyEC;
import qa.qcri.aidr.predict.dbentities.NominalAttributeEC;
import qa.qcri.aidr.predict.dbentities.NominalLabelEC;
import qa.qcri.aidr.predict.featureextraction.WordSet;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import snaq.db.ConnectionPool;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.SparseInstance;

/**
 * Wrapper class for database communication (both MySQL and Redis).
 *
 * @author jrogstadius
 */
public class DataStore extends Loggable {

    /*
     * TODO: Rename all database columns and tables to use underscore_notation.
     * Everything was initially created with camelCaseNotation, but apparently
     * MySQL has a configuration where everything is forced into lowercase on
     * database creation. This resulted in that all queries broke when when
     * moving the code to the production server, so the current status is that
     * the naming is... ugly.
     */
    static class TrainingSampleNotification {

        public int crisisID;
        public Collection<Integer> attributeIDs;

        public TrainingSampleNotification(int crisisID,
                Collection<Integer> attributeIDs) {
            this.crisisID = crisisID;
            this.attributeIDs = attributeIDs;
        }
    }
    static JedisPool jedisPool;
    static ConnectionPool mySqlPool;

    /* REDIS */
    public static Jedis getJedisConnection() {
        try {
            if (jedisPool == null) {
                jedisPool = new JedisPool(new JedisPoolConfig(),
                        Config.REDIS_HOST);
            }
            return jedisPool.getResource();
        } catch (Exception e) {
            System.out
                    .println("Could not establish Redis connection. Is the Redis server running?");
            throw e;
        }
    }

    public static void close(Jedis resource) {
        jedisPool.returnResource(resource);
    }

    public static void clearRedisPipeline() {
        Jedis redis = getJedisConnection();
        redis.del(Config.REDIS_FOR_CLASSIFICATION_QUEUE);
        redis.del(Config.REDIS_FOR_EXTRACTION_QUEUE);
        redis.del(Config.REDIS_FOR_OUTPUT_QUEUE);
        redis.del(Config.REDIS_LABEL_TASK_WRITE_QUEUE);
        redis.del(Config.REDIS_TRAINING_SAMPLE_INFO_QUEUE);
        close(redis);
    }
    public static final int MODEL_ID_ERROR = -1;

    /* MYSQL */
    static void initializeMySqlPool() throws SQLException {
        try {
            Class<?> c = Class.forName("com.mysql.jdbc.Driver");
            Driver driver = (Driver) c.newInstance();
            DriverManager.registerDriver(driver);

            mySqlPool = new ConnectionPool("aidr-backend",
                    10, // min-pool default = 1
                    20, // max-pool default = 5
                    35, // max-size default 30
                    180000, // timeout (ms)
                    Config.MYSQL_PATH, Config.MYSQL_USERNAME,
                    Config.MYSQL_PASSWORD);
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            log("DataStore", "Exception when initializing MySQL connection", e);
        }
    }

    public static Connection getMySqlConnection() throws SQLException {
        if (mySqlPool == null) {
            initializeMySqlPool();
        }

        long timeout = 3000;
        Connection con = mySqlPool.getConnection(timeout);
        if (con == null) {
            log(LogLevel.ERROR, "DataStore",
                    "The created MySQL connection is null");
        }
        return con;
    }

    public static void close(Connection con) {
        if (con == null) {
            return;
        }
        try {
            con.close();
        } catch (SQLException e) {
            log("DataStore", "Exception when returning MySQL connection", e);
        }
    }

    public static void close(Statement statement) {
        if (statement == null) {
            return;
        }
        try {
            statement.close();
        } catch (SQLException e) {
            log("DataStore", "Could not close statement", e);
        }
    }

    public static void close(ResultSet resultset) {
        if (resultset == null) {
            return;
        }
        try {
            resultset.close();
        } catch (SQLException e) {
            log("DataStore", "Could not close statement", e);
        }
    }

    public static Integer getNullLabelID(int attributeID) {
        String sql = "select nominalLabelID from nominal_label where nominalAttributeID="
                + attributeID + " and nominalLabelCode='null'";
        Connection conn = null;
        try {
            conn = getMySqlConnection();
            PreparedStatement query = conn.prepareStatement(sql);
            ResultSet result = query.executeQuery();
            if (result.next()) {
                return result.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DataStore.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            close(conn);
        }
        return null;
    }

    public static Instances getTrainingSet(int crisisID, int attributeID)
            throws Exception {
        ArrayList<String[]> wordVectors = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<>();
        String sql = "SELECT wordFeatures, nominalLabelID FROM nominal_label_training_data WHERE crisisID = "
                + crisisID + " AND nominalAttributeID = " + attributeID;

        getLabeledSet(sql, wordVectors, labels);

        return createInstances(wordVectors, labels);
    }

    public static Instances getEvaluationSet(int crisisID, int attributeID,
            Instances trainingData) throws Exception {
        ArrayList<String[]> wordVectors = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<>();
        String sql = "SELECT wordFeatures, nominalLabelID FROM nominal_label_evaluation_data WHERE crisisID = "
                + crisisID + " AND nominalAttributeID = " + attributeID;

        getLabeledSet(sql, wordVectors, labels);

        return createFormattedInstances(trainingData, wordVectors, labels);
    }

    static void getLabeledSet(String sql, ArrayList<String[]> wordVectors,
            ArrayList<String> labels) {
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet result = null;
        String wordFeatures = null;
        try {
            conn = getMySqlConnection();
            statement = conn.prepareStatement(sql);
            result = statement.executeQuery();
            while (result.next()) {
                //Weka class attributes only accept string values, hence the toString
                labels.add(Integer.toString(result.getInt("nominalLabelID")));
                wordFeatures = result.getString("wordFeatures");
                JSONObject wordsJson = new JSONObject(
                        Helpers.unescapeJson(wordFeatures));
                wordVectors.add(Helpers.toStringArray(wordsJson
                        .getJSONArray("words")));
            }
        } catch (SQLException e) {
            log("DataStore", "Exception while fetching dataset", e);
        } catch (Exception e) {
            log("DataStore", "Exception while fetching dataset", e);
        } finally {
            close(result);
            close(statement);
            close(conn);
        }
    }

    static Instances createInstances(ArrayList<String[]> wordVectors,
            ArrayList<String> labels) throws Exception {
        if (wordVectors.size() != labels.size()) {
            throw new Exception();
        }

        // Build a dictionary based on words in the documents, and transform
        // documents into word vectors
        HashSet<String> uniqueWords = new HashSet<String>();
        for (String[] words : wordVectors) {
            uniqueWords.addAll(Arrays.asList(words));
        }

        // Create attributes based on the dictionary
        ArrayList<Attribute> attributes = new ArrayList<Attribute>();
        for (String word : uniqueWords) {
            attributes.add(new Attribute(word));
        }

        // Make class attribute
        HashSet<String> uniqueLabels = new HashSet<String>(labels);
        ArrayList<String> uniqueLabelsList = new ArrayList<String>(uniqueLabels);
        Attribute classAttribute = new Attribute("___aidrclass___",
                uniqueLabelsList);
        attributes.add(classAttribute);

        // Create the dataset
        Instances instances = new Instances("data", attributes,
                wordVectors.size());
        double[] missingVal = new double[attributes.size()];
        instances.setClass(classAttribute);

        // Add each document as an instance
        for (int i = 0; i < wordVectors.size(); i++) {

            Instance item = new SparseInstance(instances.numAttributes());
            item.setDataset(instances);

            for (String word : wordVectors.get(i)) {
                Attribute attribute = instances.attribute(word);
                if (attribute != null) {
                    item.setValue(attribute, 1);
                }
            }

            item.setValue(classAttribute, labels.get(i));
            item.replaceMissingValues(missingVal);
            instances.add(item);
        }

        return instances;
    }

    static Instances createFormattedInstances(Instances headerSet,
            ArrayList<String[]> wordVectors, ArrayList<String> labels)
            throws Exception {

        if (wordVectors.size() != labels.size()) {
            throw new Exception();
        }

        // Build a dictionary based on words in the documents, and transform
        // documents into word vectors
        HashSet<String> uniqueWords = new HashSet<String>();
        for (String[] words : wordVectors) {
            uniqueWords.addAll(Arrays.asList(words));
        }

        // Create the dataset
        Instances instances = new Instances(headerSet, wordVectors.size());
        double[] missingVal = new double[headerSet.numAttributes()];

        // Set class index
        instances.setClassIndex(headerSet.numAttributes() - 1);
        Attribute classAttribute = instances.classAttribute();

        // Get valid class labels
        HashSet<String> classValues = new HashSet<String>();
        Enumeration<?> classEnum = classAttribute.enumerateValues();
        while (classEnum.hasMoreElements()) {
            classValues.add((String) classEnum.nextElement());
        }

        // Add each document as an instance
        for (int i = 0; i < wordVectors.size(); i++) {

            if (!classValues.contains(labels.get(i))) {
                log(LogLevel.WARNING, "DataStore",
                        "New class label found in evaluation set. Discarding value.");
                continue;
                /*
                 * TODO: Handle unseen labels in a better way, as this will
                 * over-estimate classification performance. Adding new values
                 * to class attributes requires recreation of the header and
                 * copying of all data to a new Instances. See:
                 * http://comments.gmane.org/gmane.comp.ai.weka/7806
                 */
            }

            Instance item = new DenseInstance(instances.numAttributes());
            item.setDataset(instances);
            // Words
            for (String word : wordVectors.get(i)) {
                Attribute attribute = instances.attribute(word);
                if (attribute != null) {
                    item.setValue(attribute, 1);
                }
            }

            item.setValue(classAttribute, labels.get(i));
            item.replaceMissingValues(missingVal);
            instances.add(item);
        }

        return instances;
    }

    public static void saveDocumentToDatabase(Document item) {
        List<Document> wrapper = new ArrayList<Document>();
        wrapper.add(item);
        saveDocumentsToDatabase(wrapper);
    }

    public static void saveDocumentsToDatabase(List<Document> items) {
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet generatedDocID = null;

        try {
            // Insert document
            conn = getMySqlConnection();

            for (Document item : items) {
                String wordFeatures = DocumentJSONConverter.getFeaturesJson(
                        WordSet.class, item);
                String geoFeatures = null; // TODO: Add geotag support

                statement = conn
                        .prepareStatement(
                        "INSERT INTO document (crisisID, hasHumanLabels, receivedAt, language, sourceIP, doctype, data, wordFeatures, geoFeatures, valueAsTrainingSample) VALUES (?,?,?,?,INET_ATON(?),?,?,?,?,?)",
                        Statement.RETURN_GENERATED_KEYS);
                statement.setInt(1, item.getCrisisID());
                statement.setInt(2, 0);
                statement.setTimestamp(3, new java.sql.Timestamp(
                        java.util.Calendar.getInstance().getTimeInMillis()));
                statement.setString(4, item.getLanguage());
                statement.setString(5, item.getSourceIP().getHostAddress());
                statement.setString(6, item.getClass().getSimpleName()
                        .toString());
                statement.setString(7,
                        Helpers.escapeJson(item.getInputJson().toString()));
                statement.setString(8, Helpers.escapeJson(wordFeatures));
                statement.setString(9, geoFeatures);
                statement.setDouble(10, item.getValueAsTrainingSample());

                statement.execute();
                generatedDocID = statement.getGeneratedKeys();
                generatedDocID.next();
                item.setDocumentID(generatedDocID.getInt(1));

                generatedDocID.close();
                statement.close();
            }
        } catch (SQLException e) {
            log("DataStore",
                    "Exception when attempting to write Document to database",
                    e);
        } finally {
            close(generatedDocID);
            close(statement);
            close(conn);
        }

        saveHumanLabels(items);
    }

    /**
     * Saves human-provided labels for a document and sends a notification via a
     * redis queue to the model controller.
     *
     * @param documents A list of human-annotated documents.
     */
    static void saveHumanLabels(List<Document> documents) {

        Connection conn = null;
        PreparedStatement statement = null;
        try {
            String insertSql = "INSERT INTO document_nominal_label (documentID, nominalLabelID) VALUES (?,?)";
            conn = getMySqlConnection();
            statement = conn.prepareStatement(insertSql);

            ArrayList<Integer> docsWithLabels = new ArrayList<>();
            ArrayList<TrainingSampleNotification> notifications = new ArrayList<>();

            int rows = 0;
            for (Document doc : documents) {
                List<NominalLabelBC> labels = doc
                        .getHumanLabels(NominalLabelBC.class);

                if (labels.isEmpty()) // Skip document if it has no
                // human-provided labels
                {
                    continue;
                }

                docsWithLabels.add(doc.getDocumentID());
                for (NominalLabelBC label : labels) {
                    statement.setInt(1, doc.getDocumentID());
                    statement.setInt(2, label.getNominalLabelID());

                    statement.execute();
                    rows++;
                }

                notifications.add(new TrainingSampleNotification(doc
                        .getCrisisID(), getAttributeIDs(labels)));
            }

            if (rows == 0) {
                return;
            }

            log(LogLevel.INFO, "DataStore", "Saved " + rows
                    + " human labels for " + docsWithLabels.size()
                    + " documents");
            statement
                    .executeUpdate("UPDATE document SET hasHumanLabels=1 WHERE documentID IN ("
                    + Helpers.join(docsWithLabels, ",") + ")");

            sendNewLabeledDocumentNotification(notifications);

        } catch (SQLException e) {
            log("DataStore",
                    "Exception when attempting to insert new document labels",
                    e);
        } finally {
            close(statement);
            close(conn);
        }
    }

    private static Collection<Integer> getAttributeIDs(List<NominalLabelBC> labels) {
        HashSet<Integer> ids = new HashSet<Integer>();
        for (NominalLabelBC l : labels) {
            ids.add(l.getAttributeID());
        }
        return ids;
    }

    public static void sendNewLabeledDocumentNotification(
            Collection<TrainingSampleNotification> notifications) {
        Jedis redis = DataStore.getJedisConnection();

        for (TrainingSampleNotification n : notifications) {
            String message = "{ \"crisis_id\": " + n.crisisID
                    + ", \"attributes\": [" + Helpers.join(n.attributeIDs, ",")
                    + "] }";
            redis.rpush(Config.REDIS_TRAINING_SAMPLE_INFO_QUEUE, message);
        }

        DataStore.close(redis);
    }

    public static ArrayList<ModelFamilyEC> getActiveModels() {
        ArrayList<ModelFamilyEC> modelFamilies = new ArrayList<>();
        Connection conn = null;
        PreparedStatement sql = null;
        ResultSet result = null;

        try {
            conn = getMySqlConnection();

            Statement sql2 = conn.createStatement();
            sql2.execute("SET group_concat_max_len = 10240");
            sql2.close();

            sql = conn
                    .prepareStatement(
                    "SELECT \n"
                    + " fam.modelFamilyID, \n"
                    + "	fam.crisisID, \n"
                    + "	crisis.code AS crisisCode, \n"
                    + "	crisis.name AS crisisName, \n"
                    + "	fam.nominalAttributeID, \n"
                    + "	attr.code AS nominalAttributeCode, \n"
                    + "	attr.name AS nominalAttributeName, \n"
                    + "	attr.description AS nominalAttributeDescription, \n"
                    + " mdl.modelID, \n"
                    + "	lbl.nominalLabelID,\n"
                    + "	lbl.nominalLabelCode,\n"
                    + "	lbl.name as nominalLabelName,\n"
                    + "	lbl.description as nominLabelDescription, \n"
                    + "	COUNT(DISTINCT dnl.documentID) AS labeledItemCount\n"
                    + "FROM model_family fam \n"
                    + "LEFT JOIN model mdl on mdl.modelFamilyID = fam.modelFamilyID \n"
                    + "JOIN crisis on crisis.crisisID = fam.crisisID \n"
                    + "JOIN nominal_attribute attr ON attr.nominalAttributeID = fam.nominalAttributeID \n"
                    + "JOIN nominal_label lbl ON lbl.nominalAttributeID = fam.nominalAttributeID \n"
                    + "LEFT JOIN document doc ON doc.crisisID=fam.crisisID \n"
                    + "LEFT JOIN document_nominal_label dnl ON dnl.documentID=doc.documentID AND dnl.nominalLabelID=lbl.nominalLabelID \n"
                    + "WHERE fam.isActive AND (mdl.modelID IS NULL OR mdl.isCurrentModel) \n"
                    + "GROUP BY crisisID, nominalAttributeID, nominalLabelID ");
            result = sql.executeQuery();

            ModelFamilyEC family = null;
            NominalAttributeEC attribute = null;
            HashMap<ModelFamilyEC, Integer> familyLabelCount = new HashMap<>();
            while (result.next()) {
                if (family == null || family.getModelFamilyID() != result.getInt("modelFamilyID")) {

                    //create attribute
                    attribute = new NominalAttributeEC();
                    attribute.setNominalAttributeID(result.getInt("nominalAttributeID"));
                    attribute.setCode(result.getString("nominalAttributeCode"));
                    attribute.setDescription(result.getString("nominalAttributeDescription"));
                    attribute.setName(result.getString("nominalAttributeName"));

                    //create model family
                    family = new ModelFamilyEC();
                    family.setCrisisID(result.getInt("crisisID"));
                    int tmpModelID = result.getInt("modelID");
                    if (!result.wasNull()) {
                        family.setCurrentModelID(tmpModelID);
                    }
                    family.setIsActive(true);
                    family.setModelFamilyID(result.getInt("modelFamilyID"));
                    family.setNominalAttribute(attribute);

                    familyLabelCount.put(family, 0);
                    modelFamilies.add(family);
                }

                //create label
                NominalLabelEC label = new NominalLabelEC();
                label.setDescription(result.getString("nominLabelDescription"));
                label.setName(result.getString("nominalLabelName"));
                label.setNominalAttribute(attribute);
                label.setNominalLabelCode(result.getString("nominalLabelCode"));
                label.setNominalLabelID(result.getInt("nominalLabelID"));
                attribute.addNominalLabel(label);

                int count = familyLabelCount.get(family);
                familyLabelCount.put(family, count + result.getInt("labeledItemCount"));

            }

            //sum training sample counts per attribute
            for (Map.Entry<ModelFamilyEC, Integer> entry : familyLabelCount.entrySet()) {
                entry.getKey().setTrainingExampleCount(entry.getValue());
            }

        } catch (SQLException e) {
            log("DataStore", "Exception when getting model state", e);
        } finally {
            close(result);
            close(sql);
            close(conn);
        }

        return modelFamilies;
    }

    public static void deleteModel(int modelID) {
        Connection conn = null;
        PreparedStatement sql = null;

        try {
            conn = getMySqlConnection();
            sql = conn.prepareStatement("DELETE FROM model WHERE modelID=" + modelID);
            sql.executeUpdate();
        } catch (SQLException e) {
            log("DataStore", "Exception while deleting model", e);
        } finally {
            close(sql);
            close(conn);
        }
    }

    public static HashMap<String, Integer> getCrisisIDs() {
        HashMap<String, Integer> crisisIDs = new HashMap<String, Integer>();
        Connection conn = null;
        PreparedStatement sql = null;
        ResultSet result = null;

        try {
            conn = getMySqlConnection();
            sql = conn.prepareStatement("select crisisID, code from crisis;");
            result = sql.executeQuery();
            while (result.next()) {
                crisisIDs.put(result.getString("code"), result.getInt("crisisID"));
            }
        } catch (SQLException e) {
            log("DataStore", "Exception when getting crisis IDs", e);
        } finally {
            close(result);
            close(sql);
            close(conn);
        }

        return crisisIDs;
    }

    public static void truncateLabelingTaskBuffer(int maxLength) {
        if (maxLength < 0) {
            throw new RuntimeException(
                    "Cannot truncate the labeling task buffer to a negative length");
        }

        Connection conn = null;
        PreparedStatement sql = null;

        try {
            conn = getMySqlConnection();
            sql = conn
                    .prepareStatement("DELETE document.* FROM "
                    + "document LEFT JOIN "
                    + "("
                    + "	SELECT * FROM ("
                    + "	SELECT"
                    + "		taskbuffer.*,"
                    + "		@r:=if(@g=crisisID, @r+1, 0) r,"
                    + "		@g:=crisisID g"
                    + "	FROM"
                    + "		(SELECT @g:=-1, @r:=-1) initvars,"
                    + "		(SELECT D.documentID, D.crisisID FROM document D "		// replacement for task_buffer view: @koushik
                    + "		left join task_assignment ASG on (ASG.documentID = D.documentID) "
                    + "		where (not D.hasHumanLabels) group by D.documentID "
                    + "		order by D.crisisID, D.valueAsTrainingSample desc, D.documentID desc"
                    + "		) taskbuffer"
                    + "	) T"
                    + "	HAVING r < "
                    + Config.LABELING_TASK_BUFFER_MAX_LENGTH
                    + ") keptIDs ON keptIDs.documentID=document.documentID "
                    + "WHERE NOT hasHumanLabels AND keptIDs.documentID IS NULL");
            sql.execute();
        } catch (SQLException e) {
            log("DataStore",
                    "Exception when attempting to truncate the labeling task buffer",
                    e);
        } finally {
            close(sql);
            close(conn);
        }
    }

    public static int saveModelToDatabase(int crisisID, int nominalAttributeID,
            Model model) {
        int modelID = MODEL_ID_ERROR;
        Connection conn = null;
        PreparedStatement modelInsert = null, mfUpdate = null;
        ResultSet result = null;
        NumberFormat format = NumberFormat.getNumberInstance(Locale.US);

        String selectModelFamilyID = "(SELECT modelFamilyID FROM model_family WHERE crisisID = "
                + crisisID + " and nominalAttributeID = " + nominalAttributeID + ")";

        try {
            // Insert the model object
            conn = getMySqlConnection();
            System.out.println("AUC: " + model.getMeanAuc());
            System.out.println("AUC formatted: " + format.format(model.getMeanAuc()));
            String modelInsertSql =
                    "INSERT INTO model (modelFamilyID, avgPrecision, avgRecall, avgAuc, trainingCount, trainingTime) VALUES "
                    + "(" + selectModelFamilyID + ", "
                    + format.format(model.getMeanPrecision())
                    + ", "
                    + format.format(model.getMeanRecall())
                    + ", "
                    + format.format(model.getMeanAuc())
                    + ", "
                    + model.getTrainingSampleCount()
                    + ", UTC_TIMESTAMP())";
            modelInsert = conn.prepareStatement(modelInsertSql, Statement.RETURN_GENERATED_KEYS);
            modelInsert.executeUpdate();

            //Get modelID of newly inserted model
            Statement getIDStatement = conn.createStatement();
            ResultSet getIDResult = getIDStatement.executeQuery("SELECT LAST_INSERT_ID()");
            getIDResult.next();
            modelID = getIDResult.getInt(1);
            getIDStatement.close();

            //result = modelInsert.getGeneratedKeys();
            //result.next();
            //modelID = result.getInt(1);
            System.out.println("Inserted a new model with model ID " + modelID); //TODO: remove

            //Insert per-label classification performance of this model 
            List<ModelNominalLabelPerformance> labelPerformaceList = model.getLabelPerformanceList();
            for (ModelNominalLabelPerformance perf : labelPerformaceList) {
                String perfInsertSql =
                        "INSERT INTO model_nominal_label (`modelID`, `nominalLabelID`, `labelPrecision`, `labelRecall`, `labelAuc`, `classifiedDocumentCount`) VALUES "
                        + "("
                        + modelID + ","
                        + perf.getNominalLabelID() + ","
                        + format.format(perf.getPrecision()) + ","
                        + format.format(perf.getRecall()) + ","
                        + format.format(perf.getAuc()) + ","
                        + "0)";
                PreparedStatement modelLabelPerfInsert = conn.prepareStatement(perfInsertSql);

                modelLabelPerfInsert.executeUpdate();
            }

            // Set the the new model as the active model of its model family
            mfUpdate = conn
                    .prepareStatement("UPDATE model SET isCurrentModel = (modelID = " + modelID + ") "
                    + "WHERE modelID = " + modelID + " OR (isCurrentModel AND modelFamilyID = "
                    + selectModelFamilyID + ")");
            mfUpdate.executeUpdate();
        } catch (SQLException e) {
            log("DataStore", "Exception while saving model to database", e);
        } finally {
            close(result);
            close(modelInsert);
            close(mfUpdate);
            close(conn);
        }

        return modelID;
    }

    /**
     * @return The added value of getting one more training sample of a given
     * label. Calculated as 1-p(label).
     */
    public static HashMap<Integer, HashMap<Integer, Double>> getNominalLabelTrainingValues() {
        //<attributeID,<labelID, trainingValueWeight>>
        HashMap<Integer, HashMap<Integer, Double>> scores = new HashMap<>();
        Connection conn = null;
        PreparedStatement sql = null;
        ResultSet result = null;

        try {
            conn = getMySqlConnection();
            sql = conn
                    .prepareStatement("select nl.nominalAttributeID, nl.nominalLabelID, 1-coalesce(count(dnl.nominalLabelID)/totalCount, 0.5) as weight \n"
                    + "from nominal_label nl \n"
                    + "left join document_nominal_label dnl on dnl.nominalLabelID=nl.nominalLabelID \n"
                    + "left join (select nominalAttributeID, greatest(count(*),1) as totalCount \n"
                    + "	from document_nominal_label natural join nominal_label group by 1) lc on lc.nominalAttributeID=nl.nominalAttributeID \n"
                    + "group by 1,2");
            result = sql.executeQuery();
            while (result.next()) {
                int attrID = result.getInt("nominalAttributeID");
                int labelID = result.getInt("nominalLabelID");
                double weight = result.getDouble("weight");

                if (!scores.containsKey(attrID)) {
                    scores.put(attrID, new HashMap<Integer, Double>());
                }
                scores.get(attrID).put(labelID, weight);
            }
        } catch (SQLException e) {
            log("DataStore",
                    "Exception when getting nominal label training values", e);
        } finally {
            close(result);
            close(sql);
            close(conn);
        }

        return scores;
    }

    public static void saveClassifiedDocumentCounts(HashMap<Integer, HashMap<Integer, Integer>> data) {
        Connection conn = null;
        PreparedStatement statement = null;

        try {
            // Insert document
            conn = getMySqlConnection();
            for (Map.Entry<Integer, HashMap<Integer, Integer>> modelDocCounts : data.entrySet()) {
                int modelID = modelDocCounts.getKey();
                for (Map.Entry<Integer, Integer> labelDocCount : modelDocCounts.getValue().entrySet()) {
                    Integer labelID = labelDocCount.getKey();
                    Integer docCount = labelDocCount.getValue();

                    statement = conn
                            .prepareStatement(
                            "INSERT INTO model_nominal_label (modelID, classifiedDocumentCount, nominalLabelID) values (" + modelID + "," + docCount + ",'" + labelID + "') "
                            + "ON DUPLICATE KEY UPDATE classifiedDocumentCount=classifiedDocumentCount+values(classifiedDocumentCount)");
                    statement.executeUpdate();

                    statement.close();
                }
            }
        } catch (SQLException e) {
            log("DataStore",
                    "Exception when attempting to write ClassifiedDocumentCount to database",
                    e);
        } finally {
            close(statement);
            close(conn);
        }
    }
}
