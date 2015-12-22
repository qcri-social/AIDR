package qa.qcri.aidr.trainer.pybossa.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import qa.qcri.aidr.trainer.pybossa.dao.ClientDao;
import qa.qcri.aidr.trainer.pybossa.entity.Client;
import qa.qcri.aidr.trainer.pybossa.service.ClientService;

/**
 * Created with IntelliJ IDEA.
 * User: jilucas
 * Date: 9/19/13
 * Time: 11:45 AM
 * To change this template use File | Settings | File Templates.
 */
@Service("clientService")
@Transactional(readOnly = true)
public class ClientServiceImpl implements ClientService {

    @Autowired
    private ClientDao clientDao;

    @Override
    public Client findClientbyID(String columnName, Long id) {
        return clientDao.findClientByID(columnName, id);
    }

    @Override
    public Client findClientByCriteria(String columnName, String value) {
        Client c = null;
        try{
            c = clientDao.findClientByCriteria(columnName, value);

        }
        catch(Exception e){
            System.out.println(e);
        }
        return c;
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
