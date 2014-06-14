package qa.qcri.aidr.trainer.api.dao.impl;

import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import qa.qcri.aidr.trainer.api.dao.ClientAppSourceDao;
import qa.qcri.aidr.trainer.api.entity.ClientAppSource;
import qa.qcri.aidr.trainer.api.store.StatusCodeType;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jlucas
 * Date: 3/4/14
 * Time: 12:17 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class ClientAppSourceDaoImpl extends AbstractDaoImpl<ClientAppSource, String> implements ClientAppSourceDao {

    protected ClientAppSourceDaoImpl(){
        super(ClientAppSource.class);
    }

    @Override
    public List<ClientAppSource> findActiveSourcePerClient(Long clientAppID) {
        return findByCriteria(Restrictions.conjunction()
                        .add(Restrictions.eq("clientAppID",clientAppID))
                        .add(Restrictions.eq("status", 1)));



    }

    @Override
    public void acceptSource(String fileURL, Long clientAppID) {
        List<ClientAppSource>  sources = findActiveSourcePerClient( clientAppID );
        ClientAppSource ca = null;
        if(sources.size() > 0){
            ca = new ClientAppSource(clientAppID, StatusCodeType.EXTERNAL_DATA_SOURCE_UPLOADED, fileURL);
        }
        else{
            ca = new ClientAppSource(clientAppID, StatusCodeType.EXTERNAL_DATA_SOURCE_ACTIVE, fileURL);
        }

        if(ca != null){
            save(ca);
        }
        //To change body of implemented methods use File | Settings | File Templates.
    }

}
