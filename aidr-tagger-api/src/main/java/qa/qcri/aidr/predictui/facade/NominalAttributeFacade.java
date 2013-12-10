/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.qcri.aidr.predictui.facade;

import java.util.List;
import javax.ejb.Local;
import qa.qcri.aidr.predictui.dto.CrisisAttributesDTO;
import qa.qcri.aidr.predictui.entities.NominalAttribute;

/**
 *
 * @author Imran
 */
@Local
public interface NominalAttributeFacade {
    
    public NominalAttribute addAttribute(NominalAttribute attribute);
    public NominalAttribute editAttribute(NominalAttribute attribute);
    public void deleteAttribute(int attributeID);
    public NominalAttribute getAttributeByID(int attributeID);
    public List<NominalAttribute> getAllAttributes();
    public List<CrisisAttributesDTO> getAllAttributesExceptCrisis(int crisisID);
    public Integer isAttributeExists(String attributeCode);
    
    
    
    
}
