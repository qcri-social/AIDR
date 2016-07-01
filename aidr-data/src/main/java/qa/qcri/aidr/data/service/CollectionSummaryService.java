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

	private static final String NA_STRING = "n/a";
	private static final String RUNNING_STATUS = "RUNNING";
	private static final String RUNNING_WARNING_STATUS = "RUNNING_WARNING";
	private static final String STOPPED_STATUS = "STOPPED";
	
	@Autowired
	private CollectionSummaryDao collectionSummaryDao;
	
    public List<CollectionSummaryInfo> fetchAllCollections() {
        List<CollectionSummary> collectionSummaries = collectionSummaryDao.getAllCollections();
        
        List<CollectionSummaryInfo> collectionSummaryInfos = new ArrayList<CollectionSummaryInfo>();
    	
    	for(CollectionSummary collectionSummary : collectionSummaries) {
    		collectionSummaryInfos.add(adaptCollectionSummaryToCollectionSummaryInfo(collectionSummary));
    	}
    	
        return collectionSummaryInfos;
    }
    
    public void saveUpdateCollectionSummary(CollectionSummaryInfo collectionSummaryInfo) {
    	
    	CollectionSummary summary = new CollectionSummary();
    	collectionSummaryDao.saveUpdateCollectionSummary(summary);
    }
	
    public void saveUpdateCollectionSummary(List<CollectionSummaryInfo> collectionSummaryInfos) {
    	List<CollectionSummary> listToSave = new ArrayList<CollectionSummary>();
    	
    	for(CollectionSummaryInfo summaryInfo : collectionSummaryInfos) {
    		CollectionSummary collectionSummary = collectionSummaryDao.getByCode(summaryInfo.getCode());
    		collectionSummary = adaptCollectionSummaryInfoToCollectionSummary(summaryInfo, collectionSummary);
    		System.out.println("Fetched Collection from aidr: "+ summaryInfo);
    		System.out.println("Populated Collectionsr: "+ collectionSummary);
    		listToSave.add(collectionSummary);
    	}
    	
    	collectionSummaryDao.saveUpdateCollectionSummaryList(listToSave);
    }

    private CollectionSummary adaptCollectionSummaryInfoToCollectionSummary(CollectionSummaryInfo summaryInfo, CollectionSummary collectionSummary) {
    	if(collectionSummary == null) {
    		collectionSummary = new CollectionSummary();
    		collectionSummary.setCode(summaryInfo.getCode());
    		collectionSummary.setCollectionCreationDate(summaryInfo.getCollectionCreationDate());
    	}
    	
    	collectionSummary.setName(summaryInfo.getName());
    	collectionSummary.setTotalCount(summaryInfo.getTotalCount());
    	collectionSummary.setCurator(summaryInfo.getCurator());
    	collectionSummary.setGeo(summaryInfo.getGeo());
    	collectionSummary.setLabelCount(summaryInfo.getLabelCount());
    	collectionSummary.setStartDate(summaryInfo.getStartDate());
    	collectionSummary.setEndDate(summaryInfo.getEndDate());
    	collectionSummary.setKeywords(summaryInfo.getKeywords());
    	collectionSummary.setStatus(summaryInfo.getStatus());
    	collectionSummary.setLanguage(summaryInfo.getLanguage());
    	collectionSummary.setPubliclyListed(summaryInfo.isPubliclyListed());
    	collectionSummary.setProvider(summaryInfo.getProvider());
    	
    	return collectionSummary;
    }
    
    private CollectionSummaryInfo adaptCollectionSummaryToCollectionSummaryInfo(CollectionSummary collectionSummary) {
    	
    	String lang = NA_STRING;
    	CollectionSummaryInfo summaryInfo = new CollectionSummaryInfo();
    	summaryInfo.setCode(collectionSummary.getCode());
    	summaryInfo.setName(collectionSummary.getName());
    	summaryInfo.setCurator(collectionSummary.getCurator());
    	summaryInfo.setTotalCount(collectionSummary.getTotalCount());
    	summaryInfo.setLabelCount(collectionSummary.getLabelCount());
    	summaryInfo.setStartDate(collectionSummary.getStartDate());
    	summaryInfo.setEndDate(collectionSummary.getEndDate());
    	summaryInfo.setLanguage(collectionSummary.getLanguage());
    	summaryInfo.setCollectionCreationDate(collectionSummary.getCollectionCreationDate());
    	summaryInfo.setKeywords(collectionSummary.getKeywords() != null ? collectionSummary.getKeywords() : NA_STRING);
    	summaryInfo.setGeo(collectionSummary.getGeo());
    	if(collectionSummary.getStatus() != null && (RUNNING_STATUS.equals(collectionSummary.getStatus()) 
    			|| RUNNING_WARNING_STATUS.equals(collectionSummary.getStatus()))) {
    		summaryInfo.setStatus(RUNNING_STATUS);
    	} else {
    		summaryInfo.setStatus(STOPPED_STATUS);
    		if(summaryInfo.getEndDate() == null) {
    			summaryInfo.setEndDate(new Date());
    		}
    	}
    	
    	if (collectionSummary.getLanguage() != null && !collectionSummary.getLanguage().isEmpty()) { 
    		lang = collectionSummary.getLanguage();
    	}
    	
    	summaryInfo.setLanguage(lang);
    	summaryInfo.setPubliclyListed(collectionSummary.isPubliclyListed());
    	summaryInfo.setProvider(collectionSummary.getProvider());
    	return summaryInfo;
    }
}
