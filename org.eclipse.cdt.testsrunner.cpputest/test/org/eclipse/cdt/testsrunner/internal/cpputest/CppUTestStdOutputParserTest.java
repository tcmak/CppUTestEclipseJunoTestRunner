package org.eclipse.cdt.testsrunner.internal.cpputest;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.eclipse.cdt.testsrunner.model.ITestItem.Status;
import org.eclipse.cdt.testsrunner.model.ITestMessage;
import org.eclipse.cdt.testsrunner.model.ITestModelUpdater;
import org.eclipse.cdt.testsrunner.model.TestingException;
import org.junit.Test;

public class CppUTestStdOutputParserTest {

	private static final String TEST_PASS_SUMMARY = "\nOK (41 tests, 1 ran, 0 checks, 0 ignored, 40 filtered out, 1 ms)\n\n";
	private static final String TEST_SKIP_SUMMARY = "\nOK (40 tests, 0 ran, 0 checks, 1 ignored, 40 filtered out, 1 ms)\n\n";
	private static final String TEST_FAIL_SUMMARY = "\nErrors (1 failures, 41 tests, 0 ran, 0 checks, 0 ignored, 40 filtered out, 1 ms)\n\n";

	ITestModelUpdater mockModelUpdater = mock(ITestModelUpdater.class);
	String testGroupName = "WonderfulTestGroup";
	String testName = "AmazingTest";
	int time = 3;

	@Test
	public void passedTest() throws TestingException {
		String testOutput = 
				"TEST(" + testGroupName + ", " + testName + ") - " + time + " ms\n" + 
				TEST_PASS_SUMMARY;
		InputStream stubInputStream = IOUtils.toInputStream(testOutput);
		
		(new CppUTestRunnerProvider()).run(mockModelUpdater, stubInputStream);
		
		stub(mockModelUpdater.currentTestSuite()).toReturn(null);
//		verify(mockModelUpdater, atLeast(1)).currentTestSuite();

		verify(mockModelUpdater).enterTestSuite(testGroupName);
		verify(mockModelUpdater).enterTestCase(testName);
		verify(mockModelUpdater).setTestingTime(time);
		verify(mockModelUpdater).setTestStatus(Status.Passed);
		verify(mockModelUpdater).exitTestCase();
		verify(mockModelUpdater).exitTestSuite();
	}
	
	@Test
	public void ignoredTest() throws TestingException {
		String testOutput = 
				"IGNORE_TEST(" + testGroupName + ", " + testName + ") - " + time + " ms\n" + 
				TEST_SKIP_SUMMARY;
		InputStream stubInputStream = IOUtils.toInputStream(testOutput);

		(new CppUTestRunnerProvider()).run(mockModelUpdater, stubInputStream);

		verify(mockModelUpdater).enterTestSuite(testGroupName);
		verify(mockModelUpdater).enterTestCase(testName);
		verify(mockModelUpdater).setTestingTime(time);
		verify(mockModelUpdater).setTestStatus(Status.Skipped);
		verify(mockModelUpdater).exitTestCase();
		verify(mockModelUpdater).exitTestSuite();
	}
	
	@Test
	public void failedTest() throws TestingException {
		String testErrorMessage = "AllTests/HelloTest.cpp:59: error: Failure in TEST(" + testGroupName + ", " + testName + ")\n" +
						"	expected <Hello Hello World!\n" + 
						">\n" + 
						"	but was  <Hello World!\n" +
						">\n" +
						"	difference starts at position 6 at: <    Hello World!\n" +
						"   >\n" + 
						"	                                               ^\n";
		String testOutput = 
				"TEST(" + testGroupName + ", " + testName + ")\n" + 
				testErrorMessage + 
				"\n" + 
				" - "+ time + " ms\n" + 
				TEST_FAIL_SUMMARY;
		InputStream stubInputStream = IOUtils.toInputStream(testOutput);

		(new CppUTestRunnerProvider()).run(mockModelUpdater, stubInputStream);

		verify(mockModelUpdater).enterTestSuite(testGroupName);
		verify(mockModelUpdater).enterTestCase(testName);
		verify(mockModelUpdater).setTestingTime(time);
		verify(mockModelUpdater).setTestStatus(Status.Failed);
		verify(mockModelUpdater).addTestMessage("AllTests/HelloTest.cpp", 59, ITestMessage.Level.Error, testErrorMessage);
		verify(mockModelUpdater).exitTestCase();
		verify(mockModelUpdater).exitTestSuite();
	}
	
	@Test
	public void crashed() throws TestingException {
		String testOutput = "TEST(" + testGroupName + ", " + testName + ")";
		InputStream stubInputStream = IOUtils.toInputStream(testOutput);
		
		(new CppUTestRunnerProvider()).run(mockModelUpdater, stubInputStream);
		
		verify(mockModelUpdater).enterTestSuite(testGroupName);
		verify(mockModelUpdater).enterTestCase(testName);
		verify(mockModelUpdater).setTestingTime(0);
		verify(mockModelUpdater).setTestStatus(Status.Failed);
		verify(mockModelUpdater).addTestMessage("", 0, ITestMessage.Level.Error, CppUTestRunnerMessages.Crashed_Unexpected_Termination);
		verify(mockModelUpdater).exitTestCase();
		verify(mockModelUpdater).exitTestSuite();
	}
	
