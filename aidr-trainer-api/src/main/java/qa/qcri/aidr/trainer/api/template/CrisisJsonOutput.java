package qa.qcri.aidr.trainer.api.template;

import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

import qa.qcri.aidr.dbmanager.entities.misc.Collection;
import qa.qcri.aidr.dbmanager.entities.model.ModelFamily;
import qa.qcri.aidr.dbmanager.entities.model.NominalAttribute;
import qa.qcri.aidr.dbmanager.entities.model.NominalLabel;

/**
 * Created with IntelliJ IDEA.
 * User: jilucas
 * Date: 9/23/13
 * Time: 8:55 AM
 * To change this template use File | Settings | File Templates.
 */
public class CrisisJsonOutput {
	
	private static Logger logger=Logger.getLogger(CrisisJsonOutput.class);

    public CrisisJsonModel crisisJsonModelGenerator(Collection crisis){
        CrisisJsonModel crisisJsonModel = new CrisisJsonModel();
        if (crisis != null) { 
        	logger.info("received crisis = " + crisis.getCode() + ", id = " + crisis.getCrisisId());
        } else {
        	logger.info("received crisis = " + crisis);
        }
        crisisJsonModel.setCrisisID(crisis.getCrisisId());
        crisisJsonModel.setCode(crisis.getCode());
        crisisJsonModel.setName(crisis.getName());

        Set<ModelFamily> modelFamilySet =  new HashSet<ModelFamily>(crisis.getModelFamilies());
        Set<NominalAttributeJsonModel> nominalAttributeJsonModelSetTemp = new HashSet <NominalAttributeJsonModel>();
        for(ModelFamily obj : modelFamilySet){
            if(obj.isIsActive()){
                NominalAttributeJsonModel nominalAttributeJsonModel = new NominalAttributeJsonModel();

                NominalAttribute nominalAttribute= obj.getNominalAttribute();
                Set<NominalLabel> nominalLabelSet = new HashSet<NominalLabel>(nominalAttribute.getNominalLabels()) ;

                nominalAttributeJsonModel.setCode(nominalAttribute.getCode());
                nominalAttributeJsonModel.setName(nominalAttribute.getName());
                nominalAttributeJsonModel.setDescription(nominalAttribute.getDescription());
                nominalAttributeJsonModel.setNominalAttributeID(nominalAttribute.getNominalAttributeId());

                Set<NominalLabelJsonModel> nominalLabelJsonModelSetTemp = new HashSet <NominalLabelJsonModel>();
                for(NominalLabel nominalLabel : nominalLabelSet){

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
