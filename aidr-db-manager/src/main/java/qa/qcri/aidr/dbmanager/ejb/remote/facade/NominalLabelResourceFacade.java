package qa.qcri.aidr.dbmanager.ejb.remote.facade;

import java.util.List;

import javax.ejb.Remote;

import qa.qcri.aidr.common.exception.PropertyNotSetException;
import qa.qcri.aidr.dbmanager.dto.NominalLabelDTO;
import qa.qcri.aidr.dbmanager.ejb.local.facade.CoreDBServiceFacade;
import qa.qcri.aidr.dbmanager.entities.model.NominalLabel;

@Remote
public interface NominalLabelResourceFacade extends CoreDBServiceFacade<NominalLabel, Long>{
    
    public void saveNominalLabel(NominalLabelDTO nominalLabel) throws PropertyNotSetException;
    
    public NominalLabelDTO addNominalLabel(NominalLabelDTO nominalLabel) throws PropertyNotSetException;
    
    public NominalLabelDTO editNominalLabel(NominalLabelDTO nominalLabel) throws PropertyNotSetException;
    
    public Integer deleteNominalLabel(NominalLabelDTO nominalLabel) throws PropertyNotSetException;
    
    public Integer deleteNominalLabelByID(Long nominalLabelID) throws PropertyNotSetException;
    
    public NominalLabelDTO getNominalLabelByID(Long nominalLabelID) throws PropertyNotSetException;
    
    public NominalLabelDTO getNominalLabelWithAllFieldsByID(Long nominalLabelID) throws PropertyNotSetException;
    
    public NominalLabelDTO getNominalLabelByCode(String code) throws PropertyNotSetException;
    
    public NominalLabelDTO getNominalLabelWithAllFieldsByCode(String code) throws PropertyNotSetException;
    
    public List<NominalLabelDTO> getAllNominalLabels() throws PropertyNotSetException;
    
    public Boolean isNominalLabelExists(Long nominalLabelID);
    
    public Boolean isNominalLabelExists(String code);
    
}
