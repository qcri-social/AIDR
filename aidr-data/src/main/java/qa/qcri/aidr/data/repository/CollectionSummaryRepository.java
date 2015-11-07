package qa.qcri.aidr.data.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import qa.qcri.aidr.data.persistence.entity.CollectionSummary;

public interface CollectionSummaryRepository extends CrudRepository<CollectionSummary, Long>{

    List<CollectionSummary> findByName(String name);
    List<CollectionSummary> findAll();
    CollectionSummary findByCode(String code);
}
