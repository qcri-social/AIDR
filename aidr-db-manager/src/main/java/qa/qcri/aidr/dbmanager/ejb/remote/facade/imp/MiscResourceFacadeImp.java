/**
 * Implements operations for managing the operations that span multiple tables of the aidr_predict DB
 * 
 * @author Koushik
 */
package qa.qcri.aidr.dbmanager.ejb.remote.facade.imp;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import qa.qcri.aidr.dbmanager.dto.DocumentDTO;
import qa.qcri.aidr.dbmanager.dto.NominalAttributeDTO;
import qa.qcri.aidr.dbmanager.dto.NominalLabelDTO;
import qa.qcri.aidr.dbmanager.dto.TaskAssignmentDTO;
import qa.qcri.aidr.dbmanager.dto.taggerapi.ItemToLabelDTO;
import qa.qcri.aidr.dbmanager.dto.taggerapi.TrainingDataDTO;
import qa.qcri.aidr.dbmanager.ejb.local.facade.impl.CoreDBServiceFacadeImp;
import qa.qcri.aidr.dbmanager.ejb.remote.facade.MiscResourceFacade;
import qa.qcri.aidr.dbmanager.entities.task.Document;
import qa.qcri.aidr.util.NativeQueryUtil;

@Stateless(name="MiscResourceFacadeImp")
public class MiscResourceFacadeImp extends CoreDBServiceFacadeImp<Document, Long> implements MiscResourceFacade {
	private static Logger logger = Logger.getLogger("aidr-db-manager");

	@EJB
	private qa.qcri.aidr.dbmanager.ejb.remote.facade.DocumentResourceFacade documentEJB;

	@EJB
	private qa.qcri.aidr.dbmanager.ejb.remote.facade.TaskAssignmentResourceFacade taskAssignmentEJB;

