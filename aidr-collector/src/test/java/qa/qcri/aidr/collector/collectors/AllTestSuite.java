package qa.qcri.aidr.collector.collectors;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import qa.qcri.aidr.collector.api.TwitterCollectorAPIRequestValidationTest;

@RunWith(Suite.class)
@SuiteClasses({
	TrackFilterTest.class,
	TwitterStreamTrackerTest.class,
	TwitterCollectorAPIRequestValidationTest.class,
	StrictLocationFilterTest.class,
	FollowFilterTest.class
	
})

//Test Suite class implemented to run all the test classes in a specified order
public class AllTestSuite {
}
