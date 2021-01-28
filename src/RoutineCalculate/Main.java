package RoutineCalculate;
import java.io.*;
import java.util.ArrayList;
import arguments.Args;
import automatic.Audit;
import org.openqa.selenium.firefox.FirefoxDriver;
import Collection.FinalProcess;
import Collection.Properties;
import Collection.TableFormat;
import automatic.AutoFlow;
public class Main {
    public static void main(String[] args) {
        Args arg = new Args(args);
        System.setProperty(FirefoxDriver.SystemProperty.BROWSER_LOGFILE, "selenium_firefox.log");
        Properties.reload();
        Properties.loadProperties();
        String file = arg.getValue("parameter");
        Audit.dateStamp = arg.getValue("creationTime");
        String params = "";
        BufferedReader br = null;
        if (file != null) {
            try {
                br = new BufferedReader(new FileReader(new File(file)));
                String str = br.readLine();
                while (str != null) {
                    params += str +"\n";
                    str = br.readLine();
                }
                br.close();
            } catch (IOException e) {
                System.out.println("参数文件[" +file+ "]读取错误！");
                return;
            }finally {
                try {
                    br.close();
                } catch (Exception e) {
                }
            }
            String [] paramss = params.split("\n");
            String lineIndex = arg.getValue("lineIndex");
            int lineI = 0;
            if (lineIndex == null) {
                lineI = 0;
            } else {
                try {
                    lineI = Integer.parseInt(lineIndex);
                } catch (Exception e) {
                    System.out.println("参数文件的行指针只能为整数！");
                    return;
                }
            }
            if (!(lineI < paramss.length && lineI > -1)) { // 参数文件指针越界时
                System.out.println("所指示行无参数可用！");
                return;
            } else {
                arg = new Args(paramss[lineI].split(" "));
            }
        }

        String help = arg.getValue("help");
        if (help != null) {
            arg.printHelp();
            return;
        }
        String mode = arg.getValue("mode");
        if (mode == null) {
            System.out.println("需要-m选项指定模式!");
            return;
        }
        Routine.connect(arg);//链接数据库
        if(Routine.form==null) {
            System.out.println("数据库未连接！");
            return;
        }
        if (mode.equals("auto")) {
            automatic(arg);
        } else if (mode.equals("calc")) {
            startup(arg);
        } else if (mode.equals("manual")) {
            manual(arg);
        } else {
            System.out.println("未知的模式：" + mode);
            return;
        }
        try {
            Audit.songhong.close();
        }catch (Exception e) {
        }
    }
    public static void manual(Args arg) {
        String workflowTitle= arg.getValue("title");
        if(workflowTitle==null) {
            System.out.println("需要-t选项指定流程标题！");
            return;
        }
        String workflow= arg.getValue("sequence");
        if(workflow==null) {
            System.out.println("需要使用-s选项指定流程审核者队列！");
            return;
        }
        String rejectStep = arg.getValue("reject");
        int step = -1;
        if (rejectStep == null) {
            step = -1;
        } else {
            try {
                step = Integer.parseInt(rejectStep.trim());
            }catch (Exception e) {
                step = 0;
            }
        }
        AutoFlow.manualFlow(workflowTitle,workflow,step);
    }
    public static void automatic(Args args) {
        int abortFrom = -1;
        String fromer= args.getValue("reason");
        if(fromer==null) {
            System.out.println("需要使用-r选项指定发起者!");
            return;
        }
        String flowName=args.getValue("name");
        if(flowName==null) {
            System.out.println("需要使用-n选项指定流程名称！");
            return;
        }
        String CustomflowName=args.getValue("title");
        if(CustomflowName==null) {
            System.out.println("需要使用-t选项指定流程标题!");
            return;
        }
        String abort = args.getValue("reject");
        if (abort == null) {
        } else {
            try {
                abortFrom = Integer.parseInt(abort.trim());
            }catch (Exception e) {
                abortFrom = 0;
            }
        }
        AutoFlow.autoFlow(flowName, fromer,CustomflowName,abortFrom);
    }
    public static void startup(Args args) {
            String flowName=args.getValue("name");
            String fromer=args.getValue("reason");
            if (flowName == null) {
                System.out.println("需要使用-n选项指定流程名称！");
                return;
            } else {
                if (fromer == null) {
                    System.out.println("需要使用-r选项指定发起者！");
                    return;
                }
            }
            FinalProcess<String, String>sh=Routine.finalFlow(Routine.calcFlow(Routine.getProcess(flowName), fromer),fromer);
            TableFormat t=new TableFormat();
            ArrayList<ArrayList<String>>process=new ArrayList<>();
            ArrayList<String>s=new ArrayList<>();
            s.add("序号");
            s.add("流程名称(Process)");
            s.add("审核者(Auditor)");
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
    public static void test() {
        FinalProcess<String, String>sh=Routine.finalFlow(Routine.calcFlow(Routine.getProcess("业务接待"), "周明"),"周明");
        for(int i=0;i<sh.size();i++) {
            System.out.println(sh.getKey(i)+"-----------------"+sh.getValue(i));
        }
    }
}
