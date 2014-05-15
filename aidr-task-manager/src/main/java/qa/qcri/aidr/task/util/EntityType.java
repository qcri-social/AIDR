package qa.qcri.aidr.task.util;

public enum EntityType {
	DOCUMENT("Document"), TASK_ASSIGNMENT("TaskAssignment"), TASK_ANSWER("TaskAnswer"),
	CRISIS("Crisis"), DOCUMENT_NOMINAL_LABEL("DocumentNominalLabel");
	
	private String entityType;
	
	private EntityType(String entityType) {
		this.entityType = entityType;
	}

	public String getEntityType() {
		return entityType;
	}

	public void setEntityType(String entityType) {
		this.entityType = entityType;	
	}
	
	public static EntityType getByEntityType(String entityType){
		for(EntityType eType: EntityType.values() ){
			if(eType.getEntityType().equals(entityType)){
				return eType;
			}
		}
		return null;
	}
	
}
