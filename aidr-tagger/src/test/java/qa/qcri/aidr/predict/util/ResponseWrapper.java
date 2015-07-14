/**
 * 
 */
package qa.qcri.aidr.predict.util;

import java.util.List;

import qa.qcri.aidr.dbmanager.dto.ModelFamilyDTO;
import qa.qcri.aidr.dbmanager.dto.ModelNominalLabelDTO;
import qa.qcri.aidr.dbmanager.dto.taggerapi.ModelHistoryWrapper;

/**
 * @author Latika
 *
 */
public class ResponseWrapper {

	    protected String statusCode;
	    protected String message;
	    protected Object dataObject;
	    private Integer total;
	    private Long entityID;
	    private ModelHistoryWrapper[] modelHistoryWrapper;
	    private ModelNominalLabelDTO[] modelNominalLabelsDTO;
	    private ModelFamilyDTO[] modelFamilies;
	    
	    public ModelFamilyDTO[] getModelFamilies() {
			return modelFamilies;
		}

		public void setModelFamilies(ModelFamilyDTO[] modelFamilies) {
			this.modelFamilies = modelFamilies;
		}

		public ModelNominalLabelDTO[] getModelNominalLabelsDTO() {
			return modelNominalLabelsDTO;
		}

		public void setModelNominalLabelsDTO(
				ModelNominalLabelDTO[] modelNominalLabelsDTO) {
			this.modelNominalLabelsDTO = modelNominalLabelsDTO;
		}

		public String getStatusCode() {
			return statusCode;
		}

		public void setStatusCode(String statusCode) {
			this.statusCode = statusCode;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}

		public Object getDataObject() {
			return dataObject;
		}

		public void setDataObject(Object dataObject) {
			this.dataObject = dataObject;
		}

		public Integer getTotal() {
			return total;
		}

		public void setTotal(Integer total) {
			this.total = total;
		}

		public Long getEntityID() {
			return entityID;
		}

		public void setEntityID(Long entityID) {
			this.entityID = entityID;
		}

		public ModelHistoryWrapper[] getModelHistoryWrapper() {
			return modelHistoryWrapper;
		}

		public void setModelHistoryWrapper(ModelHistoryWrapper[] modelHistoryWrapper) {
			this.modelHistoryWrapper = modelHistoryWrapper;
		}
}
