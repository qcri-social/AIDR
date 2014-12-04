/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.qcri.aidr.dbmanager.ejb.remote.facade.imp;

import java.util.List;
import javax.ejb.Stateless;
import org.apache.log4j.Logger;
import qa.qcri.aidr.common.exception.PropertyNotSetException;
import qa.qcri.aidr.dbmanager.dto.NominalAttributeDTO;
import qa.qcri.aidr.dbmanager.ejb.local.facade.impl.CoreDBServiceFacadeImp;
import qa.qcri.aidr.dbmanager.ejb.remote.facade.NominalAttributeResourceFacade;
import qa.qcri.aidr.dbmanager.entities.model.NominalAttribute;

/**
 *
 * @author Imran
 */
@Stateless(name="NominalAttributeResourceFacadeImp")
public class NominalAttributeResourceFacadeImp extends CoreDBServiceFacadeImp<NominalAttribute, Long> implements NominalAttributeResourceFacade {

    private static Logger logger = Logger.getLogger("db-manager-log");

    public boolean addAttribute(NominalAttributeDTO attribute) throws PropertyNotSetException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public NominalAttribute editAttribute(NominalAttribute attribute) throws PropertyNotSetException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void deleteAttribute(int attributeID) throws PropertyNotSetException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public NominalAttribute getAttributeByID(int attributeID) throws PropertyNotSetException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public List<NominalAttribute> getAllAttributes() throws PropertyNotSetException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Integer isAttributeExists(String attributeCode) throws PropertyNotSetException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
}
