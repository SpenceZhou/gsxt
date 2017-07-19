package com.dataofbank.gsxt.verification;

import java.io.IOException;
import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;

import com.dataofbank.gsxt.img.ImgUtil;


public class Code {

	static {
		System.setProperty("webdriver.chrome.driver", "C:\\Program Files (x86)\\chromedriver_win32\\chromedriver.exe");
	}
	
	public void verify(String keyword) throws IOException{
		
		String url = "http://www.gsxt.gov.cn/index.html";
		
		ChromeDriver driver =  new ChromeDriver();
		
		driver.get(url);
		
		WebElement input = null;
		try {
			input = driver.findElementById("keyword");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		
		input.clear();
		input.sendKeys(keyword);
		
		sleep(2000);
		
		WebElement queryBtn = driver.findElementById("btn_query");
		queryBtn.click();
		
		
		byte[] screen = driver.getScreenshotAs(OutputType.BYTES);
		
		
		byte[] screenCut = ImgUtil.cutImage(screen, 325, 380, 250, 100);
		

		
		sleep(1000);
		WebElement sliderBtn = driver.findElement(By.className("gt_slider_knob"));
		sliderBtn.click();
		sleep(2000);
		
		
		byte[] screen1 = driver.getScreenshotAs(OutputType.BYTES);
		
		byte[] screenCut1 = ImgUtil.cutImage(screen1, 325, 380, 250, 100);
		
		
		int x = ImgUtil.getX(screenCut, screenCut1);
		

		int[] xArray = getX(x,10);
        
		Actions action = new Actions(driver);
		
		for(int i=0;i<xArray.length;i++){
			
			System.out.println(xArray[i]);
			
			action.clickAndHold(sliderBtn).moveByOffset(xArray[i], 0);
			action.pause(Duration.ofMillis(200));
			
		}
		
		action.moveToElement(sliderBtn).release();
		Action actions = action.build();
		
		actions.perform();
		sleep(1000);
	}
	
	
	private int[] getX(int x,int length){
		
		System.out.println("总长度:"+x);
		int[] result = new int[length];
		int r = x;
		for(int i=0;i<length-1;i++){
			Float f =  0.3f;//new Random().nextFloat();
	
			int value = (int) ((int) r*f);
			result[i] = value>0?value:1;
			r = r - result[i];
		}
		
		result[0] = result[0]+r-1;
		result[length-1] = 1;
		return result;
	}
	
	private void sleep(int millis){
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws IOException {
		Code code = new Code();
		code.verify("国家电网");
	}
}
