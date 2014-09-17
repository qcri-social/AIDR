/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.qcri.aidr.predictui.util;

import qa.qcri.aidr.predictui.dto.*;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


//import org.codehaus.jackson.map.annotate.JsonSerialize;
//import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

import com.fasterxml.jackson.annotation.JsonInclude;


import qa.qcri.aidr.predictui.entities.AidrCollection;
import qa.qcri.aidr.predictui.entities.Crisis;
import qa.qcri.aidr.predictui.entities.Document;
import qa.qcri.aidr.predictui.entities.Model;
import qa.qcri.aidr.predictui.entities.ModelFamily;
import qa.qcri.aidr.predictui.entities.ModelNominalLabel;
import qa.qcri.aidr.predictui.entities.NominalAttribute;
import qa.qcri.aidr.predictui.entities.NominalLabel;

/**
 *
 * @author Muhammad Imran
 */
/*
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "responseWrapper", propOrder = {
    "statusCode",
    "message",
    "dataObject",
    "crisisTypes",
    "crisises",
    "nominalLabels",
    "nominalAttributes",
    "models",
    "documents",
    "modelFamilies",
    "modelNominalLabels",
    "modelNominalLabelsDTO",
    "collections",
    "modelWrapper",
    "modelHistoryWrapper",
    "taggersForCodes",
    "crisisAttributes",
    "trainingData",
    "total"
})*/
@XmlRootElement(name = "responseWrapper")
//@JsonSerialize(include = Inclusion.NON_DEFAULT)
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class ResponseWrapper implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@XmlElement protected String statusCode;
	@XmlElement protected String message;
	@XmlElement protected Object dataObject;
	@XmlElement private List<CrisisTypeDTO> crisisTypes;
	@XmlElement private List<Crisis> crisises;
	@XmlElement private List<NominalLabel> nominalLabels;
	@XmlElement private List<NominalAttribute> nominalAttributes;
	@XmlElement private List<Model> models;
	@XmlElement private List<Document> documents;
	@XmlElement private List<ModelFamily> modelFamilies;
	@XmlElement private List<ModelNominalLabel> modelNominalLabels;
	@XmlElement private List<ModelNominalLabelDTO> modelNominalLabelsDTO;
	@XmlElement private List<AidrCollection> collections;
	@XmlElement private List<ModelWrapper> modelWrapper;
	@XmlElement private List<ModelHistoryWrapper> modelHistoryWrapper;
	@XmlElement private List<TaggersForCodes> taggersForCodes;
	@XmlElement private List<CrisisAttributesDTO> crisisAttributes;
	@XmlElement private List<TrainingDataDTO> trainingData;
	@XmlElement private Integer total;

    public ResponseWrapper(String statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }
    
    public ResponseWrapper(String statusCode) {
        this.statusCode = statusCode;
    }

    public ResponseWrapper(String statusCode, String message, Object obj) {
        this.statusCode = statusCode;
        this.message = message;
        this.dataObject = obj;
    }

    public ResponseWrapper() {
//        why do we need initialize it in default constructor?
//        crisisTypes = new ArrayList<CrisisTypeDTO>();
    }

    public String getStatusCode() {
        return statusCode;
    }

    /**
     * @param statusCode the statusCode to set
     */
    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * @return the dataObject
     */
    public Object getDataObject() {
        return dataObject;
    }

    /**
     * @param dataObject the dataObject to set
     */
    public void setDataObject(Object dataObject) {
        this.dataObject = dataObject;
    }

    /**
     * @return the crisisTypes
     */
    public List<CrisisTypeDTO> getCrisisTypes() {
        return crisisTypes;
    }

    /**
     * @param crisisTypes the crisisTypes to set
     */
    public void setCrisisTypes(List<CrisisTypeDTO> crisisTypes) {
        this.crisisTypes = crisisTypes;
    }

    /**
     * @return the crisises
     */
    public List<Crisis> getCrisises() {
        return crisises;
    }

    /**
     * @param crisises the crisises to set
     */
    public void setCrisises(List<Crisis> crisises) {
        this.crisises = crisises;
    }

    /**
     * @return the nominalLabels
     */
    public List<NominalLabel> getNominalLabels() {
        return nominalLabels;
    }

    /**
     * @param nominalLabels the nominalLabels to set
     */
    public void setNominalLabels(List<NominalLabel> nominalLabels) {
        this.nominalLabels = nominalLabels;
    }

    /**
     * @return the nominalAttributes
     */
    public List<NominalAttribute> getNominalAttributes() {
        return nominalAttributes;
    }

    /**
     * @param nominalAttributes the nominalAttributes to set
     */
    public void setNominalAttributes(List<NominalAttribute> nominalAttributes) {
        this.nominalAttributes = nominalAttributes;
    }

    /**
     * @return the models
     */
    public List<Model> getModels() {
        return models;
    }

    /**
     * @param models the models to set
     */
    public void setModels(List<Model> models) {
        this.models = models;
    }

    /**
     * @return the documents
     */
    public List<Document> getDocuments() {
        return documents;
    }

    /**
     * @param documents the documents to set
     */
    public void setDocuments(List<Document> documents) {
        this.documents = documents;
    }

    /**
     * @return the modelFamilies
     */
    public List<ModelFamily> getModelFamilies() {
        return modelFamilies;
    }

    /**
     * @param modelFamilies the modelFamilies to set
     */
    public void setModelFamilies(List<ModelFamily> modelFamilies) {
        this.modelFamilies = modelFamilies;
    }

    /**
     * @return the modelNominalLabels
     */
    public List<ModelNominalLabel> getModelNominalLabels() {
        return modelNominalLabels;
    }

    /**
     * @param modelNominalLabels the modelNominalLabels to set
     */
    public void setModelNominalLabels(List<ModelNominalLabel> modelNominalLabels) {
        this.modelNominalLabels = modelNominalLabels;
    }

    /**
     * @return the collections
     */
    public List<AidrCollection> getCollections() {
        return collections;
    }

    /**
     * @param collections the collections to set
     */
    public void setCollections(List<AidrCollection> collections) {
        this.collections = collections;
    }

    /**
     * @return the modelWrapper
     */
    public List<ModelWrapper> getModelWrapper() {
        return modelWrapper;
    }

    /**
     * @param modelWrapper the modelWrapper to set
     */
    public void setModelWrapper(List<ModelWrapper> modelWrapper) {
        this.modelWrapper = modelWrapper;
    }

    /**
     * @return the modelHistoryWrapper
     */
    public List<ModelHistoryWrapper> getModelHistoryWrapper() {
        return modelHistoryWrapper;
    }

    /**
     * @param modelHistoryWrapper the modelHistoryWrapper to set
     */
    public void setModelHistoryWrapper(List<ModelHistoryWrapper> modelHistoryWrapper) {
        this.modelHistoryWrapper = modelHistoryWrapper;
    }

    /**
     * @return the taggersForCodes
     */
    public List<TaggersForCodes> getTaggersForCodes() {
        return taggersForCodes;
    }

    /**
     * @param taggersForCodes the taggersForCodes to set
     */
    public void setTaggersForCodes(List<TaggersForCodes> taggersForCodes) {
        this.taggersForCodes = taggersForCodes;
    }

    /**
     * @return the crisisAttributes
     */
    public List<CrisisAttributesDTO> getCrisisAttributes() {
        return crisisAttributes;
    }

    /**
     * @param crisisAttributes the crisisAttributes to set
     */
    public void setCrisisAttributes(List<CrisisAttributesDTO> crisisAttributes) {
        this.crisisAttributes = crisisAttributes;
    }

    /**
     * @return the trainingData
     */
    public List<TrainingDataDTO> getTrainingData() {
        return trainingData;
    }

    /**
     * @param trainingData the trainingData to set
     */
    public void setTrainingData(List<TrainingDataDTO> trainingData) {
        this.trainingData = trainingData;
    }

    /**
     * @return the modelNominalLabelsDTO
     */
    public List<ModelNominalLabelDTO> getModelNominalLabelsDTO() {
        return modelNominalLabelsDTO;
    }

    /**
     * @param modelNominalLabelsDTO the modelNominalLabelsDTO to set
     */
    public void setModelNominalLabelsDTO(List<ModelNominalLabelDTO> modelNominalLabelsDTO) {
        this.modelNominalLabelsDTO = modelNominalLabelsDTO;
    }

    /**
     * @return the total
     */
    public Integer getTotal() {
        return total;
    }

    /**
     * @param total the total to set
     */
    public void setTotal(Integer total) {
        this.total = total;
    }
}
