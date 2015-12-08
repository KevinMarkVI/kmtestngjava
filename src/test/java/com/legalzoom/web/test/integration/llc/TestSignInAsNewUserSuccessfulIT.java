package com.legalzoom.web.test.integration.llc;
/**
 *
hmaheshwari
Dec 7, 2015
10:21:05 AM
 */

import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;
// import com.legalzoom.web.page.*;
import org.testng.annotations.BeforeTest;   
import java.sql.Timestamp;


//import Sauce TestNG helper libraries
import com.saucelabs.common.SauceOnDemandAuthentication;
import com.saucelabs.common.SauceOnDemandSessionIdProvider;
import com.saucelabs.testng.SauceOnDemandAuthenticationProvider;
import com.saucelabs.testng.SauceOnDemandTestListener;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
//import selenium libraries
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.RemoteWebDriver;

//import testng libraries
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

import java.awt.Toolkit;
//import java libraries
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;

@Listeners({SauceOnDemandTestListener.class})
public class TestSignInAsNewUserSuccessfulIT implements SauceOnDemandSessionIdProvider, SauceOnDemandAuthenticationProvider {
    
        String user;
        
        // Sauce username
        public String username = System.getenv("SAUCE_USERNAME");

        // Sauce access key
        public String accesskey = System.getenv("SAUCE_ACCESS_KEY");


        /**
         * Constructs a {@link SauceOnDemandAuthentication} instance using the supplied user name/access key.  To use the authentication
         * supplied by environment variables or from an external file, use the no-arg {@link SauceOnDemandAuthentication} constructor.
         */
        public SauceOnDemandAuthentication authentication = new SauceOnDemandAuthentication(username, accesskey);

        /**
         * ThreadLocal variable which contains the  {@link WebDriver} instance which is used to perform browser interactions with.
         */
        private ThreadLocal<WebDriver> webDriver = new ThreadLocal<WebDriver>();

        /**
         * ThreadLocal variable which contains the Sauce Job Id.
         */
        private ThreadLocal<String> sessionId = new ThreadLocal<String>();

        /**
         * DataProvider that explicitly sets the browser combinations to be used.
         *
         * @param testMethod
         * @return Two dimensional array of objects with browser, version, and platform information
         */
        @DataProvider(name = "hardCodedBrowsers", parallel = true)
        public static Object[][] sauceBrowserDataProvider(Method testMethod) {
            return new Object[][]{
                    new Object[]{"internet explorer", "11", "Windows 8.1"},
                    new Object[]{"chrome", "41", "Windows 7"},
                    new Object[]{"safari", "7", "OS X 10.9"},
                    new Object[]{"firefox", "35", "Windows 7"}
            };
        }
        
        /**
         * @return the {@link WebDriver} for the current thread
         */
        public WebDriver getWebDriver() {
            return webDriver.get();
        }

        /**
         *
         * @return the Sauce Job id for the current thread
         */
        public String getSessionId() {
            return sessionId.get();
        }

        /**
         *
         * @return the {@link SauceOnDemandAuthentication} instance containing the Sauce username/access key
         */
       //@Override
        public SauceOnDemandAuthentication getAuthentication() {
            return authentication;
        }

