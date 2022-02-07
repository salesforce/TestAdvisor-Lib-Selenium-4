# TestAdvisor-Lib-Selenium-4
Provides Selenium 4 specific classes to be used together with TestAdvisor-Lib.

## Why is there a TestAdvisor-Lib-Selenium-4 repo?

The *TestAdvisor-Lib-Selenium-4* repo contains a very small fork of only four classes from the official WebDriver library:
* RemoteKeyboard.java
* RemoteMouse.java
* RemoteWebDriver.java
* RemoteWebElement.java

residing in the package *org.openqa.selenium.remote*. All four classes are providing implementations for a host of official
Selenium interfaces.

It was necessary to modify these classes to ensure that Test Advisor receives the proper event notifications while at the
same time avoiding the explicit use of a Test Advisor specific WebDriver by our customers.

For our patched version of these classes to take effect, the jar file for *TestAdvisor-Lib-Selenium-4* has to be on the 
Java classpath in front of the official Selenium WebDriver. That way Java will load “our” copy of the RemoteWebDriver class
and load any other classes than these four mentioned above from the official jar file. In other words: “our” classes
practically eclipse the official ones.

When a test project is using Maven, the best way to achieve this is to add the dependencies on the various Test Advisor
components and **to remove any explicitly entered Selenium dependencies**.

Making sure of this positioning is the only change required by customers in order to use Test Advisor Client in their
test projects.

## Which Selenium Version does TestAdvisor-Lib-Selenium-4 support?

| Selenium 4 version | Minimum TA Lib Selenium 4 version | Recommended TA Lib Selenium 4 version  |
|--------------------|-----------------------------------|----------------------------------------|
| 4.1.2              | 0.2.2                             | 0.2.2                                  |
| 4.1.1              | 0.2.1                             | 0.2.1                                  |
| 4.1.0              | n/a                               | 0.2.1                                  |
| 4.0.0              | n/a                               | 0.2.1                                  |

If you have to use a newer version of Selenium than the one *TestAdvisor-Lib-Selenium-4* depends on, and there are
_no API changes_ on Selenium side in these four classes (ie. no additional methods, no modified methods, no removed methods),
then the latest *TestAdvisor-Lib-Selenium-4* version can still be used. Of course the requirement has to be met, that 
the jar file for *TestAdvisor-Lib-Selenium-4* is on the Java classpath in front of the official Selenium WebDriver.
