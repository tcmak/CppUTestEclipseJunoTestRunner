package org.eclipse.cdt.testsrunner.internal.cpputest;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class GetAdditionalLaunchParametersTest {

	private CppUTestRunnerProvider testRunner = new CppUTestRunnerProvider();

	@Test
	public void shouldReturnBasicSwitchesOnNoSpecifiedTests() {
		String[][][] nullCases = new String[][][] {
			null,
			{null,null},
			{},
			{{}},
			{{},{}}
		};
		
		for (int i=0; i<nullCases.length; i++) {
			String[] result = testRunner.getAdditionalLaunchParameters(nullCases[i]);
			assertEquals(2, result.length);
			assertEquals("-r1", result[0]); //$NON-NLS-1$
			assertEquals("-v", result[1]); //$NON-NLS-1$
		}
	}

	@Test
	public void shouldSpecifiedTestGroup() {
		String[][][] groupOnlyCombinations = new String[][][] {
				{ { "TestGroup" } },
				{ { "TestGroup","" } },				
				{ { "TestGroup",null } }				
		};

		for (int i=0; i<groupOnlyCombinations.length; i++) { 
			String[] result = testRunner.getAdditionalLaunchParameters(groupOnlyCombinations[i]);
			assertEquals(3, result.length);
			assertEquals("-sgTestGroup", result[result.length-1]);
		}
	}

	@Test
	public void shouldSpecifiedTest() {
		String[][][] testOnlyCombinations = new String[][][] {	
			{ { "", "Test" } },
			{ { null, "Test" } },
		};
		
		for (int i=0; i< testOnlyCombinations.length; i++) {
			String[] result = testRunner.getAdditionalLaunchParameters(testOnlyCombinations[i]);
			assertEquals(3, result.length);
			assertEquals("-snTest", result[result.length-1]);
		}
	}
	
	@Test
	public void shouldSpecifiedGroupAndTest() {
		String[] result = testRunner.getAdditionalLaunchParameters(new String[][] { { "Group", "Test" } });
		assertEquals(4, result.length);
		assertEquals("-sgGroup", result[result.length-2]);
		assertEquals("-snTest", result[result.length-1]);
	}

}
