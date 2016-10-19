package com.test.selenium;

import io.ddavison.conductor.Browser;
import io.ddavison.conductor.Config;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import org.testng.Assert;


@Config(
        browser = Browser.CHROME,
        url     = "http://eu.wiley.com/WileyCDA/"
)

/**
 * Created by baboonik on 10/18/16.
 */

public class WileyTest  {

    private WebDriver driver;

    @Before
    public void setUp() {
        driver = new ChromeDriver();
    }


    @Test //Test #1:  Open http://www.wiley.com/WileyCDA/
    public void testTabsAreNamed() throws IOException{
        driver.get("http://eu.wiley.com/WileyCDA/");
        driver.findElement(By.linkText("Home")).isDisplayed();
        driver.findElement(By.linkText("Subjects")).isDisplayed();
        driver.findElement(By.linkText("About Wiley")).isDisplayed();
        driver.findElement(By.linkText("Contact Us")).isDisplayed();
        driver.findElement(By.linkText("Help")).isDisplayed();
        driver.close();
    }

    @Test //Test #2:  Checking items under Resources sub-header
    public void resouresItems() throws IOException{
        driver.get("http://eu.wiley.com/WileyCDA/");

        List<WebElement> liElements = driver.findElements(By.xpath("//*[@id=\"homepage-links\"]/ul/li"));
            Assert.assertEquals(liElements.size(), 9);

        String[] VALUES =     new String[] {"Students", "Authors", "Instructors", "Librarians", "Societies",
                "Conferences", "Booksellers", "Corporations", "Institutions"};

        for(WebElement childLi : liElements){
            Assert.assertEquals(Arrays.asList(VALUES).contains(childLi.getText()), true);
        }
        driver.close();

    }

    @Test //Test #3:  Clicking “Students” item
    public void clickStudentElement() throws IOException {
        driver.get("http://eu.wiley.com/WileyCDA/");

        driver.findElement(By.xpath("//*[@id=\"homepage-links\"]/ul/li[1]/a")).click();

        WebDriverWait wait = new WebDriverWait(driver, 5);
        wait.until(ExpectedConditions.titleIs("Wiley: Students"));

        driver.get(driver.getCurrentUrl());
        Assert.assertEquals(driver.getCurrentUrl(), "http://eu.wiley.com/WileyCDA/Section/id-404702.html");

        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        Assert.assertEquals(driver.findElement(By.xpath("//*[@id=\"page-title\"]/h1")).getText(),
                                                                   "Students");
        driver.close();
    }
    @Test //Test #4:  Checking “Resources For” menu on the left
    public void resourcesForMenu() throws IOException {
        driver.get("http://eu.wiley.com/WileyCDA/Section/id-404702.html");

        List<WebElement> liElements = driver.findElements(By.xpath("//*[@id=\"sidebar\"]/div/ul/li/ul/li"));
        Assert.assertEquals(liElements.size(), 8);

        String[] VALUES = new String[] {"Authors", "Librarians", "Booksellers",  "Institutions", "Students",
                "Government Employees", "Societies", "Corporate Partners"};

        for(WebElement childLi : liElements){
            Assert.assertEquals(Arrays.asList(VALUES).contains(childLi.getText()), true);
        }
        driver.close();
    }

    @Test //Test #5:  Checking “Students” item is selected
    public void studentItemSelected() throws IOException {
        driver.get("http://eu.wiley.com/WileyCDA/Section/id-404702.html");

        WebElement studentsSelector = driver.findElement(By.xpath("//*[@id=\"sidebar\"]/div/ul/li/ul/li[5]/span"));

        Assert.assertEquals(studentsSelector.getAttribute("href"), null);
        Assert.assertEquals(studentsSelector.getText(), "Students");
        Assert.assertEquals(studentsSelector.getCssValue("color"), "rgba(2, 95, 98, 1)");
        driver.close();
    }

    @Test //#6:  Click “Home” link at the top navigation menu
    public void clickHome() throws IOException {
        driver.get("http://eu.wiley.com/WileyCDA/Section/id-404702.html");
        driver.findElement(By.xpath("//*[@id=\"links-site\"]/ul/li[1]/a")).click();

        WebDriverWait wait = new WebDriverWait(driver, 60);
        wait.until(ExpectedConditions.titleIs("Wiley: Journals, books, and online products and services"));
        driver.close();
    }

