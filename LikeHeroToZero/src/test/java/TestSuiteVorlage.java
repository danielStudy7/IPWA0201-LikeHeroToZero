import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

import fastTest.RunAllFastTests;
import slowTest.RunAllSlowTests;

@Suite
@SelectClasses({RunAllFastTests.class, RunAllSlowTests.class})
public class TestSuiteVorlage {
	//
}
