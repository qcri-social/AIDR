package qa.qcri.aidr.task.ejb.bean;

import static org.junit.Assert.*;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.EJB;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.junit.BeforeClass;
import org.junit.Test;

import qa.qcri.aidr.task.ejb.TaskManagerRemote;
import qa.qcri.aidr.task.entities.Document;

public class EJBAnnotationTest {
	//
	@EJB
	static TaskManagerRemote<Document, Serializable> taskManager;

	@BeforeClass
	public static void setUp() throws Exception {
		taskManager = new TaskManagerBean<Document, Serializable>();
	}
	
	@Test
	public void getTaskByIdTest() {
		//TODO id for what? + String format
		String c = taskManager.getTaskById(new Long(4579254));
		assertNotNull(c);
		assertEquals("test", c);
	}
	//
	@Test
	public void getAllTasksTest() {
		// TODO String format
		String c = taskManager.getAllTasks();
		assertNotNull(c);
		assertEquals("test", c);
	}
	//
	@Test
	public void getTaskCollectionByCriterionTest() {
		// TODO
		Criterion criterion = Restrictions.eq("hasHumanLabels", true);
		String c = taskManager.getTaskCollectionByCriterion(new Long(51), 0, criterion);
		assertNotNull(c);
		assertEquals("test", c);
	}
	//
	@Test
	public void deleteTaskByIdTest() {
		// TODO id value
		int result = taskManager.deleteTaskById(new Long(1));
		assertEquals(0, result);
	}
	//
	@Test
	public void updateTaskTest() {
		// TODO 
		Document document = taskManager.getDocumentById(new Long(4579250));
		document.setHasHumanLabels(false);
		//
		assertNotNull(document);
		//
		taskManager.updateTask(document);
		Document updatedDoc = taskManager.getDocumentById(new Long(4579250));
		//
		assertNotNull(updatedDoc);
		assertEquals(false, updatedDoc.isHasHumanLabels());
	}
	//
	@Test
	public void setTaskParameterTest() {
		//TODO
		Map<String, String> paramMap = new HashMap<String, String>();
    	paramMap.put("setHasHumanLabels", new Boolean(false).toString());
    	paramMap.put("setNominalLabelCollection", null);
		taskManager.setTaskParameter(Document.class, new Long(4579255), paramMap);
		//
		Document updatedTask = taskManager.getDocumentById(new Long(4579255));
		//
		assertNotNull(updatedTask);
		assertEquals(false, updatedTask.isHasHumanLabels());
	}
	//
	@Test
	public void deleteTaskTest() {
		// TODO
		Document doc = taskManager.getDocumentById(new Long(4580324));
		int result = taskManager.deleteTask(doc);
		assertEquals(0, result);
	}
	//
	@Test
	public void deleteStaleTasksTest() {
		// TODO
		final String TASK_EXPIRY_AGE_LIMIT = "5s";
		final String TASK_BUFFER_SCAN_INTERVAL = "1s";
		int result = taskManager.deleteStaleTasks("JOIN",  "aidr_predict.task_assignment", "documentID", "ASC", null, TASK_EXPIRY_AGE_LIMIT, TASK_BUFFER_SCAN_INTERVAL);
		assertEquals(0, result);
	}
}
