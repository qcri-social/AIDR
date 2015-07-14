package qa.qcri.aidr.trainer.api.dao.impl;

import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import qa.qcri.aidr.trainer.api.dao.ModelFamilyDao;
import qa.qcri.aidr.trainer.api.entity.Crisis;
import qa.qcri.aidr.trainer.api.entity.ModelFamily;
import qa.qcri.aidr.trainer.api.template.CrisisNominalAttributeModel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jilucas
 * Date: 9/28/13
 * Time: 6:32 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class ModelFamilyDaoImpl extends AbstractDaoImpl<ModelFamily,String> implements ModelFamilyDao {

    protected ModelFamilyDaoImpl(){
        super(ModelFamily.class);
    }

    @Override
    public List<CrisisNominalAttributeModel> getActiveCrisisNominalAttribute() {

        List<CrisisNominalAttributeModel> crisisNominalAttributeModelList = new ArrayList<CrisisNominalAttributeModel>();

        List<ModelFamily> modelFamilyList =  findByCriteria(Restrictions.eq("isActive",true)) ;

        Iterator<ModelFamily> iterator = modelFamilyList.iterator();

        while (iterator.hasNext()) {
            ModelFamily modelFamily = (ModelFamily)iterator.next();
            Long crisisID = modelFamily.getCrisisID();
            Long attributeID = modelFamily.getNominalAttributeID();
            Crisis crisis = modelFamily.getCrisis();
            if(crisis.getMicromapperEnabled() != null && crisis.getMicromapperEnabled())
            {
            	CrisisNominalAttributeModel temp = new CrisisNominalAttributeModel(crisisID, attributeID);

            	if(!findDuplicate(crisisNominalAttributeModelList, temp)) {
            		crisisNominalAttributeModelList.add(temp);
            	}
            }

        }

        return crisisNominalAttributeModelList;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<ModelFamily> getActiveCrisisNominalAttributeByCrisisID(Long crisisID) {

        return findByCriteria(Restrictions.conjunction()
                .add(Restrictions.eq("crisisID",crisisID))
                .add(Restrictions.eq("isActive", true)));


    }

    private boolean findDuplicate(List<CrisisNominalAttributeModel> crisisNominalAttributeModelList, CrisisNominalAttributeModel crisisNominalAttributeModel){

        boolean found = false;
        Iterator<CrisisNominalAttributeModel> iterator = crisisNominalAttributeModelList.iterator();

        while (iterator.hasNext()){
            CrisisNominalAttributeModel cm =  (CrisisNominalAttributeModel)iterator.next();
            Long crisisID = cm.getCririsID();
            Long attID = cm.getNominalAttributeID();
            if(cm.getCririsID().equals(crisisNominalAttributeModel.getCririsID())){
                if(cm.getNominalAttributeID().equals(crisisNominalAttributeModel.getNominalAttributeID())){
                    found = true;
                }
            }
        }

        return found;
    }
}
