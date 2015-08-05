package qa.qcri.aidr.dbmanager.ejb.remote.facade.imp;

import javax.ejb.Stateless;

import org.apache.log4j.Logger;

import qa.qcri.aidr.common.exception.PropertyNotSetException;
import qa.qcri.aidr.dbmanager.dto.SystemEventDTO;
import qa.qcri.aidr.dbmanager.ejb.local.facade.impl.CoreDBServiceFacadeImp;
import qa.qcri.aidr.dbmanager.ejb.remote.facade.SystemEventResourceFacade;
import qa.qcri.aidr.dbmanager.entities.misc.SystemEvent;

@Stateless(name="SystemEventResourceFacadeImp")
public class SystemEventResourceFacadeImp extends CoreDBServiceFacadeImp<SystemEvent, Long>
		implements SystemEventResourceFacade {

	private Logger logger = Logger.getLogger(SystemEventResourceFacadeImp.class);
	public SystemEventResourceFacadeImp() {
		super(SystemEvent.class);
	}

	@Override
	public void insertSystemEvent(SystemEventDTO event) {
		try {
			SystemEvent sysevent = event.toEntity();
			em.persist(sysevent);
			em.flush();
			em.refresh(sysevent);
		} catch (PropertyNotSetException e1) {
			logger.error("Error in insertSystemEvent.");
		}
		catch (Exception e) {
			logger.error("Unable to save event to database", e);
		}
	}

	@Override
	public void insertSystemEvent(String severity, String module, String description) {
		insertSystemEvent(severity,module,null,description);
	}

	@Override
	public void insertSystemEvent(String severity, String module, String code, String description) {
		insertSystemEvent(severity,module,code,description,false);
	}

	@Override
	public void insertSystemEvent(String severity, String module, String code,
			String description, Boolean emailSent) {
		try {
			SystemEvent sysevent = new SystemEvent(severity,module,code,description,emailSent);
			em.persist(sysevent);
			em.flush();
			em.refresh(sysevent);
		}
		catch (Exception e){
			logger.error("Unable to save event to database", e);
		}

	}

}
