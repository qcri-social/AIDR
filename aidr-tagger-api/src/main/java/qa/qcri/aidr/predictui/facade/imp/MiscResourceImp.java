/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.qcri.aidr.predictui.facade.imp;

import qa.qcri.aidr.predictui.dto.ItemToLabelDTO;
import qa.qcri.aidr.predictui.dto.NominalAttributeDTO;
import qa.qcri.aidr.predictui.dto.NominalLabelDTO;
import qa.qcri.aidr.predictui.dto.TrainingDataDTO;
import qa.qcri.aidr.predictui.facade.MiscResourceFacade;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Imran
 */
@Stateless
public class MiscResourceImp implements MiscResourceFacade {

    @PersistenceContext(unitName = "qa.qcri.aidr.predictui-EJBS")
    private EntityManager em;

    @Override
    public List<TrainingDataDTO> getTraningDataByCrisisAndAttribute(int crisisID,
                                                                    int modelFamilyID,
                                                                    int fromRecord,
                                                                    int limit,
                                                                    String sortColumn,
                                                                    String sortDirection) {
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
                sortDirection = "ASC";
            }
            orderSQLPart += " ORDER BY " + sortColumn + " " + sortDirection + " ";
        }

        String sql = " SELECT lbl.nominalLabelID, lbl.name labelName, d.data tweetJSON, u.userID, u.name labelerName, dnl.timestamp "
                + " FROM document_nominal_label dnl "
                + " JOIN nominal_label lbl on lbl.nominalLabelID=dnl.nominalLabelID "
                + " JOIN model_family mf on mf.nominalAttributeID=lbl.nominalAttributeID "
                //+ " JOIN model m on m.modelFamilyID= mf.modelFamilyID "
                + " JOIN document d on d.documentID = dnl.documentID "
                + " JOIN task_answer ta on ta.documentID = d.documentID "
                + " JOIN users u on u.userID = ta.userID "
                + " WHERE mf.modelFamilyID = :modelFamilyID AND d.crisisID = :crisisID " + orderSQLPart
                + " LIMIT :fromRecord, :limit";

        String sqlCount = " SELECT count(*) "
                + " FROM document_nominal_label dnl "
                + " JOIN nominal_label lbl on lbl.nominalLabelID=dnl.nominalLabelID "
                + " JOIN model_family mf on mf.nominalAttributeID=lbl.nominalAttributeID "
                //+ " JOIN model m on m.modelFamilyID= mf.modelFamilyID "
                + " JOIN document d on d.documentID = dnl.documentID "
                + " JOIN task_answer ta on ta.documentID = d.documentID "
                + " JOIN users u on u.userID = ta.userID "
                + " WHERE mf.modelFamilyID = :modelFamilyID AND d.crisisID = :crisisID ";
        try {
            Integer totalRows;

            Query queryCount = em.createNativeQuery(sqlCount);
            queryCount.setParameter("crisisID", crisisID);
            queryCount.setParameter("modelFamilyID", modelFamilyID);
            Object res = queryCount.getSingleResult();
            totalRows = Integer.parseInt(res.toString());

            if (totalRows > 0){
                Query query = em.createNativeQuery(sql);
                query.setParameter("crisisID", crisisID);
                query.setParameter("modelFamilyID", modelFamilyID);
                query.setParameter("fromRecord", fromRecord);
                query.setParameter("limit", limit);

                List<Object[]> rows = query.getResultList();
                TrainingDataDTO trainingDataRow;
                for (Object[] row : rows) {
                    trainingDataRow = new TrainingDataDTO();
                    trainingDataRow.setLabelID(((Integer) row[0]).intValue());
                    trainingDataRow.setLabelName((String) row[1]);
                    trainingDataRow.setTweetJSON((String) row[2]);
                    trainingDataRow.setLabelerID(((Integer) row[3]).intValue());
                    trainingDataRow.setLabelerName((String) row[4]);
                    trainingDataRow.setLabeledTime(((Date) row[5]));
                    trainingDataRow.setTotalRows(totalRows);
                    trainingDataList.add(trainingDataRow);
                }
            }
            return trainingDataList;
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public ItemToLabelDTO getItemToLabel(int crisisID, int modelFamilyID) {
        // with attributeID get attribute and labels details
        // with crisisID get an item from document table for which hasHumanLabel is FALSE
        // packup both info into one class DTO and return
        
        NominalAttributeDTO attributeDTO = new NominalAttributeDTO();
        ItemToLabelDTO itemToLabel = new ItemToLabelDTO();
        try{
            String sqlToGetAttribute = "SELECT na.nominalAttributeID, na.code, na.name, na.description FROM nominal_attribute na"
                    + " JOIN model_family mf on mf.nominalAttributeID = na.nominalAttributeID WHERE mf.modelFamilyID = :modelFamilyID";
            Query attributeQuery = em.createNativeQuery(sqlToGetAttribute);
            attributeQuery.setParameter("modelFamilyID", modelFamilyID);
            List<Object[]> attributeResults = attributeQuery.getResultList();
            attributeDTO.setNominalAttributeID(((Integer)attributeResults.get(0)[0]).intValue());
            attributeDTO.setCode((String) attributeResults.get(0)[1]);
            attributeDTO.setName((String) attributeResults.get(0)[2]);
            attributeDTO.setDescription((String) attributeResults.get(0)[3]);
            
            String sqlToGetLabel = "SELECT nominalLabelCode, name, description FROM nominal_label WHERE nominalAttributeID = :attributeID";
            Query labelQuery = em.createNativeQuery(sqlToGetLabel);
            labelQuery.setParameter("attributeID", attributeDTO.getNominalAttributeID());
            List<Object[]> labelsResults = labelQuery.getResultList();
            
            Collection<NominalLabelDTO> labelDTOList = new ArrayList<NominalLabelDTO>();
            
            for (Object[] label: labelsResults){
                NominalLabelDTO labelDTO = new NominalLabelDTO();
                labelDTO.setNominalLabelCode((String)label[0]);
                labelDTO.setName((String) label[1]);
                labelDTO.setDescription((String) label[2]);
                
                labelDTOList.add(labelDTO);
            }
            attributeDTO.setNominalLabelCollection(labelDTOList);
            
            //here retrieve data from document table
            String sqlToGetItem = "SELECT documentID, data FROM document WHERE crisisID = :crisisID AND hasHumanLabels = 0 ORDER BY RAND() LIMIT 0, 1";
            Query documentQuery = em.createNativeQuery(sqlToGetItem);
            documentQuery.setParameter("crisisID", crisisID);
            List<Object[]> documentResult = documentQuery.getResultList();
            itemToLabel.setItemID(((BigInteger) documentResult.get(0)[0]));
            itemToLabel.setItemText(documentResult.get(0)[1].toString());
            
            itemToLabel.setAttribute(attributeDTO);
            
        }catch(NoResultException e) {
            return null;
            
        }
        return itemToLabel;
    }
}
