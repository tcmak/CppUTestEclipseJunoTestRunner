package org.eclipse.cdt.testsrunner.internal.cpputest;

import org.eclipse.osgi.util.NLS;

public class CppUTestRunnerMessages extends NLS {
	private static final String BUNDLE_NAME = "org.eclipse.cdt.testsrunner.internal.cpputest.CppUTestRunnerMessages"; //$NON-NLS-1$
	public static String CppUTestRunner_error_format;
	public static String CppUTestRunner_io_error_prefix;
	public static String Crashed_Unexpected_Termination;
	
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, CppUTestRunnerMessages.class);
	}

	private CppUTestRunnerMessages() {
	}
}
