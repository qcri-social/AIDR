package qa.qcri.aidr.trainer.api.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import qa.qcri.aidr.common.exception.PropertyNotSetException;
import qa.qcri.aidr.dbmanager.dto.CollectionDTO;
import qa.qcri.aidr.dbmanager.ejb.remote.facade.CollectionResourceFacade;
import qa.qcri.aidr.dbmanager.ejb.remote.facade.ModelFamilyResourceFacade;
import qa.qcri.aidr.dbmanager.entities.misc.Collection;
import qa.qcri.aidr.dbmanager.entities.misc.CustomUiTemplate;
import qa.qcri.aidr.dbmanager.entities.model.ModelFamily;
import qa.qcri.aidr.dbmanager.entities.model.NominalAttribute;
import qa.qcri.aidr.dbmanager.entities.model.NominalLabel;
import qa.qcri.aidr.trainer.api.service.CrisisService;
import qa.qcri.aidr.trainer.api.service.CustomUITemplateService;
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
	protected static Logger logger = Logger.getLogger(CrisisServiceImpl.class);
	
    @Autowired
    private CollectionResourceFacade remoteCollectionResourceEJB;

    @Autowired
    private ModelFamilyResourceFacade remoteModelFamilyResourceEJB;

    @Autowired
    private CustomUITemplateService customUITemplateService;
    
    @Override
    public Collection findByCrisisID(Long id) {
        return remoteCollectionResourceEJB.getById(id);  //To change body of implemented methods use File | Settings | File Templates.
    }
    
    @Override
    public List<CollectionDTO> findByCrisisCode(String crisisCode) {
    	try {
    		return remoteCollectionResourceEJB.findByCriteria("code", crisisCode);  
        }catch(Exception e){
        	logger.error("Exception while fetching collection for crisis="+crisisCode+"\t"+e.getStackTrace());
        	return null;
        }
    }

    @Override
    public CrisisJsonModel findByOptimizedCrisisID(Long id) {
    	Collection crisis = remoteCollectionResourceEJB.getById(id);
        CrisisJsonModel jsonOutput = new CrisisJsonOutput().crisisJsonModelGenerator(crisis);
        List<CustomUiTemplate> uiTemps =  customUITemplateService.getCustomUiTemplateByCrisisWithType(id, CodeLookUp.CURATOR_NAME);

        if (!uiTemps.isEmpty()) {
            jsonOutput.setCuratorInfo(uiTemps.get(0).getTemplateValue());
        }

        return jsonOutput;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<Collection> findByCriteria(String columnName, Object value) {
    	return remoteCollectionResourceEJB.getAllByCriteria(Restrictions.eq(columnName,value));
    }

  /*  @Override
    public List<Collection> findByCriteria(String columnName, Long value) {
        return crisisDao.findByCriteria(columnName,value);  //To change body of implemented methods use File | Settings | File Templates.
    }*/

    @Override
    public List<Collection> findAllActiveCrisis() {
        return remoteCollectionResourceEJB.getAll();
    }

    @Override
    public List<CrisisNominalAttributeModel> getAllActiveCrisisNominalAttribute() {
    	List<CrisisNominalAttributeModel> crisisNominalAttributeModelList = new ArrayList<CrisisNominalAttributeModel>();

        List<ModelFamily> modelFamilyList =  remoteModelFamilyResourceEJB.getAllByCriteria(Restrictions.eq("isActive",true)) ;

        Iterator<ModelFamily> iterator = modelFamilyList.iterator();

        while (iterator.hasNext()) {
            ModelFamily modelFamily = (ModelFamily)iterator.next();
            Long crisisID = modelFamily.getCollection().getCrisisId();
            Long attributeID = modelFamily.getNominalAttribute().getNominalAttributeId();
            Collection crisis = modelFamily.getCollection();
            if(crisis.isMicromapperEnabled())
            {
            	CrisisNominalAttributeModel temp = new CrisisNominalAttributeModel(crisisID, attributeID);
            	if(!findDuplicate(crisisNominalAttributeModelList, temp)) {
            		crisisNominalAttributeModelList.add(temp);
            	}
            }
        }
        return crisisNominalAttributeModelList;
    }

    private boolean findDuplicate(List<CrisisNominalAttributeModel> crisisNominalAttributeModelList, CrisisNominalAttributeModel crisisNominalAttributeModel){
        boolean found = false;
        Iterator<CrisisNominalAttributeModel> iterator = crisisNominalAttributeModelList.iterator();

        while (iterator.hasNext()){
            CrisisNominalAttributeModel cm =  (CrisisNominalAttributeModel)iterator.next();
            if(cm.getCririsID().equals(crisisNominalAttributeModel.getCririsID())){
                if(cm.getNominalAttributeID().equals(crisisNominalAttributeModel.getNominalAttributeID())){
                    found = true;
                }
            }
        }
        return found;
    }
    @Override
    public Set<NominalLabel> getNominalLabelByCrisisID(Long crisisID, Long nominalAtrributeID){
        Set<NominalLabel> nominalLabels = null;
       //[{"qa":"severe"}, {"qa":"mild"}, {"qa":"none"}]
        Set<ModelFamily> families = new HashSet<ModelFamily>(this.findByCrisisID(crisisID).getModelFamilies());
        for(ModelFamily family : families){
            if(family.getNominalAttribute().getNominalAttributeId().equals(nominalAtrributeID)){
                NominalAttribute nom = family.getNominalAttribute();
                nominalLabels =   new HashSet<NominalLabel>(nom.getNominalLabels());
            }
        }

        return nominalLabels;

    }

    @Override
    public List<Collection> findActiveCrisisInfo() {
    	List<Collection> collectionList = new ArrayList<Collection>();
		try {
			List<CollectionDTO> activeCollectionsDTOList = remoteCollectionResourceEJB.findActiveCrisis();
		
			
			for (CollectionDTO collectionDTO : activeCollectionsDTOList) {
				collectionList.add(collectionDTO.toEntity());
			}
		}catch (PropertyNotSetException e) {
			logger.error("PropertyNotSetException while fetching all active collections",e);
		}
    	return collectionList;
    }

}
