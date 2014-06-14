package qa.qcri.aidr.trainer.api.dao.impl;

import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import qa.qcri.aidr.trainer.api.dao.CrisisDao;
import qa.qcri.aidr.trainer.api.entity.Crisis;
import qa.qcri.aidr.trainer.api.entity.ModelFamily;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: jilucas
 * Date: 9/11/13
 * Time: 8:35 AM
 * To change this template use File | Settings | File Templates.
 */

@Repository
public class CrisisDaoImpl extends AbstractDaoImpl<Crisis, String> implements CrisisDao{

    protected CrisisDaoImpl(){
        super(Crisis.class);
    }

    @Override
    public Crisis findByCrisisID(Long id) {
        Crisis crisis = findByCriterionID(Restrictions.eq("crisisID",id));
        return crisis;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<Crisis> findByCriteria(String columnName, Long value) {
        return findByCriteria(Restrictions.eq(columnName,value))  ;
    }

    @Override
    public List<Crisis> findByCriteria(String columnName, String value) {
        return findByCriteria(Restrictions.eq(columnName,value))  ;
    }

    @Override
    public List<Crisis> findAllActiveCrisis() {
        List<Crisis> crisisList = findAll();

        Iterator<Crisis> iterator = crisisList.iterator();
        while (iterator.hasNext()) {
            Crisis crisis = (Crisis)iterator.next();
            Set<ModelFamily> modelFamilyList = crisis.getModelFamilySet();
            Iterator it = modelFamilyList.iterator();
            boolean hasActiveModelFamily = false;
            while (it.hasNext() && !hasActiveModelFamily) {
                ModelFamily modelFamily = (ModelFamily)it.next();
                if(modelFamily.isActive()){
                    hasActiveModelFamily = true;
                }
            }
            if(!hasActiveModelFamily) {
                crisisList.remove(crisis);
            }

        }

        return crisisList;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<Crisis> findActiveCrisis() {

        List<Crisis> crisisList = findByCriteria(Restrictions.eq("isTrashed", false));
        List<Crisis> filteredCrises = new ArrayList<Crisis>();

        for (Crisis crisis : crisisList) {
            Set<ModelFamily> modelFamilyList = crisis.getModelFamilySet();
            Iterator it = modelFamilyList.iterator();
            while (it.hasNext()) {
                ModelFamily modelFamily = (ModelFamily)it.next();
                if(modelFamily.isActive()){
                    Crisis aCrisis = new Crisis(crisis.getCrisisID(), crisis.getName(), crisis.getCode(), crisis.getTrashed());
                    filteredCrises.add(aCrisis) ;
                    break;
                }
            }

        }

        return filteredCrises;
    }



}
