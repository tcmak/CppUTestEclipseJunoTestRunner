package org.eclipse.cdt.testsrunner.internal.cpputest;

import org.eclipse.cdt.testsrunner.model.ITestItem.Status;
import org.eclipse.cdt.testsrunner.model.ITestModelUpdater;
import org.eclipse.cdt.testsrunner.model.ITestMessage;


public class CppUTestCrashedTestCase extends CppUTestCase {

	@Override
	public void updateModel(ITestModelUpdater modelUpdater) {
		modelUpdater.enterTestCase(getCaseName());
		modelUpdater.setTestStatus(Status.Failed);
		modelUpdater.addTestMessage("", 0, ITestMessage.Level.Error, CppUTestRunnerMessages.Crashed_Unexpected_Termination);
		modelUpdater.setTestingTime(0);
		modelUpdater.exitTestCase();
	}
}
