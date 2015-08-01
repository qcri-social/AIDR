package qa.qcri.aidr.dbmanager.dto.taggerapi;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.log4j.Logger;

import qa.qcri.aidr.dbmanager.dto.HumanLabeledDocumentList;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@JsonIgnoreProperties(ignoreUnknown=true)
@XmlRootElement
public class HumanLabeledDocumentListWrapper implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7413555749237029785L;
	private Logger logger = Logger.getLogger(HumanLabeledDocumentListWrapper.class);
	
	@XmlElement HumanLabeledDocumentList dtoList;
	
	@XmlElement String queryString;
	
	public HumanLabeledDocumentListWrapper() {}
	
	public HumanLabeledDocumentListWrapper(HumanLabeledDocumentList dtoList, String queryString) {
		this();
		this.dtoList = dtoList;
		this.queryString = queryString;
	}
	

	public HumanLabeledDocumentList getDtoList() {
		return this.dtoList;
	}
	
	public void setDtoList(HumanLabeledDocumentList dtoList) {
		this.dtoList = dtoList;
	}
	
	public String getQueryString() {
		return this.queryString;
	}
	
	public void setQueryString(String queryString) {
		this.queryString = queryString;
	}
	
	public String toJsonString() {
		Gson jsonObject = new GsonBuilder().serializeNulls().disableHtmlEscaping()
				.serializeSpecialFloatingPointValues().setPrettyPrinting()
				.create();
		try {
			String jsonString = jsonObject.toJson(this, HumanLabeledDocumentListWrapper.class);
			return jsonString;
		} catch (Exception e) {
			logger.warn("Error while parsing document list.");
			return null;
		}
	}
}
