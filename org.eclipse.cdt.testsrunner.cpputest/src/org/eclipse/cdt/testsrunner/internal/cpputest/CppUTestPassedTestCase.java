package org.eclipse.cdt.testsrunner.internal.cpputest;

import org.eclipse.cdt.testsrunner.model.ITestModelUpdater;
import org.eclipse.cdt.testsrunner.model.ITestItem.Status;

public class CppUTestPassedTestCase extends CppUTestCase {

	@Override
	public void updateModel(ITestModelUpdater modelUpdater) {
		modelUpdater.enterTestCase(getCaseName());
		modelUpdater.setTestStatus(Status.Passed);
		modelUpdater.setTestingTime(getTestingTime());
		modelUpdater.exitTestCase();
	}

}