        /**
         * Constructs a new {@link RemoteWebDriver} instance which is configured to use the capabilities defined by the browser,
         * version and os parameters, and which is configured to run against ondemand.saucelabs.com, using
         * the username and access key populated by the {@link #authentication} instance.
         *
         * @param browser Represents the browser to be used as part of the test run.
         * @param version Represents the version of the browser to be used as part of the test run.
        * @param os Represents the operating system to be used as part of the test run.
         * @param methodName Represents the name of the test case that will be used to identify the test on Sauce.
         * @return
         * @throws MalformedURLException if an error occurs parsing the url
         */
        private WebDriver createDriver(String browser, String version, String os, String methodName) throws MalformedURLException {
        

        
            //ChromeOptions options = new ChromeOptions();
           // options.addArguments("--start-maximized");
            
                DesiredCapabilities capabilities = new DesiredCapabilities();

                // set desired capabilities to launch appropriate browser on Sauce
                capabilities.setCapability(CapabilityType.BROWSER_NAME, browser);
                capabilities.setCapability(CapabilityType.VERSION, version);
                capabilities.setCapability(CapabilityType.PLATFORM, os);
                capabilities.setCapability("name", methodName);
                capabilities.setCapability("screenResolution", "1920x1200");
               // capabilities.setCapability(ChromeOptions.CAPABILITY, options);

            
        	
    		

            // Launch remote browser and set it as the current thread
            webDriver.set(new RemoteWebDriver(
                    new URL("http://" + authentication.getUsername() + ":" + authentication.getAccessKey() + "@ondemand.saucelabs.com:80/wd/hub"),
                    capabilities));
           

            // set current sessionId        
            String id = ((RemoteWebDriver) getWebDriver()).getSessionId().toString();
            sessionId.set(id);

            // print out sessionId and jobname for consumption by Sauce Jenkins plugin
            System.out.println(String.format("SauceOnDemandSessionID=%1$s job-name=%2$s", id, methodName));

            return webDriver.get();
        }
        
        /**
         * Method that gets invoked after test.
         * Closes the browser
         *
         * @param testMethod
         * @return
         */
        @AfterMethod
        public void tearDown() throws Exception {
            webDriver.get().quit();
        }

        /**
         * Runs a simple test verifying inputField can typed into.
         *
         * @param browser
         * @param version Represents the version of the browser to be used as part of the test run.
         * @param os Represents the operating system to be used as part of the test run.
         * @param method Represents the name of the test.
         * @throws Exception if an error occurs during the running of the test
         */
        @Test(enabled = true, dataProvider = "hardCodedBrowsers")
        public void test_SignIn_As_New_User_Should_Successful(String browser, String version, String os, Method method) throws Exception {
            // all variable declarations should be at top of method
            WebDriver driver = createDriver(browser, version, os, method.getName());  // create the driver / browser instance
                              
            /*
             actions and interaction with page should go here...
            */
            driver.get("http://wwwqa.legalzoom.com/?optimizely_x3726960599=1");
            driver.manage().deleteAllCookies();
            Thread.sleep(5000);

            driver.manage().window().maximize();


        //     HomePage homePage = new HomePage(driver);
        //  LLCSignInPage llcSignInPage = homePage.clickLogInButton();  
         
        //  Reporter.log("Signing in as <"+this.user+">");
        //  LLCAccountOverviewPage llcAccountOverviewPage = llcSignInPage.signInAsNewUserSuccessful(this.user, "11111111");
                                                     
        //  Reporter.log("Displayed Email is <"+llcAccountOverviewPage.getDisplayedEmail()+">");
        // Assert.assertTrue(llcAccountOverviewPage.getDisplayedEmail().contains(this.user.subSequence(0, 13)));  
        //  Assert.assertTrue(llcAccountOverviewPage.getPageTitle().contains("LegalZoom: Account Overview"));
        //  Reporter.log("Account Overview Page Title is <"+llcAccountOverviewPage.getPageTitle()+">");
        //  Reporter.log("Logged in successfully as a new user <"+this.user+">");
         
        //  homePage = llcAccountOverviewPage.logOutFromAccountOverviewPage();
        //  Reporter.log("home Page Title is <"+homePage.getPageTitle()+">");
        //  Reporter.log("Logged out successfully ...");
        }
        
        @BeforeTest
        public void setUsername(){
        java.util.Date date= new java.util.Date();
        String timestamp = (new Timestamp(date.getTime()).toString());          
         timestamp = timestamp.replaceAll("[-.:]","").replaceAll("\\s","").substring(4, 12);        
         this.user = "cwu"+timestamp+"@legalzoom.com";
        }             
                        
        
} 


