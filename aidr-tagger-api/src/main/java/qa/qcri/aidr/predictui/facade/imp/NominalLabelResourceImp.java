/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.qcri.aidr.predictui.facade.imp;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import qa.qcri.aidr.common.exception.PropertyNotSetException;
import qa.qcri.aidr.dbmanager.dto.NominalLabelDTO;
import qa.qcri.aidr.predictui.facade.NominalLabelResourceFacade;

/**
 *
 * @author Imran
 * updated by Koushik
 */
@Stateless
public class NominalLabelResourceImp implements NominalLabelResourceFacade {


    @EJB
	private qa.qcri.aidr.dbmanager.ejb.remote.facade.NominalLabelResourceFacade remoteNominalLabelEJB;

    @Override
    public NominalLabelDTO addNominalLabel(NominalLabelDTO label) {
        try {
			return remoteNominalLabelEJB.addNominalLabel(label);
		} catch (PropertyNotSetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
    }

    @Override
    public NominalLabelDTO getNominalLabelByID(Long id) {
        try {
			return remoteNominalLabelEJB.getNominalLabelByID(id);
		} catch (PropertyNotSetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
    }

    @Override
    public NominalLabelDTO editNominalLabel(NominalLabelDTO label) {
        try {
			return remoteNominalLabelEJB.editNominalLabel(label);
		} catch (PropertyNotSetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
    }

    @Override
    public List<NominalLabelDTO> getAllNominalLabel() {
        try {
			return remoteNominalLabelEJB.getAllNominalLabels();
		} catch (PropertyNotSetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
    }

    @Override
    public void deleteNominalLabel(Long labelID) {
        try {
			remoteNominalLabelEJB.deleteNominalLabelByID(labelID);
		} catch (PropertyNotSetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
