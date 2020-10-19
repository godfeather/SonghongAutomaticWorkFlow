package automatic;

import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.server.handler.FindElements;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.w3c.dom.Element;

import Collection.Properties;
public class Audit {
	static boolean debug=false;
	public static String loginHome="http://t1.itsmore.com:62046/Login/Index";
	public static int loginTryingCount=5;
	public static int tryingCount=15;
	public static long wait=1000;
	public static WebDriver songhong;
	public static WebDriverWait waiter;
	public static void main(String[] args) {
		System.setProperty(FirefoxDriver.SystemProperty.BROWSER_LOGFILE, "selenium_firefox.log");
		songhong=new FirefoxDriver();
		waiter=new WebDriverWait(songhong, 5);
	}
	public static boolean login(String user,String pwd) {
		debug=Properties.getParameter("debug")!=null&&Properties.getParameter("debug").equals("true")?true:false;
		songhong.get(loginHome);
		try {
			songhong.findElement(By.id("lr_username")).sendKeys(user);
			songhong.findElement(By.id("lr_password")).sendKeys(pwd);
			Thread.sleep(wait);
			songhong.findElement(By.id("lr_login_btn")).click();
		}catch(Exception e) {
			if(debug) {
				e.printStackTrace();
			}
			return false;
		}
		for(int i=0;i<loginTryingCount;i++) {
			try {
				songhong.findElement(By.id("lr_username"));					//查找用户名输入框，如果存在反复查找，直到超过尝试次数，则表示登录失败
				Thread.sleep(1000);
			}catch(Exception e) {
				if(debug) {
					e.printStackTrace();
				}
				return true;
			}
		}
		return false;
	}
	/**
	 * 通过提供的流程标题进入流程审核页
	 * @param title 流程标题
	 * @return 审核状态
	 */
	public static int clickFlowCe(String title) {			//根据流程名选中
		try {
			Thread.sleep(wait);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		JavascriptExecutor js=(JavascriptExecutor)songhong;
		trying( By.className("lr-menu-item-icon"));
		js.executeScript("$(\"#021a59b0-2589-4f9e-8140-6052177a967c\").click();");
		try {
			Thread.sleep(wait);
		} catch (InterruptedException e2) {}
		trying( By.id("lr_iframe_021a59b0-2589-4f9e-8140-6052177a967c"));
		songhong.switchTo().frame("lr_iframe_021a59b0-2589-4f9e-8140-6052177a967c");
		try {
			trying( By.className("jfgrid-data-cell"));
		}catch(Exception e) {
			System.err.println("未找到为何数据，该用户的任务列表未空！");
			return -1;
		}
		WebElement worksta=songhong.findElement(By.id("jfgrid_scrollarea_girdtable1"));
		List<WebElement> li=worksta.findElements(By.className("jfgrid-data-cell"));
		int width=9;
		for(int i=2;i<li.size();i++) {
			
			if(width==9) {
				String ftitle=null;
						while(true) {
							try {
								ftitle=li.get(i).getText();
								break;
							}catch(StaleElementReferenceException e) {
								if(debug) {
									e.printStackTrace();
									System.out.println("元素过期");
								}
								worksta=songhong.findElement(By.id("jfgrid_scrollarea_girdtable1"));
								li=worksta.findElements(By.className("jfgrid-data-cell"));
								
							}
						}
						
				if(ftitle.equals(title)) {
					while(true) {
						try {
							li.get(i).click();
						songhong.findElement(By.id("lr_verify")).click();
						
						}catch(Exception e) {
							if(debug) {
								e.printStackTrace();
							}
						}
						songhong.switchTo().defaultContent();
						String str=songhong.findElement(By.className("active")).getAttribute("id");
						if((str.equals("lr_tab_021a59b0-2589-4f9e-8140-6052177a967c"))) {
							try {
								Thread.sleep(wait);
							} catch (InterruptedException e) {}
							songhong.switchTo().frame("lr_iframe_021a59b0-2589-4f9e-8140-6052177a967c");
							trying( By.className("jfgrid-data-cell"));
							worksta=songhong.findElement(By.id("jfgrid_scrollarea_girdtable1"));
							li=worksta.findElements(By.className("jfgrid-data-cell"));
						}else {
							break;
						}
					}
					return 0;
				}
				width=0;
			}
			width++;
			if(i==li.size()-1) {
				System.out.println("未找到流程！");
				return -1;
			}
		}
		return -1;
	}
	
	public static void shut() {
		if(songhong!=null) {
			songhong.close();
		}
	}
	public static boolean resubmit(String title) {
		try {
		List<WebElement> web=songhong.findElements(By.tagName("iframe"));
		WebElement iframe=web.get(web.size()-1);
		songhong.switchTo().frame(iframe);
		while(true) {
			try {
				trying(By.id("verify_submit"),true);
				songhong.findElement(By.id("verify_submit")).click();
				songhong.switchTo().defaultContent();
				trying( By.id("layui-layer-iframe1"));	  
				break;
			}catch(Exception e) {
				if(debug) {
					e.printStackTrace();
				}
				while(true){
					try{
						songhong.switchTo().frame(iframe);
						break;
					}catch(Exception g){
						if(debug) {
							g.printStackTrace();
						}
						songhong.switchTo().defaultContent();
						web=songhong.findElements(By.tagName("iframe"));
						iframe=web.get(web.size()-1);
					}
				}


			}
		}
		songhong.switchTo().defaultContent();
		while(true) {
			try {
				songhong.findElement(By.id("layui-layer1")).findElement(By.className("layui-layer-btn0")).click();
				break;
			}catch(Exception e) {
				if(debug) {
					e.printStackTrace();
				}
				songhong.switchTo().frame(iframe);
				songhong.findElement(By.id("verify_submit")).click();
				songhong.switchTo().defaultContent();
			}
		}
		Thread.sleep(wait);
		while(true) {								//检测重新提交窗口是否关闭
			try {
				trying(By.className("layui-layer-iframe1"));			//抛出异常说明未找到元素直接跳出如果找到提交对话框说明提交未成功则重新提交
				try {
					songhong.findElement(By.id("layui-layer1")).findElement(By.className("layui-layer-btn0")).click();
					System.out.println("发起人已重新提交……");
				}catch(Exception e) {
					if(debug) {
						e.printStackTrace();
					}
				}
			}catch(Exception e) {
				if(debug) {
					e.printStackTrace();
				}
				break;
			}
		}
		for(int i=0;i<tryingCount;i++) {																								//等待审核完毕返回任务页
			String id=null;
			try {
				id=songhong.findElement(By.className("active")).getAttribute("id");
			}catch(StaleElementReferenceException e) {
				if(debug) {
					e.printStackTrace();
					System.out.println("查找到的iframe过期");
				}
				return true;
			}
			if(id.equals("lr_tab_021a59b0-2589-4f9e-8140-6052177a967c")) {
				return true;
			}else {
				Thread.sleep(1000);
				System.out.println("正在重新提交……");
				if(i==tryingCount-1) {
					System.out.println(tryingCount+"次等待失败，该流程未知是否已审核通过");
				}
			}
		}
		}catch(Exception e) {
			if(debug) {
				e.printStackTrace();
			}
			return false;
		}
		return false;
	}
	public static boolean accept(boolean accept,String title) {
		try {
		List<WebElement> web=songhong.findElements(By.tagName("iframe"));
		WebElement iframe=web.get(web.size()-1);
		songhong.switchTo().frame(iframe);
		while(true) {
			try {
				trying(By.id("verify"),true);
				songhong.findElement(By.id("verify")).click();
				songhong.switchTo().defaultContent();
				trying( By.id("layui-layer-iframe1"));	  
				break;
				}catch(Exception e) {
					if(debug) {
						e.printStackTrace();
					}
					songhong.switchTo().frame(iframe);
				}
		}
		trying(By.id("layui-layer-iframe1"));
		songhong.switchTo().frame("layui-layer-iframe1");
		JavascriptExecutor js=(JavascriptExecutor)songhong;
		
		while(true){
			try{
			js.executeScript("$(\"#lr_form_bg\").hide()");
			break;
			}catch(Exception e){
				if(debug) {
					e.printStackTrace();
					System.out.println("js执行失败，隐藏元素失败");
				}
			}
		}
		Thread.sleep(wait);
		WebElement we=null;
		while(true){
			try{
			if(accept) {
			we=songhong.findElement(By.id("verifyType1"));		//选中同意
				while(!we.isSelected()) {							//如果未选中就多次
					try{
					we.click();
					}catch(Exception e){
						if(debug) {
							e.printStackTrace();
						}
					}
					System.out.println("审核值：------------------------【同意】");
				}
				break;
			}else {
				we=songhong.findElement(By.id("verifyType2"));
				while(!we.isSelected()) {
					try{
					we.click();
					}catch(Exception g){
						if(debug) {
							g.printStackTrace();
						}
					}
					System.out.println("审核值：------------------------【不同意】");
				}
				break;
			}
		}catch(Exception e){
			if(debug) {
				e.printStackTrace();
			}
			continue;	
		}
		}
		songhong.switchTo().defaultContent(); 
		songhong.findElement(By.id("layui-layer1")).findElement(By.className("layui-layer-btn0")).click();
		while(true) {								//检测重新提交窗口是否关闭
			try {
				songhong.findElement(By.className("layui-layer-iframe1"));			//抛出异常说明未找到元素直接跳出如果找到提交对话框说明提交未成功则重新提交
				try {
					songhong.findElement(By.id("layui-layer1")).findElement(By.className("layui-layer-btn0")).click();
				}catch(Exception e) {
					e.printStackTrace();
				}
			}catch(Exception e) {
				if(debug) {
					e.printStackTrace();
				}
				break;
			}
		}
		for(int i=0;i<tryingCount;i++) {	//等待审核完毕返回任务页
			String id=null;
			try {
				id=songhong.findElement(By.className("active")).getAttribute("id");
			}catch(Exception e) {
				if(debug) {
					e.printStackTrace();
					System.out.println("验证是否审核通过时，active抛出异常，详情查看异常内容");
				}
				continue;
			}
			if(id.equals("lr_tab_021a59b0-2589-4f9e-8140-6052177a967c")) {
				if(debug) {
					System.out.println("审核成功，因为当前激活frame与任务列表id相同");
				}
				System.out.println("流程审核成功");
				return true;
			}else {
				Thread.sleep(1000);
				System.out.println("正在提交流程……");
				if(i==tryingCount-1) {
					System.out.println(tryingCount+"次等待失败，该流程未知是否已审核通过");
				}
			}
		}
		}catch(Exception e) {
			if(debug) {
				e.printStackTrace();
			}
			return false;
		}
		try {
			Thread.sleep(wait);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	static boolean trying(By by,boolean needtoclick,int scecond) {
		if(waiter==null) {
			waiter=new WebDriverWait(songhong,scecond);
		}
		
		if(needtoclick) {
				waiter.until(ExpectedConditions.elementToBeClickable(by));
			
		}else {
			
			
				waiter.until(ExpectedConditions.presenceOfElementLocated(by));
				
		}
		return true;
	}
	static boolean  trying(By by){
		return trying(by,false,5);
	}
	static boolean  trying(By by,boolean needclick){
		return trying(by,needclick,5);
	}
	static boolean trying(By by,int second) {
		return trying(by,false,second);
	}
	/**
	 * 用于获取已办任务中指定流程标题的流程状态
	 * @param flowName
	 * @return 对应流程的状态
	 * 调用时机：在某个节点审核通过后，该方法会在“已办任务”中查找对应的流程的状态并返回
	 */
	public static String getFlowStatusAfterAudit(String flowName){
		trying( By.id("lr_iframe_021a59b0-2589-4f9e-8140-6052177a967c"));
		songhong.switchTo().frame("lr_iframe_021a59b0-2589-4f9e-8140-6052177a967c");
		while (true) {
		    try {
				songhong.findElement(By.id("lr_left_tree_3")).click();
				break;
			} catch (Exception e) {
				System.out.println("查找元素和点击失败");
				try {
					Thread.sleep(100);
				} catch (InterruptedException interruptedException) {
					interruptedException.printStackTrace();
				}
			}
		}
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		try {
			trying( By.className("jfgrid-data-cell"));
		}catch(Exception e) {
			System.err.println("该员工已办列表为空!");
			return null;
		}
		WebElement worksta=songhong.findElement(By.id("jfgrid_scrollarea_girdtable1"));
		List<WebElement> li=worksta.findElements(By.className("jfgrid-data-cell"));
		int width=9;
		for(int i=2;i<li.size();i++) {
			if(width==9) {
				String ftitle=null;
						while(true) {
							try {
								ftitle=li.get(i).getText();
								if(ftitle.equals(flowName)){
								return li.get(i + 2).findElements(By.tagName("span")).get(0).getText();
								}
								break;
							}catch(StaleElementReferenceException e) {
								if(debug) {
									System.out.println("元素已过期，将会重试！");
								}
								worksta=songhong.findElement(By.id("jfgrid_scrollarea_girdtable1"));
								li=worksta.findElements(By.className("jfgrid-data-cell"));
							}
						}

				width=0;
			}
			width++;
			if(i==li.size()-1) {
				System.err.println("未找到\""+flowName+"\"流程!!");
				return null;
			}
		}
		return null;
	}
}
