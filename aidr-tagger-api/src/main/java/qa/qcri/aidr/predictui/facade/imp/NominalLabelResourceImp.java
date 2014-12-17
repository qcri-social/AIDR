/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.qcri.aidr.predictui.facade.imp;

import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import qa.qcri.aidr.dbmanager.dto.NominalLabelDTO;
import qa.qcri.aidr.predictui.facade.NominalLabelResourceFacade;

/**
 *
 * @author Imran
 */
@Stateless
public class NominalLabelResourceImp implements NominalLabelResourceFacade {

    @EJB
    NominalLabelResourceFacade remoteNominalLabelEJB;

    @Override
    public NominalLabelDTO addNominalLabel(NominalLabelDTO label) {
        return remoteNominalLabelEJB.addNominalLabel(label);
    }

    @Override
    public NominalLabelDTO getNominalLabelByID(Long id) {
        return remoteNominalLabelEJB.getNominalLabelByID(id);
    }

    @Override
    public NominalLabelDTO editNominalLabel(NominalLabelDTO label) {
        return remoteNominalLabelEJB.editNominalLabel(label);
    }

    @Override
    public List<NominalLabelDTO> getAllNominalLabel() {
        return remoteNominalLabelEJB.getAllNominalLabel();
    }

    @Override
    public void deleteNominalLabel(Long labelID) {
        remoteNominalLabelEJB.deleteNominalLabel(labelID);
    }
}
