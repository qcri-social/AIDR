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
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import qa.qcri.aidr.dbmanager.dto.DocumentDTO;
import qa.qcri.aidr.dbmanager.dto.DocumentNominalLabelDTO;
import qa.qcri.aidr.dbmanager.dto.DocumentNominalLabelIdDTO;
import qa.qcri.aidr.dbmanager.ejb.remote.facade.TaskManagerRemote;
import qa.qcri.aidr.predict.classification.nominal.Model;
import qa.qcri.aidr.predict.classification.nominal.ModelNominalLabelPerformance;
import qa.qcri.aidr.predict.classification.nominal.NominalLabelBC;
import qa.qcri.aidr.predict.common.Helpers;
import qa.qcri.aidr.predict.common.TaggerConfigurationProperty;
import qa.qcri.aidr.predict.common.TaggerConfigurator;
import qa.qcri.aidr.predict.common.TaggerErrorLog;
import qa.qcri.aidr.predict.data.Document;
import qa.qcri.aidr.predict.dbentities.ModelFamilyEC;
import qa.qcri.aidr.predict.dbentities.NominalAttributeEC;
import qa.qcri.aidr.predict.dbentities.NominalLabelEC;
import qa.qcri.aidr.predict.dbentities.TaggerDocument;
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
 * @author koushik
 */
public class DataStore {

	public static TaskManagerRemote<DocumentDTO, Long> taskManager = null;

	private static Logger logger = Logger.getLogger(DataStore.class);

	private static final String remoteEJBJNDIName = TaggerConfigurator
			.getInstance().getProperty(
					TaggerConfigurationProperty.REMOTE_TASK_MANAGER_JNDI_NAME);

	private static final long LOG_INTERVAL = Integer
			.parseInt(TaggerConfigurator.getInstance().getProperty(
					TaggerConfigurationProperty.LOG_INTERVAL_MINUTES)) * 60 * 1000;
	private static int saveNewDocumentsCount = 0;
	private static long lastSaveTime = 0;

	private static JedisPool jedisPool = null;
	private static ConnectionPool mySqlPool = null;
	private static int attempts = 0;
	private static final int MAX_RECREATE_POOL_ATTEMPTS = 3;
	private static Object lockObject = new Object();
	
	private static HashMap<Integer,NominalAttributeEC> attLabels = new HashMap<Integer,NominalAttributeEC>();

	public static synchronized void initializeJedisPool() throws Exception {
		if (null == jedisPool) {
			jedisPool = new JedisPool(new JedisPoolConfig(), TaggerConfigurator
					.getInstance().getProperty(
							TaggerConfigurationProperty.REDIS_HOST));
			logger.info("Initialized jedisPool = " + jedisPool);
		} else {
			logger.warn("Attempting to initialize an active JedisPool!");
		}
	}

