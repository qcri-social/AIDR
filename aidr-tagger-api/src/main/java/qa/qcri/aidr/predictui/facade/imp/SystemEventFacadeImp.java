package qa.qcri.aidr.predictui.facade.imp;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;

import qa.qcri.aidr.dbmanager.dto.SystemEventDTO;
import qa.qcri.aidr.predictui.facade.SystemEventFacade;

@Stateless
public class SystemEventFacadeImp implements SystemEventFacade{

	private Logger logger = Logger.getLogger(SystemEventFacadeImp.class);
	
	@EJB
	private qa.qcri.aidr.dbmanager.ejb.remote.facade.SystemEventResourceFacade remotesystemEJB;
	
	@Override
	public void insertSystemEvent(SystemEventDTO event) {
		remotesystemEJB.insertSystemEvent(event);
				
	}

	@Override
	public void insertSystemEvent(String severity, String module,
			String description) {
		remotesystemEJB.insertSystemEvent(severity, module, description);
	}

	@Override
	public void insertSystemEvent(String severity, String module, String code,
			String description) {
		remotesystemEJB.insertSystemEvent(severity, module, code, description);
	}

	@Override
	public void insertSystemEvent(String severity, String module, String code,
			String description, Boolean emailSent) {
		logger.info("inserting into sys event");
		remotesystemEJB.insertSystemEvent(severity, module, code, description, emailSent);
	}

}
