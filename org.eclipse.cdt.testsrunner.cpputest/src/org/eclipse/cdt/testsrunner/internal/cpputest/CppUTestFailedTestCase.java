package org.eclipse.cdt.testsrunner.internal.cpputest;

import org.eclipse.cdt.testsrunner.model.ITestModelUpdater;
import org.eclipse.cdt.testsrunner.model.ITestItem.Status;
import org.eclipse.cdt.testsrunner.model.ITestMessage;


public class CppUTestFailedTestCase extends CppUTestCase {

	private String errorMessage;
	private String fileName;
	private int lineNumber;

	@Override
	public void updateModel(ITestModelUpdater modelUpdater) {
		modelUpdater.enterTestCase(getCaseName());
		modelUpdater.setTestStatus(Status.Failed);
		modelUpdater.setTestingTime(getTestingTime());
		modelUpdater.addTestMessage(fileName, lineNumber, ITestMessage.Level.Error, errorMessage);
		modelUpdater.exitTestCase();
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public void setLineNumber(int lineNumber) {
		this.lineNumber = lineNumber;
	}

}
