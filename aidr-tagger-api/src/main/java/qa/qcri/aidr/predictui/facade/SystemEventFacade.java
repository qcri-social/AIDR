package qa.qcri.aidr.predictui.facade;

import javax.ejb.Local;

import qa.qcri.aidr.dbmanager.dto.SystemEventDTO;

@Local
public interface SystemEventFacade {
	
	public void insertSystemEvent(SystemEventDTO event);
	
	public void insertSystemEvent(String severity, String module, String description);
	 
	public void insertSystemEvent(String severity, String module, String code, String description);
	
	public void insertSystemEvent(String severity, String module, String code, String description, Boolean emailSent);

}
