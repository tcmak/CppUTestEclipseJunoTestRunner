package org.eclipse.cdt.testsrunner.internal.cpputest;

import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.cdt.testsrunner.launcher.ITestsRunnerProvider;
import org.eclipse.cdt.testsrunner.model.ITestModelUpdater;
import org.eclipse.cdt.testsrunner.model.TestingException;

public class CppUTestRunnerProvider implements ITestsRunnerProvider{

	@Override
	public String[] getAdditionalLaunchParameters(String[][] testAndGroupNames) {
		if (testAndGroupNames == null || testAndGroupNames.length==0 || testAndGroupNames[0] == null )
			return getDefaultLaunchParameters().toArray(new String[0]);

		return getComplexLaunchParameters(testAndGroupNames).toArray(new String[0]);
	}

	private List<String> getComplexLaunchParameters(String[][] testGroupAndTestNames) {
		List<String> launchParameters = getDefaultLaunchParameters();
		for (String[] testGroupAndTestName : testGroupAndTestNames) {
			addGroupNameToLaunchParameters(getGroupName(testGroupAndTestName), launchParameters);
			addTestNameToLaunchParameters(getTestName(testGroupAndTestName), launchParameters);
		}
		return launchParameters;
	}

	private List<String> getDefaultLaunchParameters() {
		List<String> launchParameters = new ArrayList<String>();
		launchParameters.add("-r1");
		launchParameters.add("-v");
		return launchParameters;
	}
	
	private void addTestNameToLaunchParameters(String testName, List<String> launchParameters) {
		if (!testName.equals(""))
			launchParameters.add("-sn" + testName);
	}

	private void addGroupNameToLaunchParameters(String groupName, List<String> launchParameters) {
		if (!groupName.equals(""))
			launchParameters.add("-sg" + groupName);
	}

	private String getGroupName(String[] testGroupAndTestName) {
		if (testGroupAndTestName.length == 0 || testGroupAndTestName[0] == null)
			return "";
		
		return testGroupAndTestName[0];
	}
	
	private String getTestName(String[] testGroupAndTestName) {
		if (testGroupAndTestName.length < 2 || testGroupAndTestName[1] == null)
			return "";
		
		return testGroupAndTestName[1];
	}
	
	@Override
	public void run(ITestModelUpdater modelUpdater, InputStream inputStream) throws TestingException {
		try {
			getParser(modelUpdater).parse(inputStream);
		} catch (IOException e) {
			throw new TestingException(getErrorText(CppUTestRunnerMessages.CppUTestRunner_io_error_prefix, e.getLocalizedMessage()));
		}
	}
	
	protected CppUTestStdOutputParser getParser(ITestModelUpdater modelUpdater) {
		return new CppUTestStdOutputParser(modelUpdater);
	}

	private String getErrorText(String prefix, String description) {
		return MessageFormat.format(CppUTestRunnerMessages.CppUTestRunner_error_format, prefix, description);
	}
}
