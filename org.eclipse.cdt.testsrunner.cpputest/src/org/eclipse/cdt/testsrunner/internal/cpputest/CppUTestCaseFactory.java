package org.eclipse.cdt.testsrunner.internal.cpputest;

public class CppUTestCaseFactory {

	private static final String TEST_START = "TEST";
	private static final String TEST_START_IGNORE = "IGNORE_TEST";
	private static final char TEST_GROUP_NAME_START = '(';
	private static final char GROUP_TEST_SEPARATOR = ',';
	private static final char TEST_GROUP_NAME_END = ')';
	private static final char SPACE = ' ';
	private static final char FILE_LINE_SEPARATOR = ':';
	private static final String NEW_LINE = "\n";
	private static final String END_OF_TEST = "ms"; // Each test ends with time of execution in ms

	/* 
	 * Sample CppUTest output:
	 * TEST(TestSuite, TestName) - 0 ms
	 */
	
	public CppUTestCase create(String testCaseOutput) {
		if (isCrashed(testCaseOutput)) 
			return createCrashTestCase(testCaseOutput);

		if (isPassed(testCaseOutput))
			return createPassedTestcase(testCaseOutput);		
		
		if (isIgnored(testCaseOutput))
			return createIgnoredTestCase(testCaseOutput);
		
		return createFailedTestcase(testCaseOutput);
	}

	private boolean isCrashed(String testCaseOutput) {
		return !testCaseOutput.endsWith(END_OF_TEST);
	}
	
	private boolean isPassed(String testCaseOutput) {
		return (testCaseOutput.startsWith(TEST_START)  && !testCaseOutput.contains(NEW_LINE));
	}

	private boolean isIgnored(String testCaseOutput) {
		return testCaseOutput.startsWith(TEST_START_IGNORE);
	}
	
	private CppUTestCase createCrashTestCase(String testCaseOutput) {
		CppUTestCrashedTestCase testCase = new CppUTestCrashedTestCase();
		testCase.setSuiteName(getTestSuiteName(testCaseOutput));
		testCase.setCaseName(getTestName(testCaseOutput));
		return testCase;
	}

	private CppUTestCase createIgnoredTestCase(String testCaseOutput) {
		CppUTestIgnoredTestCase testCase = new CppUTestIgnoredTestCase();
		testCase.setSuiteName(getTestSuiteName(testCaseOutput));
		testCase.setCaseName(getTestName(testCaseOutput));
		testCase.setTestingTime(getTestingTime(testCaseOutput));
		return testCase;
	}
	
	private CppUTestCase createPassedTestcase(String testCaseOutput) {
		CppUTestPassedTestCase testCase = new CppUTestPassedTestCase();
		testCase.setSuiteName(getTestSuiteName(testCaseOutput));
		testCase.setCaseName(getTestName(testCaseOutput));
		testCase.setTestingTime(getTestingTime(testCaseOutput));
		return testCase;
	}
	
	private CppUTestCase createFailedTestcase(String testCaseOutput) {
		CppUTestFailedTestCase testCase = new CppUTestFailedTestCase();
		testCase.setSuiteName(getTestSuiteName(testCaseOutput));
		testCase.setCaseName(getTestName(testCaseOutput));
		testCase.setTestingTime(getTestingTime(testCaseOutput));
		
		String errorMessage = getErrorMessage(testCaseOutput);
		testCase.setErrorMessage(errorMessage);
		testCase.setFileName(getFileName(errorMessage));
		testCase.setLineNumber(getLineNumber(errorMessage));
		return testCase;
	}

	private String getTestSuiteName(String testCaseOutput) {
		return testCaseOutput.substring(testCaseOutput.indexOf(TEST_GROUP_NAME_START)+1, testCaseOutput.indexOf(GROUP_TEST_SEPARATOR));
	}
	
	private String getTestName(String testCaseOutput) {
		return testCaseOutput.substring(testCaseOutput.indexOf(GROUP_TEST_SEPARATOR)+2, testCaseOutput.indexOf(TEST_GROUP_NAME_END));
	}
	
	private int getTestingTime(String testCaseOutput) {
		return Integer.parseInt(
				testCaseOutput.substring(
						testCaseOutput.lastIndexOf(SPACE, testCaseOutput.lastIndexOf(SPACE)-1)+1, 
						testCaseOutput.lastIndexOf(SPACE)
						)
				);
	}
	private String getErrorMessage(String errorMessage) {
		return errorMessage.substring(errorMessage.indexOf(NEW_LINE.charAt(0))+1,errorMessage.lastIndexOf(NEW_LINE+NEW_LINE)+1);
	}

	private int getLineNumber(String errorMessage) {
		int positionAfterFirstColon = errorMessage.indexOf(FILE_LINE_SEPARATOR) + 1;
		int positionAfterSecondColon = errorMessage.indexOf(FILE_LINE_SEPARATOR, positionAfterFirstColon);
		return Integer.parseInt(errorMessage.substring(positionAfterFirstColon, positionAfterSecondColon));
	}
	
	private String getFileName(String errorMessage) {
		return errorMessage.substring(0, errorMessage.indexOf(FILE_LINE_SEPARATOR));
	}
	
}
