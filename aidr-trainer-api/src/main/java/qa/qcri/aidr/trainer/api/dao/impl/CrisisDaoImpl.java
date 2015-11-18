package qa.qcri.aidr.trainer.api.dao.impl;

import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import qa.qcri.aidr.trainer.api.dao.CrisisDao;
import qa.qcri.aidr.trainer.api.entity.Collection;
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
public class CrisisDaoImpl extends AbstractDaoImpl<Collection, Long> implements CrisisDao{

    protected CrisisDaoImpl(){
        super(Collection.class);
    }

    @Override
    public Collection findByCrisisID(Long id) {
        Collection crisis = findById(id);
        return crisis;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<Collection> findByCriteria(String columnName, Long value) {
        return findByCriteria(Restrictions.eq(columnName,value))  ;
    }

    @Override
    public List<Collection> findByCriteria(String columnName, String value) {
        return findByCriteria(Restrictions.eq(columnName,value))  ;
    }

    @Override
    public List<Collection> findAllActiveCrisis() {
        List<Collection> crisisList = findAll();

        Iterator<Collection> iterator = crisisList.iterator();
        while (iterator.hasNext()) {
            Collection crisis = (Collection)iterator.next();
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
    public List<Collection> findActiveCrisis() {

        List<Collection> collections = findByCriteria(Restrictions.eq("trashed", false));
        List<Collection> filteredCrises = new ArrayList<Collection>();

        for (Collection collection : collections) {
            Set<ModelFamily> modelFamilyList = collection.getModelFamilySet();
            Iterator it = modelFamilyList.iterator();
            while (it.hasNext()) {
                ModelFamily modelFamily = (ModelFamily)it.next();
                if(modelFamily.isActive()){
                    Collection aCollection = new Collection(collection.getCrisisID(), collection.getName(), collection.getCode(), collection.getTrashed());
                    filteredCrises.add(aCollection) ;
                    break;
                }
            }

        }

        return filteredCrises;
    }



}
