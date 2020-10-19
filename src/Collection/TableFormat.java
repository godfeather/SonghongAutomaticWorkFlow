package Collection;

import java.util.ArrayList;

/**
 * 用于生成表格，
 * */
public class TableFormat {
	private ArrayList<String> all=new ArrayList<>();
	public ArrayList<ArrayList<String >> comp(String resource){
		return comp(resource,false);
	}
	public ArrayList<ArrayList<String >> comp(ArrayList<String> resource){
		String re="";
		for(String st:resource) {
			re+=st+"\n";
		}
		return comp(re,false);
	}
	public ArrayList<ArrayList<String >> comp(ArrayList<String> resource,boolean index){
		String re="";
		for(String st:resource) {
			re+=st+"\n";
		}
		return comp(re,index);
	}
	public ArrayList<ArrayList<String>> comp(String resource,boolean index) {//获取表格的所有内容
		String[] row=resource.split("\n");
		ArrayList<ArrayList<String>> content=new ArrayList<>();				//内容保存
		int [] count=new int[row.length];
		for(int i=0;i<row.length;i++) {//遍历行

			ArrayList<String> al=new ArrayList<String>();
			char[] singleRow=row[i].trim().toCharArray();
			String map=row[i].trim();//上行的字段映射
			int start=0;
			int end=0;		
			boolean have=false;							//找到“"”符号的表示
			boolean needComma=false;							//当找到"符号时，就会触发该标志，当标志为ture时，它会持续直到找到Comma（，）符号或者到行尾才收尾
			int endPoint=0;										//另一半"符号出现的位置
			for(int j=0;j<singleRow.length;j++) {			//统计字段个数
				if(index) {
					if(j==0&&i!=0) {
						al.add(""+i);							//表格文本行编号
						count[i]++;
					}else if(j==0&&i==0){
						al.add("NO");
						count[i]++;
					}
				}
													
				end=j;
				if((singleRow[j]=='"')&&have==false&&needComma==false) {
					
						start=j+1;
						have=true;
						if(j<singleRow.length-1) {								//如果"符号就是本行的最后一位，那么不需要寻找结束的逗号（，）
							needComma=true;
						}
					
				}else if((singleRow[j]=='"')&&have) {
					have=false;
					if(j==singleRow.length-1) {
						end-=1;
					}
					endPoint=j;
				}
				if((singleRow[j]==','||singleRow[j]=='，')&&have==false) {	//以，作为行分割，统计行数并统计字段最多值
					if(j==0||singleRow[j-1]!='\\'){
							end=j;
							
							if(needComma) {
								al.add(map.substring(start,endPoint).trim());
							}else {
								al.add(map.substring(start,end).trim());
							}
							needComma=false;								//执行了该Case说明在""外找到了逗号，因此剪掉字符并归零准备可以查找下一组引号（""）
								end=j;										//绝不是多余的赋值；若找到一个逗号，恰巧需要一个逗号时，则上边的赋值会被改变，这里的赋值保证无论发生什么都会从下一格逗号
								count[i]++;
							start=end+1;
						
					}else if(singleRow[j-1]=='\\'){
						
						String cEnd=map.substring(j+1,map.length());
						String cStart=map.substring(0,j-1)+",";
						map=cStart+cEnd;
						singleRow=map.toCharArray();
						j--;
						
						
						System.out.println("endPoint:"+endPoint);
						System.out.println("J:"+j);
						System.out.println("end:"+j);
						System.out.println("Len:"+map.length());
						System.out.println("start:"+start);
						if(j==singleRow.length-1) {												//如果最后出现被注释掉的逗号，直接开始截，否则将没有机会再截，会造成格子为空的情况
							al.add(map.substring(start,j+1));
						}
					}		
				}else if(j==singleRow.length-1){
					if(needComma&&have==false) {												//如果缺少逗号但字符串已走到尽头时，从"符号处截断，多余部分不要！！
						al.add(map.substring(start,endPoint));
					}else {
						al.add(map.substring(start,end+1));
					}
				}
			}
			content.add(al);
		}
		int max=0;
		for(int i=0;i<count.length;i++) {
			if(count[i]>max) {
				max=count[i];
			}
		}
		max++;
		for(int i=0;i<content.size();i++) {
			while(content.get(i).size()<max) {
				content.get(i).add("");
				
			}
		}
		return content;
	}
	private ArrayList<ArrayList<String>> format(ArrayList<ArrayList<String>> as) {	//调整表格所有内容水平居中
		ArrayList<ArrayList<String>> aas=as;
		
		int[] size=countMax(aas);
		ArrayList<ArrayList<String>> all=new ArrayList<>();
		
		for(int i=0;i<aas.size();i++) {
			ArrayList<String> al=aas.get(i);						//每条数据				
			ArrayList<String>arrayli=new ArrayList<String>();		//cell
			for(int j=0;j<al.size();j++) {
				int spaceTotal=(size[j]-al.get(j).getBytes().length)+4;		//当前字段需要增加的空格总数	
				
				int spaceHalf=spaceTotal/2;										//当前字段两边各添加空格数量
				String field="";
				for(int z=0;z<spaceTotal;z++) {
					
					if(z==spaceHalf){
						field+=al.get(j);
					}
						field+=" ";
					
				}
				
				arrayli.add(field);
				
			}
			all.add(arrayli);
		}
		return all;
	}
	private ArrayList<String> getTable(ArrayList<ArrayList<String>> aas){					//传入表格结构的集合，可使该集合变成表格
		ArrayList<String>title=aas.get(0);
		String tit="";
		for(int i=0;i<title.size();i++) {										//拼接字段
			tit+="+";
			for(int j=0;j<title.get(i).getBytes().length;j++) {
				tit+="=";
			}
		}
		tit+="+\r\n";
		for(int i=0;i<title.size();i++) {										
			tit+="#"+title.get(i);
		}
		tit+="#\r\n";
		for(int i=0;i<title.size();i++) {										
			tit+="+";
			for(int j=0;j<title.get(i).getBytes().length;j++) {
				tit+="=";
			}
		}			//头字段拼接完成
		tit+="+\r\n";
		all.add(tit);
		for(int z=1;z<aas.size();z++) {
			tit="";
			ArrayList<String>row=aas.get(z);
			for(int i=0;i<row.size();i++) {										
				tit+="|"+row.get(i);
			}
			tit+="|\r\n";
			for(int i=0;i<row.size();i++) {										//拼接字段
				if(i==0) {
					tit+="|";
				}else {
					tit+="+";
				}
				for(int j=0;j<row.get(i).getBytes().length;j++) {
					tit+="-";
				}
			}
			tit+="|\r\n";
			all.add(tit);
		}
		return all;
	}
	public ArrayList<String> getTableFormat(ArrayList<ArrayList<String>> tableModel){
		all.clear();
		return getTable(format(tableModel));
	}
	public void showTable() {			//打印表格
		for(String al:all) {
			System.out.print(al);
		}
	}
	@Override
	public String toString() {
		String temp="";
		for(String al:all) {
			temp+=al;
		}
		return temp;
	}
	public void showTemp() {
		
	}
	public int[] countMax(ArrayList<ArrayList<String>> aas) {
		int arrayLengh=0;
		for(ArrayList<String> row:aas) {
			if(row.size()>arrayLengh) {
				arrayLengh=row.size();
			}
		}
		int []size=new int[arrayLengh];						//统计字段数
		for(int i=0;i<aas.size();i++) {					//该循环用于把统计每列最大字符数以字节进行统计，保证换行格式
			for(int j=0;j<aas.get(i).size();j++) {
				if(size[j]<aas.get(i).get(j).getBytes().length) {
					size[j]=aas.get(i).get(j).getBytes().length;
				}
			}
		}
		return size;
	}
	
	private void getSuperTable(int MaxWidth,ArrayList<ArrayList<String>> comp) {			//表格单个字段的最大字数和表格映射
		if(MaxWidth>=1) {
			
		}else {
			System.out.println("字段宽度不能小于1");
			return;
		}
		ArrayList<ArrayList<String>> table=comp;
		int stand[]=countMax(comp);
		for(int i=0;i<table.size();i++) {				//每行
			
		}
}
}
