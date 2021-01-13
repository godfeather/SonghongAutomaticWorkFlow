package RoutineCalculate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;
import Collection.FinalProcess;
import arguments.Args;

public class Routine {
	/**
	 * 数据库名称，如mysql，sqlserver
	 */
	public static String databaseBrand;
	static Statement form;
	public static String user="";
	public static String pwd="";
	public static String defaultHost="";
	public static FinalProcess<String, String> finalFlow(String[] cf,String fromer) {
		if(cf==null||fromer==null) {
			return null;
		}
		FinalProcess<String, String> finalFlow=new FinalProcess<>();			//cf表示calcFlow
		HashMap<String,Integer> al=new HashMap<String,Integer>();				//表示审核者与连续审核次数的映射						
		for(int i=0;i<cf.length;i++) {
			if(cf[i].endsWith("部门总监")) {			//本部门审核
				calculate(al, cf, i, fromer, finalFlow, "部门总监");
			}else if(cf[i].endsWith("执行总监")) {
				calculate(al, cf, i, fromer, finalFlow, "执行总监");
			}else if(cf[i].endsWith("分管副总")) {
				calculate(al, cf, i, fromer, finalFlow, "分管副总");
			}else if(cf[i].endsWith("总经理")) {
				String []auditer=getName("总经理");
				for(String au:auditer) {					//添加数量
					if(al.get(au)==null) {
						al.put(au,1);
					}else {
						al.put(au, al.get(au)+1);
					}
				}
				Iterator<String>s=al.keySet().iterator();
				String last=null;
				while(s.hasNext()) {			//找寻是否有人连续审核
					String name=s.next();
					boolean have=false;				//有说明已经更新，没有更新则为不连续
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
				for(String au:auditer) {					//添加数量
					if(al.get(au)==null) {
						al.put(au,1);
					}else {
						al.put(au, al.get(au)+1);
					}
				}
				
				Iterator<String>s=al.keySet().iterator();
				String last=null;
				while(s.hasNext()) {			//找寻是否有人连续审核
					String name=s.next();
					boolean have=false;				//有说明已经更新，没有更新则为不连续
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
			if(node.endsWith("部门总监")) {			//流程节点为部门总监审核
				nodelevel=2;
				if(node.length()==4) {				//如果为发起人部门审核
					int level=getLevel(fromer);
					if(level>nodelevel) {
					}else if(level==nodelevel) {
						String[]auditer=getName("部门总监", getDepart(fromer));
						if(auditer.length!=1) {
							flow.add(rl[i]);
						}else if(auditer[0].equals(fromer)) {			//等于自己时不需要审核
						}else {
							flow.add(rl[i]);
						}
					}else {
						flow.add(rl[i]);
					}
				}else {								//其它部门审核
					String nodeDepart=node.substring(0,node.length()-4);
					String fromerDepart=getDepart(fromer);
					if(nodeDepart.equals(fromerDepart)) {
						nodelevel=2;
						int level=getLevel(fromer);
						if(level>nodelevel) {
						}else if(level==nodelevel) {
							String[]auditer=getName("部门总监", getDepart(fromer));
							if(auditer.length!=1) {
								flow.add(rl[i]);
							}else if(auditer[0].equals(fromer)) {			//等于自己时不需要审核
							}else {
								flow.add(rl[i]);
							}
						}else {
							flow.add(rl[i]);
						}
					}else if(fromerDepart.equals("no")){			//部门为no则为总经理
						if(rl[i].endsWith("部门总监")) {
						}else if(rl[i].endsWith("执行总监")) {
						}else if(rl[i].endsWith("分管副总")) {
						}else  {
							flow.add(rl[i]);
						}
					}else {
						flow.add(rl[i]);
					}
				}
			}else if(node.endsWith("执行总监")) {
				nodelevel=3;
				if(node.length()==4) {				//如果为发起人部门审核
					int level=getLevel(fromer);
					if(level>nodelevel) {
					}else if(level==nodelevel) {
						String[]auditer=getName("执行总监", getDepart(fromer));
						if(auditer.length!=1) {
							flow.add(rl[i]);
						}else if(auditer[0].equals(fromer)) {			//等于自己时不需要审核
						}else {
							flow.add(rl[i]);
						}
					}else {
						flow.add(rl[i]);
					}
				}else {								//其它部门审核
					String nodeDepart=node.substring(0,node.length()-4);
					String fromerDepart=getDepart(fromer);
					if(nodeDepart.equals(fromerDepart)) {
						nodelevel=3;
						int level=getLevel(fromer);
						if(level>nodelevel) {
						}else if(level==nodelevel) {
							String[]auditer=getName("执行总监", getDepart(fromer));
							if(auditer.length!=1) {
								flow.add(rl[i]);
							}else if(auditer[0].equals(fromer)) {			//等于自己时不需要审核
							}else {
								flow.add(rl[i]);
							}
						}else {
							flow.add(rl[i]);
						}
					}else if(fromerDepart.equals("no")){			//部门为no则为总经理
						if(rl[i].endsWith("部门总监")) {
						}else if(rl[i].endsWith("执行总监")) {
						}else if(rl[i].endsWith("分管副总")) {
						}else  {
							flow.add(rl[i]);
						}
					}else {
						flow.add(rl[i]);
					}
				}
			}else if(node.endsWith("分管副总")) {
				nodelevel=4;
				if(node.length()==4) {				//如果为发起人部门审核
					int level=getLevel(fromer);
					if(level>nodelevel) {
					}else if(level==nodelevel) {
						String[]auditer=getName("分管副总", getDepart(fromer));
						if(auditer.length!=1) {
							flow.add(rl[i]);
						}else if(auditer[0].equals(fromer)) {			//等于自己时不需要审核
						}else {
							flow.add(rl[i]);
						}
					}else {
						flow.add(rl[i]);
					}
				}else {								//其它部门审核
					String nodeDepart=node.substring(0,node.length()-4);
					String fromerDepart=getDepart(fromer);
					if(nodeDepart.equals(fromerDepart)) {
						nodelevel=4;
						int level=getLevel(fromer);
						if(level>nodelevel) {
						}else if(level==nodelevel) {
							String[]auditer=getName("分管副总", getDepart(fromer));
							if(auditer.length!=1) {
								flow.add(rl[i]);
							}else if(auditer[0].equals(fromer)) {			//等于自己时不需要审核
							}else {
								flow.add(rl[i]);
							}
						}else {
							flow.add(rl[i]);
						}
					}else if(fromerDepart.equals("no")){			//部门为no则为总经理
						if(rl[i].endsWith("部门总监")) {
						}else if(rl[i].endsWith("执行总监")) {
						}else if(rl[i].endsWith("分管副总")) {
						}else  {
							flow.add(rl[i]);
						}
					}else {
						flow.add(rl[i]);
					}
				}
			}else if(node.equals("总经理")) {
				nodelevel=5;
				int level=getLevel(fromer);
				if(level>nodelevel) {
				}else if(level==nodelevel) {
					String[]auditer=getName("总经理", getDepart(fromer));
					if(auditer.length!=1) {
						flow.add(rl[i]);
					}else if(auditer[0].equals(fromer)) {			//等于自己时不需要审核
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
	public static Statement connect(Args args) {
		String ipAndPort=args.getValue("connectionString") == null ? defaultHost : args.getValue("connectionString");
		String userr = args.getValue("user") == null ? user : args.getValue("user");
		String pwdd= args.getValue("password") == null ? pwd : args.getValue("password");
				if("mysql".equals(databaseBrand)) {
					form=Database.mysqlConnect(ipAndPort,userr, pwdd);
				}else{
			form=Database.connect(ipAndPort, userr, pwdd);
				}
			return form;
	}
	public static String[] getProcess(String name) {		//获取流程
		if(name==null) {
			System.out.println("error:workflow name must be not null!");
			return null;
		}
		ArrayList<String>ar=new ArrayList<>();//流程
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
				int index=flowpro.indexOf("，", start);
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
	public static int getLevel(String name) {				//获取该职员的最大级别
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
				System.err.println("error：[select level from staff where name='"+name+"']语句未找到任何行！");

			}
			r.close();
			return i;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1;
	}
	public static String[] getName(String post,String depart) {					//通过岗位和部门获取职员列表
		try {
			ResultSet rs=form.executeQuery("select name from staff where post='"+post+"' and department='"+depart+"';");
			ArrayList<String>a=new ArrayList<>();
			boolean have=false;
			while(rs.next()) {
				a.add(rs.getString(1).trim());
				have=true;
			}
			if(!have) {
				System.err.println("error：[select name from staff where post='"+post+"' and department='"+depart+"']语句未找到任何行！");
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
	public static String getDepart(String name) {								//通过姓名获取员工的部门
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
	public static String[] getName(String post) {					//通过岗位查询职员
		try {
			ResultSet rs=form.executeQuery("select name from staff where post='"+post+"';");
			ArrayList<String>a=new ArrayList<>();
			boolean have=false;
			while(rs.next()) {
				a.add(rs.getString(1).trim());
				have=true;
			}
			if(!have) {
				System.out.println("警告：[select name from staff where post='"+post+"']语句未找到任何行！");
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
			for(String au:auditer) {					//添加数量
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
			while(s.hasNext()) {			//找寻是否有人连续审核
				String name=s.next();
				boolean have=false;				//有说明已经更新，没有更新则为不连续
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
		}else {				//其它部门审核
			String []auditer=getName(post, cf[i].substring(0,cf[i].length()-4));
			for(String au:auditer) {					//添加数量
				if(al.get(au)==null) {
					al.put(au,1);
				}else {
					al.put(au, al.get(au)+1);
				}
			}
			Iterator<String>s=al.keySet().iterator();
			String last=null;
			while(s.hasNext()) {			//找寻是否有人连续审核
				boolean have=false;				//有说明已经更新，没有更新则为不连续
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
	private  ArrayList<String[]> query(String sql) {			//未完
		if(!sql.toLowerCase().startsWith("select")) {
			System.out.println("error:can't execute the statement!!");
			return null;
		}
		ResultSet result=null;
		try {
			result=form.executeQuery(sql);
			
		} catch (SQLException e) {
			System.out.println("警告："+e.getMessage());
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
