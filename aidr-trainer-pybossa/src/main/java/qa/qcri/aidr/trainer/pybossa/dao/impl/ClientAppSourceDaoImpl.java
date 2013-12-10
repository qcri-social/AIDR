package qa.qcri.aidr.trainer.pybossa.dao.impl;

import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import qa.qcri.aidr.trainer.pybossa.dao.ClientAppSourceDao;
import qa.qcri.aidr.trainer.pybossa.entity.ClientAppSource;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jlucas
 * Date: 10/18/13
 * Time: 2:26 AM
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class ClientAppSourceDaoImpl extends AbstractDaoImpl<ClientAppSource, String> implements ClientAppSourceDao {

    protected ClientAppSourceDaoImpl(){
        super(ClientAppSource.class);
    }

    @Override
    public List<ClientAppSource> getClientAppSource(Long clientAppID, int status) {
        return findByCriteria(Restrictions.conjunction()
                .add(Restrictions.eq("clientAppID",clientAppID))
                .add(Restrictions.eq("status", status)));
    }

    @Override
    public void updateClientAppSourceStatus(Long clientAppSourceID, int status) {
        ClientAppSource clientAppSource = findByCriterionID(Restrictions.eq("clientAppSourceID",clientAppSourceID));
        clientAppSource.setStatus(status);

        saveOrUpdate(clientAppSource);
    }

    @Override
    public void insertClientAppSource(ClientAppSource clientAppSource) {
        saveOrUpdate(clientAppSource);
    }
}
