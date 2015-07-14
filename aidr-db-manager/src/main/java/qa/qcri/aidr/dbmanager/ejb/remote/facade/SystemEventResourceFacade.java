package qa.qcri.aidr.dbmanager.ejb.remote.facade;

import javax.ejb.Remote;

import qa.qcri.aidr.dbmanager.dto.SystemEventDTO;
import qa.qcri.aidr.dbmanager.ejb.local.facade.CoreDBServiceFacade;
import qa.qcri.aidr.dbmanager.entities.misc.SystemEvent;

@Remote
public interface SystemEventResourceFacade extends CoreDBServiceFacade<SystemEvent, Long> {
	
	 public void insertSystemEvent(SystemEventDTO event);
	 
	 public void insertSystemEvent(String severity, String module, String description);
	 
	 public void insertSystemEvent(String severity, String module, String code, String description);
	 
	 public void insertSystemEvent(String severity, String module, String code, String description, Boolean emailSent);

}
