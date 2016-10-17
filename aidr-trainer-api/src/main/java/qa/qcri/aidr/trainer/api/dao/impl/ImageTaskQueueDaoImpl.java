package qa.qcri.aidr.trainer.api.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import qa.qcri.aidr.trainer.api.dao.ImageTaskQueueDao;
import qa.qcri.aidr.trainer.api.dto.ImageTaskQueueDTO;
import qa.qcri.aidr.trainer.api.entity.ImageTaskQueue;

@SuppressWarnings("unchecked")
@Repository
public class ImageTaskQueueDaoImpl extends AbstractDaoImpl<ImageTaskQueue, String> implements ImageTaskQueueDao {

    protected ImageTaskQueueDaoImpl(){
        super(ImageTaskQueue.class);
    }


	@Override
	public List<ImageTaskQueue> findImageTaskQueue(Long taskID, Long clientAppID) {
		 return findByCriteria(Restrictions.conjunction()
	                .add(Restrictions.eq("taskQueue.taskQueueID",taskID))
	                .add(Restrictions.eq("taskQueue.clientAppID", clientAppID))
	                .add(Restrictions.isNotNull("category")));
	}


	@Override
	public List<ImageTaskQueue> findImageTaskQueueSetByClientApp(Long clientAppID) {
        return findByCriteria(Restrictions.conjunction()
        		.add(Restrictions.eq("taskQueue.clientAppID", clientAppID))
        		.add(Restrictions.isNotNull("category")));

	}


	@Override
	public List<ImageTaskQueue> findImageTaskQueueSetByCrisis(Long crisisID) {
		List<ImageTaskQueue> imageTasks = new ArrayList<ImageTaskQueue>();
		String sql = "SELECT a.image_url,a.image_text,a.category,a.lat,a.lon,a.location "
				+ " FROM image_task_queue a \n"
				+ " JOIN task_queue b ON a.task_queue_id = b.id \n"
				+ " JOIN client_app c ON b.client_app_id = c.id \n"
				+ " where c.crisis_id = :crisisID and a.category IS NOT NULL";
		try {
			Query query = getCurrentSession().createSQLQuery(sql);
			query.setParameter("crisisID", crisisID);
			List<Object[]> results = query.list();
			for (Object[] result : results) {
				ImageTaskQueue imageTaskQueue = new ImageTaskQueue();
				imageTaskQueue.setImageUrl((String)result[0]);
				imageTaskQueue.setImageText((String)result[1]);
				imageTaskQueue.setCategory((String)result[2]);
				imageTaskQueue.setLatitude((String)result[3]);
				imageTaskQueue.setLongitude((String)result[4]);
				imageTaskQueue.setLocation((String)result[5]);
				
				imageTasks.add(imageTaskQueue);
			}
		} catch (Exception e) {
			System.out.println("exception in imageTaskQueue:" + e);
		}
		return imageTasks;
	}


	@Override
	public Long findImageTaskQueueCountByCrisis(Long crisisID) {
		Long totalRows = 0L;
		String sql = "SELECT count(1) FROM image_task_queue a \n"
				+ " JOIN task_queue b ON a.task_queue_id = b.id \n"
				+ " JOIN client_app c ON b.client_app_id = c.id \n"
				+ " where c.crisis_id = :crisisID and a.category IS NOT NULL";
		
		Query query = getCurrentSession().createSQLQuery(sql);
		query.setParameter("crisisID", crisisID);
		Object res = query.uniqueResult();
		if (res != null) { 
			totalRows = Long.parseLong(res.toString());
		}
		return totalRows;
	}


	@Override
	public List<ImageTaskQueueDTO> findImageTaskQueueSetByCrisis(Long crisisID, int fromRecord, int limit, String sortColumn,
			String sortDirection) {
		List<ImageTaskQueueDTO> imageTasks = new ArrayList<ImageTaskQueueDTO>();
		
		String orderSQLPart = "";
		if (sortColumn != null && !sortColumn.isEmpty()){
			if (sortDirection != null && !sortDirection.isEmpty()) {
				if ("ASC".equals(sortDirection)) {
					sortDirection = "ASC";
				} else {
					sortDirection = "DESC";
				}
			} else {
				sortDirection = "DESC";
			}
			orderSQLPart += " ORDER BY " + sortColumn + " " + sortDirection + " ";
		}
		
		Long totalRows = findImageTaskQueueCountByCrisis(crisisID);
		
		String sql = "SELECT a.image_url,a.image_text,a.category,a.lat,a.lon,a.location "
				+ " FROM image_task_queue a \n"
				+ " JOIN task_queue b ON a.task_queue_id = b.id \n"
				+ " JOIN client_app c ON b.client_app_id = c.id \n"
				+ " where c.crisis_id = :crisisID and a.category IS NOT NULL "  + orderSQLPart
				+ " LIMIT :fromRecord, :limit";
		try {
			Query query = getCurrentSession().createSQLQuery(sql);
			query.setParameter("crisisID", crisisID);
			query.setParameter("fromRecord", fromRecord);
			query.setParameter("limit", limit);
			List<Object[]> results = query.list();
			for (Object[] result : results) {
				ImageTaskQueueDTO imageTaskQueue = new ImageTaskQueueDTO();
				imageTaskQueue.setImageUrl((String)result[0]);
				imageTaskQueue.setImageText((String)result[1]);
				imageTaskQueue.setCategory((String)result[2]);
				imageTaskQueue.setLatitude((String)result[3]);
				imageTaskQueue.setLongitude((String)result[4]);
				imageTaskQueue.setLocation((String)result[5]);
				imageTaskQueue.setTotalRows(totalRows);
				imageTasks.add(imageTaskQueue);
			}
		} catch (Exception e) {
			System.out.println("exception in imageTaskQueue:" + e);
		}

		return null;
	}

}
