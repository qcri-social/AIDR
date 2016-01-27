package qa.qcri.aidr.trainer.api.dao.impl;

import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import qa.qcri.aidr.trainer.api.dao.ClientDao;
import qa.qcri.aidr.trainer.api.entity.Client;


/**
 * Created with IntelliJ IDEA.
 * User: jilucas
 * Date: 9/18/13
 * Time: 6:08 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class ClientDaoImpl extends AbstractDaoImpl<Client, String> implements ClientDao {

    protected ClientDaoImpl(){
        super(Client.class);
    }

    @Override
    public Client findClientByID(String columnName,Long id) {
        Client client = findByCriterionID(Restrictions.eq(columnName, id));
        return client;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Client findClientByCriteria(String columnName, String value) {
        // List<TaskAssignment> taskAssignments = findByCriteria(Restrictions.eq("userID",userID));
        Client client = findByCriterionID(Restrictions.eq(columnName, value));
        return client;
    }
}
