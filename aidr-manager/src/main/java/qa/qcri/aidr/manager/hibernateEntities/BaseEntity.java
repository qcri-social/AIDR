package qa.qcri.aidr.manager.hibernateEntities;

import java.io.Serializable;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * @author Latika
 *
 */
@MappedSuperclass
public class BaseEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 9015978098898586703L;
	@Id @GeneratedValue(strategy=GenerationType.AUTO) 
	protected Long id;

}