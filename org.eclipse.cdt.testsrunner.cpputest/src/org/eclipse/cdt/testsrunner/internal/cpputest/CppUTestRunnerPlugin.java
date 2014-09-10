package org.eclipse.cdt.testsrunner.internal.cpputest;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.Status;


public class CppUTestRunnerPlugin extends Plugin {

	public static final String PLUGIN_ID = "org.eclipse.cdt.testsrunner.cpputest";
	private static CppUTestRunnerPlugin plugin;

	public CppUTestRunnerPlugin() {
		super();
		plugin = this;
	}

	public static CppUTestRunnerPlugin getDefault() {
		return plugin;
	}

	public static String getUniqueIdentifier() {
		return PLUGIN_ID;
	}

	public static void log(IStatus status) {
		getDefault().getLog().log(status);
	}
	
	public static void log(Throwable e) {
		log(new Status(IStatus.ERROR, getUniqueIdentifier(), IStatus.ERROR, e.getMessage(), e));
	}

}
