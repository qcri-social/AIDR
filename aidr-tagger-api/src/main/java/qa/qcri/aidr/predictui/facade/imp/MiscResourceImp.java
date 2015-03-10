/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.qcri.aidr.predictui.facade.imp;


import qa.qcri.aidr.common.values.DownloadType;
import qa.qcri.aidr.dbmanager.dto.DocumentDTO;
import qa.qcri.aidr.dbmanager.dto.HumanLabeledDocumentDTO;
import qa.qcri.aidr.dbmanager.dto.HumanLabeledDocumentList;
import qa.qcri.aidr.dbmanager.dto.taggerapi.HumanLabeledDocumentListWrapper;
import qa.qcri.aidr.dbmanager.dto.taggerapi.ItemToLabelDTO;
import qa.qcri.aidr.dbmanager.dto.taggerapi.TrainingDataDTO;
import qa.qcri.aidr.predictui.facade.MiscResourceFacade;
import qa.qcri.aidr.task.ejb.TaskManagerRemote;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.glassfish.jersey.jackson.JacksonFeature;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static qa.qcri.aidr.predictui.util.ConfigProperties.getProperty;

/**
 *
 * @author Imran
 */
@Stateless
public class MiscResourceImp implements MiscResourceFacade {

	private final String persisterMainUrl = getProperty("AIDR_PERSISTER_URL");

	@EJB
	private qa.qcri.aidr.dbmanager.ejb.remote.facade.MiscResourceFacade remoteMiscEJB;

	@EJB
	private TaskManagerRemote<DocumentDTO, Long> remoteTaskManager;

	private static Logger logger = Logger.getLogger(MiscResourceImp.class);

	@Override
	public List<TrainingDataDTO> getTraningDataByCrisisAndAttribute(long crisisID,
			int modelFamilyID,
			int fromRecord,
			int limit,
			String sortColumn,
			String sortDirection) {
		List<TrainingDataDTO> trainingDataList = new ArrayList<TrainingDataDTO>();

		logger.info("getTraningDataByCrisisAndAttribute, crisisID = " + crisisID + ", modelFamilyID =  " + modelFamilyID);
		try {
			trainingDataList = remoteMiscEJB.getTraningDataByCrisisAndAttribute(new Long(crisisID), new Long(modelFamilyID), fromRecord, limit, sortColumn, sortDirection);
			logger.info("Fetched training data list size: " + (trainingDataList != null ? trainingDataList.size() : 0));
			return trainingDataList;
		} catch (Exception e) {
			logger.error("exception for crisisID = " + crisisID + ", modelFamilyID = " + modelFamilyID + ": " + e);
			return null;
		}
	}

	@Override
	public ItemToLabelDTO getItemToLabel(long crisisID, int modelFamilyID) {
		try{
			ItemToLabelDTO itemToLabel = remoteMiscEJB.getItemToLabel(new Long(crisisID), new Long(modelFamilyID));
			return itemToLabel;
		} catch(Exception e) {
			logger.error("exception for crisisID = " + crisisID + ", modelFamilyID = " + modelFamilyID + ": " + e);
			return null;  
		}
	}

	@Override
	public List<HumanLabeledDocumentDTO> getHumanLabeledDocumentsByCrisisID(Long crisisID, Integer count) {
		try {
			return remoteTaskManager.getHumanLabeledDocumentsByCrisisID(crisisID, count);
		} catch (Exception e) {
			logger.error("exception for crisisID = " + crisisID);
			e.printStackTrace();
			return null;  
		}
	}

	@Override
	public List<HumanLabeledDocumentDTO> getHumanLabeledDocumentsByCrisisCode(String crisisCode, Integer count) {
		try {
			return remoteTaskManager.getHumanLabeledDocumentsByCrisisCode(crisisCode, count);
		} catch (Exception e) {
			logger.error("exception for crisis code = " + crisisCode);
			e.printStackTrace();
			return null;  
		}
	}

