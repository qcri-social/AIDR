/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package qa.qcri.aidr.dbmanager.ejb.remote.facade;

import java.util.List;
import javax.ejb.Remote;
import qa.qcri.aidr.dbmanager.entities.model.Model;

/**
 *
 * @author Imran
 */
@Remote
public interface ModelFacadeRemote {
    
    public List<Model> getModelByID();
    
}
