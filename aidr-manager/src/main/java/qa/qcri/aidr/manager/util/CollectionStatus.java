package qa.qcri.aidr.manager.util;

public enum CollectionStatus {
	RUNNING("RUNNING"),
	STOPPED("STOPPED"),
	INITIALIZING("INITIALIZING"),
	FATAL_ERROR("FATAL_ERROR"),
	NOT_FOUND("NOT_FOUND"),
	RUNNING_WARNING("RUNNING_WARNING"),
	NOT_RUNNING("NOT_RUNNING"),
	TRASHED("TRASHED"),
	WARNING("WARNING");

	private CollectionStatus(String status) {
		this.status = status;
	}

	private String status;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public static CollectionStatus getByStatus(String status){
		for(CollectionStatus collectionStatus : CollectionStatus.values() ){
			if(collectionStatus.getStatus().equals(status)){
				return collectionStatus;
			}
		}
		return null;
	}
	
}
