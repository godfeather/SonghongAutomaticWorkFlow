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
		login("shenggang","0000");
		checkFlowStatus("�ʲ�����-ͯ����");
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
				songhong.findElement(By.id("lr_username"));					//�����û��������������ڷ������ң�ֱ���������Դ��������ʾ��¼ʧ��
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
	 * ͨ���ṩ�����̱�������������ҳ
	 * @param title ���̱���
	 * @return ���״̬
	 */
	public static int clickFlowCe(String title) {			//����������ѡ��
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
			System.err.println("δ�ҵ�Ϊ�����ݣ����û��������б�δ�գ�");
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
									System.out.println("Ԫ�ع���");
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
				System.out.println("δ�ҵ����̣�");
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
		while(true) {								//��������ύ�����Ƿ�ر�
			try {
				trying(By.className("layui-layer-iframe1"));			//�׳��쳣˵��δ�ҵ�Ԫ��ֱ����������ҵ��ύ�Ի���˵���ύδ�ɹ��������ύ
				try {
					songhong.findElement(By.id("layui-layer1")).findElement(By.className("layui-layer-btn0")).click();
					System.out.println("�������������ύ����");
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
		for(int i=0;i<tryingCount;i++) {																								//�ȴ������Ϸ�������ҳ
			String id=null;
			try {
				id=songhong.findElement(By.className("active")).getAttribute("id");
			}catch(StaleElementReferenceException e) {
				if(debug) {
					e.printStackTrace();
					System.out.println("���ҵ���iframe����");
				}
				return true;
			}
			if(id.equals("lr_tab_021a59b0-2589-4f9e-8140-6052177a967c")) {
				return true;
			}else {
				Thread.sleep(1000);
				System.out.println("���������ύ����");
				if(i==tryingCount-1) {
					System.out.println(tryingCount+"�εȴ�ʧ�ܣ�������δ֪�Ƿ������ͨ��");
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
					System.out.println("jsִ��ʧ�ܣ�����Ԫ��ʧ��");
				}
			}
		}
		Thread.sleep(wait);
		WebElement we=null;
		while(true){
			try{
			if(accept) {
			we=songhong.findElement(By.id("verifyType1"));		//ѡ��ͬ��
				while(!we.isSelected()) {							//���δѡ�оͶ��
					try{
					we.click();
					}catch(Exception e){
						if(debug) {
							e.printStackTrace();
						}
					}
					System.out.println("���ֵ��------------------------��ͬ�⡿");
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
					System.out.println("���ֵ��------------------------����ͬ�⡿");
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
		while(true) {								//��������ύ�����Ƿ�ر�
			try {
				songhong.findElement(By.className("layui-layer-iframe1"));			//�׳��쳣˵��δ�ҵ�Ԫ��ֱ����������ҵ��ύ�Ի���˵���ύδ�ɹ��������ύ
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
		for(int i=0;i<tryingCount;i++) {	//�ȴ������Ϸ�������ҳ
			String id=null;
			try {
				id=songhong.findElement(By.className("active")).getAttribute("id");
			}catch(Exception e) {
				if(debug) {
					e.printStackTrace();
					System.out.println("��֤�Ƿ����ͨ��ʱ��active�׳��쳣������鿴�쳣����");
				}
				continue;
			}
			if(id.equals("lr_tab_021a59b0-2589-4f9e-8140-6052177a967c")) {
				if(debug) {
					System.out.println("��˳ɹ�����Ϊ��ǰ����frame�������б�id��ͬ");
				}
				System.out.println("������˳ɹ�");
				return true;
			}else {
				Thread.sleep(1000);
				System.out.println("�����ύ���̡���");
				if(i==tryingCount-1) {
					System.out.println(tryingCount+"�εȴ�ʧ�ܣ�������δ֪�Ƿ������ͨ��");
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
	 * ��ȡ����״̬
	 * @param flowName
	 * @return
	 */
	static return getFlowStatus(String flowName){
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
		songhong.findElement(By.id("lr_left_tree_3")).click();
		try {
			trying( By.className("jfgrid-data-cell"));
		}catch(Exception e) {
			System.err.println("��Ա���Ѱ��б�Ϊ��!");
			return false;
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
								return li.get(i+2)..getElementsByTagName("span")[0].getText();
								}
								break;
							}catch(StaleElementReferenceException e) {
								if(debug) {
									System.out.println("Ԫ���ѹ��ڣ��������ԣ�");
								}
								worksta=songhong.findElement(By.id("jfgrid_scrollarea_girdtable1"));
								li=worksta.findElements(By.className("jfgrid-data-cell"));
							}
						}

				width=0;
			}
			width++;
			if(i==li.size()-1) {
				System.err.println("δ�ҵ�\""+flowName+"\"����!!");
				return false;
			}
		}
		return null;
	}
}
