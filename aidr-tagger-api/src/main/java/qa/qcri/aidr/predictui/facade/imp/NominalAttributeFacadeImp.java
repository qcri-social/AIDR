/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.qcri.aidr.predictui.facade.imp;

import qa.qcri.aidr.predictui.facade.*;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import qa.qcri.aidr.common.exception.PropertyNotSetException;
import qa.qcri.aidr.dbmanager.dto.NominalAttributeDTO;

/**
 *
 * @author Imran
 */
@Stateless
public class NominalAttributeFacadeImp implements NominalAttributeFacade {

	@EJB
	private qa.qcri.aidr.dbmanager.ejb.remote.facade.NominalAttributeResourceFacade nominalAttributeRemoteEJB;
	
    public List<NominalAttributeDTO> getAllAttributes() throws PropertyNotSetException {
        return nominalAttributeRemoteEJB.getAllAttributes();
    }

    public List<qa.qcri.aidr.dbmanager.dto.CrisisAttributesDTO> getAllAttributesExceptCrisis(Long crisisID) throws PropertyNotSetException {
        return nominalAttributeRemoteEJB.getAllAttributesExceptCrisis(crisisID);
    }

    public NominalAttributeDTO addAttribute(NominalAttributeDTO attribute) throws PropertyNotSetException {
        return nominalAttributeRemoteEJB.addAttribute(attribute);
    }

    public NominalAttributeDTO editAttribute(NominalAttributeDTO attribute) throws PropertyNotSetException {
        return nominalAttributeRemoteEJB.editAttribute(attribute);

    }

    public NominalAttributeDTO getAttributeByID(Long attributeID) throws PropertyNotSetException {
        return nominalAttributeRemoteEJB.getAttributeByID(attributeID);
    }

    public boolean deleteAttribute(Long attributeID) throws PropertyNotSetException {
        return nominalAttributeRemoteEJB.deleteAttribute(attributeID);
    }

    public Long isAttributeExists(String attributeCode) throws PropertyNotSetException {
        return nominalAttributeRemoteEJB.isAttributeExists(attributeCode);

    }
}
