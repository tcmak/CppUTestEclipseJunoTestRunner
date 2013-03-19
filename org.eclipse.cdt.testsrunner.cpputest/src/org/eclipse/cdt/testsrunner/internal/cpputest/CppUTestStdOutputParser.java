package org.eclipse.cdt.testsrunner.internal.cpputest;

import java.io.IOException;
import java.io.InputStream;
import org.eclipse.cdt.testsrunner.model.ITestModelUpdater;

public class CppUTestStdOutputParser {

	ITestModelUpdater modelUpdater;
	
	public CppUTestStdOutputParser(ITestModelUpdater modelUpdater) {
		this.modelUpdater = modelUpdater;
	}

	public void parse(InputStream inputStream) throws IOException {
		TestIterator testIterator = new TestIterator(inputStream);
		CppUTestCase testCase = null;
		String lastTestSuiteName = null;
		
		while ((testCase = testIterator.fetchTest()) != null) {
			lastTestSuiteName = getNewTestSuiteName(lastTestSuiteName, testCase.getSuiteName());
			testCase.updateModel(modelUpdater);
		}
		modelUpdater.exitTestSuite();
	}
	
	private String getNewTestSuiteName(String lastTestSuiteName, String newTestSuiteName) {
		if (newTestSuiteName.equals(lastTestSuiteName)) 
			return newTestSuiteName;
		
		if (lastTestSuiteName != null)
			modelUpdater.exitTestSuite();
		
		modelUpdater.enterTestSuite(newTestSuiteName);
		return newTestSuiteName;
	}


}
