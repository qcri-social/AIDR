/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.qcri.aidr.predictui.facade.imp;

import qa.qcri.aidr.dbmanager.dto.taggerapi.ItemToLabelDTO;
import qa.qcri.aidr.dbmanager.dto.taggerapi.TrainingDataDTO;

import qa.qcri.aidr.predictui.facade.MiscResourceFacade;


import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Imran
 */
@Stateless
public class MiscResourceImp implements MiscResourceFacade {

	@EJB
	private qa.qcri.aidr.dbmanager.ejb.remote.facade.MiscResourceFacade remoteMiscEJB;
	
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
}
