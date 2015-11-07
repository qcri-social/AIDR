package qa.qcri.aidr.data.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import qa.qcri.aidr.data.dao.CollectionSummaryDao;
import qa.qcri.aidr.data.model.CollectionSummaryInfo;
import qa.qcri.aidr.data.persistence.entity.CollectionSummary;


@Service
public class CollectionSummaryService {

	@Autowired
	private CollectionSummaryDao collectionSummaryDao;
	
    public List<CollectionSummaryInfo> fetchAllCollections() {
        List<CollectionSummary> aidrData = collectionSummaryDao.getAllCollections();
        return adaptCollectionSummaryListToCollectionSummaryInfoList(aidrData);
    }
    
    public void saveUpdateCollectionSummary(CollectionSummaryInfo collectionSummaryInfo) {
    	
    	CollectionSummary summary = new CollectionSummary();
    	collectionSummaryDao.saveUpdateCollectionSummary(summary);
    }
	
    public void saveUpdateCollectionSummary(List<CollectionSummaryInfo> collectionSummaryInfos) {
    	List<CollectionSummary> listToSave = new ArrayList<CollectionSummary>();
    	
    	for(CollectionSummaryInfo summaryInfo : collectionSummaryInfos) {
    		CollectionSummary collectionSummary = collectionSummaryDao.getByCode(summaryInfo.getCode());
    		adaptCollectionSummaryInfoToCollectionSummary(summaryInfo, collectionSummary);
    		listToSave.add(collectionSummary);
    	}
    	collectionSummaryDao.saveUpdateCollectionSummaryList(listToSave);
    }

    private void adaptCollectionSummaryInfoToCollectionSummary(CollectionSummaryInfo summaryInfo, CollectionSummary collectionSummary) {
    	if(collectionSummary == null) {
    		collectionSummary = new CollectionSummary();
    	}
    	
    	collectionSummary.setName(summaryInfo.getName());
    	collectionSummary.setTotalCount(summaryInfo.getTotalCount());
    	if(summaryInfo.getStoppedAt() != null) {
    		collectionSummary.setEndDate(new Date(summaryInfo.getStoppedAt()));
    	}
    	collectionSummary.setCurator(summaryInfo.getCurator());
    }
    
    private List<CollectionSummaryInfo> adaptCollectionSummaryListToCollectionSummaryInfoList(List<CollectionSummary> collectionSummaries) {
    	
    	List<CollectionSummaryInfo> collectionSummaryInfos = new ArrayList<CollectionSummaryInfo>();
    	
    	for(CollectionSummary collectionSummary : collectionSummaries) {
    		collectionSummaryInfos.add(adaptCollectionSummaryToCollectionSummaryInfo(collectionSummary));
    	}

    	return collectionSummaryInfos;
    }
    
    private CollectionSummaryInfo adaptCollectionSummaryToCollectionSummaryInfo(CollectionSummary collectionSummary) {
    	
    	CollectionSummaryInfo summaryInfo = new CollectionSummaryInfo();
    	summaryInfo.setCode(collectionSummary.getCode());
    	summaryInfo.setName(collectionSummary.getName());
    	summaryInfo.setCurator(collectionSummary.getCurator());
    	summaryInfo.setTotalCount(collectionSummary.getTotalCount());
    	
    	return summaryInfo;
    }
}
