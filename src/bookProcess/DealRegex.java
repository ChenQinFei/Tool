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

public class DealRegex {
	public static void main(String args[]) throws IOException{
		//DealZhiAiYanFang400();
		//DealZhuYanYouShuPianYanFang();
		//DealHaiYangYaoWuYuXiaoFang();
		DealChangYongZhongYao();
	}
	public static void DealChangYongZhongYao() throws IOException{

		String notRepeat="";
		String Repeat="<标准名>##; [功能主治]<功能主治>//;[用法用量]<用法用量>//;[别名]<别名>//;";
		//txt所在文件夹
		String fileFolder="E:\\processData\\全文识别\\常用中药别名速查手册\\txt\\";
		//指定输出文件
		String outputFileName="E:\\processData\\Output\\常用中药别名速查手册.csv";
		//txt起始页码和结束页码
		int startPageNum=21;
		int endPageNum=241;

		//创建处理完正则表达式的list
		ArrayList<String[]> notRepeatList = dealNotRepeatRegex(notRepeat);
		ArrayList<String[]> repeatList = dealRepeatRegex(Repeat);
		File inputFile;
		File outputFile=new File(outputFileName);
		BufferedReader reader;
		BufferedWriter writer=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile), "GBK"));
		//写入csv header部分
		csvHeaderWriteIn(writer,notRepeatList,repeatList);
		
		int indexForNotRepeat=0;
		int indexForRepeat=0;
		String recordString="";
		boolean WaitingSecondLabel=false;
		int listLength=notRepeatList.size()+repeatList.size();
		String[] clause=new String[listLength];
		for(int i=startPageNum;i<=endPageNum;i++){
			inputFile=new File(fileFolder+completeFileName(i));
			try{
				reader=new BufferedReader(new InputStreamReader(new FileInputStream(inputFile),"utf-16"));
				//开始处理
				String tempString=null;
				boolean FirstLabelFound=false;
				boolean SecondLabelFound=false;
				while((tempString=reader.readLine())!=null){
					FirstLabelFound=false;
					SecondLabelFound=false;
					//先判断是否进入重复部分
					if(tempString.indexOf(repeatList.get(indexForRepeat)[0])!=-1){
						indexForNotRepeat=notRepeatList.size();
					}
					//处理非重复部分
					if(indexForNotRepeat<notRepeatList.size()){
						//如果未查找到第i个的label
						if(tempString.indexOf(notRepeatList.get(indexForNotRepeat)[1])==-1){
							tempString=tempString.trim();
							recordString=recordString+tempString;
						}
						//如果查找到第i个的label
						else{
							tempString=tempString.trim();
							tempString=tempString.substring(0, tempString.length()-notRepeatList.get(indexForNotRepeat)[1].length());
							recordString=recordString+tempString;
							clause[indexForNotRepeat]=recordString;
							indexForNotRepeat=indexForNotRepeat+1;
						}
					}
					//处理重复部分
					else{
						while(true){
							//如果查找到第i个的前label
							if(tempString.indexOf(repeatList.get(indexForRepeat)[0])!=-1){
								FirstLabelFound=true;
								recordString="";
								//在本行找其后label，若找到则继续在本行查找
								if(tempString.indexOf(repeatList.get(indexForRepeat)[2])!=-1){
									clause[indexForNotRepeat+indexForRepeat]=tempString.substring(tempString.indexOf(repeatList.get(indexForRepeat)[0])+repeatList.get(indexForRepeat)[0].length(), tempString.indexOf(repeatList.get(indexForRepeat)[2]));
									tempString=tempString.substring(tempString.indexOf(repeatList.get(indexForRepeat)[2])+repeatList.get(indexForRepeat)[2].length(),tempString.length());
									if(indexForRepeat==repeatList.size()-1){
										indexForNotRepeat=0;
										indexForRepeat=0;
										csvContentWriteIn(writer,clause);
										break;
									}
									else{
										indexForRepeat=indexForRepeat+1;
									}
									WaitingSecondLabel=false;
									continue;
								}
								//若未找到后label，储存该段数据到recordString
								else{
									tempString=tempString.trim();
									recordString=recordString+tempString.substring(tempString.indexOf(repeatList.get(indexForRepeat)[0])+repeatList.get(indexForRepeat)[0].length(), tempString.length());	
									WaitingSecondLabel=true;
									break;
								}
							}
							//如果未查找到第i个的前label
							else{
								break;
							}
						}
						//如果查找到第i个的后label
						if(WaitingSecondLabel&&tempString.indexOf(repeatList.get(indexForRepeat)[2])!=-1){
							SecondLabelFound=true;
							tempString=tempString.trim();
							tempString=tempString.substring(0, tempString.length()-repeatList.get(indexForRepeat)[2].length());
							recordString=recordString+tempString;
							clause[indexForNotRepeat+indexForRepeat]=recordString;
							WaitingSecondLabel=false;
							//如果到了repeat段的末尾
							if(indexForRepeat==repeatList.size()-1){
								indexForNotRepeat=0;
								indexForRepeat=0;
								recordString="";
								csvContentWriteIn(writer,clause);
							}
							else{
								indexForRepeat=indexForRepeat+1;
							}
						}
						if((!FirstLabelFound)&&(!SecondLabelFound)){
							tempString=tempString.trim();
							recordString=recordString+tempString;
						}
					}
				}
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
				writer.close();
			}
		}
		writer.close();
	}
	public static void DealZhiAiYanFang400() throws IOException{

		String notRepeat="<章节>//;";
		String Repeat="．<方剂名>（;（<来源>）;［组成与用法］<组成与用法>##;［功效与应用］<功效与应用>//;";
		//txt所在文件夹
		String fileFolder="E:\\data\\李哲蓉\\第一批\\1治癌验方400\\TXT\\";
		//指定输出文件
		String outputFileName="E:\\worktest\\outputForTest.csv";
		//txt起始页码和结束页码
		int startPageNum=22;
		int endPageNum=187;

		//创建处理完正则表达式的list
		ArrayList<String[]> notRepeatList=dealNotRepeatRegex(notRepeat);
		ArrayList<String[]> repeatList=dealRepeatRegex(Repeat);
		File inputFile;
		File outputFile=new File(outputFileName);
		BufferedReader reader;
		BufferedWriter writer=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile), "GBK"));
		//写入csv header部分
		csvHeaderWriteIn(writer,notRepeatList,repeatList);
		
		int indexForNotRepeat=0;
		int indexForRepeat=0;
		String recordString="";
		boolean WaitingSecondLabel=false;
		int listLength=notRepeatList.size()+repeatList.size();
		String[] clause=new String[listLength];
		for(int i=startPageNum;i<=endPageNum;i++){
			inputFile=new File(fileFolder+completeFileName(i));
			try{
				reader=new BufferedReader(new InputStreamReader(new FileInputStream(inputFile),"utf-16"));
				//开始处理
				String tempString=null;
				boolean FirstLabelFound=false;
				boolean SecondLabelFound=false;
				while((tempString=reader.readLine())!=null){
					FirstLabelFound=false;
					SecondLabelFound=false;
					//先判断是否进入重复部分
					if(tempString.indexOf(repeatList.get(indexForRepeat)[0])!=-1){
						indexForNotRepeat=notRepeatList.size();
					}
					//处理非重复部分
					if(indexForNotRepeat<notRepeatList.size()){
						//如果未查找到第i个的label
						if(tempString.indexOf(notRepeatList.get(indexForNotRepeat)[1])==-1){
							tempString=tempString.trim();
							recordString=recordString+tempString;
						}
						//如果查找到第i个的label
						else{
							tempString=tempString.trim();
							tempString=tempString.substring(0, tempString.length()-notRepeatList.get(indexForNotRepeat)[1].length());
							recordString=recordString+tempString;
							clause[indexForNotRepeat]=recordString;
							indexForNotRepeat=indexForNotRepeat+1;
						}
					}
					//处理重复部分
					else{
						while(true){
							//如果查找到第i个的前label
							if(tempString.indexOf(repeatList.get(indexForRepeat)[0])!=-1){
								FirstLabelFound=true;
								recordString="";
								//在本行找其后label，若找到则继续在本行查找
								if(tempString.indexOf(repeatList.get(indexForRepeat)[2])!=-1){
									clause[indexForNotRepeat+indexForRepeat]=tempString.substring(tempString.indexOf(repeatList.get(indexForRepeat)[0])+repeatList.get(indexForRepeat)[0].length(), tempString.indexOf(repeatList.get(indexForRepeat)[2]));
									tempString=tempString.substring(tempString.indexOf(repeatList.get(indexForRepeat)[2])-repeatList.get(indexForRepeat)[2].length(),tempString.length());
									if(indexForRepeat==repeatList.size()-1){
										indexForNotRepeat=0;
										indexForRepeat=0;
										csvContentWriteIn(writer,clause);
										break;
									}
									else{
										indexForRepeat=indexForRepeat+1;
									}
									continue;
								}
								//若未找到后label，储存该段数据到recordString
								else{
									tempString=tempString.trim();
									recordString=recordString+tempString.substring(tempString.indexOf(repeatList.get(indexForRepeat)[0])+repeatList.get(indexForRepeat)[0].length(), tempString.length());	
									WaitingSecondLabel=true;
									break;
								}
							}
							//如果未查找到第i个的前label
							else{
								break;
							}
						}
						//如果查找到第i个的后label
						if(WaitingSecondLabel&&tempString.indexOf(repeatList.get(indexForRepeat)[2])!=-1){
							SecondLabelFound=true;
							tempString=tempString.trim();
							tempString=tempString.substring(0, tempString.length()-repeatList.get(indexForRepeat)[2].length());
							recordString=recordString+tempString;
							clause[indexForNotRepeat+indexForRepeat]=recordString;
							WaitingSecondLabel=false;
							//如果到了repeat段的末尾
							if(indexForRepeat==repeatList.size()-1){
								indexForNotRepeat=0;
								indexForRepeat=0;
								recordString="";
								csvContentWriteIn(writer,clause);
							}
							else{
								indexForRepeat=indexForRepeat+1;
							}
						}
						if((!FirstLabelFound)&&(!SecondLabelFound)){
							tempString=tempString.trim();
							recordString=recordString+tempString;
						}
					}
				}
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
				writer.close();
			}
		}
		writer.close();
	}
	/*未用的DealWeiChangBinYanFang
	public static void DealWeiChangBinYanFang() throws IOException{

			String notRepeat="";
			String Repeat="//<方名>##;【药物组成】<组成>##;【适用病症】<适用>##;【用药方法】<用药>##;【临床疗效】<疗效>##;【验方来源】<来源>//";
			//txt所在文件夹
			String fileFolder="E:\\data\\李哲蓉\\第一批\\1胃肠病验方\\TXT\\";
			//指定输出文件
			String outputFileName="E:\\worktest\\outputForTest.csv";
			//txt起始页码和结束页码
			int startPageNum=18;
			int endPageNum=30;
			
			//创建处理完正则表达式的list
			ArrayList<String[]> notRepeatList=dealNotRepeatRegex(notRepeat);
			ArrayList<String[]> repeatList=dealRepeatRegex(Repeat);
			File inputFile;
			File outputFile=new File(outputFileName);
			BufferedReader reader;
			BufferedWriter writer=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile), "GBK"));
			//写入csv header部分
			csvHeaderWriteIn(writer,notRepeatList,repeatList);
			
			int indexForNotRepeat=0;
			int indexForRepeat=0;
			String recordString="";
			boolean WaitingSecondLabel=false;
			boolean WaitingFirstLabel=false;
			int listLength=notRepeatList.size()+repeatList.size();
			String[] clause=new String[listLength];
			for(int i=startPageNum;i<=endPageNum;i++){
				inputFile=new File(fileFolder+completeFileName(i));
				try{
					reader=new BufferedReader(new InputStreamReader(new FileInputStream(inputFile),"utf-16"));
					//开始处理
					String tempString=null;
					boolean FirstLabelFound=false;
					boolean SecondLabelFound=false;
					while((tempString=reader.readLine())!=null){
						FirstLabelFound=false;
						SecondLabelFound=false;
						//先判断是否进入重复部分
						if(tempString.indexOf(repeatList.get(indexForRepeat)[0])!=-1 || WaitingSecondLabel==true){
							indexForNotRepeat=notRepeatList.size();
						}
						//处理非重复部分
						if(indexForNotRepeat<notRepeatList.size()){
							//如果未查找到第i个的label
							if(tempString.indexOf(notRepeatList.get(indexForNotRepeat)[1])==-1){
								tempString=tempString.trim();
								recordString=recordString+tempString;
							}
							//如果查找到第i个的label
							else{
								tempString=tempString.trim();
								tempString=tempString.substring(0, tempString.length()-notRepeatList.get(indexForNotRepeat)[1].length());
								recordString=recordString+tempString;
								clause[indexForNotRepeat]=recordString;
								indexForNotRepeat=indexForNotRepeat+1;
							}
						}
						//处理重复部分
						else{
							while(true){
								//如果查找到第i个的前label
								if(tempString.indexOf(repeatList.get(indexForRepeat)[0])!=-1){
									FirstLabelFound=true;
									recordString="";
									//在本行找其后label，若找到则继续在本行查找
									if(tempString.indexOf(repeatList.get(indexForRepeat)[2])!=-1){
										clause[indexForNotRepeat+indexForRepeat]=tempString.substring(tempString.indexOf(repeatList.get(indexForRepeat)[0])+repeatList.get(indexForRepeat)[0].length(), tempString.indexOf(repeatList.get(indexForRepeat)[2]));
										tempString=tempString.substring(tempString.indexOf(repeatList.get(indexForRepeat)[2])-repeatList.get(indexForRepeat)[2].length(),tempString.length());
										if(indexForRepeat==repeatList.size()-1){
											indexForNotRepeat=0;
											indexForRepeat=0;
											csvContentWriteIn(writer,clause);
											break;
										}
										else{
											indexForRepeat=indexForRepeat+1;
										}
										continue;
									}
									//若未找到后label，储存该段数据到recordString
									else{
										tempString=tempString.trim();
										recordString=recordString+tempString.substring(tempString.indexOf(repeatList.get(indexForRepeat)[0])+repeatList.get(indexForRepeat)[0].length(), tempString.length());	
										WaitingSecondLabel=true;
										break;
									}
								}
								//如果未查找到第i个的前label
								else{
									break;
								}
							}
							//如果查找到第i个的后label
							if(WaitingSecondLabel&&tempString.indexOf(repeatList.get(indexForRepeat)[2])!=-1){
								SecondLabelFound=true;
								tempString=tempString.trim();
								recordString=recordString+tempString.substring(0, tempString.length()-repeatList.get(indexForRepeat)[2].length());
								WaitingFirstLabel=true;
								//clause[indexForNotRepeat+indexForRepeat]=recordString;
								WaitingSecondLabel=false;
								//如果到了repeat段的末尾
								if(indexForRepeat==repeatList.size()-1){
									indexForNotRepeat=0;
									indexForRepeat=0;
									recordString="";
									csvContentWriteIn(writer,clause);
								}
								else{
									indexForRepeat=indexForRepeat+1;
								}
								if(tempString.indexOf(repeatList.get(indexForRepeat)[0])!=-1){
									FirstLabelFound=true;
									recordString=recordString+tempString.substring(tempString.indexOf(repeatList.get(indexForRepeat)[0])+repeatList.get(indexForRepeat)[0].length(), tempString.length());
									WaitingSecondLabel=true;
								}
							}
							if((!FirstLabelFound)&&(!SecondLabelFound)){
								tempString=tempString.trim();
								recordString=recordString+tempString;
							}
						}
					}
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
					writer.close();
				}
			}
			writer.close();
		}
	*/
	//处理fileName
	public static String completeFileName(int ordernum){
		String num=Integer.toString(ordernum);
		String filename="";
		for(int i=0;i<8-num.length();i++)
			filename=filename+"0";
		filename=filename+num+".txt";
		return filename;
	}
	//处理非重复部分正则表达式
	public static ArrayList<String[]> dealNotRepeatRegex(String s){
		ArrayList<String[]> list=new ArrayList<String[]>();
		if(!s.equals("")){
			String[] part=s.split(";");
			for(int i=0;i<part.length;i++){
				String name=part[i].substring(part[i].indexOf("<")+1, part[i].indexOf(">"));
				String label=part[i].substring(part[i].indexOf(">")+1, part[i].length());
				String[] column={name,label};
				list.add(column);
			}
		}
		return list;
	}
	//处理重复部分正则表达式
	public static ArrayList<String[]> dealRepeatRegex(String s){
		ArrayList<String[]> list=new ArrayList<String[]>();
		String[] part=s.split(";");
		for(int i=0;i<part.length;i++){
			String label1=part[i].substring(0, part[i].indexOf("<"));
			String name=part[i].substring(part[i].indexOf("<")+1, part[i].indexOf(">"));
			String label2=part[i].substring(part[i].indexOf(">")+1, part[i].length());
			String[] column={label1,name,label2};
			list.add(column);
		}
		return list;
	}
	//写入CSV文件头
	public static void csvHeaderWriteIn(BufferedWriter writer, ArrayList<String[]> notRepeatList,ArrayList<String[]> repeatList) throws IOException{
		String header="";
		for(int i=0;i<notRepeatList.size();i++){
			header=header+notRepeatList.get(i)[0]+",";
		}
		for(int i=0;i<repeatList.size();i++){
			header=header+repeatList.get(i)[1]+",";
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
		System.out.println(content);
		writer.write(content);
	}
}
