package qa.qcri.aidr.trainer.api.template;

import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import qa.qcri.aidr.dbmanager.dto.CollectionDTO;
import qa.qcri.aidr.dbmanager.dto.ModelFamilyDTO;
import qa.qcri.aidr.dbmanager.dto.NominalLabelDTO;
import qa.qcri.aidr.dbmanager.entities.model.NominalLabel;
import qa.qcri.aidr.dbmanager.entities.task.Document;
import qa.qcri.aidr.dbmanager.entities.task.DocumentNominalLabel;
import qa.qcri.aidr.dbmanager.entities.task.DocumentNominalLabelId;
import qa.qcri.aidr.dbmanager.entities.task.TaskAnswer;
import qa.qcri.aidr.trainer.api.service.CrisisService;

/**
 * Created with IntelliJ IDEA.
 * User: jilucas
 * Date: 9/16/13
 * Time: 4:57 PM
 * To change this template use File | Settings | File Templates.
 */
public class PybossaTemplate {
	
	private static Logger logger=Logger.getLogger(PybossaTemplate.class);

    public TaskAnswerResponse getPybossaTaskAnswer(String data, CrisisService crisisService){

        TaskAnswer taskAnswer = null;
        JSONArray  attributeIDJson = new JSONArray();
        TaskAnswerResponse taskAnswerResponse = new TaskAnswerResponse();

        try {
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(data);

            JSONArray jsonObject = (JSONArray) obj;

            Iterator itr= jsonObject.iterator();

            while(itr.hasNext()){
                JSONArray taskAnswerJson = new JSONArray();

                JSONObject featureJsonObj = (JSONObject)itr.next();
                JSONObject info = (JSONObject)featureJsonObj.get("info");
                String category = (String)info.get("category");
                String[] categorySet = category.split("\\,");
                Long crisisID = (Long)info.get("crisisID");
                Long documentID = (Long)info.get("documentID");
                Long userID = (Long)info.get("aidrID");
                Long attributeID = (Long)info.get("attributeID");

                CollectionDTO crisis = crisisService.findByCrisisID(crisisID) ;
                List<ModelFamilyDTO> modelFamilySet= crisis.getModelFamiliesDTO();

                DocumentNominalLabel documentNominalLabel = null;
                // String category ="Food and water";

                for (ModelFamilyDTO modelFamily : modelFamilySet){
                    if(modelFamily.getNominalAttributeDTO().getNominalAttributeId().equals(attributeID)){
                        ModelFamilyDTO currentModelFamily = modelFamily;

                        List<NominalLabelDTO> nominalLabelSet = modelFamily.getNominalAttributeDTO().getNominalLabelsDTO();
                        logger.info("attribute name : "   + modelFamily.getNominalAttributeDTO().getName() + "\n");
                        for (NominalLabelDTO nominalLabel : nominalLabelSet){
                            JSONObject taskAnswerElement = new JSONObject();
                            if(nominalLabel.getNominalAttributeDTO().getNominalAttributeId().equals(attributeID)) {
                                if(findMatchingLabel(categorySet, nominalLabel.getNominalLabelCode())) {
                                    Long labelID = nominalLabel.getNominalLabelId().longValue();
                                    if(!attributeIDJson.contains(attributeID) ){
                                        attributeIDJson.add(attributeID ) ;
                                    }

                                    taskAnswerElement.put("attributeID", attributeID) ;
                                    taskAnswerElement.put("labelID", labelID) ;

                                    taskAnswerJson.add(taskAnswerElement);
                                    DocumentNominalLabelId documentNominalLabelId = new DocumentNominalLabelId(documentID,labelID, userID);
                                    NominalLabel nl = new NominalLabel();
                                    nl.setNominalLabelId(labelID);
                                    Document d = new Document();
                                    d.setDocumentId(documentID);
                                    documentNominalLabel = new DocumentNominalLabel(documentNominalLabelId, nl, d);
                                    taskAnswerResponse.setDocumentNominalLabelList(documentNominalLabel);
                                }
                            }

                        }
                    }

                }

                taskAnswerJson.add(getUtilDatJson(featureJsonObj));
                taskAnswer = new TaskAnswer(documentID, userID, taskAnswerJson.toJSONString()) ;
                taskAnswerResponse.setTaskAnswerList(taskAnswer);
                taskAnswerResponse.setJedisJson(getJedisJson(crisisID, attributeIDJson));
                taskAnswerResponse.setDocumentID(documentID);
                taskAnswerResponse.setUserID(userID);

               // System.out.print("category: " + category + " crisisID:" + crisisID+  "   documentID:" + documentID + "taskAnswerJson" + taskAnswerJson.toJSONString());

            }
        } catch (ParseException e) {
            //e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        	logger.error("Exception while getting Pybossa task answer \t"+e.getStackTrace());
        }
        return taskAnswerResponse;
    }


    private boolean findMatchingLabel(String[] categorySet, String label){

        boolean found = false;
        for(int i=0; i< categorySet.length; i++){
              String category =  categorySet[i];
              if(category.equalsIgnoreCase(label)) {
                  found = true;
              }
        }

        return found;

    }

    private String getJedisJson(Long crisisID, JSONArray attributeIDJson){
        JSONObject jedisJson = new JSONObject();

        jedisJson.put("crisis_id", crisisID);
        jedisJson.put("attributes", attributeIDJson);

        return jedisJson.toJSONString();

    }

    private JSONObject getUtilDatJson(JSONObject featureJsonObj){

        JSONObject utilJson = new JSONObject();
        utilJson.put("clientuserID",featureJsonObj.get("user_id")) ;
        utilJson.put("datetimelog",featureJsonObj.get("dateHistory")) ;

        return utilJson;
    }
}
