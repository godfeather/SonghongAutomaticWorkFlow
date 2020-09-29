package RoutineCalculate;
import java.util.ArrayList;
import java.util.Scanner;
import org.openqa.selenium.firefox.FirefoxDriver;
import Collection.FinalProcess;
import Collection.Properties;
import Collection.TableFormat;
import automatic.AutoFlow;
public class Main {
	public static void main(String[] args) {
		System.setProperty(FirefoxDriver.SystemProperty.BROWSER_LOGFILE, "selenium_firefox.log");
		Properties.reload();
		Properties.loadProperties();
		Routine.connect();//�������ݿ�
		if(Routine.form==null) {
			return;
		}
		Scanner sc=new Scanner(System.in);
		while(true) {
			System.out.println("������ʹ�ù��ܣ�");
			System.out.println("[1]---���̼���");
			System.out.println("[2]---�����Զ�");
			System.out.println("[3]---�ֶ�����");
			String b=read(sc);
			if(b==null) {
				break;
			}else if(b.equals("1")) {
				startup();
			}else if(b.equals("2")) {
				automatic();
			}else if(b.equals("3")) {
				manual();
				
			}
		}
		
	}
	public static void manual() {
		System.out.println("��˳�������ֶ�����������̽ڵ�������ߵ���������ʹ�ö���\",\"������\nͨ�����̱���ȷ����Ҫ��ҳ���в�����˵�����\n\n");
		System.out.println("���������̱�������ȷ��ҳ�����������ƣ�");
		Scanner sc=new Scanner(System.in);
		String workflowTitle=read(sc);
		if(workflowTitle==null) {
			System.out.println("��ȡ��");
			return;
		}
		System.out.println("��������������ʹ�ö��Ÿ�����");
		String workflow=read(sc);
		if(workflow==null) {
			System.out.println("��ȡ��");
			return;
		}
		AutoFlow.manualFlow(workflowTitle,workflow,-1);
	}
	public static void automatic() {
		int abortFrom=10000;
		System.out.println("�����ߣ�");
		Scanner sc=new Scanner(System.in);
		String fromer=read(sc);
		if(fromer==null) {
			System.out.println("��ȡ��");
			return;
		}
		System.out.println("�������ƣ�");
		String flowName=read(sc);
		if(flowName==null) {
			System.out.println("��ȡ��");
			return;
		}
		System.out.println("���̱��⣺");
		String CustomflowName=read(sc);
		if(CustomflowName==null) {
			System.out.println("��ȡ��");
			return;
		}
		System.out.println("�Ƿ���չ����?    y--��");
		String advance=read(sc);
		boolean testAbort=false;
		if(advance==null) {
			System.out.println("��ȡ��");
			return;
		}else if(advance.equalsIgnoreCase("y")){
			
			System.out.println("���ĸ��ڵ㿪ʼ���أ�Ĭ�ϴ�0��ʼ��-1��ʾ�������һ���ڵ㣬ֵ>=���̼��ϳ��ȱ�ʾ������,�س�������");
			while(true) {
				String s=sc.nextLine().trim();
				if(s.equals("")) {
					break;
				} if(s.equals("quit")){
					return;
				}else {
					try {
						int j=Integer.parseInt(s);
						
						abortFrom=j;
						break;
					}catch(Exception e) {
						System.out.println("����Ϊ��������");
					}
				}
				
			}
		}
		
		AutoFlow.autoFlow(flowName, fromer,CustomflowName,abortFrom);
	}
	public static void startup() {
		Scanner sc=new Scanner(System.in);
		while(true) {
			System.out.println("�������ƣ�");
			String flowName=sc.nextLine().trim();
			if(flowName.equals("")) {
				continue;
			}else if(flowName.equalsIgnoreCase("quit")) {
				return;
			}else {
				
			}
			String fromer=null;
			while(true){
				System.out.println("�����ߣ�");
				fromer=sc.nextLine().trim();
				if(fromer.equals("")) {
					
				}else if(fromer.equalsIgnoreCase("quit")) {
					return;
				}else {
					break;
				}
			}
			FinalProcess<String, String>sh=Routine.finalFlow(Routine.calcFlow(Routine.getProcess(flowName), fromer),fromer);
			if(sh==null) {
				continue;
			}
			TableFormat t=new TableFormat();
			ArrayList<ArrayList<String>>process=new ArrayList<>();
			ArrayList<String>s=new ArrayList<>();
			s.add("���");
			s.add("��������(Process)");
			s.add("�����(Auditer)");
			process.add(s);
			for(int i=0;i<sh.size();i++) {
				ArrayList<String>p=new ArrayList<>();
				p.add((i+1)+"");
				p.add(sh.getKey(i));
				p.add(sh.getValue(i));
				process.add(p);
			}
			t.getTableFormat(process);
			t.showTable();
		}
	}
	public static void test() {
		FinalProcess<String, String>sh=Routine.finalFlow(Routine.calcFlow(Routine.getProcess("ҵ��Ӵ�"), "����"),"����");
		for(int i=0;i<sh.size();i++) {
			System.out.println(sh.getKey(i)+"-----------------"+sh.getValue(i));
		}
	}
	public static String read(Scanner in) {
		while(true) {
			String msg=in.nextLine().trim();
			if(msg.equals("")) {
				
			}else if(msg.equals("quit")){
				break;
			}else {
				return msg;
			}
		}
		return null;
	}
}
