package qa.qcri.aidr.task.ejb.bean;

import static org.junit.Assert.*;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import qa.qcri.aidr.task.entities.DocumentNominalLabel;

public class DocumentNominalLabelServiceBeanTest {
	//
	static DocumentNominalLabelServiceBean documentNominalLabelServiceBean;
	static DocumentNominalLabel documentNominalLabel;
	//
	@BeforeClass
	public static void setUp() throws Exception {
		documentNominalLabelServiceBean = new DocumentNominalLabelServiceBean();
		EntityManager em = Persistence.createEntityManagerFactory("ProjectTest-ejbPU").createEntityManager();
		documentNominalLabelServiceBean.setEntityManager(em);
		documentNominalLabel = new DocumentNominalLabel(new Long(4579250), new Long(323), new Long(10));
	}
	
	@AfterClass
	public static void shutDown() throws Exception {
		documentNominalLabelServiceBean.getEntityManager().close();
	}
	//
	@Test
	public void saveDocumentNominalLabelTest() {
		//
		documentNominalLabelServiceBean.save(documentNominalLabel);
		DocumentNominalLabel doc = documentNominalLabelServiceBean.getById(new Long(4579250));
		Long userID = doc.getUserID();
		//
		assertNotNull(doc);
		assertEquals(new Long(10), userID);
	}
	//
	@Test
	public void foundDuplicateTest() {
		//
		boolean booleanValue = documentNominalLabelServiceBean.foundDuplicate(documentNominalLabel);
		assertEquals(true, booleanValue);
	}
}