	@Test
	public void summaryOKNotMistakenAsTest() throws TestingException {
		String testOutput = "OK (1 tests, 1 ran, 1 checks, 0 ignored, 0 filtered out, 1 ms)";
		InputStream stubInputStream = IOUtils.toInputStream(testOutput);
		
		(new CppUTestRunnerProvider()).run(mockModelUpdater, stubInputStream);
		
		verify(mockModelUpdater).exitTestSuite();
		verifyNoMoreInteractions(mockModelUpdater);
	}
	
	@Test
	public void summaryErrorsNotMistakenAsTest() throws TestingException {
		String testOutput = "Errors (1 failures, 1 tests, 1 ran, 1 checks, 0 ignored, 0 filtered out, 1 ms)";
		InputStream stubInputStream = IOUtils.toInputStream(testOutput);
		
		(new CppUTestRunnerProvider()).run(mockModelUpdater, stubInputStream);
		
		verify(mockModelUpdater).exitTestSuite();
		verifyNoMoreInteractions(mockModelUpdater);
	}
	
	@Test
	public void severalTestsInSuite() throws TestingException {
		String testOutput = 
		"TEST(" + testGroupName + ", FillEmptyThenPrint) - 0 ms\n" + 
		"TEST(" + testGroupName + ", PrintBoundary) - 1 ms\n" +
		"TEST(" + testGroupName + ", EmptyAfterCreation) - 2 ms\n" +
		"\n" + 
		"Errors (1 failures, 41 tests, 41 ran, 48 checks, 0 ignored, 0 filtered out, 17 ms)\n";

		InputStream stubInputStream = IOUtils.toInputStream(testOutput);
		(new CppUTestRunnerProvider()).run(mockModelUpdater, stubInputStream);

		verify(mockModelUpdater).enterTestSuite(testGroupName);
		
		verify(mockModelUpdater).enterTestCase("FillEmptyThenPrint");
		verify(mockModelUpdater).setTestingTime(0);
		verify(mockModelUpdater, times(3)).setTestStatus(Status.Passed);
		verify(mockModelUpdater, times(3)).exitTestCase();

		verify(mockModelUpdater).enterTestCase("PrintBoundary");
		verify(mockModelUpdater).setTestingTime(1);
		
		verify(mockModelUpdater).enterTestCase("EmptyAfterCreation");
		verify(mockModelUpdater).setTestingTime(2);
		
		verify(mockModelUpdater).exitTestSuite();
	}

	@Test
	public void severalTestsInSeveralSuites() throws TestingException {
		String testOutput = 
		"TEST(" + "WonderfulGroup1" + ", FillEmptyThenPrint) - 0 ms\n" + 
		"IGNORE_TEST(" + "WonderfulGroup1" + ", PrintBoundary) - 1 ms\n" +
		"TEST(" + "WonderfulGroup2" + ", EmptyAfterCreation) - 2 ms\n" +
		"TEST(" + testGroupName + ", " + testName + ")\n" + 
		"AllTests/HelloTest.cpp:59: error: Failure in TEST(" + testGroupName + ", " + testName + ")\n" +
				"	expected <Hello Hello World!\n" + 
				">\n" + 
				"	but was  <Hello World!\n" +
				">\n" +
				"	difference starts at position 6 at: <    Hello World!\n" +
				"   >\n" + 
				"	                                               ^\n" +
				"\n" + 
				" - "+ time + " ms\n" +
				"\n" + 
		"Errors (1 failures, 41 tests, 41 ran, 48 checks, 0 ignored, 0 filtered out, 17 ms)\n";

		InputStream stubInputStream = IOUtils.toInputStream(testOutput);
		(new CppUTestRunnerProvider()).run(mockModelUpdater, stubInputStream);

		verify(mockModelUpdater).enterTestSuite(testGroupName);
		verify(mockModelUpdater).enterTestSuite("WonderfulGroup1");
		verify(mockModelUpdater).enterTestSuite("WonderfulGroup2");
		
		verify(mockModelUpdater).enterTestCase("FillEmptyThenPrint");
		verify(mockModelUpdater).enterTestCase("PrintBoundary");
		verify(mockModelUpdater).enterTestCase("EmptyAfterCreation");
		verify(mockModelUpdater).enterTestCase(testName);

		verify(mockModelUpdater).setTestingTime(0);
		verify(mockModelUpdater).setTestingTime(1);
		verify(mockModelUpdater).setTestingTime(2);
		verify(mockModelUpdater).setTestingTime(time);
		
		verify(mockModelUpdater, times(2)).setTestStatus(Status.Passed);
		verify(mockModelUpdater, times(1)).setTestStatus(Status.Skipped);
		verify(mockModelUpdater, times(1)).setTestStatus(Status.Failed);

		verify(mockModelUpdater, times(4)).exitTestCase();

		verify(mockModelUpdater, times(3)).exitTestSuite();
	}
	
	public class CppUTestRunnerProviderStub extends CppUTestRunnerProvider {
		CppUTestStdOutputParser parser;
		
		protected CppUTestStdOutputParser getParser(ITestModelUpdater modelUpdater) {
			return parser;
		}
		
		public void setParser(CppUTestStdOutputParser parser) {
			this.parser = parser;
		}
	};
	
	@Test 
	public void getTestingExceptionUponIOException() {
		CppUTestStdOutputParser parser = new CppUTestStdOutputParser(null) {
			public void parse(InputStream inputStream) throws IOException {
				throw new IOException("Some IO Error");
			}
		};
		
		CppUTestRunnerProviderStub testRunner = new CppUTestRunnerProviderStub();
		
		try {
			testRunner.setParser(parser);
			testRunner.run(null, null);
		} catch (TestingException e) {
			assertEquals("I/O Error: Some IO Error", e.getMessage());
		}
	}
}

