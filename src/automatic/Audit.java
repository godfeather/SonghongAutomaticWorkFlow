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
	public static String dateStamp;
	/** 页面中流程的创建时间；(dateStamp为空时登记时间戳，不验证)
	 * 后续都会审核相同的时间戳并且标题相同的流程,列表中找到相同标题的流程但时间戳不一致也视为流程未找到；
	 * 用于确保整个流程中对同一个流程进行审核
	 */
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
		login("org","0000");
		debug = true;
		//Audit.dateStamp = "2021-01-10 16:43:00";
        if (clickFlowCe("领用-林瑜") == 0) {
			boolean su = accept(true,"");
			if (su) {
				if (checkFlowShouldInUndealtListAfterAccept("领用-林瑜","李丹","赵艳")) {
					logD("没问题");
				} else {
					logD("与预期不符");
				}
			}
		}

	}
	public static boolean login(String user,String pwd) {
		debug=Properties.getParameter("debug")!=null&&Properties.getParameter("debug").equals("true")?true:false;
		songhong.get(loginHome);
		try {
			songhong.findElement(By.id("lr_username")).sendKeys(user);
			songhong.findElement(By.id("lr_password")).sendKeys(pwd);
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
	 * @return 审核状态 0 表示成功 -1 表示失败未找到
	 */
	public static int clickFlowCe(String title) {			//根据流程名选中流程
		JavascriptExecutor js=(JavascriptExecutor)songhong;
        while (true) {
            try {
				js.executeScript("$(\"#021a59b0-2589-4f9e-8140-6052177a967c\").click();");
				songhong.switchTo().frame("lr_iframe_021a59b0-2589-4f9e-8140-6052177a967c");
				logD("已打开“代办任务”列表");
				break;
			} catch (Exception e) {
				logD("打开代办列表失败，正在重试...");
			}
		}
		// 以下为增强实现
        int loadingFlag = 0; // 0表示未加载，1表示正在加载
		while (true) { // 等待显示正在加载的窗口
			WebElement loadingLayer = null;
			try {
				loadingLayer = songhong.findElement(By.id("jfgrid_loading_girdtable1"));
			} catch (Exception e) {
				logD("判断列表是否加载的标志元素未找到，正在重试");
			    continue;
			}
			String display = loadingLayer.getCssValue("display");
			if (loadingFlag == 0) {
				if (!"none".equals(display)) {
					logD("列表开始加载，正在等待其加载结束");
					loadingFlag = 1;
				}
			} else {
			    if ("none".equals(display)) {
			        logD("列表加载完成");
			        break;
				}
			}
		}
		// 以上为增强实现
		try {
			trying( By.className("jfgrid-data-cell"));
		}catch(Exception e) {
			System.err.println("未找到为何数据，该用户的任务列表未空！");
			return -1;
		}
		WebElement worksta=songhong.findElements(By.className("jfgrid-scrollarea-content")).get(1);
		List<WebElement> li=worksta.findElements(By.className("jfgrid-data-cell"));
		logD("任务列表长度: " + li.size());
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
					String time = li.get(i + 4).getText();
					if (dateStamp != null && dateStamp.equals(time) || dateStamp == null) { // 时间与预期一致或时间未登记都可以审核
					    if (dateStamp == null) {
							dateStamp = time;
						}
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
					} else { // 时间已登记但与当前检索流程的时间不一致
					}

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

	/**
	 * 检查流程在审核后是否应该在待办列表中,
	 * @return 返回值为true表示正确，false表示不正确
	 * @param title 流程标题
	 * @param current 当前审核者
	 * @param next 下一个审核者
	 */
	public static boolean checkFlowShouldInUndealtListAfterAccept (String title,String current,String next) {
		while (true) {
			try {
				songhong.switchTo().frame("lr_iframe_021a59b0-2589-4f9e-8140-6052177a967c");
				logD("web content已切换到待办列表所对应的iframe");
				break;
			} catch (Exception e) {
				logD("试图切换web content到待办列表所对应的iframe失败，正在重试...");
			}
		}
		int loadingFlag = 1; // 0表示未加载，1表示正在加载
		while (true) { // 等待显示正在加载的窗口
			WebElement loadingLayer = null;
			try {
				loadingLayer = songhong.findElement(By.id("jfgrid_loading_girdtable1"));
			} catch (Exception e) {
				logD("判断列表是否加载的标志元素未找到，正在重试");
				continue;
			}
			String display = loadingLayer.getCssValue("display");
			if (loadingFlag == 0) {
				if (!"none".equals(display)) {
					logD("列表开始加载，正在等待其加载结束");
					loadingFlag = 1;
				}
			} else {
				if ("none".equals(display)) {
					logD("列表加载完成");
					break;
				}
			}
		}
		WebElement worksta = songhong.findElement(By.id("jfgrid_scrollarea_girdtable1"));
		List<WebElement> li = worksta.findElements(By.className("jfgrid-data-cell"));
		int width=9;
		for(int i=2;i<li.size();i++) {
			if(width==9) {
				String ftitle=null;
				while(true) {
					try {
						ftitle=li.get(i).getText();
						break;
					}catch(StaleElementReferenceException e) {
					    logD("元素过期，正在重新尝试");
						worksta=songhong.findElement(By.id("jfgrid_scrollarea_girdtable1"));
						li=worksta.findElements(By.className("jfgrid-data-cell"));
					}
				}

				if(ftitle.equals(title)) {
					String time = li.get(i + 4).getText();
					if (dateStamp != null && dateStamp.equals(time) || dateStamp == null) { // 时间与预期一致或时间未登记都可以审核
						if (current.equals(next)) {
						    return true;
						}
					} else { // 时间已登记但与当前检索流程的时间不一致
					}

				}
				width=0;
			}
			width++;
		}
		if (current.equals(next)) {
		    return false;
		} else {
			return true;
		}
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
					trying( By.className("layui-layer-iframe"),1);
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
					songhong.findElement(By.className("layui-layer-btn0")).click();
					try {
						trying(By.className("layui-layer-iframe"),1);			//抛出异常说明未找到元素直接跳出如果找到提交对话框说明提交未成功则重新提交
						System.out.println("提交失败，正在重试……");
					}catch(Exception e) {
						if(debug) {
							e.printStackTrace();
						}
						System.out.println("提交完成");
						break;
					}
				}catch(Exception e) {
					System.out.println("点击“确认”按钮失败，正在重试……");
					if(debug) {
						e.printStackTrace();
					}
				}
			}
			Thread.sleep(wait);

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
						System.out.println("流程【" +title+ "】在" + tryingCount+"秒内未重新提交完成，请手动重新提交后输入“done”继续");
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
			while (true) {
			    try {
					songhong.findElement(By.id("verify")).click();
					logD("点击成功");
					break;
				} catch (Exception e) {
			        logD("点击“审核流程”按钮失败，正在重新尝试");
				}
			}
            songhong.switchTo().defaultContent();
			for (int i = 0;; i ++) { // 判断点击审核按钮后是否打开审核流程的窗口（可能因某些原因无法打开）
			    try {
					songhong.findElement(By.className("layui-layer-iframe"));
					logD("已检测到审核窗口被打开");
					break;
				}catch (Exception e) {
			        logD("未检测到审核窗口打开，正在重试。。。");
				}
				if (i >= 100) {
				    logD("在指定次数的检测中仍未打开审核窗口,已放弃");
					return false;
				}
			}
			while (true) {
			    try {
					WebElement auditFormIframe = songhong.findElement(By.id("AuditFlowForm")).findElement(By.tagName("iframe"));
					songhong.switchTo().frame(auditFormIframe.getAttribute("id"));
					logD("Web content已成功切换至审核表单iframe");
					break;
				} catch (Exception e) {
			        logD("审核表单所对应的iframe未找到，正在重试...");
				}
			}
			JavascriptExecutor js=(JavascriptExecutor)songhong;

			while(true){
				try{
					js.executeScript("$(\"#lr_form_bg\").hide()");
					logD("元素已成功隐藏");
					break;
				}catch(Exception e){
					logD("js执行失败，正在重试...");
				}
			}
			//Thread.sleep(wait);
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
			for (int i = 0;;i ++) {
			    try {
					songhong.findElement(By.className("layui-layer-iframe")).findElement(By.className("layui-layer-btn0")).click();
					logD("审核窗口已正确关闭");
					break;
				} catch (Exception e) {
			        logD("检测到审核窗口未关闭，正在重试...");
				}
			    if (i > 100) {
			        return false;
				}
			}
			for(int i=0;i<tryingCount;i++) {	//等待审核完毕返回任务页
				String id=null;
				try {
					id=songhong.findElement(By.className("active")).getAttribute("id");
				}catch(Exception e) {
					if(debug) {
						System.out.println("验证是否审核通过时，active抛出异常，详情查看异常内容");
					}
					continue;
				}
				if(id.equals("lr_tab_021a59b0-2589-4f9e-8140-6052177a967c")) {
					logD("检测到流程已成功审核");
					return true;
				}else {
					Thread.sleep(1000);
						String out = "正在重新检查流程状态..(检查会在" + (tryingCount - i) + "次后退出)";
						cleanScreenLine(out.length() + 20);
						System.out.print(out);
					if(i==tryingCount-1) {
						cleanScreenLine(out.length() + 20);
						System.out.println("流程【" + title + "】在" + tryingCount+"次检查期间内未完成审核，请手动审核为【" + (accept ? "同意": "不同意") + "】后输入“done”继续");
					}
				}
			}
		}catch(Exception e) {
			if(debug) {
			    logD("未预期的错误: " + e.getMessage());
			}
			return false;
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
		System.out.println("点击成功，正在等待页面加载结束");
		while (true) {
			String displayValue = songhong.findElement(By.id("jfgrid_loading_girdtable")).getCssValue("display");
			if ("none".equals(displayValue)) {
				System.out.println("页面加载结束");
				break;
			}
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
				System.err.println("未找到\""+flowName+"\"流程!");
				return null;
			}
		}
		return null;
	}

	/**
	 * 检查流程节点
	 */
	public static void checkFLow() {

	}

	/**
	 * 调试日志信息
	 * @param str 输入内容
	 */
	public static void logD(String str) {
	    if (debug) {
			System.out.println(str);
		}
	}
	public static void cleanScreenLine (int charCount) {
	    for (int i = 0;i < charCount; i ++) {
			System.out.print("\b");
		}
	}
}