	public static void initDBPools() {
		try {
			initializeJedisPool();
		} catch (Exception e1) {
			logger.error("Unable to allocate JEDIS Pool!");
			logger.error("Exception", e1);
			TaggerErrorLog.sendErrorMail("Redis", "Could not establish Redis connection. " + e1.getMessage());
		}
		try {
			initializeMySqlPool();
		} catch (Exception e) {
			logger.error("Unable to allocate MySQL Pool!");
			logger.error("Exception", e);
			TaggerErrorLog.sendErrorMail("Mysql connection", "Could not initialize mysql connection. " + e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	public static void initTaskManager() {
		if (taskManager != null) {
			logger.warn("taskManager has already been initialized: " + taskManager
					+ ". Hence, skipping taskManager initialization attempt...");
			return;
		}

		// Else initialize taskManager
		try {
			long startTime = System.currentTimeMillis();
			//Properties props = new Properties();
			//props.setProperty("java.naming.factory.initial", "com.sun.enterprise.naming.SerialInitContextFactory");
			//props.setProperty("java.naming.factory, url.pkgs", "com.sun.enterprise.naming");
			//props.setProperty("java.naming.factory.state", "com.sun.corba.ee.impl.presentation.rmi.JNDIStateFactoryImpl");

			//props.setProperty("org.omg.CORBA.ORBInitialHost", "localhost");
			//props.setProperty("org.omg.CORBA.ORBInitialPort", "3700");

			//InitialContext ctx = new InitialContext(props);
			InitialContext ctx = new InitialContext();

			taskManager = (TaskManagerRemote<DocumentDTO, Long>) ctx.lookup(DataStore.remoteEJBJNDIName);
			logger.info("taskManager: " + taskManager + ", time taken to initialize = " + (System.currentTimeMillis() - startTime));
			if (taskManager != null) {
				logger.info("Success in connecting to remote EJB to initialize taskManager");
			}
		} catch (NamingException e) {
			logger.error("Error in JNDI lookup for initializing remote EJB", e);
		}
	}

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

	/* REDIS */
	public static Jedis getJedisConnection() {
		try {
			if (jedisPool != null) 
				return jedisPool.getResource();
			else {
				logger.error("Jedis Pool is NULL!");
				initializeJedisPool();
				return jedisPool.getResource();
			}
		} catch (Exception e) {
			logger.error("Could not establish Redis connection. Is the Redis server running?");
			logger.error("Exception", e);
			TaggerErrorLog.sendErrorMail("Redis", "Could not establish Redis connection. " + e.getMessage());
		}
		return null;
	}

	public static void close(Jedis resource) {
		jedisPool.returnResource(resource);
	}

	public static void clearRedisPipeline() {
		Jedis redis = getJedisConnection();
		redis.del(TaggerConfigurator.getInstance().getProperty(
				TaggerConfigurationProperty.REDIS_FOR_CLASSIFICATION_QUEUE));
		redis.del(TaggerConfigurator.getInstance().getProperty(
				TaggerConfigurationProperty.REDIS_FOR_EXTRACTION_QUEUE));
		redis.del(TaggerConfigurator.getInstance().getProperty(
				TaggerConfigurationProperty.REDIS_FOR_OUTPUT_QUEUE));
		redis.del(TaggerConfigurator.getInstance().getProperty(
				TaggerConfigurationProperty.REDIS_LABEL_TASK_WRITE_QUEUE));
		redis.del(TaggerConfigurator.getInstance().getProperty(
				TaggerConfigurationProperty.REDIS_TRAINING_SAMPLE_INFO_QUEUE));
	close(redis);
	}
	public static final int MODEL_ID_ERROR = -1;

	/* MYSQL */
	public static synchronized void initializeMySqlPool() throws SQLException {
		if (null == mySqlPool) {
			try {
				Class<?> c = Class.forName("com.mysql.jdbc.Driver");		
				Driver driver = (Driver) c.newInstance();
				DriverManager.registerDriver(driver);

				mySqlPool = new ConnectionPool("aidr-backend",
						10, // min-pool default = 1
						40, // max-pool default = 5
						200, // max-size default 30
						500, // timeout (sec)
						TaggerConfigurator.getInstance().getProperty(
								TaggerConfigurationProperty.MYSQL_PATH),
						TaggerConfigurator.getInstance().getProperty(
								TaggerConfigurationProperty.MYSQL_USERNAME),
						TaggerConfigurator.getInstance().getProperty(
								TaggerConfigurationProperty.MYSQL_PASSWORD));
				logger.info("Initialized mySQLPool = " + mySqlPool);
				attempts = 0;
			} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
				logger.error("Exception when initializing MySQL connection");
				logger.error("Exception:", e);
				++attempts;
			}
		} else {
			logger.warn("Attempting to initialize an active MySqlPool, attempts = " + attempts);
		}
	}

	public static synchronized Connection getMySqlConnection() throws SQLException {
		long timeout = 30000;
		Connection con = null;
		try {
			if (mySqlPool != null) {
				con = mySqlPool.getConnection(timeout);
			} else {
				logger.error("MySql Pool is NULL");
				initializeMySqlPool();
				con = mySqlPool.getConnection(timeout);
			}
			if (con != null) {
				attempts = 0;
				return con;
			} else {
				if (attempts < MAX_RECREATE_POOL_ATTEMPTS) {
					if (!mySqlPool.isReleased() && mySqlPool != null) {
						mySqlPool.release();
						mySqlPool = null;
						++attempts;
					}
					initializeMySqlPool();
					con = mySqlPool.getConnection(timeout);
					logger.warn("MySQL Pool reallocated!");
					if (null == con) {
						logger.error("The created MySQL connection is null even AFTER Reinitializing the MySQL Pool!!! Giving up...");
					} else {
						attempts = 0;	//reset
					}
					return con;
				}

			}	

		} catch (Exception e) {
			logger.error("Exception", e);

			if (attempts < MAX_RECREATE_POOL_ATTEMPTS) {
				if (!mySqlPool.isReleased() && mySqlPool != null) {
					mySqlPool.release();
					mySqlPool = null;
					++attempts;
				}
				initializeMySqlPool();
				con = mySqlPool.getConnection(timeout);
				if (con != null) {
					attempts = 0;		//reset
				}
			}
		}
		return con;
	}		

	public static void close(Connection con) {
		if (con == null) {
			return;
		}
		try {
			con.close();
			con = null;
		} catch (SQLException e) {
			logger.error("Exception when returning MySQL connection", e);
		}
	}

	public static void close(Statement statement) {
		if (statement == null) {
			return;
		}
		try {
			statement.close();
			statement = null;
		} catch (SQLException e) {
			logger.error("Could not close statement", e);
		}
	}

	public static void close(ResultSet resultset) {
		if (resultset == null) {
			return;
		}
		try {
			resultset.close();
			resultset = null;
		} catch (SQLException e) {
			logger.error("Could not close resultSet", e);
		}
	}

	public static Integer getNullLabelID(int attributeID) {
		String sql = "select nominalLabelID from nominal_label where nominalAttributeID="
				+ attributeID + " and nominalLabelCode='null'";
		Connection conn = null;
		PreparedStatement query = null;
		ResultSet result = null;
		try {
			conn = getMySqlConnection();
			query = conn.prepareStatement(sql);
			result = query.executeQuery();
			if (result.next()) {
				return result.getInt(1);
			}
		} catch (SQLException ex) {
			logger.error("Error in executing SQL statement: " + sql, ex);
		} finally {
			close(result);
			close(query);
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
			logger.error("Exception while fetching dataset. ", e);
		} catch (Exception e) {
			logger.error("Exception while fetching dataset", e);
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
				logger.error("New class label found in evaluation set. Discarding value.");
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

	public static boolean canLog()  {
		if (0 == lastSaveTime) {
			lastSaveTime = System.currentTimeMillis();
			return true;
		} else {
			if ((System.currentTimeMillis() - lastSaveTime) > LOG_INTERVAL) {
				return true;
			} else {
				return false;
			}
		}
	}


	public static void saveDocumentsToDatabase(List<Document> items) {
		try {
			for (Document item : items) {
				TaggerDocument doc = Document.fromDocumentToTaggerDocument(item);
				//System.out.println("Attempting to save NEW document for crisis = " + doc.getCrisisCode());
				//logger.info("Attempting to save NEW document for crisis = " + doc.getCrisisCode());
				Long docID = taskManager.saveNewTask(TaggerDocument.toDocumentDTO(doc), doc.getCrisisID());
				++saveNewDocumentsCount;
				if (docID.longValue() != -1) {
					// Update document with auto generated Doc
					item.setDocumentID(docID);
					//logger.info("Success in saving NEW document: " + item.getDocumentID() + ", for crisis = " + item.getCrisisCode());
				} else {
					logger.error("Something went wrong in saving document: " + item.getDocumentID() + ", for crisis = " + item.getCrisisCode());
				}
			}
			if (canLog()) {
				logger.info("In interval " + new Date(lastSaveTime) + " - " + new Date() + ", save NEW documents count = " + saveNewDocumentsCount);
				lastSaveTime = System.currentTimeMillis();
				saveNewDocumentsCount = 0;
			}
		} catch (Exception e) {
			logger.error("Exception when attempting to write Document to database", e);
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
		try {
			/*
				String insertSql = "INSERT INTO document_nominal_label (documentID, nominalLabelID) VALUES (?,?)";
			 */
			ArrayList<Integer> docsWithLabels = new ArrayList<>();
			ArrayList<TrainingSampleNotification> notifications = new ArrayList<>();
			int rows = 0;
			for (Document d: documents) {
				List<NominalLabelBC> labels = d.getHumanLabels(NominalLabelBC.class);
				if (labels.isEmpty()) // Skip document if it has no human-provided labels
				{
					continue;
				}
				docsWithLabels.add(d.getDocumentID().intValue());

				for (NominalLabelBC label : labels) {
					//statement.setInt(1, doc.getDocumentID());
					//statement.setInt(2, label.getNominalLabelID());
					//statement.execute();
					Long userID = d.getUserID() != null ? d.getUserID() : 1L;		// default labeler : 'System' user (userID = 1 in DB)
					DocumentNominalLabelIdDTO idDTO = new DocumentNominalLabelIdDTO(d.getDocumentID(), new Long(label.getNominalLabelID()), userID);
					DocumentNominalLabelDTO dto = new DocumentNominalLabelDTO();
					dto.setIdDTO(idDTO);
					if (canLog()) {
						logger.info("Attempting to save LABELED document: " + dto.getIdDTO().getDocumentId() + " with nominal labelID=" + dto.getIdDTO().getNominalLabelId() + ", for crisis = " + d.getCrisisCode() + ", userID = " + dto.getIdDTO().getUserId());
						lastSaveTime = System.currentTimeMillis();
						saveNewDocumentsCount = 0;
					}
					//System.out.println("Attempting to save LABELED document: " + dto.getIdDTO().getDocumentId() + " with nominal labelID=" + dto.getIdDTO().getNominalLabelId() + ", for crisis = " + d.getCrisisCode() + ", userID = " + dto.getIdDTO().getUserId());
					taskManager.saveDocumentNominalLabel(dto);
					rows++;
				}
				notifications.add(new TrainingSampleNotification(d.getCrisisID().intValue(), getAttributeIDs(labels)));
			}
			if (rows == 0) {
				return;
			}

			logger.info("Saved " + rows + " human labels for " + docsWithLabels.size()
					+ " documents");
			//statement.executeUpdate("UPDATE document SET hasHumanLabels=1 WHERE documentID IN (" + Helpers.join(docsWithLabels, ",") + ")");
			sendNewLabeledDocumentNotification(notifications);

		} catch (Exception e) {
			logger.error("Exception when attempting to insert new document labels", e);
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
			redis.rpush(
					TaggerConfigurator
							.getInstance()
							.getProperty(
									TaggerConfigurationProperty.REDIS_TRAINING_SAMPLE_INFO_QUEUE),
					message);
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
			logger.error("Exception when getting model state", e);
		} finally {
			close(result);
			close(sql);
			close(conn);
		}

		return modelFamilies;
	}
	
	public static void getActiveModelsDocCount(HashMap<Integer, HashMap<Integer, ModelFamilyEC>> modelFamilies, HashMap<Integer, 
			HashMap<String, ModelFamilyEC>> modelFamiliesByCode) {
		Connection conn = null;
		PreparedStatement sql = null;
		ResultSet result = null;

		try {
			conn = getMySqlConnection();

			sql = conn
					.prepareStatement(
							"SELECT \n"
									+ " fam.modelFamilyID, \n"
									+ "	fam.crisisID, \n"
									+ "	fam.nominalAttributeID, \n"
									+ " mdl.modelID, \n"
									+ "	lbl.nominalLabelID,\n"
									+ "	COUNT(DISTINCT dnl.documentID) AS labeledItemCount\n"
									+ "FROM model_family fam \n"
									+ "LEFT JOIN model mdl on mdl.modelFamilyID = fam.modelFamilyID \n"																		
									+ "JOIN nominal_label lbl ON lbl.nominalAttributeID = fam.nominalAttributeID \n"
									+ "LEFT JOIN document doc ON doc.crisisID=fam.crisisID \n"
									+ "LEFT JOIN document_nominal_label dnl ON dnl.documentID=doc.documentID AND dnl.nominalLabelID=lbl.nominalLabelID \n"
									+ "WHERE fam.isActive AND (mdl.modelID IS NULL OR mdl.isCurrentModel) \n"
									+ "GROUP BY crisisID, nominalAttributeID, nominalLabelID ");
			result = sql.executeQuery();

			ModelFamilyEC family = null;
			NominalAttributeEC attribute = null;
			NominalLabelEC label = null;
			HashMap<ModelFamilyEC, Integer> familyLabelCount = new HashMap<>();
			Integer crisisID = null;
			Integer attributeID = null;
			Integer nominalLabelID = null;
			int count;
			
			while (result.next()) {
				crisisID = result.getInt("crisisID");
				attributeID = result.getInt("nominalAttributeID");
				nominalLabelID = result.getInt("nominalLabelID");
				if (!modelFamilies.containsKey(crisisID)) {
					modelFamilies.put(crisisID, new HashMap<Integer, ModelFamilyEC>());
					modelFamiliesByCode.put(crisisID, new HashMap<String, ModelFamilyEC>());
				}
				if(modelFamilies.get(crisisID).get(attributeID) == null) {	
					//create model family
					family = new ModelFamilyEC();
					family.setCrisisID(crisisID);
					int tmpModelID = result.getInt("modelID");
					if (!result.wasNull()) {
						family.setCurrentModelID(tmpModelID);
					}
					family.setIsActive(true);
					family.setModelFamilyID(result.getInt("modelFamilyID"));
				}
				else
					family = modelFamilies.get(crisisID).get(attributeID);
					
				attribute = family.getNominalAttribute();
				if(attribute == null)
				{
					if(attLabels.containsKey(attributeID))
					{
						attribute = attLabels.get(attributeID);
						family.setNominalAttribute(attribute);
					}
					else
					{
						synchronized(attLabels) {
							getAttributesLabels();

							if(attLabels.containsKey(attributeID))
							{
								attribute = attLabels.get(attributeID);
								family.setNominalAttribute(attribute);
							}
						}
					}

				}

				label = attribute.getNominalLabel(nominalLabelID);
				if(label == null)
				{
					synchronized(attLabels) {
						updateLabels(attributeID);
						attribute = attLabels.get(attributeID);
					}
				}
				
				if(familyLabelCount.get(family) == null)
					familyLabelCount.put(family, 0);
				
				modelFamilies.get(crisisID).put(attributeID, family);
				modelFamiliesByCode.get(crisisID).put(attribute.getCode(), family);

				count = familyLabelCount.get(family);
				familyLabelCount.put(family, count + result.getInt("labeledItemCount"));

			}

			//sum training sample counts per attribute
			for (Map.Entry<ModelFamilyEC, Integer> entry : familyLabelCount.entrySet()) {
				entry.getKey().setTrainingExampleCount(entry.getValue());
				//logger.info("training example count: " + entry.getValue() + " for family" + entry.getKey().getModelFamilyID());
			}

		} catch (SQLException e) {
			logger.error("Exception when getting model state ::", e);
		} catch (Exception e) {
			logger.error("Exception in getActiveModelsDocCount ::", e);
		} finally {
			close(result);
			close(sql);
			close(conn);
		}
	}

	public static void deleteModel(int modelID) {
		Connection conn = null;
		PreparedStatement sql = null;

		try {
			conn = getMySqlConnection();
			sql = conn.prepareStatement("DELETE FROM model WHERE modelID=" + modelID);
			sql.executeUpdate();
		} catch (SQLException e) {
			logger.error("Exception while deleting model");
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
			logger.error("Exception when getting crisis IDs", e);
		} finally {
			close(result);
			close(sql);
			close(conn);
		}

		return crisisIDs;
	}


	public static void truncateLabelingTaskBufferForCrisis(int crisisID, int maxLength) {
		if (maxLength < 0 || crisisID < 0) {
			logger.error("Cannot truncate the labeling task buffer - negative parameter(s)");
			throw new RuntimeException(
					"Cannot truncate the labeling task buffer - negative parameter(s)");
		}
		final int ERROR_MARGIN = 0;		// if less than this, then skip delete
		int deleteCount = taskManager.truncateLabelingTaskBufferForCrisis(crisisID, maxLength, ERROR_MARGIN);
		logger.info("Truncation results for crisis " + crisisID + ", deleted doc count = " + deleteCount);

	}

	public static int saveModelToDatabase(int crisisID, int nominalAttributeID,
			Model model) {
		int modelID = MODEL_ID_ERROR;
		Connection conn = null;
		PreparedStatement modelInsert = null, mfUpdate = null;
		PreparedStatement modelLabelPerfInsert = null;
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
					"INSERT INTO model (modelFamilyID, avgPrecision, avgRecall, avgAuc, isCurrentModel, trainingCount, trainingTime) VALUES "
							+ "(" + selectModelFamilyID + ", "
							+ format.format(model.getMeanPrecision())
							+ ", "
							+ format.format(model.getMeanRecall())
							+ ", "
							+ format.format(model.getMeanAuc())
							+ ",false,"
							+ model.getTrainingSampleCount()
							+ ", UTC_TIMESTAMP())";
			modelInsert = conn.prepareStatement(modelInsertSql, Statement.RETURN_GENERATED_KEYS);
			modelInsert.executeUpdate();
			result = modelInsert.getGeneratedKeys();
			
			if (result != null && result.next()) {
			    modelID = result.getInt(1);
			}
			
			System.out.println("Inserted a new model with model ID " + modelID); //TODO: remove
			logger.info("Inserted a new model with model ID " + modelID);
			//Insert per-label classification performance of this model 
			List<ModelNominalLabelPerformance> labelPerformaceList = model.getLabelPerformanceList();
			String perfInsertSql = "INSERT INTO model_nominal_label (`modelID`, `nominalLabelID`, `labelPrecision`, `labelRecall`, `labelAuc`, `classifiedDocumentCount`) "
					+ " VALUES (?,?,?,?,?,0);";
			modelLabelPerfInsert = conn.prepareStatement(perfInsertSql);
			for (ModelNominalLabelPerformance perf : labelPerformaceList) {
				
				modelLabelPerfInsert.setInt(1, modelID);
				modelLabelPerfInsert.setInt(2, perf.getNominalLabelID());
				modelLabelPerfInsert.setString(3, format.format(perf.getPrecision()));
				modelLabelPerfInsert.setString(4, format.format(perf.getRecall()));
				modelLabelPerfInsert.setString(5, format.format(perf.getAuc()));

				modelLabelPerfInsert.executeUpdate();
			}

			// Set the the new model as the active model of its model family
			mfUpdate = conn
					.prepareStatement("UPDATE model SET isCurrentModel = (modelID = " + modelID + ") "
							+ "WHERE modelID = " + modelID + " OR (isCurrentModel AND modelFamilyID = "
							+ selectModelFamilyID + ")");
			mfUpdate.executeUpdate();
		} catch (SQLException e) {
			logger.error("Exception while saving model to database", e);
		} finally {
			close(modelLabelPerfInsert);
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
							+ "left join (select nominalAttributeID, greatest(count(1),1) as totalCount \n"
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
			logger.error("Exception when getting nominal label training values", e);
		} finally {
			close(result);
			close(sql);
			close(conn);
		}

		return scores;
	}

	public static void saveClassifiedDocumentCounts(HashMap<Integer, HashMap<Integer, Integer>> data) {
		Connection conn = null;
		PreparedStatement selectStatement = null;
		PreparedStatement updateStatement = null;
		ResultSet resultSet = null;
		
		String updateQuery = "UPDATE model_nominal_label SET classifiedDocumentCount = classifiedDocumentCount + ? "
				+ "WHERE modelID = ? AND nominalLabelID = ?"; 
		try {
			// Insert document
			conn = getMySqlConnection();
			updateStatement = conn.prepareStatement(updateQuery);
			
			for (Map.Entry<Integer, HashMap<Integer, Integer>> modelDocCounts : data.entrySet()) {
				int modelID = modelDocCounts.getKey();
				for (Map.Entry<Integer, Integer> labelDocCount : modelDocCounts.getValue().entrySet()) {
					Integer labelID = labelDocCount.getKey();
					Integer docCount = labelDocCount.getValue();
					
					updateStatement.setInt(1, docCount);
					updateStatement.setInt(2, modelID);
					updateStatement.setInt(3, labelID);
					updateStatement.executeUpdate();
				}
			}
		} catch (SQLException e) {
			logger.error("Exception when attempting to write ClassifiedDocumentCount to database : " + data, e);
		} finally {
			close(resultSet);
			close(updateStatement);
			close(selectStatement);
			close(conn);
		}
	}

	
	//update nominal labels for an attribute from db
	private static void updateLabels(Integer attributeID){

		Connection conn = null;
		PreparedStatement selectStatement = null;
		ResultSet result = null;
		String selectQuery = "SELECT nominalLabelID,l.nominalLabelCode,l.name as nominalLabelName,"
				+ "l.description as nominLabelDescription FROM nominal_label l where l.nominalAttributeID = ?";
		
		try {
			conn = getMySqlConnection();
			selectStatement = conn.prepareStatement(selectQuery);
			selectStatement.setInt(1, attributeID);
			result = selectStatement.executeQuery();
			NominalAttributeEC attribute = null;
			NominalLabelEC label = null;

			while (result.next()) {
				int labelID = result.getInt("nominalLabelID");
				attribute = attLabels.get(attributeID);
				attribute.resetNominalLabels();
				if(attribute.getNominalLabel(labelID) == null)
				{
					label = new NominalLabelEC();
					label.setDescription(result.getString("nominLabelDescription"));
					label.setName(result.getString("nominalLabelName"));
					label.setNominalAttribute(attribute);
					label.setNominalLabelCode(result.getString("nominalLabelCode"));
					label.setNominalLabelID(result.getInt("nominalLabelID"));
					attribute.addNominalLabel(label);
				}
			}


		} catch (SQLException e) {
			logger.error("Exception while updating nominal labels ::", e);
		} finally {
			close(result);
			close(selectStatement);
			close(conn);
		}
	}
	
	//get all attributes and labels, and store in hashmap
	//invoke this method after a timed wait - to refresh the data structure
	public static void getAttributesLabels() {
		Connection conn = null;
		PreparedStatement selectStatement = null;
		ResultSet result = null;

		String selectQuery = "SELECT a.nominalAttributeID, a.code as nominalAttributeCode,"
				+ " a.description as nominalAttributeDescription, a.name as nominalAttributeName, l.nominalLabelID, l.nominalLabelCode,l.name as nominalLabelName,"
				+ "l.description as nominLabelDescription FROM nominal_attribute a join nominal_label l on a.nominalAttributeID = l.nominalAttributeID";

		try {
			conn = getMySqlConnection();
			selectStatement = conn.prepareStatement(selectQuery);
			result = selectStatement.executeQuery();
			NominalAttributeEC attribute = null;
			NominalLabelEC label = null;

			while (result.next()) {
				int attrID = result.getInt("nominalAttributeID");
				int labelID = result.getInt("nominalLabelID");
				if(!attLabels.containsKey(attrID))
				{
					attribute = new NominalAttributeEC();
					attribute.setNominalAttributeID(attrID);
					attribute.setCode(result.getString("nominalAttributeCode"));
					attribute.setDescription(result.getString("nominalAttributeDescription"));
					attribute.setName(result.getString("nominalAttributeName"));
					attLabels.put(attrID, attribute);
				}
				else
					attribute = attLabels.get(attrID);

				if(attribute.getNominalLabel(labelID) == null)
				{
					label = new NominalLabelEC();
					label.setDescription(result.getString("nominLabelDescription"));
					label.setName(result.getString("nominalLabelName"));
					label.setNominalAttribute(attribute);
					label.setNominalLabelCode(result.getString("nominalLabelCode"));
					label.setNominalLabelID(result.getInt("nominalLabelID"));
					attribute.addNominalLabel(label);
				}
			}


		} catch (SQLException e) {
			logger.error("Exception while creating nominal attributes ::", e);
		} finally {
			close(result);
			close(selectStatement);
			close(conn);
		}
		
	}

}
