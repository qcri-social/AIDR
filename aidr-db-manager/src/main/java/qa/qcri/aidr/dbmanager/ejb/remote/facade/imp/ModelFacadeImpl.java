/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package qa.qcri.aidr.dbmanager.ejb.remote.facade.imp;

import javax.ejb.Stateless;
import qa.qcri.aidr.dbmanager.dto.ModelDTO;
import qa.qcri.aidr.dbmanager.ejb.remote.facade.ModelFacadeRemote;

/**
 *
 * @author Imran
 */
@Stateless
public class ModelFacadeImpl implements ModelFacadeRemote{

    public ModelDTO getModelByID() {
        throw new UnsupportedOperationException("Not supported yet."); 
    }
    
    
}
