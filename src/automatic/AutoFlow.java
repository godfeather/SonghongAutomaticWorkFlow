package automatic;

import java.util.List;
import java.util.Scanner;

import org.openqa.selenium.By;
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
		workFlow.put(0,"发起者",fromer); 
		long start =System.currentTimeMillis();
		plantFlowCore(flowTitle,rejectStep, workFlow);
		long end=System.currentTimeMillis();
		long cast=end-start;
		String timeunite="毫秒";
		if(cast>=1000) {
			cast=cast/1000;
			timeunite="秒";
		}
		if(cast>=60) {
			cast=cast/60;
			timeunite="分钟";
		}
		System.out.println(">>>>>>>>测试结束，本次消耗时间："+cast+timeunite);
		System.out.println("\n流程标题："+flowTitle);
		System.out.println("\n发起者："+fromer);
		System.out.println("\n流程名称："+flowName);
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
			System.out.println("警告：未找到该发起人，请重试");
			return;
		}
		for(int i=1;i<sh.size();i++) {
			boolean arrave=false;
			String auditer=Routine.getAccountByName(sh.getValue(i));		//获取审核者的账号
			if(sh.getValue(i).equals("申请人")) {
				auditer=fromaccount;
			}
			System.out.println(sh.getKey(i)+"----------------------------------"+sh.getValue(i)+"开始审核");
			boolean log_1=Audit.login(auditer,Properties.getParameter("globalPwd") );
			arrave=log_1;
			int log_2=-1;

			if(log_1) {
				log_2=Audit.clickFlowCe(flowTitle);
			}else{
				System.out.println("用户登录失败，请手动登录，完成后再输入“done”继续");
				boolean continu=manual();
				if(!continu){
					System.out.println("流程已中断！");	
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
						System.out.println("流程未成功审核，请手动审核，完成后在输入“done”继续");
						boolean continu=manual();
						if(!continu) {
							System.out.println("流程已中断！");
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
						System.out.println("流程未成功审核，请手动审核，完成后在输入“done”继续");
						boolean continu = manual();
						if(! continu) {
							System.out.println("流程已中断！");
							return;
						}
						try {
							Audit.songhong.close();
						}catch(Exception e) { }
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
				System.out.println("流程审核故障，请手动审核，完成后在输入“done”继续");
				boolean continu=manual();
				if(!continu) {
					System.out.println("流程已中断！");
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
			System.out.println("正在校验流程是否结束……");
			String status = Audit.getFlowStatusAfterAudit(flowTitle);
			System.out.println("流程审核结束，校验流程状态是否显示已结束：【" + status + "】");
			if ("结束".equals(status)) {
				System.out.println("检验结果：流程已结束");
			} else {
				System.err.println("检验结果：流程未结束!");
			}
		}


	}
	public static void manualFlow(String name,String flowString,int rejectStep) {
		if(flowString.contains("，")) {
		String[]str=flowString.split("，");
		FinalProcess<String,String> proc=new FinalProcess<String, String>();
		for(int i=0;i<str.length;i++) {
			proc.put("手动流程",str[i]);
		}
long start =System.currentTimeMillis();
		plantFlowCore(name,rejectStep,proc);
		long end=System.currentTimeMillis();
		long cast=end-start;
		String timeunite="毫秒";
		if(cast>=1000) {
			cast=cast/1000;
			timeunite="秒";
		}
		if(cast>=60) {
			cast=cast/60;
			timeunite="分钟";
		}
		System.out.println("\t\t【测试结束】】，本次消耗时间："+cast+timeunite);
		System.out.println("\n本次流程审核的每个节点均由该次任务的执行者提供，测试脚本并未对提供的流程节点进行校核，"
				+ "\n故不能作为流程节点正确的依据，若您确保流程节点正确并且在本次任务执行完成后检查了该流程状态已变为“结束”，"
				+ "\n则可以认为该次流程节点正确（您需要确保），并且流程可以正常结束（本次任务已说明）");
		System.out.println("\n>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>\n\n\n\n");
		}else {
			System.out.println("流程格式无效,请重试!!");
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
