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
        Routine.connect();//链接数据库
        if(Routine.form==null) {
            return;
        }
        Scanner sc=new Scanner(System.in);
        while(true) {
            System.out.println("输入编号使用功能：");
            System.out.println("[1]---流程计算");
            System.out.println("[2]---流程自动");
            System.out.println("[3]---手动流程");
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
        System.out.println("按顺序输入手动计算出的流程节点中审核者的中文名称使用逗号\",\"隔开；\n通过流程标题确定需要在页面中查找审核的流程\n\n");
        System.out.println("请输入流程标题用于确定页面上流程名称：");
        Scanner sc=new Scanner(System.in);
        String workflowTitle=read(sc);
        if(workflowTitle==null) {
            System.out.println("已取消");
            return;
        }
        System.out.println("请输入流程序列使用逗号隔开：");
        String workflow=read(sc);
        if(workflow==null) {
            System.out.println("已取消");
            return;
        }
        AutoFlow.manualFlow(workflowTitle,workflow,-1);
    }
    public static void automatic() {
        int abortFrom = -1;
        System.out.println("发起者：");
        Scanner sc=new Scanner(System.in);
        String fromer=read(sc);
        if(fromer==null) {
            System.out.println("已取消");
            return;
        }
        System.out.println("流程名称：");
        String flowName=read(sc);
        if(flowName==null) {
            System.out.println("已取消");
            return;
        }
        System.out.println("流程标题：");
        String CustomflowName=read(sc);
        if(CustomflowName==null) {
            System.out.println("已取消");
            return;
        }
        System.out.println("是否扩展功能?    y--是");
        String advance=read(sc);
        boolean testAbort=false;
        if(advance==null) {
            System.out.println("已取消");
            return;
        }else if(advance.equalsIgnoreCase("y")){
            testAbort=true;
            System.out.println("驳回间隔，默认0表示无间隔，-1表示驳回,回车使用默认值");
            while(true) {
                String s=sc.nextLine().trim();
                if(s.equals("")) {
                    break;
                } if(s.equals("quit")){
                    return;
                }else {
                    try {
                        int j = Integer.parseInt(s);
                        abortFrom = j;
                        break;
                    }catch(Exception e) {
                        System.out.println("必须为整数！！");
                    }
                }

            }
        }

        AutoFlow.autoFlow(flowName, fromer,CustomflowName,(testAbort?abortFrom:-1));
    }
    public static void startup() {
        Scanner sc=new Scanner(System.in);
        while(true) {
            System.out.println("流程名称：");
            String flowName=sc.nextLine().trim();
            if(flowName.equals("")) {
                continue;
            }else if(flowName.equalsIgnoreCase("quit")) {
                return;
            }else {

            }
            String fromer=null;
            while(true){
                System.out.println("发起者：");
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
            s.add("序号");
            s.add("流程名称(Process)");
            s.add("审核者(Auditer)");
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
        FinalProcess<String, String>sh=Routine.finalFlow(Routine.calcFlow(Routine.getProcess("业务接待"), "周明"),"周明");
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