	public MiscResourceFacadeImp() {
		super(Document.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TrainingDataDTO> getTraningDataByCrisisAndAttribute(Long crisisID, Long modelFamilyID, int fromRecord, int limit,
			String sortColumn, String sortDirection) {
		List<TrainingDataDTO> trainingDataList = new ArrayList<TrainingDataDTO>();

		String orderSQLPart = "";
		if (sortColumn != null && !sortColumn.isEmpty()){
			if (sortDirection != null && !sortDirection.isEmpty()) {
				if ("ASC".equals(sortDirection)) {
					sortDirection = "ASC";
				} else {
					sortDirection = "DESC";
				}
			} else {
				sortDirection = "DESC";
			}
			orderSQLPart += " ORDER BY " + sortColumn + " " + sortDirection + " ";
		}
		else{
			orderSQLPart += " ORDER BY dnl.timestamp DESC";
		}

		String sql = " SELECT distinct lbl.nominalLabelID, lbl.name labelName, d.data tweetJSON, u.id, u.user_name labelerName, dnl.timestamp, d.documentID "
				+ " FROM document_nominal_label dnl "
				+ " JOIN nominal_label lbl on lbl.nominalLabelID=dnl.nominalLabelID "
				+ " JOIN model_family mf on mf.nominalAttributeID=lbl.nominalAttributeID "
				+ " JOIN document d on d.documentID = dnl.documentID "
				+ " JOIN account u on u.id = dnl.userID "
				+ " WHERE mf.modelFamilyID = :modelFamilyID AND d.crisisID = :crisisID " + orderSQLPart
				+ " LIMIT :fromRecord, :limit";

		String sqlCount = " SELECT count(1) "
				+ " FROM document_nominal_label dnl "
				+ " JOIN nominal_label lbl on lbl.nominalLabelID=dnl.nominalLabelID "
				+ " JOIN model_family mf on mf.nominalAttributeID=lbl.nominalAttributeID "
				+ " JOIN document d on d.documentID = dnl.documentID "				
				+ " WHERE mf.modelFamilyID = :modelFamilyID AND d.crisisID = :crisisID";

		try {
			Integer totalRows = null;
			Session session = getCurrentSession();
			//Query queryCount = em.createNativeQuery(sqlCount);
			Query queryCount = session.createSQLQuery(sqlCount);
			//logger.info("getTraningDataByCrisisAndAttribute count query: " + sqlCount);
			queryCount.setParameter("modelFamilyID", modelFamilyID.intValue());
			queryCount.setParameter("crisisID", crisisID.intValue());
			
			Object res = queryCount.uniqueResult();
			if (res != null) { 
				totalRows = Integer.parseInt(res.toString());
			}
			logger.info("getTraningDataByCrisisAndAttribute: rows count = " + res);
			if (totalRows != null && totalRows > 0) {
				Query query = session.createSQLQuery(sql);
				query.setParameter("crisisID", crisisID.intValue());
				query.setParameter("modelFamilyID", modelFamilyID.intValue());
				query.setParameter("fromRecord", fromRecord);
				query.setParameter("limit", limit);
				
				List<Object[]> rows = query.list();
				//logger.info("[getTraningDataByCrisisAndAttribute] fetched rows count = " + (rows != null ? rows.size() : "null"));
				TrainingDataDTO trainingDataRow = null;
				//int count = 0;
				for (Object[] row : rows) {
					trainingDataRow = new TrainingDataDTO();
					//                    Removed .intValue() as we already cast to Integer
					trainingDataRow.setLabelID(((BigInteger) row[0]).intValue());
					trainingDataRow.setLabelName((String) row[1]);
					trainingDataRow.setTweetJSON((String) row[2]);
					trainingDataRow.setLabelerID(((BigInteger) row[3]).intValue());
					trainingDataRow.setLabelerName((String) row[4]);
					trainingDataRow.setLabeledTime(((Date) row[5]));
					trainingDataRow.setDocumentID(((BigInteger) row[6]).longValue());
					trainingDataRow.setTotalRows(totalRows);
					trainingDataList.add(trainingDataRow);
					//logger.info("Added to DTO training data, training data #" + count);
					//++count;
				}
			}
			logger.info("Fetched training data list size: " + (trainingDataList != null ? trainingDataList.size() : 0));
			return trainingDataList;
		} catch (Exception e) {
			logger.error("exception", e);
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public ItemToLabelDTO getItemToLabel(Long crisisID, Long modelFamilyID) {
		// with attributeID get attribute and labels details
		// with crisisID get an item from document table for which hasHumanLabel is FALSE
		// packup both info into one class DTO and return

		// TODO: the fields of NominalAttributeDTO need to match the ones in Tagger-API original
		NominalAttributeDTO attributeDTO = new NominalAttributeDTO();
		ItemToLabelDTO itemToLabel = new ItemToLabelDTO();
		try{
			Session session = getCurrentSession();
			String sqlToGetAttribute = "SELECT na.nominalAttributeID, na.code, na.name, na.description FROM nominal_attribute na"
					+ " JOIN model_family mf on mf.nominalAttributeID = na.nominalAttributeID WHERE mf.modelFamilyID = :modelFamilyID";
			Query attributeQuery = session.createSQLQuery(sqlToGetAttribute);
			attributeQuery.setParameter("modelFamilyID", modelFamilyID.intValue());
			
			List<Object[]> attributeResults = attributeQuery.list();
			if (attributeResults != null && !attributeResults.isEmpty()) {
				attributeDTO.setNominalAttributeId(((Long)attributeResults.get(0)[0]).longValue());
				attributeDTO.setCode((String) attributeResults.get(0)[1]);
				attributeDTO.setName((String) attributeResults.get(0)[2]);
				attributeDTO.setDescription((String) attributeResults.get(0)[3]);

				String sqlToGetLabel = "SELECT nominalLabelCode, name, description FROM nominal_label WHERE nominalAttributeID = :attributeID";
				Query labelQuery = session.createSQLQuery(sqlToGetLabel);
				labelQuery.setParameter("attributeID", attributeDTO.getNominalAttributeId());
				List<Object[]> labelsResults = labelQuery.list();

				List<NominalLabelDTO> labelDTOList = new ArrayList<NominalLabelDTO>();

				for (Object[] label: labelsResults){
					NominalLabelDTO labelDTO = new NominalLabelDTO();
					labelDTO.setNominalLabelCode((String)label[0]);
					labelDTO.setName((String) label[1]);
					labelDTO.setDescription((String) label[2]);

					labelDTOList.add(labelDTO);
				}
				attributeDTO.setNominalLabelsDTO(labelDTOList);
			}
			//here retrieve data from document table
			//String sqlToGetItem = "SELECT documentID, data FROM document WHERE crisisID = :crisisID AND hasHumanLabels = 0 ORDER BY RAND() LIMIT 0, 1";
			//Query documentQuery = em.createNativeQuery(sqlToGetItem);
			//documentQuery.setParameter("crisisID", crisisID);
			//List<Object[]> documentResult = documentQuery.getResultList();
			//itemToLabel.setItemID(((BigInteger) documentResult.get(0)[0]));
			//itemToLabel.setItemText(documentResult.get(0)[1].toString());

			DocumentDTO documentResult = getNewTask(crisisID);
			if (documentResult != null) {
				logger.info("For crisisID: " + crisisID + ", fetched doc id in ItemToLabel: " + documentResult.getDocumentID());
				itemToLabel.setItemID(BigInteger.valueOf(documentResult.getDocumentID()));
				itemToLabel.setItemText(documentResult.getData());
				itemToLabel.setAttribute(attributeDTO);
			} else {
				logger.info("For crisisID: " + crisisID + ", doc id: null");
			}

		} catch(Exception e) {
			logger.error("exception", e);
			return null;  
		}
		return itemToLabel;
	}


	@SuppressWarnings("unchecked")
	@Override
	public Map<Long, Long> getTrainingCountForCrisis(Long crisisID) {

		Map<Long, Long> countMap = new HashMap<Long, Long>();
		try {
			Session session = getCurrentSession();
			Query query = session.createSQLQuery(NativeQueryUtil.TRAINING_COUNT_FOR_CRISIS);
			query.setParameter("crisisID", crisisID.intValue());
			
			List<Object[]> rows = query.list();
			for (Object[] row : rows) {
				countMap.put(((BigInteger)row[0]).longValue(), ((BigInteger)row[1]).longValue());
			}
				
		} catch (Exception e) {
			logger.error("exception", e);
		}
		
		return countMap;
	}

	private DocumentDTO getNewTask(Long crisisID) {
		Criterion newCriterion = Restrictions.conjunction()
				.add(Restrictions.eq("crisis.crisisId",crisisID))
				.add(Restrictions.eq("hasHumanLabels",false));
		try {
			Document document = getByCriteria(newCriterion);
			//logger.info("[MiscResourceFacadeImp:getNewTask] New task: " + document);
			if (document != null)  {
				List<TaskAssignmentDTO> tList = taskAssignmentEJB.findTaskAssignmentByID(document.getDocumentId());
				if (tList != null && !tList.isEmpty()) {
					logger.info("[MiscResourceFacadeImp:getNewTask] New task: " + document.getDocumentId());
					return new DocumentDTO(document);
				}
			} else {
				//logger.info("[getNewTask] New task: " + document);
			}
		} catch (Exception e) {
			logger.error("[MiscResourceFacadeImp:getNewTask] Error in getting new Task for crisisID: " + crisisID);
			logger.error("exception", e);
		}
		return null;
	}
}
