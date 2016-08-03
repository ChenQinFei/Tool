package bookProcess;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class TXTregex {
	public static void main(String args[]) throws Exception{
		//DealWeiChangBinYanFang();
		//DealNongCunZhongYiDanYanFang500();
		//DealFangZhiChangJianBinDanFangYanFangJingXuan();
		DealHaiYangYaoWuYuXiaoFang();
	}
	public static void DealHaiYangYaoWuYuXiaoFang() throws Exception{
		//输入处理的正则表达式
		String inputString="<标准名>//<功能主治>功能主治<用法用量>用法用量<别名>别名";
		//txt所在文件夹
		//String fileFolder="E:\\source\\李哲蓉\\12本成品数据\\20150001\\txt\\";
		String fileFolder="E:\\processData\\全文识别\\常用中药别名速查手册\\txt\\";
		//指定输出文件
		String outputFileName="E:\\processData\\Output\\常用中药别名速查手册3.csv";
		//txt起始页码和结束页码
		int startPageNum=21;
		int endPageNum=241;
		//分隔符位置
		String pos="before";
		
		ArrayList<String[]> list=dealRegex(inputString);
		File outputFile=new File(outputFileName);
		BufferedWriter writer=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile), "GBK"));
		//BufferedWriter writer=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile), "utf-8"));
		csvHeaderWriteIn(writer,list);
		if(pos.equals("after")){
			dealWithLabelAfter(writer,list,startPageNum,endPageNum,fileFolder);
		}
		else{
			dealWithLabelBefore(writer,list,startPageNum,endPageNum,fileFolder);
		}
		writer.close();
	}
	public static void DealFangZhiChangJianBinDanFangYanFangJingXuan() throws Exception{
		//输入处理的正则表达式
		String inputString="<1>【组成】<2>【用法】";
		//txt所在文件夹
		//String fileFolder="E:\\source\\李哲蓉\\12本成品数据\\20150001\\txt\\";
		String fileFolder="E:\\data\\李哲蓉\\第一批\\1防治常见病单方验方精选\\TXT\\";
		//指定输出文件
		String outputFileName="E:\\worktest\\outputForTest.csv";
		//txt起始页码和结束页码
		int startPageNum=9;
		int endPageNum=320;
		//分隔符位置
		String pos="before";
		
		ArrayList<String[]> list=dealRegex(inputString);
		File outputFile=new File(outputFileName);
		BufferedWriter writer=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile), "GBK"));
		//BufferedWriter writer=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile), "utf-8"));
		csvHeaderWriteIn(writer,list);
		if(pos.equals("after")){
			dealWithLabelAfter(writer,list,startPageNum,endPageNum,fileFolder);
		}
		else{
			dealWithLabelBefore(writer,list,startPageNum,endPageNum,fileFolder);
		}
		writer.close();
	}
	public static void DealChangJianBinDanFangYanFang() throws Exception{
		//输入处理的正则表达式
		String inputString="<1>【方<2>【用法】";
		//txt所在文件夹
		//String fileFolder="E:\\source\\李哲蓉\\12本成品数据\\20150001\\txt\\";
		String fileFolder="E:\\data\\李哲蓉\\第一批\\1常见病单方·验方\\TXT\\";
		//指定输出文件
		String outputFileName="E:\\worktest\\outputForTest.csv";
		//txt起始页码和结束页码
		int startPageNum=6;
		int endPageNum=210;
		//分隔符位置
		String pos="before";
		
		ArrayList<String[]> list=dealRegex(inputString);
		File outputFile=new File(outputFileName);
		BufferedWriter writer=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile), "GBK"));
		//BufferedWriter writer=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile), "utf-8"));
		csvHeaderWriteIn(writer,list);
		if(pos.equals("after")){
			dealWithLabelAfter(writer,list,startPageNum,endPageNum,fileFolder);
		}
		else{
			dealWithLabelBefore(writer,list,startPageNum,endPageNum,fileFolder);
		}
		writer.close();
	}
	public static void DealNongCunZhongYiDanYanFang500() throws Exception{
		//输入处理的正则表达式
		String inputString="<1>方<2>主治：<3>功效：<4>用法：";
		//txt所在文件夹
		//String fileFolder="E:\\source\\李哲蓉\\12本成品数据\\20150001\\txt\\";
		String fileFolder="E:\\data\\李哲蓉\\第一批\\1农村中医单验方500首\\TXT\\";
		//指定输出文件
		String outputFileName="E:\\worktest\\outputForTest.csv";
		//txt起始页码和结束页码
		int startPageNum=10;
		int endPageNum=235;
		//分隔符位置
		String pos="before";
		
		ArrayList<String[]> list=dealRegex(inputString);
		File outputFile=new File(outputFileName);
		BufferedWriter writer=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile), "GBK"));
		//BufferedWriter writer=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile), "utf-8"));
		csvHeaderWriteIn(writer,list);
		if(pos.equals("after")){
			dealWithLabelAfter(writer,list,startPageNum,endPageNum,fileFolder);
		}
		else{
			dealWithLabelBefore(writer,list,startPageNum,endPageNum,fileFolder);
		}
		writer.close();
	}
	public static void DealZhongCaoYaoDanFangYanFangXuanBian() throws Exception{
		//输入处理的正则表达式
		String inputString="<0>处方<1>用法";
		//txt所在文件夹
		//String fileFolder="E:\\source\\李哲蓉\\12本成品数据\\20150001\\txt\\";
		String fileFolder="E:\\data\\李哲蓉\\第一批\\1中草药单方验方选编\\TXT\\";
		//指定输出文件
		String outputFileName="E:\\worktest\\outputForTest.csv";
		//txt起始页码和结束页码
		int startPageNum=11;
		int endPageNum=149;
		//分隔符位置
		String pos="before";
		
		ArrayList<String[]> list=dealRegex(inputString);
		File outputFile=new File(outputFileName);
		BufferedWriter writer=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile), "GBK"));
		//BufferedWriter writer=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile), "utf-8"));
		csvHeaderWriteIn(writer,list);
		if(pos.equals("after")){
			dealWithLabelAfter(writer,list,startPageNum,endPageNum,fileFolder);
		}
		else{
			dealWithLabelBefore(writer,list,startPageNum,endPageNum,fileFolder);
		}
		writer.close();
	}
	public static void DealWeiChangBinYanFang() throws Exception{
		//输入处理的正则表达式
		String inputString="<0>//<1>【药物组成】<2>【适用病症】<3>【用药方法】<4>【临床疗效】<5>【验方来源】";
		//txt所在文件夹
		//String fileFolder="E:\\source\\李哲蓉\\12本成品数据\\20150001\\txt\\";
		String fileFolder="E:\\data\\李哲蓉\\第一批\\1胃肠病验方\\TXT\\";
		//指定输出文件
		String outputFileName="E:\\worktest\\outputForTest.csv";
		//txt起始页码和结束页码
		int startPageNum=18;
		int endPageNum=384;
		//分隔符位置
		String pos="before";
		
		ArrayList<String[]> list=dealRegex(inputString);
		File outputFile=new File(outputFileName);
		BufferedWriter writer=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile), "GBK"));
		//BufferedWriter writer=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile), "utf-8"));
		csvHeaderWriteIn(writer,list);
		if(pos.equals("after")){
			dealWithLabelAfter(writer,list,startPageNum,endPageNum,fileFolder);
		}
		else{
			dealWithLabelBefore(writer,list,startPageNum,endPageNum,fileFolder);
		}
		writer.close();
	}
	
	public static void dealWithLabelAfter(BufferedWriter writer, ArrayList<String[]> list,int startPageNum,int endPageNum,String fileFolder) throws IOException{
		BufferedReader reader;
		File inputFile;
		Boolean blankLine=false;
		String tempString=null;
		Boolean waitingForSingle=true;
		int indexForList=0;
		int listLength=list.size();
		String recordString="";
		String[] clause=new String[listLength];
		for(int i=startPageNum;i<=endPageNum;i++){
			inputFile=new File(fileFolder+completeFileName(i));
			reader=new BufferedReader(new InputStreamReader(new FileInputStream(inputFile),"utf-16"));
			while((tempString=reader.readLine())!=null){
				blankLine=false;
				if(tempString.trim().equals("")){
					blankLine=true;
					waitingForSingle=true;
				}
				if(indexForList==0 && list.get(indexForList)[2].equals("single")){
					tempString=tempString.trim();
					if(!blankLine){
						if(waitingForSingle){
							clause[indexForList]=tempString;
							if(indexForList==listLength-1){
								csvContentWriteIn(writer,clause);
								indexForList=0;
							}
							else
								indexForList=indexForList+1;
							waitingForSingle=false;
							continue;
						}
						else{
							indexForList=indexForList+1;
						}
					}
				}
				if(list.get(indexForList)[2].equals("repeat")){
					if(blankLine){
						clause[indexForList]=recordString;
						recordString="";
						csvContentWriteIn(writer,clause);
						if(indexForList==listLength-1){
							indexForList=0;
						}
						else
							indexForList=indexForList+1;
					}
					else{
						int indexForTest;
						if(indexForList==listLength-1){
							for(indexForTest=0;indexForTest<indexForList;indexForTest++){
								if(tempString.indexOf(list.get(indexForTest)[1])!=-1){
									clause[indexForList]=recordString;
									recordString="";
									csvContentWriteIn(writer,clause);
									indexForList=indexForTest;
									break;
								}
								else{
									if(list.get(indexForTest)[2].equals("onetime")  || list.get(indexForTest)[2].equals("single")){
										continue;
									}
									else{
										break;
									}
								}
							}
						}
						else{
							indexForTest=indexForList+1;
							if(tempString.indexOf(list.get(indexForTest)[1])!=-1){
								clause[indexForList]=recordString;
								recordString="";
								indexForList=indexForTest;
							}
						}
					}
				}
				if(list.get(indexForList)[2].equals("onetime")){
					int indexForTest;
					for(indexForTest=indexForList;;indexForTest++){
						if(tempString.indexOf(list.get(indexForTest)[1])!=-1){
							indexForList=indexForTest;
							break;
						}
						else{
							if(list.get(indexForTest)[2].equals("onetime")){
								continue;
							}
							else{
								break;
							}
						}
					}
				}
				if(blankLine)
					continue;
				if(tempString.indexOf(list.get(indexForList)[1])==-1){
					tempString=tempString.trim();
					recordString=recordString+tempString;
					continue;
				}
				else{
					if(list.get(indexForList)[2].equals("repeat")){
						tempString=tempString.trim();
						tempString=tempString.substring(0, tempString.length()-list.get(indexForList)[1].length());
						recordString=recordString+tempString;
					}
					else{
						tempString=tempString.trim();
						tempString=tempString.substring(0, tempString.length()-list.get(indexForList)[1].length());
						clause[indexForList]=recordString+tempString;
						recordString="";
						if(indexForList==listLength-1){
							csvContentWriteIn(writer,clause);
							indexForList=0;
						}
						else
							indexForList=indexForList+1;
					}
				}
			}
			reader.close();
			if(list.get(indexForList)[2].equals("repeat")){
				clause[indexForList]=recordString;
				csvContentWriteIn(writer,clause);
			}
		}
	}
	
	public static void dealWithLabelBefore(BufferedWriter writer, ArrayList<String[]> list,int startPageNum,int endPageNum,String fileFolder) throws IOException{
		BufferedReader reader;
		File inputFile;
		String tempString=null;
		Boolean blankLine=false;
		boolean waitingForLine=true; 
		boolean start=true;
		int indexForList=0;
		int listLength=list.size();
		String recordString="";
		String[] clause=new String[listLength];
		for(int i=startPageNum;i<=endPageNum;i++){
			inputFile=new File(fileFolder+completeFileName(i));
			reader=new BufferedReader(new InputStreamReader(new FileInputStream(inputFile),"utf-16"));
			while((tempString=reader.readLine())!=null){
				if(start){
					//tempString=tempString.substring(1,tempString.length());
					start=false;
				}
				blankLine=false;
				if(tempString.trim().equals(""))
					blankLine=true;
//				if(indexForList==0 && list.get(indexForList)[2].equals("single")){
//					if(waitingForLine==true){
//						tempString=tempString.trim();
//						if(!blankLine){
//							clause[indexForList]=tempString;
//							if(indexForList==listLength-1){
//								csvContentWriteIn(writer,clause);
//								indexForList=0;
//								waitingForLine=true;
//							}
//							else{
//								indexForList=indexForList+1;
//								waitingForLine=false;
//							}
//							continue;
//						}
//					}
//				}
				//解决寻找最后一项没有加label问题，通过空行判断
				if(indexForList==0 &&(blankLine)){
					if(!recordString.equals("")){
						clause[listLength-1]=recordString;
						csvContentWriteIn(writer,clause);
						recordString="";
						indexForList += 1;
						clause=new String[listLength];
						continue;
					}
					waitingForLine=true;
				}
				else
					waitingForLine=false;
				//判断当前行是否有匹配label
				if(tempString.indexOf(list.get(indexForList)[1])==-1){
					tempString=tempString.trim();
					recordString=recordString+tempString;
				}
				else{
					if(!recordString.equals("")){
						//判断当前匹配label是否为第一个，即最后一项，若是，则写入文件
						if(indexForList==0){
							if(clause[0]!=""){
								recordString=recordString+tempString.substring(0,tempString.indexOf(list.get(indexForList)[1]));
								clause[listLength-1]=recordString;
								System.out.println(clause[0]);
								csvContentWriteIn(writer,clause);
								clause=new String[listLength];							
								tempString=tempString.trim();
								recordString=tempString.substring(tempString.indexOf(list.get(indexForList)[1])+list.get(indexForList)[1].length(), tempString.length());		
							}
						}
						else{
							clause[indexForList-1]=recordString;
							tempString=tempString.trim();
							recordString=tempString.substring(list.get(indexForList)[1].length()+2, tempString.length()).trim();		
						}
						//循环label的index
						if(indexForList==listLength-1){
							indexForList=0;
						}
						else
							indexForList=indexForList+1;
					}
					else{
						tempString=tempString.trim();
						recordString=tempString.substring(tempString.indexOf(list.get(indexForList)[1])+list.get(indexForList)[1].length(), tempString.length());
						if(indexForList==listLength-1){
							indexForList=0;
						}
						else
							indexForList=indexForList+1;
					}
				}
			}
			reader.close();
		}
		clause[listLength-1]=recordString;
		csvContentWriteIn(writer,clause);
	}
	
	public static String completeFileName(int ordernum){
		String num=Integer.toString(ordernum);
		String filename="";
		for(int i=0;i<8-num.length();i++)
			filename=filename+"0";
		filename=filename+num+".txt";
		return filename;
	}
	//处理正则表达式
	public static ArrayList<String[]> dealRegex(String s){
		ArrayList<String[]> list=new ArrayList<String[]>();
		int startnum=0;
		if(s.indexOf("<",startnum)==-1){
			System.out.println("unrecognized input format");
			System.exit(0);
		}		
		while(true){
			int startDesPoint=s.indexOf("<",startnum);
			int endDesPoint=s.indexOf(">",startnum);
			String name=s.substring(startDesPoint+1, endDesPoint);
			int nextDesPoint=s.indexOf("<",endDesPoint+1);
			if(nextDesPoint==-1){
				String label=s.substring(endDesPoint+1, s.length());
				String info=null;
				String pos=null;
				list.add(checkAttribute(name,label,info,pos));
				break;

			}
			else{
				String label=s.substring(endDesPoint+1, nextDesPoint);
				String info=null;
				String pos=null;
				list.add(checkAttribute(name,label,info,pos));
				startnum=endDesPoint+1;
			}
		}
		return list;
	}
	/*处理表达式的中的属性
	 * 其中{a}表示分隔符后置
	 * {r}表示存在重复
	 * {o}表示下文存在重复，自身只出现一次
	 * {s}用于主标题
	 */
	public static String[] checkAttribute(String name, String label, String info, String pos){
		if(label.endsWith("{a}")){
			pos="after";
			label=label.substring(0, label.length()-3);
		}
		else
			pos="before";
		
		if(label.endsWith("{r}")){
			info="repeat";
			label=label.substring(0, label.length()-3);
		}
		else if(label.endsWith("{o}")){
			info="onetime";
			label=label.substring(0, label.length()-3);
		}
		else if(label.endsWith("{s}")){
			info="single";
			label=label.substring(0, label.length()-3);
		}
		else
			info="none";
		String[] column={name,label,info,pos};
		return column;
	}
	//写入CSV文件头
	public static void csvHeaderWriteIn(BufferedWriter writer, ArrayList<String[]> list) throws IOException{
		String header="";
		for(int i=0;i<list.size();i++){
			header=header+list.get(i)[0]+",";
		}
		header=header.substring(0, header.length()-1)+"\r\n";
		writer.write(header);
	}
	//写入CSV文件内容
	public static void csvContentWriteIn(BufferedWriter writer,String[] clause) throws IOException{
		String content="";
		for(int i=0;i<clause.length;i++){
			content=content+clause[i]+",";
		}
		content=content.substring(0, content.length()-1)+"\r\n";
		writer.write(content);
	}
}