	@Override
	public List<HumanLabeledDocumentDTO> getHumanLabeledDocumentsByCrisisIDUserID(Long crisisID, Long userID, Integer count) {
		try {
			return remoteTaskManager.getHumanLabeledDocumentsByCrisisIDUserID(crisisID, userID, count);
		} catch (Exception e) {
			logger.error("exception for crisisID = " + crisisID + ", userID = " + userID);
			e.printStackTrace();
			return null;  
		}
	}

	@Override
	public List<HumanLabeledDocumentDTO> getHumanLabeledDocumentsByCrisisIDUserName(Long crisisID, String userName, Integer count)  {
		try {
			return remoteTaskManager.getHumanLabeledDocumentsByCrisisIDUserName(crisisID, userName, count);
		} catch (Exception e) {
			logger.error("exception for crisisID = " + crisisID + ", userName = " + userName);
			e.printStackTrace();
			return null;  
		}
	}

	@Override
	public String downloadItems(HumanLabeledDocumentList dtoList, String queryString, String crisisCode, String userName, 
								Integer count, String fileType, String contentType) {

		try {
			HumanLabeledDocumentListWrapper postBody = new HumanLabeledDocumentListWrapper(dtoList, queryString);
			//System.out.println("Request POST body: " + postBody.toJsonString());

			System.out.println("Received request to create file for: " + dtoList.getTotal() + "items for crisis = " + crisisCode + "userName = " + userName);
			Client client = ClientBuilder.newBuilder().register(JacksonFeature.class).build();

			String targetAPI = getPersisterTargetAPI(fileType, contentType, count);
			WebTarget webResource = client.target(persisterMainUrl + targetAPI + "collectionCode=" + crisisCode + "&userName=" + userName);
			Response clientResponse = webResource.request(MediaType.APPLICATION_JSON)
					.post(Entity.json(postBody), Response.class);
			System.out.println("received raw response from persister: " + clientResponse);
			Map<String, Object> jsonResponse = clientResponse.readEntity(Map.class);
			System.out.println("Received response from persister: " + jsonResponse);
			if (jsonResponse.get("url") != null) {
				return jsonResponse.get("url").toString();
			} else {
				return new String("Exception in generating file from human labeled items");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new String("Exception in generating file from human labeled items");
		}
	}

	private String getPersisterTargetAPI(String fileType, String contentType, Integer exportLimit) {
		StringBuffer targetURL = new StringBuffer().append("/listPersister/filter/");
		if (fileType.equalsIgnoreCase(DownloadType.CSV)) {
			if (contentType.equalsIgnoreCase(DownloadType.TWEET_IDS)) {
				targetURL.append("genCSVTweetIds?");
				return targetURL.toString();
			} else {
				// default
				targetURL.append("genCSV?");
				if (exportLimit != null) {
					targetURL.append("exportLimit=").append(exportLimit).append("&");
				}
				return targetURL.toString();
			}			
		} else if (fileType.equalsIgnoreCase(DownloadType.JSON_OBJECT)) {
			if (contentType.equalsIgnoreCase(DownloadType.TWEET_IDS)) {
				targetURL.append("genJsonTweetIds?jsonType=").append(DownloadType.JSON_OBJECT).append("&");
				return targetURL.toString();
			} else {
				//default
				targetURL.append("genJson?jsonType=").append(DownloadType.JSON_OBJECT).append("&");
				if (exportLimit != null) {
					targetURL.append("exportLimit=").append(exportLimit).append("&");
				}
				return targetURL.toString();
			}
		} else if (fileType.equalsIgnoreCase(DownloadType.TEXT_JSON)) {
			if (contentType.equalsIgnoreCase(DownloadType.TWEET_IDS)) {
				targetURL.append("genJsonTweetIds?jsonType=").append(DownloadType.TEXT_JSON).append("&");
				return targetURL.toString();
			} else {
				//default
				targetURL.append("genJson?jsonType=").append(DownloadType.TEXT_JSON).append("&");
				if (exportLimit != null) {
					targetURL.append("exportLimit=").append(exportLimit).append("&");
				}
				return targetURL.toString();
			}
		} else {
			// default behavior
			targetURL.append("genCSV?");
			if (exportLimit != null) {
				targetURL.append("exportLimit=").append(exportLimit).append("&");
			}
			return targetURL.toString();
		}
	}
}
