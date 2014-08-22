This is the test runner plugin for CppUTest (http://www.cpputest.org) with Eclipse Juno CDT Test Runner.

HOW TO INSTALL

1. Create directory "<YOUR_PATH_TO_ECLIPSE>/eclipse/dropins/CppUTest".

2. In this repository, change into the directory   "org.eclipse.cdt.testrunner.cpputest".

3. Copy the following artifacts from there to "<YOUR_PATH_TO_ECLIPSE>/eclipse/dropins/CppUTest":

  bin/org/           -->      org/
  META-INF/
  plugin.properties
  plugin.xml

  You must copy "bin/org/" to "org/" and not to "bin/org/" !

4. Close Eclipse.

Start Eclipse from a command shell with the following command:

  Eclipse -clean -console -consoleLog

"CppUTest Runner Tests" should now be selectable as a test runner within Eclipse and successfully run a CppUTest test project.