    @Test //Test #7:  Finding “Sign up to receive Wiley updates”
    public void signUpAllertTest() throws Exception {
        driver.get("http://eu.wiley.com/WileyCDA/");
        WebElement element = driver.findElement(By.xpath("//*[@id=\"id2d\"]/div[1]/div/h2"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        Thread.sleep(500);

        driver.findElement(By.id("id31")).click();
        WebDriverWait wait = new WebDriverWait(driver, 30);
        wait.until(ExpectedConditions.alertIsPresent());

        Alert alert = driver.switchTo().alert();
        String alertText = alert.getText();
        Assert.assertEquals(alertText, "Please enter email address");
        alert.accept();
        driver.close();
    }

    @Test //Test #8:  Entering invalid email (for example without @)
    public void sendInvalidEmail() throws Exception {
        driver.get("http://eu.wiley.com/WileyCDA/");
        WebElement element = driver.findElement(By.xpath("//*[@id=\"id2d\"]/div[1]/div/h2"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        Thread.sleep(500);

        WebElement email=driver.findElement(By.xpath("//*[@id=\"EmailAddress\"]"));
        email.sendKeys("nshakuntalasgmail.com");

        driver.findElement(By.id("id31")).click();
        WebDriverWait wait = new WebDriverWait(driver, 30);
        wait.until(ExpectedConditions.alertIsPresent());

        Alert alert = driver.switchTo().alert();
        String alertText = alert.getText();
        Assert.assertEquals(alertText, "Invalid email address.");
        alert.accept();
        driver.close();
    }

    @Test //Test #9: Enter “for dummies” to the input field
    public void enterForDummies() throws Exception {
        driver.get("http://eu.wiley.com/WileyCDA/Section/id-WILEYEUROPE2_SEARCH_RESULT.html?query=for%20dummies");
        WebElement element = driver.findElement(By.xpath("//*[@id=\"query\"]"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        Thread.sleep(500);

        element.sendKeys("for dummies");

        driver.findElement(By.xpath("//*[@id=\"links-site\"]/form/fieldset/input[2]")).click();

        boolean listAppeared =  (driver.findElement(By.id("search-results")) != null);
        Assert.assertEquals(listAppeared, true);
        driver.close();
    }

    @Test //Test #10: Click random item link
    public void clickingRandomLink() throws Exception {
        driver.get("http://eu.wiley.com/WileyCDA/Section/id-WILEYEUROPE2_SEARCH_RESULT.html?query=for%20dummies");

        WebElement link = driver.findElement(By.xpath("//*[@id=\"search-results\"]/div[4]/div[2]/a"));
        String oldLinkText = link.getText();
        link.click();
        driver.get(driver.getCurrentUrl());
        Assert.assertEquals(
                driver.findElement(By.xpath("//*[@id=\"main-content-left\"]/table/tbody/tr/td/div[2]/h1")).getText(),
                oldLinkText);
        Assert.assertEquals(
                driver.findElement(By.xpath("//*[@id=\"main-content-left\"]/table/tbody/tr/td/div[2]/h1")).isDisplayed(),
                true);
        driver.close();
    }

    @Test //#11: Click “Home” link at the top navigation menu
    public void clickHome2() throws IOException {
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        driver.get("http://eu.wiley.com/WileyCDA/WileyTitle/productCd-1118852788.html");
        driver.findElement(By.xpath("//*[@id=\"links-site\"]/ul/li[1]/a")).click();

        WebDriverWait wait = new WebDriverWait(driver, 60);
        wait.until(ExpectedConditions.titleIs("Wiley: Journals, books, and online products and services"));
        driver.close();
    }

    @Test //Test #12: Click “Institutions” icon under Resources sub-header
    public void clickInstitution() throws IOException {
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        driver.get("http://eu.wiley.com/WileyCDA/");
        driver.findElement(By.xpath("//*[@id=\"homepage-links\"]/ul/li[9]/a")).click();
        ArrayList<String> tabs = new ArrayList<String> (driver.getWindowHandles());
        driver.switchTo().window(tabs.get(1));
        String nameNewTab = driver.getCurrentUrl();
        driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
        driver.get(driver.getCurrentUrl());

        Assert.assertEquals(nameNewTab, "http://wileyedsolutions.com/");
        driver.close();
    }
}
