package qa.qcri.aidr.trainer.api.template;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import qa.qcri.aidr.dbmanager.dto.CollectionDTO;
import qa.qcri.aidr.dbmanager.dto.ModelFamilyDTO;
import qa.qcri.aidr.dbmanager.dto.NominalAttributeDTO;
import qa.qcri.aidr.dbmanager.dto.NominalLabelDTO;

/**
 * Created with IntelliJ IDEA.
 * User: jilucas
 * Date: 9/23/13
 * Time: 8:55 AM
 * To change this template use File | Settings | File Templates.
 */
public class CrisisJsonOutput {
	
	private static Logger logger=Logger.getLogger(CrisisJsonOutput.class);

    public CrisisJsonModel crisisJsonModelGenerator(CollectionDTO crisis){
        CrisisJsonModel crisisJsonModel = new CrisisJsonModel();
        if (crisis != null) { 
        	logger.info("received crisis = " + crisis.getCode() + ", id = " + crisis.getCrisisID());
        } else {
        	logger.info("received crisis = " + crisis);
        }
        crisisJsonModel.setCrisisID(crisis.getCrisisID());
        crisisJsonModel.setCode(crisis.getCode());
        crisisJsonModel.setName(crisis.getName());

        List<ModelFamilyDTO> modelFamilySet = crisis.getModelFamiliesDTO();
        Set<NominalAttributeJsonModel> nominalAttributeJsonModelSetTemp = new HashSet <NominalAttributeJsonModel>();
        for(ModelFamilyDTO obj : modelFamilySet){
            if(obj.isIsActive()){
                NominalAttributeJsonModel nominalAttributeJsonModel = new NominalAttributeJsonModel();

                NominalAttributeDTO nominalAttribute= obj.getNominalAttributeDTO();
                List<NominalLabelDTO> nominalLabelSet = nominalAttribute.getNominalLabelsDTO();

                nominalAttributeJsonModel.setCode(nominalAttribute.getCode());
                nominalAttributeJsonModel.setName(nominalAttribute.getName());
                nominalAttributeJsonModel.setDescription(nominalAttribute.getDescription());
                nominalAttributeJsonModel.setNominalAttributeID(nominalAttribute.getNominalAttributeId());

                Set<NominalLabelJsonModel> nominalLabelJsonModelSetTemp = new HashSet <NominalLabelJsonModel>();
                for(NominalLabelDTO nominalLabel : nominalLabelSet){

                    NominalLabelJsonModel nominalLabelJsonModel = new NominalLabelJsonModel();
                    nominalLabelJsonModel.setName(nominalLabel.getName());
                    nominalLabelJsonModel.setNorminalLabelCode(nominalLabel.getNominalLabelCode());
                    nominalLabelJsonModel.setNorminalLabelID(nominalLabel.getNominalLabelId().longValue());
                    nominalLabelJsonModel.setDescription(nominalLabel.getDescription());
                    nominalLabelJsonModel.setSequence(nominalLabel.getSequence());
                    nominalLabelJsonModelSetTemp.add(nominalLabelJsonModel) ;

                }
                nominalAttributeJsonModel.setNominalLabelJsonModelSet(nominalLabelJsonModelSetTemp);
                nominalAttributeJsonModelSetTemp.add(nominalAttributeJsonModel);
            }
        }
        crisisJsonModel.setNominalAttributeJsonModelSet(nominalAttributeJsonModelSetTemp);

        return crisisJsonModel;
    }
}
