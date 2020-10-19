package Collection;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

import RoutineCalculate.Routine;
import automatic.AutoFlow;
import automatic.Audit;

public class Properties {
	private static HashMap <String,String> properties=new HashMap<>();
	public static String getValue(String key) {
		return properties.get(key);
	}
	static{
		File proper=new File("properties/proerties.txt");
		if(!proper.exists()) {
			try {
				new File("properties").mkdir();
				proper.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				FileWriter fw=new FileWriter(proper);
				String pro="\r\n# 				【【淞泓流程自动测试-配置文件】】"
						+ "\r\n# Author : Mrright"
						+ "\r\n# LastUpdateTime: 2020/06/29"
						+ "\r\n#"
						+ "\r\n#"
						+ "\r\n# 【使用习惯与注意】："
						+ "\r\n# 	1. 配置文件自动生成，不需要手动创建"
						+ "\r\n# 	2. 文件损坏后直接删掉原来的文件即可，在下一次运行时会生成新的配置文件"
						+ "\r\n#	3. 配置文件中关于流程进行时的配置项可以在流程开始之前配置(流程开始时会重读配置)，不需要重启程序"
						+ "\r\n#	4. 修改配置值时不要改动键（=号左边),以防发生读取为空的情况"
						+ "\r\n#	5. 每行只有一个配置项，每行配置项中不能包含任何空白符"
						+ "\r\n#	6. \"#\"表示注释符，读取配置时，系统会忽略这些被注释的行，注释时只能注释一整行，并且注释符要在行首第一个位置"
						+ "\r\n#	7. 配置文件严格区分大小写，键遵循小驼峰标志约定俗称"
						+ "\r\r#	8. 配置项必须由键值对组成"
						+ "\r\r#"
						+ "\r\ndriver=firefox"
						+ "\r\n# 测试启动的浏览器，需要特定的驱动支持，建议使用火狐浏览器驱动(firefox)对应驱动名称gecko,可选择chrome浏览器驱动"
						+ "\r\nwait=1000"
						+ "\r\n# 每个节点的等待时间，单位为毫秒；当网络状况不好时可以尝试增加等待时长"
						+ "\r\ntryingCount=60"
						+ "\r\n# 系统判定流程是否被成功审核，如果失败会一直尝试大约{tryingCount}秒，如果在这期间系统判定审核完成，则会继续流程；如果超过尝试次数，系统直接等待用户确认"
						+ "\r\nloginHome=http://t1.itsmore.com:62046/Login/Index"
						+ "\r\n# web登录页，强调的是，必须是登录页的URL不能是首页"
						+ "\r\nrejectStep=1"
						+ "\r\n# 驳回步长：正常情况下，流程走过时所有的审核均给予“同意”，而当需要进行驳回测试时，就会在适当的时候给予节点“不同意”的审批结果，使流程回到发起人；"
						+ "\r\n# 默认情况下，驳回测试每一个节点都会进行一次驳回（取决于rejectStep值为1）,这会花费非常长的时间来审核，而如果需要进行驳回测试，仅进行抽检，"
						+ "\r\n# 则可以设置驳回步长，表示间隔（rejectStep-1）驳回一次"
						+ "\r\ndatabaseServer=localhost:1433"
						+ "\r\n# 数据库IP:Port"
						+ "\r\ncredential=sa/123"
						+ "\r\n# 数据库登录凭据，用户名在前，密码在后，使用“/”隔开"
						+ "\r\ndatabaseBrand=sqlserver"
						+ "\r\n# 数据库品牌，可选项为mysql和sqlserver，默认为sqlserver,mysql仅支持8.0以上版本"
						+ "\r\nloginTryingCount=5"
						+ "\r\n# 登录确认次数，当登录似乎未成功时，系统将开始尝试确认是否登录成功（是否已进入主页）；尝试设定次数后（1秒确认一次），会停止系统等待用户确认；"
						+ "\r\n# 若登入首页过程中由于网络状况不好而导致超出确认次数，可增加尝试次数"
						+ "\r\ndebug=false"
						+ "\r\n# debug模式，如果为true会输出报错信息(并不一定是真意义上的错误，相反，有的错误信息在特定的情况下是需要产生的，以此来驱动另外的逻辑条件)，"
						+ "\r\n# 使用系统时不建议开启"
						+ "\r\nglobalPwd=0000"
						+ "\r\n# 所有用户的登录密码，不需要作任何更改";
				fw.write(pro);
				fw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	public static void reload() {
		FileReader fr=null;
		try {
			fr = new FileReader("properties/proerties.txt");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		BufferedReader bu=new BufferedReader(fr);
		String str=null;
		try {
			str = bu.readLine().trim();
		} catch (IOException e) {
			e.printStackTrace();
		}
		while(str!=null) {
			if(!str.startsWith("#")&&(!str.equals(""))) {
				int devidingLine=str.indexOf("=");
				properties.put(str.substring(0,devidingLine), str.substring(devidingLine+1,str.length()));
			}
			try {
				str=bu.readLine();
				if(str!=null) {
					str=str.trim();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			bu.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static String getParameter(String key) {
		return properties.get(key);
	}
	public static void loadProperties() {
		Audit.loginHome=properties.get("loginHome")==null?"http://t1.itsmore.com:62046/Login/Index":properties.get("loginHome");
		Audit.tryingCount=Integer.parseInt(properties.get("tryingCount")==null?"15":properties.get("tryingCount"));
		Audit.wait=Integer.parseInt(properties.get("wait")==null?"1000":properties.get("wait"));
		AutoFlow.rejectStep=Integer.parseInt(properties.get("rejectStep")==null?"1":properties.get("rejectStep"));
		Routine.defaultHost=properties.get("databaseServer");
		Audit.loginTryingCount=Integer.parseInt(properties.get("loginTryingCount")==null?"5":properties.get("loginTryingCount"));
		String credential=properties.get("credential");
		Routine.databaseBrand=properties.get("databaseBrand");
		if(credential==null) {
			return;
		}
		Routine.user=credential.substring(0,credential.indexOf("/"));
		Routine.pwd=credential.substring(credential.indexOf("/")+1,credential.length());
	}
}
