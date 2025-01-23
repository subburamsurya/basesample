package org.global;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.Alert;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

import io.github.bonigarcia.wdm.WebDriverManager;

public class BaseClass {

	public static WebDriver driver;
	public static JavascriptExecutor javascriptExecutor;
	public static File file;
	public static Workbook book;
	public static Sheet sheet;
	public static Row row;
	public static Cell cell;

//the code was changed by the qa to  check conflict merger
//verify it and send by qa 	
// some change has made by dev ro know the conflict
	//dev req

	
	public static BaseClass baseClass= new BaseClass();


	//getting driver
	public void getDriver(String browserName) {
		switch (browserName) {
		case "Chrome":
			WebDriverManager.chromedriver().setup();
			driver= new ChromeDriver();
			break;
		case "Edge":
			WebDriverManager.edgedriver().setup();
			driver= new EdgeDriver();
			break;
		case "Firefox":
			WebDriverManager.firefoxdriver().setup();
			driver= new FirefoxDriver();
		default:
			System.out.println("Invalid Browser Type");
			break;
		}
	}

	//getting url as string
	public void getUrl(String url) {
		driver.get(url);
	}
	//to maximize the window
	public void maxWindow() {
		driver.manage().window().maximize();
	}

	//to minimize the window
	public void minWindow() {
		driver.manage().window().minimize();
	}

	//takeScreenshot
	public void takeScreenshot(String name) throws IOException {
		TakesScreenshot takesScreenshot= (TakesScreenshot)driver;
		File sourceFile = takesScreenshot.getScreenshotAs(OutputType.FILE);
		File targetFile= new File("C:\\Users\\ASUS\\eclipse-workspace\\AdactinHotel\\ErrorImages\\"+name+".png");
		FileUtils.copyFile(sourceFile, targetFile);

	}

	//getText
	public String getTextBYJava(WebElement element) {
		String text = element.getText();
		return text;
	}

	//getAttribute by java
	public String getAttributeByJava(WebElement element) {
		String attribute = element.getAttribute("value");
		return attribute;
	}

	//sendKeys 
	public void sendKeysByJava(WebElement element,String keysToSend) {
		element.sendKeys(keysToSend);
	}

	//click
	public void clickByJava(WebElement element) {
		element.click();
	}

	//select by value
	public void selectByValue(WebElement element,String value) {
		Select select= new Select(element);
		select.selectByValue(value);
	}

	//alert accept
	public void alertAccept() {
		Alert a = driver.switchTo().alert();
		a.accept();
	}

	//alert dismiss
	public void alertDismiss() {
		Alert a = driver.switchTo().alert();
		a.dismiss();
	}

	//sendkeys by javascript executor
	public void sendKeysByJSE(WebElement element,String keysToSend) {
		javascriptExecutor=(JavascriptExecutor) driver;
		javascriptExecutor.executeScript("arguments[0].setAttribute('value','"+keysToSend+"')", element);
	}

	//click by javascript executor
	public void clickByJSE(WebElement element) {
		javascriptExecutor=(JavascriptExecutor)driver;
		javascriptExecutor.executeScript("arguments[0].click()", element);
	}

	//scrollup by javascript executor
	public void ScrollByJSE(WebElement element,String scrollType) {
		javascriptExecutor=(JavascriptExecutor)driver;
		switch (scrollType) {
		case "Up":
			javascriptExecutor.executeScript("arguments[0].scrollIntoView(false)", element);
			break;

		case "Down":
			javascriptExecutor.executeScript("arguments[0].scrollIntoView(true)", element);
			break;

		default:
			System.out.println("Invalid Scroll Type");
			break;
		}
	}

	//getAttribute by javascript executor
	public Object getAttributeByJSE(WebElement element) {
		javascriptExecutor= (JavascriptExecutor) driver;
		Object executeScript = javascriptExecutor.executeScript("return arguments[0].getAttribute('value')", element);
		return executeScript;
	}

