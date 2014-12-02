package qa.qcri.aidr.predictui.facade;

import javax.ejb.Local;


@Local
public interface CrisisManagementResourceFacade {

	public String trashByCrisisCode(String crisisCode);
	public String untrashByCrisisCode(String crisisCode);
}
