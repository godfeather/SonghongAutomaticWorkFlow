package RoutineCalculate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;
import Collection.FinalProcess;
public class Routine {
	/**
	 * ���ݿ����ƣ���mysql��sqlserver
	 */
	public static String databaseBrand;
	static Statement form;
	public static String user="sa";
	public static String pwd="123";
	public static String defaultHost="192.168.99.102:1433";
	public static FinalProcess<String, String> finalFlow(String[] cf,String fromer) {
		if(cf==null||fromer==null) {
			return null;
		}
		FinalProcess<String, String> finalFlow=new FinalProcess<>();			//cf��ʾcalcFlow
		HashMap<String,Integer> al=new HashMap<String,Integer>();				//��ʾ�������������˴�����ӳ��						
		for(int i=0;i<cf.length;i++) {
			if(cf[i].endsWith("�����ܼ�")) {			//���������
				calculate(al, cf, i, fromer, finalFlow, "�����ܼ�");
			}else if(cf[i].endsWith("ִ���ܼ�")) {
				calculate(al, cf, i, fromer, finalFlow, "ִ���ܼ�");
			}else if(cf[i].endsWith("�ֹܸ���")) {
				calculate(al, cf, i, fromer, finalFlow, "�ֹܸ���");
			}else if(cf[i].endsWith("�ܾ���")) {
				String []auditer=getName("�ܾ���");
				for(String au:auditer) {					//�������
					if(al.get(au)==null) {
						al.put(au,1);
					}else {
						al.put(au, al.get(au)+1);
					}
				}
				Iterator<String>s=al.keySet().iterator();
				String last=null;
				while(s.hasNext()) {			//��Ѱ�Ƿ������������
					String name=s.next();
					boolean have=false;				//��˵���Ѿ����£�û�и�����Ϊ������
					for(int j=0;j<auditer.length;j++) {
						if(name.equals(auditer[j])) {
							have=true;
							
						}
					}
					if(have) {
						last=name;
					}else {
						finalFlow.put(cf[i-1],name);
						s.remove();
					}
				}
				if(i==cf.length-1) {
					for(String key:al.keySet()){
						finalFlow.put(cf[i],key);
					}
				}
			}else {
				String[] auditer=getName(cf[i]);
				for(String au:auditer) {					//�������
					if(al.get(au)==null) {
						al.put(au,1);
					}else {
						al.put(au, al.get(au)+1);
					}
				}
				
				Iterator<String>s=al.keySet().iterator();
				String last=null;
				while(s.hasNext()) {			//��Ѱ�Ƿ������������
					String name=s.next();
					boolean have=false;				//��˵���Ѿ����£�û�и�����Ϊ������
					for(int j=0;j<auditer.length;j++) {
						if(name.equals(auditer[j])) {
							have=true;
							
						}
					}
					
					if(have) {
						last=name;
					}else {
						finalFlow.put(cf[i-1],name);
						s.remove();
					}
				}
				if(i==cf.length-1) {
					for(String key:al.keySet()){
						finalFlow.put(cf[i],key);
					}
				}
			}
		}
		return finalFlow;
	}
	public static String[] calcFlow(String[] rl,String fromer) {
		if(rl==null||fromer==null) {
			return null;
		}
		ArrayList<String>flow=new ArrayList<>();
		for(int i=0;i<rl.length;i++) {
			String node=rl[i];
			int nodelevel=0;
			if(node.endsWith("�����ܼ�")) {			//���̽ڵ�Ϊ�����ܼ����
				nodelevel=2;
				if(node.length()==4) {				//���Ϊ�����˲������
					int level=getLevel(fromer);
					if(level>nodelevel) {
					}else if(level==nodelevel) {
						String[]auditer=getName("�����ܼ�", getDepart(fromer));
						if(auditer.length!=1) {
							flow.add(rl[i]);
						}else if(auditer[0].equals(fromer)) {			//�����Լ�ʱ����Ҫ���
						}else {
							flow.add(rl[i]);
						}
					}else {
						flow.add(rl[i]);
					}
				}else {								//�����������
					String nodeDepart=node.substring(0,node.length()-4);
					String fromerDepart=getDepart(fromer);
					if(nodeDepart.equals(fromerDepart)) {
						nodelevel=2;
						int level=getLevel(fromer);
						if(level>nodelevel) {
						}else if(level==nodelevel) {
							String[]auditer=getName("�����ܼ�", getDepart(fromer));
							if(auditer.length!=1) {
								flow.add(rl[i]);
							}else if(auditer[0].equals(fromer)) {			//�����Լ�ʱ����Ҫ���
							}else {
								flow.add(rl[i]);
							}
						}else {
							flow.add(rl[i]);
						}
					}else if(fromerDepart.equals("no")){			//����Ϊno��Ϊ�ܾ���
						if(rl[i].endsWith("�����ܼ�")) {
						}else if(rl[i].endsWith("ִ���ܼ�")) {
						}else if(rl[i].endsWith("�ֹܸ���")) {
						}else  {
							flow.add(rl[i]);
						}
					}else {
						flow.add(rl[i]);
					}
				}
			}else if(node.endsWith("ִ���ܼ�")) {
				nodelevel=3;
				if(node.length()==4) {				//���Ϊ�����˲������
					int level=getLevel(fromer);
					if(level>nodelevel) {
					}else if(level==nodelevel) {
						String[]auditer=getName("ִ���ܼ�", getDepart(fromer));
						if(auditer.length!=1) {
							flow.add(rl[i]);
						}else if(auditer[0].equals(fromer)) {			//�����Լ�ʱ����Ҫ���
						}else {
							flow.add(rl[i]);
						}
					}else {
						flow.add(rl[i]);
					}
				}else {								//�����������
					String nodeDepart=node.substring(0,node.length()-4);
					String fromerDepart=getDepart(fromer);
					if(nodeDepart.equals(fromerDepart)) {
						nodelevel=3;
						int level=getLevel(fromer);
						if(level>nodelevel) {
						}else if(level==nodelevel) {
							String[]auditer=getName("ִ���ܼ�", getDepart(fromer));
							if(auditer.length!=1) {
								flow.add(rl[i]);
							}else if(auditer[0].equals(fromer)) {			//�����Լ�ʱ����Ҫ���
							}else {
								flow.add(rl[i]);
							}
						}else {
							flow.add(rl[i]);
						}
					}else if(fromerDepart.equals("no")){			//����Ϊno��Ϊ�ܾ���
						if(rl[i].endsWith("�����ܼ�")) {
						}else if(rl[i].endsWith("ִ���ܼ�")) {
						}else if(rl[i].endsWith("�ֹܸ���")) {
						}else  {
							flow.add(rl[i]);
						}
					}else {
						flow.add(rl[i]);
					}
				}
			}else if(node.endsWith("�ֹܸ���")) {
				nodelevel=4;
				if(node.length()==4) {				//���Ϊ�����˲������
					int level=getLevel(fromer);
					if(level>nodelevel) {
					}else if(level==nodelevel) {
						String[]auditer=getName("�ֹܸ���", getDepart(fromer));
						if(auditer.length!=1) {
							flow.add(rl[i]);
						}else if(auditer[0].equals(fromer)) {			//�����Լ�ʱ����Ҫ���
						}else {
							flow.add(rl[i]);
						}
					}else {
						flow.add(rl[i]);
					}
				}else {								//�����������
					String nodeDepart=node.substring(0,node.length()-4);
					String fromerDepart=getDepart(fromer);
					if(nodeDepart.equals(fromerDepart)) {
						nodelevel=4;
						int level=getLevel(fromer);
						if(level>nodelevel) {
						}else if(level==nodelevel) {
							String[]auditer=getName("�ֹܸ���", getDepart(fromer));
							if(auditer.length!=1) {
								flow.add(rl[i]);
							}else if(auditer[0].equals(fromer)) {			//�����Լ�ʱ����Ҫ���
							}else {
								flow.add(rl[i]);
							}
						}else {
							flow.add(rl[i]);
						}
					}else if(fromerDepart.equals("no")){			//����Ϊno��Ϊ�ܾ���
						if(rl[i].endsWith("�����ܼ�")) {
						}else if(rl[i].endsWith("ִ���ܼ�")) {
						}else if(rl[i].endsWith("�ֹܸ���")) {
						}else  {
							flow.add(rl[i]);
						}
					}else {
						flow.add(rl[i]);
					}
				}
			}else if(node.equals("�ܾ���")) {
				nodelevel=5;
				int level=getLevel(fromer);
				if(level>nodelevel) {
				}else if(level==nodelevel) {
					String[]auditer=getName("�ܾ���", getDepart(fromer));
					if(auditer.length!=1) {
						flow.add(rl[i]);
					}else if(auditer[0].equals(fromer)) {			//�����Լ�ʱ����Ҫ���
					}else {
						flow.add(rl[i]);
					}
				}else {
					flow.add(rl[i]);
				}
			}else {
				nodelevel=1;
				flow.add(rl[i]);
			}
		}
		String []str=new String[flow.size()];
		for(int i=0;i<flow.size();i++) {
			str[i]=flow.get(i);
		}
		return str;
	}
	public static Statement connect() {
		Scanner in =new Scanner(System.in);
		String ipAndPort=null;
		String userr=null;
		String pwdd=null;
			System.out.println("����IP:Port�������ݿ�");
			String msg=in.nextLine().trim();
				ipAndPort=msg.equals("")?defaultHost:msg;
			System.out.println("�û�����");
			String msg1=in.nextLine().trim();
				userr=msg1.equals("")?user:msg1;
			System.out.println("���룺");
				String msg2=in.nextLine().trim();
				pwdd=msg2.equals("")?pwd:msg2;
				if("mysql".equals(databaseBrand)) {
					form=Database.mysqlConnect(ipAndPort,userr, pwdd);
				}else{
			form=Database.connect(ipAndPort, userr, pwdd);
				}
			return form;
	}
	public static String[] getProcess(String name) {		//��ȡ����
		if(name==null) {
			System.out.println("error:workflow name must be not null!");
			return null;
		}
		ArrayList<String>ar=new ArrayList<>();//����
		if(form==null) {
			System.out.println("error:database has not been connect!");
			return null;
		}
		try {
			ResultSet result=form.executeQuery("select flow from flow where name like'"+name+"%';");
			result.next();
			String flowpro=null;
			try {
				flowpro=result.getString(1).trim();
			}catch(Exception e) {
				System.out.println("error:workflow not exist!");
				return null;
			}
			int start=0;
			int end=0;
			for(;;) {
				int index=flowpro.indexOf("��", start);
				if(index!=-1) {
					end=index;
					ar.add(flowpro.substring(start,end).trim());
					start=end+1;
				}else {
					ar.add(flowpro.substring(start,flowpro.length()));
					break;
				}
			}
			result.close();
			System.out.println(ar);
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		String[] s=new String[ar.size()];
		for(int i=0;i<s.length;i++) {
			s[i]=ar.get(i);
		}
		return s;
	}
	public static int getLevel(String name) {				//��ȡ��ְԱ����󼶱�
		try {
			ResultSet r=form.executeQuery("select level from staff where name='"+name+"'");
			boolean have=false;
			int i=0;
			while(r.next()) {
				int si=r.getInt(1);
				if(si>i) {
					i=si;
					have=true;
				}
			}
			if(!have) {
				System.err.println("error��[select level from staff where name='"+name+"']���δ�ҵ��κ��У�");

			}
			r.close();
			return i;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1;
	}
	public static String[] getName(String post,String depart) {					//ͨ����λ�Ͳ��Ż�ȡְԱ�б�
		try {
			ResultSet rs=form.executeQuery("select name from staff where post='"+post+"' and department='"+depart+"';");
			ArrayList<String>a=new ArrayList<>();
			boolean have=false;
			while(rs.next()) {
				a.add(rs.getString(1).trim());
				have=true;
			}
			if(!have) {
				System.err.println("error��[select name from staff where post='"+post+"' and department='"+depart+"']���δ�ҵ��κ��У�");
			}
			String str[]=new String[a.size()];
			for(int i=0;i<str.length;i++) {
				str[i]=a.get(i);
			}
			rs.close();
			return str;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public static String getDepart(String name) {								//ͨ��������ȡԱ���Ĳ���
		try {
			ResultSet rs=form.executeQuery("select department from staff where name='"+name+"'");
			rs.next();
			String s=null;
			try {
				s=rs.getString(1).trim();
			}catch(Exception e) {
				System.out.println("error:staff not exist!!");
				e.printStackTrace();
				return null;
			}
			rs.close();
			return s;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public static String[] getName(String post) {					//ͨ����λ��ѯְԱ
		try {
			ResultSet rs=form.executeQuery("select name from staff where post='"+post+"';");
			ArrayList<String>a=new ArrayList<>();
			boolean have=false;
			while(rs.next()) {
				a.add(rs.getString(1).trim());
				have=true;
			}
			if(!have) {
				System.out.println("���棺[select name from staff where post='"+post+"']���δ�ҵ��κ��У�");
			}
			String str[]=new String[a.size()];
			for(int i=0;i<str.length;i++) {
				str[i]=a.get(i);
			}
			rs.close();
			return str;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public static void calculate(HashMap<String, Integer> al,String[] cf,int i,String fromer,FinalProcess<String , String > finalFlow,String post) {
		if(cf[i].length()==4) {
			String []auditer=getName(post, getDepart(fromer));
			for(String au:auditer) {					//�������
				if(au.equals(fromer)) {
					
				}else {
					if(al.get(au)==null) {
						al.put(au,1);
					}else {
						al.put(au, al.get(au)+1);
					}
				}
			}
			Iterator<String> s=al.keySet().iterator();
			String last=null;
			while(s.hasNext()) {			//��Ѱ�Ƿ������������
				String name=s.next();
				boolean have=false;				//��˵���Ѿ����£�û�и�����Ϊ������
				for(int j=0;j<auditer.length;j++) {
					if(name.equals(auditer[j])) {
						have=true;
						
					}
				}
				if(have) {
					last=name;
				}else {
					finalFlow.put(cf[i-1],name);
					s.remove();
				}
			}
			if(i==cf.length-1) {
				for(String key:al.keySet()){
						finalFlow.put(cf[i],key);
				}
			}
		}else {				//�����������
			String []auditer=getName(post, cf[i].substring(0,cf[i].length()-4));
			for(String au:auditer) {					//�������
				if(al.get(au)==null) {
					al.put(au,1);
				}else {
					al.put(au, al.get(au)+1);
				}
			}
			Iterator<String>s=al.keySet().iterator();
			String last=null;
			while(s.hasNext()) {			//��Ѱ�Ƿ������������
				boolean have=false;				//��˵���Ѿ����£�û�и�����Ϊ������
				String name=s.next();
				for(int j=0;j<auditer.length;j++) {
					
					if(name.equals(auditer[j])) {
						have=true;
						
					}
				}
				if(have) {
					last=name;
				}else {
					finalFlow.put(cf[i-1],name);
					s.remove();
				}
			}
			if(i==cf.length-1) {
				for(String key:al.keySet()){
						finalFlow.put(cf[i],key);
				}
			}
		}
		
	}
	private  ArrayList<String[]> query(String sql) {			//δ��
		if(!sql.toLowerCase().startsWith("select")) {
			System.out.println("error:can't execute the statement!!");
			return null;
		}
		ResultSet result=null;
		try {
			result=form.executeQuery(sql);
			
		} catch (SQLException e) {
			System.out.println("���棺"+e.getMessage());
			return null;
		}
		return null;
	}
	public static String getAccountByName(String name) {
		ResultSet re=null;
		try {
			re=form.executeQuery("select account from staff where name='"+name+"';");
			String s=null;
			while(re.next()) {
				s=re.getString(1);
				re.close();
				return s;
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			return null;
		}
		return null;
	}
}
