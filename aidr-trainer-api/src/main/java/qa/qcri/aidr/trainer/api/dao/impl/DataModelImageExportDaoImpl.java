package qa.qcri.aidr.trainer.api.dao.impl;

import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import qa.qcri.aidr.trainer.api.dao.DataModelImageExportDao;
import qa.qcri.aidr.trainer.api.entity.DataModelImageExport;

/**
 * @author Latika
 *
 */
@Repository
public class DataModelImageExportDaoImpl extends AbstractDaoImpl<DataModelImageExport, Long> implements DataModelImageExportDao {

    protected DataModelImageExportDaoImpl() {
		super(DataModelImageExport.class);
	}

	@Override
	public Long getTotalImageCountForCollection(String collectionCode) {
		
		Criteria criteria = getCurrentSession().createCriteria(DataModelImageExport.class);
		criteria.setProjection(Projections.rowCount());
		criteria.add(Restrictions.eq("collectionCode", collectionCode));
		Long count = (Long) criteria.uniqueResult();
		return count;
	}
}
