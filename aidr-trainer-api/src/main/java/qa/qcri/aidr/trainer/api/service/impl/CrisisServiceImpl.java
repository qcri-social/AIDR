package qa.qcri.aidr.trainer.api.service.impl;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import qa.qcri.aidr.dbmanager.ejb.remote.facade.CollectionResourceFacade;
import qa.qcri.aidr.trainer.api.dao.CrisisDao;
import qa.qcri.aidr.trainer.api.dao.CustomUITemplateDao;
import qa.qcri.aidr.trainer.api.dao.ModelFamilyDao;
import qa.qcri.aidr.trainer.api.entity.Collection;
import qa.qcri.aidr.trainer.api.entity.CustomUITemplate;
import qa.qcri.aidr.trainer.api.entity.ModelFamily;
import qa.qcri.aidr.trainer.api.entity.NominalAttribute;
import qa.qcri.aidr.trainer.api.entity.NominalLabel;
import qa.qcri.aidr.trainer.api.service.CrisisService;
import qa.qcri.aidr.trainer.api.store.CodeLookUp;
import qa.qcri.aidr.trainer.api.template.CrisisJsonModel;
import qa.qcri.aidr.trainer.api.template.CrisisJsonOutput;
import qa.qcri.aidr.trainer.api.template.CrisisNominalAttributeModel;

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

    @Autowired
    private CustomUITemplateDao customUITemplateDao;

    @Autowired
	private CollectionResourceFacade crisisService;
    
    @Override
    public Collection findByCrisisID(Long id) {

        return crisisDao.findByCrisisID(id);  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public CrisisJsonModel findByOptimizedCrisisID(Long id) {
    	Collection crisis = crisisDao.findByCrisisID(id);
        CrisisJsonModel jsonOutput = new CrisisJsonOutput().crisisJsonModelGenerator(crisis);
        List<CustomUITemplate> uiTemps =  customUITemplateDao.getTemplateByCrisisWithType(id, CodeLookUp.CURATOR_NAME);

        if (!uiTemps.isEmpty()) {
            jsonOutput.setCuratorInfo(uiTemps.get(0).getTemplateValue());
        }

        return jsonOutput;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<Collection> findByCriteria(String columnName, String value) {
        return crisisDao.findByCriteria(columnName,value);
    }

    @Override
    public List<Collection> findByCriteria(String columnName, Long value) {
        return crisisDao.findByCriteria(columnName,value);  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List findAllActiveCrisis() {
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

    @Override
    public List<Collection> findActiveCrisisInfo() {
        return crisisDao.findActiveCrisis();
    }

}