	public void windowsHandling(int indexOfRequiredWindows) {
		Set<String> windowHandles = driver.getWindowHandles();
		//creating empty list
		List<String> list= new LinkedList<String>();
		//add all from set to list
		list.addAll(windowHandles);
		//get particular window tab by using get method from list
		String requiredWindowId = list.get(indexOfRequiredWindows);
		//now change window using driver
		driver.switchTo().window(requiredWindowId);
	}

	public void navigation(String command) {
		switch (command) {
		case "refresh":
			driver.navigate().refresh();
			break;
		case "forward":
			driver.navigate().forward();
			break;
		case "back":
			driver.navigate().back();
			break;
		default:
			System.out.println("Invalid command");
			break;
		}
	}

	public void dragAndDropByJava(WebElement source,WebElement target) {
		Actions action=new Actions(driver);
		action.dragAndDrop(source, target).build().perform();
	}

	public  String getExcelData(String sheetName,int rowNum,int cellNum) throws IOException {



		file= new File("DataBase\\HelloWorld.xlsx");
		FileInputStream fileInputStream=new FileInputStream(file);
		book= new XSSFWorkbook(fileInputStream);

		sheet = book.getSheet(sheetName);
		Row row = sheet.getRow(rowNum);
		cell = row.getCell(cellNum);

		CellType cellType = cell.getCellType();
		String value= null;

		switch (cellType) {

		case STRING:
			value = cell.getStringCellValue();

			break;
		case NUMERIC:

			if (DateUtil.isCellDateFormatted(cell)) {
				Date dateCellValue = cell.getDateCellValue();
				SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd/MM/yyyy");
				value = simpleDateFormat.format(dateCellValue);

			} else {
				double numericCellValue = cell.getNumericCellValue();
				long ref= (long) numericCellValue;
				BigDecimal valueOf = BigDecimal.valueOf(ref);
				value = valueOf.toString();
			}
			break;
		default:
			break;
		}

		return value;
	}

	public void writeNewExcelData(WebElement element,int rownum,int cellnum,String success,String Failure) throws IOException {
		//file= new File("DataBase\\\\HelloWorld.xlsx");
		FileOutputStream fileOutputStream= new FileOutputStream(file);
		cell= sheet.createRow(rownum).createCell(cellnum);

		if(element.isDisplayed()) {
			cell.setCellValue(success);
		}else {
			cell.setCellValue(Failure);
		}
		book.write(fileOutputStream);
	}

	public  void updateExcelData(String newValue,String oldValue,String sheetName,int rowNum,int cellNum) throws IOException {


		file= new File("DataBase\\HelloWorld.xlsx");
		FileInputStream fileInputStream=new FileInputStream(file);
		book= new XSSFWorkbook(fileInputStream);

		sheet = book.getSheet(sheetName);
		row = sheet.getRow(rowNum);
		cell = row.getCell(cellNum);

		CellType cellType = cell.getCellType();
		String value= null;

		switch (cellType) {

		case STRING:
			value = cell.getStringCellValue();

			break;
		case NUMERIC:

			if (DateUtil.isCellDateFormatted(cell)) {
				Date dateCellValue = cell.getDateCellValue();
				SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd/MM/yyyy");
				value = simpleDateFormat.format(dateCellValue);

			} else {
				double numericCellValue = cell.getNumericCellValue();
				long ref= (long) numericCellValue;
				BigDecimal valueOf = BigDecimal.valueOf(ref);
				value = valueOf.toString();
			}
			break;
		default:
			break;
		}

		if(value.equals(oldValue)) {
			cell.setCellValue(newValue);
			FileOutputStream fileOutputStream= new FileOutputStream(file);
			book.write(fileOutputStream);
		}
	}


	public String getPropertiesData(String key) throws IOException {

		File file=new File("");
		FileReader fileReader= new FileReader(file);
		Properties properties=new Properties();
		properties.load(fileReader);
		String property = properties.getProperty(key);
		return property;
	}


	public void validateContent(WebElement element, String successMessage,String errorMessage) {
		if (element.isDisplayed()) {
			if (element.isEnabled()) {
				System.out.println(successMessage);
			}
		}
		else {
			System.out.println(errorMessage);
		}
	}





}
