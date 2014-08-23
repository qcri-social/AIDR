package qa.qcri.aidr.task.ejb.bean;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

import org.hibernate.criterion.Restrictions;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import qa.qcri.aidr.task.entities.Document;
import qa.qcri.aidr.task.entities.TaskAssignment;

public class TaskAssignmentServiceBeanTest {
	//
	static TaskAssignmentServiceBean taskAssignmentServiceBean;
	static DocumentServiceBean documentServiceBean;
	//
	@BeforeClass
	public static void setUp() throws Exception {
		taskAssignmentServiceBean = new TaskAssignmentServiceBean();
		EntityManager em = Persistence.createEntityManagerFactory("ProjectTest-ejbPU").createEntityManager();
		taskAssignmentServiceBean.setEntityManager(em);
		//
		documentServiceBean = new DocumentServiceBean();
		EntityManager emD = Persistence.createEntityManagerFactory("ProjectTest-ejbPU").createEntityManager();
		documentServiceBean.setEntityManager(emD);
	}

	@AfterClass
	public static void shutDown() throws Exception {
		taskAssignmentServiceBean.getEntityManager().close();
		documentServiceBean.getEntityManager().close();
	}
	//
	@Test
	public void insertTaskAssignmentTest() {
		Document doc = documentServiceBean.getById(new Long(4579256));
		Document doc1 = documentServiceBean.getById(new Long(4579266));
		List<Document> collection = new ArrayList<Document>();
		collection.add(doc);
		collection.add(doc1);
		System.out.println("Going to insert into task_assignment" + doc.getDocumentID() + ", " + doc1.getDocumentID());
		//documentServiceBean.save(collection);
		//
		int insertValue = taskAssignmentServiceBean.insertTaskAssignment(collection, new Long(1));
		assertEquals(1, insertValue);
	}
	//
	@Test
	public void insertOneTaskAssignmentTest() {
		int insertValue = taskAssignmentServiceBean.insertOneTaskAssignment(new Long(4579250), new Long(1));
		assertEquals(1, insertValue);
	}
	//
	@Test
	public void undoTaskAssignmentListDocTest() {
		Document doc = documentServiceBean.getById(new Long(4579256));
		Document doc1 = documentServiceBean.getById(new Long(4579266));
		List<Document> collection = new ArrayList<Document>();
		collection.add(doc);
		collection.add(doc1);
		taskAssignmentServiceBean.insertTaskAssignment(collection, new Long(1));
		//
		int undoResult = taskAssignmentServiceBean.undoTaskAssignment(collection, new Long(1));
		assertEquals(1, undoResult);
	}
	//
	@Test
	public void undoTaskAssignmentMapTest() {
		Document doc = documentServiceBean.getById(new Long(4579256));
		Document doc1 = documentServiceBean.getById(new Long(4579266));
		Document doc3 = documentServiceBean.getById(new Long(4579273));
		List<Document> collection = new ArrayList<Document>();
		collection.add(doc);
		collection.add(doc1);
		collection.add(doc3);
		int insertResult = taskAssignmentServiceBean.insertTaskAssignment(collection, new Long(1));
		assertEquals(1, insertResult);
		

		Map<Long, Long> taskMap = new HashMap<Long, Long>();
		taskMap.put(new Long(4579256), new Long(1));
		taskMap.put(new Long(4579266), new Long(1));
		taskMap.put(new Long(4579273), new Long(1));
		int undoResult = taskAssignmentServiceBean.undoTaskAssignment(taskMap);
		//
		assertEquals(1, undoResult);
	}
	//
	@Test
	public void undoTaskAssignmentTest() {
		taskAssignmentServiceBean.insertOneTaskAssignment(new Long(4579273), new Long(1));
		int undoResult = taskAssignmentServiceBean.undoTaskAssignment(new Long(4579273), new Long(1));
		//
		assertEquals(1, undoResult);
	}
	//
	@Test
	public void findTaskAssignmentTest() {
		taskAssignmentServiceBean.insertOneTaskAssignment(new Long(4579273), new Long(1));
		TaskAssignment taskAssignment = taskAssignmentServiceBean.findTaskAssignment(new Long(4579273), new Long(1));
		//
		assertNotNull(taskAssignment);
		assertEquals(new Long(4579273), taskAssignment.getDocumentID());
		// task not found
		//TaskAssignment taskAssignment2 = taskAssignmentServiceBean.findTaskAssignment(new Long(4579291), new Long(1));
		//assertEquals(null, taskAssignment2);
	}
	//
	@Test
	public void findTaskAssignmentByID() {
		List<TaskAssignment> list = taskAssignmentServiceBean.findTaskAssignmentByID(new Long(4579273));
		//
		assertNotNull(list);
		assertNotNull(list.get(0));
		assertEquals(new Long(4579273), list.get(0).getDocumentID());
	}
	//
	@Test
	public void getPendingTaskCountTest() {
		int pendingCount = taskAssignmentServiceBean.getPendingTaskCount(new Long(1));
		//
		List<TaskAssignment> taskAssignments = taskAssignmentServiceBean.getAllByCriteria(Restrictions.eq("userID",new Long(1)));
		assertNotNull(taskAssignments);
		assertEquals(taskAssignments.size(), pendingCount);
	}
}
