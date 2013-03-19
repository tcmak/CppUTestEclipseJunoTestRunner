package org.eclipse.cdt.testsrunner.internal.cpputest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TestIterator {

	private static final String NEW_LINE = "\n";
	private static final String END_OF_TEST = "ms"; // Each test ends with time of execution in ms

	private final BufferedReader bufferedReader;
	
	public TestIterator(InputStream inputStream) {
		bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
	}

	public CppUTestCase fetchTest() throws IOException {
		String output = "";
		String thisLine = null;
		
		for (; (thisLine = bufferedReader.readLine()) != null; output+= NEW_LINE){
			output += thisLine;
			if (output.endsWith(END_OF_TEST)) break;
		}
		
		if (output.length() == 0 || output.startsWith(NEW_LINE))
			return null;
		
		return new CppUTestCaseFactory().create(output);
	}

}
