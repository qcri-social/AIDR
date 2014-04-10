package qa.qcri.aidr.trainer.api.service.impl;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import qa.qcri.aidr.trainer.api.dao.CrisisDao;
import qa.qcri.aidr.trainer.api.dao.ModelFamilyDao;
import qa.qcri.aidr.trainer.api.entity.Crisis;
import qa.qcri.aidr.trainer.api.entity.ModelFamily;
import qa.qcri.aidr.trainer.api.entity.NominalAttribute;
import qa.qcri.aidr.trainer.api.entity.NominalLabel;
import qa.qcri.aidr.trainer.api.service.CrisisService;
import qa.qcri.aidr.trainer.api.store.CodeLookUp;
import qa.qcri.aidr.trainer.api.template.CrisisJsonModel;
import qa.qcri.aidr.trainer.api.template.CrisisJsonOutput;
import qa.qcri.aidr.trainer.api.template.CrisisNominalAttributeModel;
import qa.qcri.aidr.trainer.api.template.NominalAttributeJsonModel;

import java.util.List;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: jilucas
 * Date: 9/11/13
 * Time: 2:33 PM
 * To change this template use File | Settings | File Templates.
 */

@Service("crisisService")
@Transactional(readOnly = true)
public class CrisisServiceImpl implements CrisisService {

    @Autowired
    private CrisisDao crisisDao;

    @Autowired
    private ModelFamilyDao modelFamilyDao;

    @Override
    public Crisis findByCrisisID(Long id) {

        return crisisDao.findByCrisisID(id);  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public CrisisJsonModel findByOptimizedCrisisID(Long id) {
        Crisis crisis = crisisDao.findByCrisisID(id);
        CrisisJsonModel jsonOutput = new CrisisJsonOutput().crisisJsonModelGenerator(crisis);
        return jsonOutput;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<Crisis> findByCriteria(String columnName, String value) {
        return crisisDao.findByCriteria(columnName,value);
    }

    @Override
    public List<Crisis> findByCriteria(String columnName, Long value) {
        return crisisDao.findByCriteria(columnName,value);  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<Crisis> findAllActiveCrisis() {
        return crisisDao.findAll();
    }

    @Override
    public List<CrisisNominalAttributeModel> getAllActiveCrisisNominalAttribute() {
        return modelFamilyDao.getActiveCrisisNominalAttribute();  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Set<NominalLabel> getNominalLabelByCrisisID(Long crisisID, Long nominalAtrributeID){
        Set<NominalLabel> nominalLabels = null;
       //[{"qa":"severe"}, {"qa":"mild"}, {"qa":"none"}]
        Set<ModelFamily> families = this.findByCrisisID(crisisID).getModelFamilySet();
        for(ModelFamily family : families){
            if(family.getNominalAttributeID().equals(nominalAtrributeID)){
                NominalAttribute nom = family.getNominalAttribute();
                nominalLabels =   nom.getNominalLabelSet();
            }
        }

        return nominalLabels;

    }

}
