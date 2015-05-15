package qa.qcri.aidr;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import qa.qcri.aidr.ConfigPropertiesTest;
import qa.qcri.aidr.persister.api.Persister4CollectorAPITest;
import qa.qcri.aidr.persister.api.Persister4TaggerAPITest;

@RunWith(Suite.class)
@SuiteClasses({
	ConfigPropertiesTest.class,
	Persister4CollectorAPITest.class,
	Persister4TaggerAPITest.class,
	
})

//Test Suite class implemented to run all the test classes in a specified order
public class AllTestSuite {
}
