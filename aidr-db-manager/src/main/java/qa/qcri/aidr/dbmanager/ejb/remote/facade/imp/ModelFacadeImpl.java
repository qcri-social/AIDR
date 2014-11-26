/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package qa.qcri.aidr.dbmanager.ejb.remote.facade.imp;

import java.util.List;
import javax.ejb.Stateless;
import qa.qcri.aidr.dbmanager.ejb.remote.facade.ModelFacadeRemote;
import qa.qcri.aidr.dbmanager.entities.model.Model;

/**
 *
 * @author Imran
 */
@Stateless
public class ModelFacadeImpl implements ModelFacadeRemote{

    public List<Model> getModelByID() {
        throw new UnsupportedOperationException("Not supported yet."); 
    }
    
    
}
