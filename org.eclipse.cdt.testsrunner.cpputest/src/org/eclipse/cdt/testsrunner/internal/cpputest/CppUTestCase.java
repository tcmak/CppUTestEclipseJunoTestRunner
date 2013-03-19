package org.eclipse.cdt.testsrunner.internal.cpputest;

import org.eclipse.cdt.testsrunner.model.ITestModelUpdater;

public abstract class CppUTestCase {
	
	String suiteName;
	String caseName;
	int testingTime;
	
	abstract public void updateModel(ITestModelUpdater modelUpdater);

	public void setSuiteName(String suiteName) {
		this.suiteName = suiteName;
	}
	
	public String getSuiteName() {
		return suiteName;
	}

	public String getCaseName() {
		return caseName;
	}
	
	public void setCaseName(String caseName) {
		this.caseName = caseName;
	}
	
	public int getTestingTime() {
		return testingTime;
	}

	public void setTestingTime(int testingTime) {
		this.testingTime = testingTime;
	}
}
