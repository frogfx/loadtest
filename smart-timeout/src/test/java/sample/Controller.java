package sample;

import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;

import java.io.File;

import org.littleshoot.proxy.HttpFilters;
import org.littleshoot.proxy.HttpFiltersSource;
import org.openqa.selenium.By;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.client.ClientUtil;
import net.lightbody.bmp.core.har.Har;
import net.lightbody.bmp.filters.RequestFilter;
import net.lightbody.bmp.filters.ResponseFilter;
import net.lightbody.bmp.util.HttpMessageContents;
import net.lightbody.bmp.util.HttpMessageInfo;


public class Controller {

public static void main(String[] args) throws Exception {
		
	
	
	BrowserMobProxyServer	server = new BrowserMobProxyServer();
	server.start();
	int port = server.getPort();
	Proxy proxy = ClientUtil.createSeleniumProxy(server);
//	Proxy proxy = new Proxy();
	
	
	server.addRequestFilter(new RequestFilter() {
		
		public HttpResponse filterRequest(HttpRequest arg0,
				HttpMessageContents arg1, HttpMessageInfo arg2) {
			// TODO Auto-generated method stub
			System.out.println(arg0.getUri());
			return null;
		}
	});
	
	
	server.addResponseFilter(new ResponseFilter() {
		
		public void filterResponse(HttpResponse response,
				HttpMessageContents contents, HttpMessageInfo messageInfo) {
			// TODO Auto-generated method stub
			System.out.println(response.getStatus().code());
		}
	});
	
	DesiredCapabilities seleniumCapabilities = new DesiredCapabilities();
	seleniumCapabilities.setCapability(CapabilityType.PROXY, proxy);

	
	 
		// Start the Browser up.
		WebDriver driver = new FirefoxDriver(seleniumCapabilities);
 
		// Create a new HAR with the label "StockMarketData"
	server.newHar("StockMarketData");
 
		// Open the Google homepage.
		driver.get("http://www.google.com");
 
		// Find the search edit box on the Google page.
		WebElement searchBox = driver.findElement(By.name("q"));
 
		// Type in Selenium.
		searchBox.sendKeys("Selenium");
 
		// Find the search button.
		WebElement button = driver.findElement(By.name("btnG"));
 
		// Click the button.
		button.click();
		
		Thread.sleep(5000);
		
		Har har = server.getHar();
		File harFile = new File("C:\\japan\\teknosa_test.har");
		har.writeTo(harFile);
		
		// Stop the BrowserMob Proxy Server
		server.stop();
		
		// Close the browser
		driver.quit();
	}

}
