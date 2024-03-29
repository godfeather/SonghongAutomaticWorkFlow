package automatic;

import java.util.List;
import java.util.Scanner;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
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
		System.out.println("\n流程创建时间："+ Audit.dateStamp);
		System.out.println("\n发起者："+fromer);
		System.out.println("\n流程名称："+flowName);
		System.out.println("\n>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>\n\n\n\n");
	}

	/**
	 *
	 * @param flowTitle 流程标题，审核时会在页面上查找该名称
	 * @param injectInterval 驳回间隔 0 表示无间隔，依次类推；-1表示不驳回
	 * @param workFlow
	 */
	public static  void plantFlowCore(String flowTitle,int injectInterval,FinalProcess<String,String> workFlow){
		System.out.println("驳回步长：" + injectInterval);
		int injectInterval2 = (injectInterval + 1);
		int rejectPointCount = injectInterval2; // 拒绝点计数，每次拒绝后加1个拒绝间隔
		System.out.println("流程标识：" + flowTitle + "-" + Audit.dateStamp);
		try {
			Audit.songhong.close();
		}catch(Exception e){
		}
		String driverType = Properties.getValue("driver");
		resetWebDriver(driverType);
		FinalProcess<String, String>sh = workFlow;
		if(sh==null) {
			System.out.println("错误：输入流程为空！");
			return;
		}
		String initator = Routine.getAccountByName(sh.getValue(0)); // 发起者名称
		if(initator == null) {
			System.out.println("警告：未找到该发起人：【" + sh.getValue(0) + "】");
			return;
		}
		for (int i = 1;i < sh.size();i ++) {
			boolean arrave = false;
			String auditer = Routine.getAccountByName(sh.getValue(i));		//获取审核者的账号
			if(sh.getValue(i).equals("申请人")) {
				auditer = initator;
			}
			System.out.println(sh.getKey(i)+"----------------------------------"+sh.getValue(i)+"开始审核");
			boolean login_suc = Audit.login(auditer,Properties.getParameter("globalPwd") );
			arrave = login_suc;
			// 流程是否找到
			int foundFlow = -1;
			if(login_suc) {
				foundFlow = Audit.clickFlowCe(flowTitle);
			}else{
				System.out.println("登录失败，请登录账号“"+auditer+"”并审核流程创建时间为[" + Audit.dateStamp + "],标题为【" +flowTitle+ "】的流程," +
						"\n本次需要审核的结果为：“" + ((rejectPointCount == i) ? "不同意" : "同意") + "”请手动完成后输入“done”继续；");
				boolean continu=manual();
				if(!continu){
					System.out.println("流程已取消审核");
					return;
				}
				resetWebDriver(driverType);
				continue;
			}
			if(foundFlow == 0) {
				if(rejectPointCount == i) {
					rejectPointCount += injectInterval2;
					Audit.accept(false,flowTitle);
					i = 0;
					boolean initLoginSuc = Audit.login(initator,Properties.getParameter("globalPwd"));
					if (initLoginSuc) {
						int initFoundFlow = Audit.clickFlowCe(flowTitle);
						if (initFoundFlow == 0) {
						} else  {
							System.out.println("流程发起者未找到创建时间为[" + Audit.dateStamp + "],流程标题为【" + flowTitle + "】的流程，" +
									"\n本次需要审核的结果为“不同意”,请手动完成后输入“done”继续");
							boolean continu=manual();
							if(!continu) {
								System.out.println("流程已取消审核");
								return;
							}
							resetWebDriver(driverType);
							continue;
						}
					} else {
						System.out.println("登录失败，请登录账号“"+auditer+"”并审核流程创建时间为[" + Audit.dateStamp + "],标题为【" +flowTitle+ "】的流程," +
								"\n本次需要审核的结果为“不同意”请手动完成后输入“done”继续；");
						boolean continu=manual();
						if(!continu){
							System.out.println("流程已中断！");
							return;
						}
						resetWebDriver(driverType);
						continue;
					}
					boolean b = Audit.resubmit(flowTitle);
					if(!b) {
						System.out.println("试图重新提交流程【" + flowTitle + "】失败，请手动完成后输入\"done\"继续。");
						boolean continu=manual();
						if(!continu) {
							System.out.println("流程已中断！");
							return;
						}
						resetWebDriver(driverType);
						continue;
					}
				}else {
					boolean b=Audit.accept(true,flowTitle);
					if(!b) {
						System.out.println("流程【" + flowTitle + "】未审核完成,本次需要审核的结果为“同意”，请手动完成后输入“done”继续");
						boolean continu = manual();
						if(! continu) {
							System.out.println("流程已取消审核");
							return;
						}
						resetWebDriver(driverType);
						continue;
					} else {
					    String current = workFlow.getValue(i);
					    String next = i == workFlow.size() - 1 ? "no need auditor" : workFlow.getValue(i + 1);
						boolean suc = true;
						if (Audit.extraCheck) {
							suc = Audit.checkFlowShouldInUndealtListAfterAccept(flowTitle,current,next);
							if(suc) {
								System.out.println("额外检查：√");
							}
						}
						if (suc) {
						} else {
							System.out.println("当前审核者为[" + current + "]，下一个审核者为[" + next + "],待办列表中可能与预期不符;\n流程“" + flowTitle + "”不应该出现在待办列表或必须出现在待办列表中; 请确认问题; \t--输入“done”继续");
							boolean continu = manual();
							if(! continu) {
								System.out.println("流程已取消审核");
								return;
							}
							resetWebDriver(driverType);
							continue;
						}
					}
				}
			}else{
				System.out.println("流程【" + flowTitle + "】审核失败，本次需要审核的结果为“" + (rejectPointCount == i ? "不同意" : "同意") + "“，请手动完成后输入“done”继续");
				boolean continu=manual();
				if(!continu) {
					System.out.println("流程已中断！");
					return;
				}
				resetWebDriver(driverType);
				continue;
			}
		}
	}
	public static void manualFlow(String name,String flowString,int rejectStep) {
	    if (flowString.trim().equals("")) {
			System.out.println("流程名不能为空！");
	        return;
		}
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
			System.out.println("\t\t【测试结束】，本次消耗时间："+cast+timeunite);
			System.out.println("\n本次流程审核的每个节点均由该次任务的执行者提供");
			System.out.println("\n>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>\n\n\n\n");
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
	private static void resetWebDriver (String driverType) {
		try {
			Audit.songhong.close();
		}catch(Exception e) {}
		if(driverType == null) {
		}else if(driverType.equals("chrome")){
			Audit.songhong=new ChromeDriver();
		}else {
			Audit.songhong=new FirefoxDriver();
		}
		Audit.songhong.manage().window().setPosition(new Point(0,0));
		Audit.songhong.manage().window().setSize(new Dimension(600,500));
		Audit.waiter = new WebDriverWait(Audit.songhong,5);
	}
}
