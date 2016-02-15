package qa.qcri.aidr.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import qa.qcri.aidr.entity.DataFeed;
import qa.qcri.aidr.utils.DataFeedInfo;

@Repository
public class DataFeedDAO extends AbstractDao<DataFeed, Long> {
	
	private Logger logger = Logger.getLogger(DataFeedDAO.class);
	
	private static final String SELECT_DATA_FEED_BY_CODE = "SELECT "
			+ "feed->>\'text\' AS text, "
			+ "cast((EXTRACT(EPOCH FROM  cast(feed->>\'created_at\' as timestamp)) * 1000) AS BIGINT)AS created_at, "
			+ "cast(geo as text), cast(place as text),"
			+ "aidr->\'nominal_labels\'->0->>\'label_code\' AS label_code, "
			+ "aidr->\'nominal_labels\'->0->>\'label_name\' AS label_name, "
			+ "aidr->\'nominal_labels\'->0->>\'attribute_code\' AS attribute_code, "
			+ "aidr->\'nominal_labels\'->0->>\'attribute_name\' AS attribute_name "
			+ "FROM data_feed "
			+ "where code =  ? and "
			+ "(geo is not null or place is not null) "
			+ "limit ? offset ?";
	
	private static final String SELECT_DATA_FEED_BY_CODE_AND_CONFIDENCE = "SELECT "
			+ "feed->>\'text\' AS text, "
			+ "cast((EXTRACT(EPOCH FROM  cast(feed->>\'created_at\' as timestamp)) * 1000) AS BIGINT)AS created_at, "
			+ "cast(geo as text), cast(place as text),"
			+ "aidr->\'nominal_labels\'->0->>\'label_code\' AS label_code, "
			+ "aidr->\'nominal_labels\'->0->>\'label_name\' AS label_name, "
			+ "aidr->\'nominal_labels\'->0->>\'attribute_code\' AS attribute_code, "
			+ "aidr->\'nominal_labels\'->0->>\'attribute_name\' AS attribute_name, "
			+ "cast(aidr->\'nominal_labels\'->0->>\'confidence\' AS float) as conf "
			+ "FROM data_feed "
			+ "where code =  ? and "
			+ "(geo is not null or place is not null) and "
			+ "cast(aidr->'nominal_labels'->0->>'confidence' AS float)>= ?"
			+ " limit ? offset ?";
	
	protected DataFeedDAO() {
		super(DataFeed.class);
	}
	
	public List<DataFeedInfo> getAllDataFeedsByCode(String code, Integer offset, Integer limit){
		Query createQuery =null;
		try{
			createQuery = getCurrentSession().createSQLQuery(SELECT_DATA_FEED_BY_CODE);
			createQuery.setParameter(0, code);
			createQuery.setParameter(1, limit);
			createQuery.setParameter(2, offset);
			
			List results = createQuery.list();
			return parseToEntity(results);
		}
		catch(Exception e){
			logger.error("Exception while fetching data from db for collectionCode: "+code , e);
			return null;
		}
	}
	
	public List<DataFeedInfo> getAllDataFeedsByCodeAndConfidence(String code, double confidence, Integer offset, Integer limit){
		Query createQuery =null;
		try{
			createQuery = getCurrentSession().createSQLQuery(SELECT_DATA_FEED_BY_CODE_AND_CONFIDENCE);
			createQuery.setParameter(0, code);
			createQuery.setParameter(1, confidence);
			createQuery.setParameter(2, limit);
			createQuery.setParameter(3, offset);
			List results = createQuery.list();
			return parseToEntity(results);
		}
		catch(Exception e){
			logger.error("Exception while fetching data from db for collectionCode: "+code , e);
			return null;
		}
	}

	private List<DataFeedInfo> parseToEntity(List results) {
		List<DataFeedInfo> dataFeedInfos = new ArrayList<DataFeedInfo>();
		DataFeedInfo dataFeedInfo;
		try{
			for (Object result : results) {
				Object[] obj = (Object[]) result;
				dataFeedInfo = new DataFeedInfo();
				dataFeedInfo.setText(obj[0] != null ? obj[0].toString() : null);
				dataFeedInfo.setCreatedAt(obj[1] != null ?new Date(Long.parseLong(obj[1].toString())) : null);
				dataFeedInfo.setGeo(obj[2] != null ? obj[2].toString() : null);
				dataFeedInfo.setPlace(obj[3] != null ? obj[3].toString() : null);
				dataFeedInfo.setLabelCode(obj[4] != null ? obj[4].toString() : null);
				dataFeedInfo.setLabelName(obj[5] != null ? obj[5].toString() : null);
				dataFeedInfo.setAttributeCode(obj[6] != null ? obj[6].toString() : null);
				dataFeedInfo.setAttributeName(obj[7] != null ? obj[7].toString() : null);
				if(obj.length>8){
					dataFeedInfo.setConfidence(obj[8] != null ? Double.parseDouble(obj[8].toString()) : null);
				}
				dataFeedInfos.add(dataFeedInfo);
			}
		}catch(Exception e){
			logger.error("Exception while parsing db result to entity in DataFeedDAO", e);
		}
		return dataFeedInfos;
	}
}
