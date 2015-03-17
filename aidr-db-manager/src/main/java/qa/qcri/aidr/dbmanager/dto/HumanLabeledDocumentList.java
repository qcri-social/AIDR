package qa.qcri.aidr.dbmanager.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@JsonIgnoreProperties(ignoreUnknown=true)
@XmlRootElement
public class HumanLabeledDocumentList implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@XmlElement private List<HumanLabeledDocumentDTO> items;

	@XmlElement private Integer total;
	
	public HumanLabeledDocumentList() {
		this.items = new ArrayList<HumanLabeledDocumentDTO>();
		this.setTotal(0);
	}

	public HumanLabeledDocumentList(List<HumanLabeledDocumentDTO> items) {
		this();
		if (items != null) {
			this.setItems(items);
			this.setTotal(items.size());
		} 
	}
	
	public List<HumanLabeledDocumentDTO> getItems() {
		return this.items;
	}

	public void setItems(List<HumanLabeledDocumentDTO> items) {
		this.items = items;
		if (items != null && !items.isEmpty()) {
			this.setTotal(items.size());
		}
	}

	public Integer getTotal() {
		return this.total;
	}

	public void setTotal(Integer total) {
		if (total != null) {
			this.total = total;
		}
	}
	
	public String toJsonString() {
		Gson jsonObject = new GsonBuilder().serializeNulls().disableHtmlEscaping()
				.serializeSpecialFloatingPointValues().setPrettyPrinting()
				.create();
		try {
			String jsonString = jsonObject.toJson(this, HumanLabeledDocumentList.class);
			return jsonString;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
