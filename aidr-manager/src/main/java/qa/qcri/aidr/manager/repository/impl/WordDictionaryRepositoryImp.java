package qa.qcri.aidr.manager.repository.impl;

import java.io.Serializable;

import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;

import qa.qcri.aidr.manager.persistence.entities.WordDictionary;
import qa.qcri.aidr.manager.repository.WordDictionaryRepository;


/**
 * @author Latika
 *
 */
@Repository
public class WordDictionaryRepositoryImp extends GenericRepositoryImpl<WordDictionary, Serializable> implements WordDictionaryRepository {

}
