package slowTest;

import org.junit.platform.suite.api.IncludeClassNamePatterns;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectPackages("slowTest")            
@IncludeClassNamePatterns(".*SlowTest")   
public class RunAllSlowTests {

}
