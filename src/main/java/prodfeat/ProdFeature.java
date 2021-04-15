package prodfeat;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

//{@literal @RunWith(JUnit4.class)}
public class ProdFeature extends App {

  private static ChromeDriverService service;
  private WebDriver driver;

  // {@literal @BeforeClass}
  public static void createAndStartService() throws IOException {
    service = new ChromeDriverService.Builder().usingDriverExecutable(new File("Resources/chromedriver.exe"))
        .usingAnyFreePort().build();
    service.start();
  }

  // {@literal @AfterClass}
  public static void createAndStopService() {
    service.stop();
  }

  // {@literal @Before}
  public void createDriver() {
    driver = new RemoteWebDriver(service.getUrl(), DesiredCapabilities.chrome());
  }

  // {@literal @After}
  public void quitDriver() {
    driver.quit();
  }

  Sheet sheet;
  Cell cell;
  String ASIN, marketplace, tab, linkToSOP, t;

  public int ketData() throws IOException {

    FileInputStream finput = null;
    int k;

    finput = new FileInputStream(new File("ProductFeature.xlsm"));
    Workbook workbook = WorkbookFactory.create(finput);

    sheet = workbook.getSheetAt(0);

    k = sheet.getLastRowNum();

    return k;
  }

  public void getValues(int j) {

    cell = sheet.getRow(j).getCell(0);
    ASIN = cell.getStringCellValue();
    cell = sheet.getRow(j).getCell(1);
    marketplace = cell.getStringCellValue();
    cell = sheet.getRow(j).getCell(2);
    tab = cell.getStringCellValue();
    cell = sheet.getRow(j).getCell(3);
    linkToSOP = cell.getStringCellValue();
  }

  // {@literal @Test}
  public void remProdFeat() throws InterruptedException {
    WebDriverWait wait = new WebDriverWait(driver, 30);
    driver.get("https://selection.amazon.com/embed/singleItem?asin=" + ASIN + "&merchantName=" + marketplace
        + "&selectedTab=" + tab);
    try {
      Thread.sleep(5000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    Thread.sleep(2000);
    System.out.println(tab);
    String t1, t2;
    t1 = "vendorContributionsTab";
    t2 = "productInformationTab";
    if (tab.equals(t1)) {
    wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@id='vendorContributionsPane']")));
      
    driver.findElement(By.xpath(
        "//body[1]/div[4]/div[1]/div[1]/div[2]/div[1]/div[1]/div[2]/div[1]/div[1]/div[1]/div[2]/table[1]/thead[1]/tr[2]/td[3]/input[1]"))
        .sendKeys("rtip_deprecated_product_features");
    try {
      driver.findElement(By.xpath("//span[contains(text(),'rtip_deprecated_product_features')]")).click();
    } catch (Exception e) {
      return;
    }
    Thread.sleep(2000);
      wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
          "//span[text()='rtip_deprecated_product_features']/ancestor::div[@id='vendorContributionsPane']//tr//div[contains(@class,'sc-attribute-controls-container sc-editable')]")));
      driver.findElement(By.xpath(
          "//span[text()='rtip_deprecated_product_features']/ancestor::div[@id='vendorContributionsPane']//tr//div[contains(@class,'sc-attribute-controls-container sc-editable')]"))
          .click();
    } else if (tab.equals(t2)) {
      wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a[contains(text(),'Product Information')]")));

    driver.findElement(By.cssSelector("div.container-fluid:nth-child(5) div.tab-content div.tab-pane.container-fluid.active:nth-child(2) table.table.sc-grid.sc-spacing-small thead.sc-grid-header tr.sc-asin-table-filter-row:nth-child(2) td.sc-filter-cell.sc-filter-cell-attribute:nth-child(2) > input.form-control.sc-grid-col-filter.sc-grid-col-filter-attribute")).sendKeys("rtip_deprecated_product_features");
    try {
      driver.findElement(By.xpath("//span[contains(text(),'rtip_deprecated_product_features')]")).click();
    } catch (Exception e) {
      return;
    }
    Thread.sleep(2000);
      wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
          "//span[text()='rtip_deprecated_product_features']/ancestor::div[@id='productInformationPane']//tr//div[contains(@class,'sc-attribute-controls-container sc-editable')]")));
      driver.findElement(By.xpath(
          "//span[text()='rtip_deprecated_product_features']/ancestor::div[@id='productInformationPane']//tr//div[contains(@class,'sc-attribute-controls-container sc-editable')]"))
          .click();
    } else {
      System.out.println("no tabs present");
      
    }
    List<WebElement> txt = driver.findElements(By.xpath("//table[contains(@class,'sc-nested-table')]//input"));
    for (WebElement tet : txt) {
      tet.click();
      tet.clear();
    }
    driver.findElement(By.xpath("//button[@id='preview-button' and @class='btn btn-primary' != @disabled]")).click();
    driver.findElement(By.xpath("//textarea")).sendKeys(linkToSOP);
    driver.findElement(By.xpath("//button[contains(text(),'Submit')]")).click();
    wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[contains(text(),'Stop submission')]")));
    Thread.sleep(2000);
  }
}