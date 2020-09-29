package automatic;

import java.util.List;
import java.util.Scanner;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import Collection.FinalProcess;
import Collection.Properties;
import RoutineCalculate.Routine;

public class AutoFlow {
	public static int rejectStep=1;
	public static void autoFlow(String flowName,String fromer,String flowTitle,int rejectStep) {
		FinalProcess<String,String> workFlow=Routine.finalFlow(Routine.calcFlow(Routine.getProcess(flowName), fromer),fromer);
		workFlow.put(0,"������",fromer); 
		long start =System.currentTimeMillis();
		plantFlowCore(flowTitle,rejectStep, workFlow);
		long end=System.currentTimeMillis();
		long cast=end-start;
		String timeunite="����";
		if(cast>=1000) {
			cast=cast/1000;
			timeunite="��";
		}
		if(cast>=60) {
			cast=cast/60;
			timeunite="����";
		}
		System.out.println(">>>>>>>>���Խ�������������ʱ�䣺"+cast+timeunite);
		System.out.println("\n���̱��⣺"+flowName);
		System.out.println("\n�����ߣ�"+fromer);
		System.out.println("\n�������ƣ�"+flowName);
		System.out.println("\n>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>\n\n\n\n");
	}
	public static  void plantFlowCore(String flowTitle,int from,FinalProcess<String,String> workFlow){
		try {
			Audit.songhong.close();
		}catch(Exception e){
		}
		Properties.reload();
		Properties.loadProperties();
		String driverType=Properties.getValue("driver");
		if(driverType==null) {
		}else if(driverType.equals("chrome")){
			Audit.songhong=new ChromeDriver();
		}else {
			Audit.songhong=new FirefoxDriver();
		}
		Audit.waiter= new WebDriverWait(Audit.songhong,5);
		int abort=1000;	
		FinalProcess<String, String>sh=workFlow;
		if(sh==null) {
			return;
		}
		if(from>-1) {
			abort=from;
		}else {
			from=sh.size()-Math.abs(from);
		}
		String fromaccount=Routine.getAccountByName(sh.getValue(0));
		if(fromaccount==null) {
			System.out.println("���棺δ�ҵ��÷����ˣ�������");
			return;
		}
		for(int i=1;i<sh.size();i++) {
			boolean arrave=false;
			String auditer=Routine.getAccountByName(sh.getValue(i));		//��ȡ����ߵ��˺�
			if(sh.getValue(i).equals("������")) {
				auditer=fromaccount;
			}
			System.out.println(sh.getKey(i)+"----------------------------------"+sh.getValue(i)+"��ʼ���");
			boolean log_1=Audit.login(auditer,Properties.getParameter("globalPwd") );
			arrave=log_1;
			int log_2=-1;
			if(log_1) {
				log_2=Audit.clickFlowCe(flowTitle);
			}else{
				System.out.println("�û���¼ʧ�ܣ����ֶ���¼����ɺ������롰done������");
				boolean continu=manual();
				if(!continu){
					System.out.println("�������жϣ�");	
					return;
				}
				try {
					Audit.songhong.close();
				}catch(Exception e) {}
				if(driverType==null) {
					
				}else if(driverType.equals("chrome")){
					Audit.songhong=new ChromeDriver();
				}else {
					Audit.songhong=new FirefoxDriver();
				}
				Audit.waiter= new WebDriverWait(Audit.songhong,5);
				continue;
			}
			if(log_2==0) {
				if(abort==i) {
					abort+=rejectStep;
					Audit.accept(false,flowTitle);
					i=-1;
					Audit.login(fromaccount,Properties.getParameter("globalPwd"));
					Audit.clickFlowCe(flowTitle);
					boolean b=Audit.resubmit(flowTitle);
					if(!b) {
						System.out.println("����δ�ɹ���ˣ����ֶ���ˣ���ɺ������롰done������");
						boolean continu=manual();
						if(!continu) {
							System.out.println("�������жϣ�");
							return;
						}
						try {
							Audit.songhong.close();
						}catch(Exception e) {}
						if(driverType==null) {
							
						}else if(driverType.equals("chrome")){
							Audit.songhong=new ChromeDriver();
						}else {
							Audit.songhong=new FirefoxDriver();
						}
						Audit.waiter= new WebDriverWait(Audit.songhong,5);
						continue;
					}
				}else {
					boolean b=Audit.accept(true,flowTitle);
					if(!b) {
						System.out.println("����δ�ɹ���ˣ����ֶ���ˣ���ɺ������롰done������");
						boolean continu=manual();
						if(!continu) {
							System.out.println("�������жϣ�");
							return;
						}
						try {
							Audit.songhong.close();
						}catch(Exception e) {}
						if(driverType==null) {
							
						}else if(driverType.equals("chrome")){
							Audit.songhong=new ChromeDriver();
						}else {
							Audit.songhong=new FirefoxDriver();
						}
						Audit.waiter= new WebDriverWait(Audit.songhong,5);
						continue;
					}
				}
			}else{
				System.out.println("������˹��ϣ����ֶ���ˣ���ɺ������롰done������");
				boolean continu=manual();
				if(!continu) {
					System.out.println("�������жϣ�");
					return;
				}
				try {
					Audit.songhong.close();
				}catch(Exception e) {}
				if(driverType==null) {
					
				}else if(driverType.equals("chrome")){
					Audit.songhong=new ChromeDriver();
				}else {
					Audit.songhong=new FirefoxDriver();
				}
				Audit.waiter= new WebDriverWait(Audit.songhong,5);
				continue;
			}
		}
	}
	public static void manualFlow(String name,String flowString,int rejectStep) {
		if(flowString.contains("��")) {
		String[]str=flowString.split("��");
		FinalProcess<String,String> proc=new FinalProcess<String, String>();
		for(int i=0;i<str.length;i++) {
			proc.put("�ֶ�����",str[i]);
		}
long start =System.currentTimeMillis();
		plantFlowCore(name,rejectStep,proc);
		long end=System.currentTimeMillis();
		long cast=end-start;
		String timeunite="����";
		if(cast>=1000) {
			cast=cast/1000;
			timeunite="��";
		}
		if(cast>=60) {
			cast=cast/60;
			timeunite="����";
		}
		System.out.println("\t\t�����Խ�����������������ʱ�䣺"+cast+timeunite);
		System.out.println("\n����������˵�ÿ���ڵ���ɸô������ִ�����ṩ�����Խű���δ���ṩ�����̽ڵ����У�ˣ�"
				+ "\n�ʲ�����Ϊ���̽ڵ���ȷ�����ݣ�����ȷ�����̽ڵ���ȷ�����ڱ�������ִ����ɺ����˸�����״̬�ѱ�Ϊ����������"
				+ "\n�������Ϊ�ô����̽ڵ���ȷ������Ҫȷ�������������̿�����������������������˵����");
		System.out.println("\n>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>\n\n\n\n");
		}else {
			System.out.println("���̸�ʽ��Ч,������!!");
		}
	}
	public static boolean manual() {
		Scanner s=new Scanner(System.in);
		while(true) {
			String msg=s.nextLine();
			if(msg.equals("done")) {
				return true;
			}else if(msg.equals("quit")) {
				return false;
			}
		}
	}
}
