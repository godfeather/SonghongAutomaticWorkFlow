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
				String pro="\r\n# 				�������������Զ�����-�����ļ�����"
						+ "\r\n# Author : Mrright"
						+ "\r\n# LastUpdateTime: 2020/06/29"
						+ "\r\n#"
						+ "\r\n#"
						+ "\r\n# ��ʹ��ϰ����ע�⡿��"
						+ "\r\n# 	1. �����ļ��Զ����ɣ�����Ҫ�ֶ�����"
						+ "\r\n# 	2. �ļ��𻵺�ֱ��ɾ��ԭ�����ļ����ɣ�����һ������ʱ�������µ������ļ�"
						+ "\r\n#	3. �����ļ��й������̽���ʱ����������������̿�ʼ֮ǰ����(���̿�ʼʱ���ض�����)������Ҫ��������"
						+ "\r\n#	4. �޸�����ֵʱ��Ҫ�Ķ�����=�����),�Է�������ȡΪ�յ����"
						+ "\r\n#	5. ÿ��ֻ��һ�������ÿ���������в��ܰ����κοհ׷�"
						+ "\r\n#	6. \"#\"��ʾע�ͷ�����ȡ����ʱ��ϵͳ�������Щ��ע�͵��У�ע��ʱֻ��ע��һ���У�����ע�ͷ�Ҫ�����׵�һ��λ��"
						+ "\r\n#	7. �����ļ��ϸ����ִ�Сд������ѭС�շ��־Լ���׳�"
						+ "\r\r#	8. ����������ɼ�ֵ�����"
						+ "\r\r#"
						+ "\r\ndriver=firefox"
						+ "\r\n# �������������������Ҫ�ض�������֧�֣�����ʹ�û�����������(firefox)��Ӧ��������gecko,��ѡ��chrome���������"
						+ "\r\nwait=1000"
						+ "\r\n# ÿ���ڵ�ĵȴ�ʱ�䣬��λΪ���룻������״������ʱ���Գ������ӵȴ�ʱ��"
						+ "\r\ntryingCount=60"
						+ "\r\n# ϵͳ�ж������Ƿ񱻳ɹ���ˣ����ʧ�ܻ�һֱ���Դ�Լ{tryingCount}�룬��������ڼ�ϵͳ�ж������ɣ����������̣�����������Դ�����ϵͳֱ�ӵȴ��û�ȷ��"
						+ "\r\nloginHome=http://t1.itsmore.com:62046/Login/Index"
						+ "\r\n# web��¼ҳ��ǿ�����ǣ������ǵ�¼ҳ��URL��������ҳ"
						+ "\r\nrejectStep=1"
						+ "\r\n# ���ز�������������£������߹�ʱ���е���˾����衰ͬ�⡱��������Ҫ���в��ز���ʱ���ͻ����ʵ���ʱ�����ڵ㡰��ͬ�⡱�����������ʹ���̻ص������ˣ�"
						+ "\r\n# Ĭ������£����ز���ÿһ���ڵ㶼�����һ�β��أ�ȡ����rejectStepֵΪ1��,��Ứ�ѷǳ�����ʱ������ˣ��������Ҫ���в��ز��ԣ������г�죬"
						+ "\r\n# ��������ò��ز�������ʾ�����rejectStep-1������һ��"
						+ "\r\ndatabaseServer=localhost:1433"
						+ "\r\n# ���ݿ�IP:Port"
						+ "\r\ncredential=sa/123"
						+ "\r\n# ���ݿ��¼ƾ�ݣ��û�����ǰ�������ں�ʹ�á�/������"
						+ "\r\ndatabaseBrand=sqlserver"
						+ "\r\n# ���ݿ�Ʒ�ƣ���ѡ��Ϊmysql��sqlserver��Ĭ��Ϊsqlserver,mysql��֧��8.0���ϰ汾"
						+ "\r\nloginTryingCount=5"
						+ "\r\n# ��¼ȷ�ϴ���������¼�ƺ�δ�ɹ�ʱ��ϵͳ����ʼ����ȷ���Ƿ��¼�ɹ����Ƿ��ѽ�����ҳ���������趨������1��ȷ��һ�Σ�����ֹͣϵͳ�ȴ��û�ȷ�ϣ�"
						+ "\r\n# ��������ҳ��������������״�����ö����³���ȷ�ϴ����������ӳ��Դ���"
						+ "\r\ndebug=false"
						+ "\r\n# debugģʽ�����Ϊtrue�����������Ϣ(����һ�����������ϵĴ����෴���еĴ�����Ϣ���ض������������Ҫ�����ģ��Դ�������������߼�����)��"
						+ "\r\n# ʹ��ϵͳʱ�����鿪��"
						+ "\r\nglobalPwd=0000"
						+ "\r\n# �����û��ĵ�¼���룬����Ҫ���κθ���";
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
