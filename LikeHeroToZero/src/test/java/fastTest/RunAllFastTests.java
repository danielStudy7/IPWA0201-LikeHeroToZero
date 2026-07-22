package fastTest;

import org.junit.platform.suite.api.IncludeClassNamePatterns;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectPackages("fastTest")            
@IncludeClassNamePatterns(".*FastTest")   
public class RunAllFastTests {
	
}
