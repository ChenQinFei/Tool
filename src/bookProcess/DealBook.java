package bookProcess;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;

import common.Database;
public class DealBook{
	private static Database db;
	public static void main(String args[]) throws IOException, Exception{
		//redealZhiAiYanFang400();
		//redealWeiChangBinYanFang();
		//redealZhongCaoYaoDanFangYanFangXuanBian();
		//redealNongCunZhongYiDanYanFang500();
		//redealZhuYanYouShuPianYanFang();
		//redealChangJianBinDanFangYanFang();
//		changYongZhongYao();
		SecondProcess();
	}
	public static void SecondProcess() throws IOException {
		String inputFilePath="E:\\processData\\Output\\常用中药别名速查手册3.csv";
		File inputFile=new File(inputFilePath);
		BufferedReader reader=new BufferedReader(new InputStreamReader(new FileInputStream(inputFile),"GBK"));
		//指定输出文件
		String outputFileName="E:\\processData\\Output\\secondPro\\常用中药别名速查手册3.csv";
		File outputFile=new File(outputFileName);
		BufferedWriter writer=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile), "GBK"));
		String tempString;
		int i=0;
		String category="",cateDetail="";
		try {
			while((tempString=reader.readLine())!=null){
				if(i==0){
					writer.write("标准名,一级目录,二级目录,功效,用法,别名,附药\r\n");
					i=1;
					continue;
				}
				String[] part=tempString.split(",");
				String name=part[0];
				if(name.contains("章")){
					category = name.substring(name.indexOf("章")+1,name.indexOf("药")+1).trim();
				}
//				if(name.contains("节")){
//					cateDetail = name.substring(name.indexOf("节")+1, name.indexOf("药")+1).trim();
//					System.out.println(cateDetail);
//				}
				if(name.contains("附药")){
					writer.write(name.substring(name.indexOf("附药")+3, name.indexOf("//")).trim()+"\r\n");
				}else {
					writer.write("\r\n");
				}
				name = name.split("[0-9]{3}")[1].replace("#","").trim();
				
				String function = part[1].trim();
				function = function.substring(0, function.length()-2).trim();
				
				String usages = part[2].trim();
				usages = usages.substring(0, usages.length()-2).trim();
				
				writer.write(name+","+category+","+cateDetail+","+function+","+usages);
				
				String bieming = part[3].trim();
				writer.write(","+bieming.substring(0,bieming.indexOf("。")+1));
				if(bieming.contains("附药")){
					writer.write(bieming.substring(bieming.indexOf("附药")+3, bieming.length()).trim());
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		reader.close();
		writer.close();
	}
	public static void changYongZhongYao() throws Exception{
		db=new Database();
		BufferedReader reader;
		String inputFilePath="E:\\processData\\Output\\常用中药别名速查手册3.csv";
		File inputFile=new File(inputFilePath);
		reader=new BufferedReader(new InputStreamReader(new FileInputStream(inputFile),"GBK"));
		String tempString;
		int id=0;
		int i=0;
		try {
			db.init("10.15.62.29:3306/bieming?user=root&password=123&useUnicode=true&characterEncoding=UTF8");
			//db.init("10.15.62.29:3306/tcm?user=root&password=123&useUnicode=true&characterEncoding=UTF8");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		while((tempString=reader.readLine())!=null){
			if(i==0){
				i=1;
				continue;
			}
			String[] part=tempString.split(",");
			String name=part[0];
			//System.out.println(id+name);
			if(name.contains("//")){
				//id=id+1;
				//System.out.println(id+name);
				if(name.trim().substring(0, 5).contains("、")){
					name=name.substring(name.indexOf("//")+2,name.length());
				}
				if(name.trim().substring(0, 5).contains("附：解")){
					name=name.substring(name.indexOf("//")+2,name.length());
				}
				String s=name.substring(0, name.indexOf("//"));
				//System.out.println(id+s);
				String animalName=s.substring(0, s.indexOf("##"));
				animalName=DelBlank(animalName);
				String animalDescription=s.substring(s.indexOf("##")+2, s.length());
				animalDescription=animalDescription.replaceAll("#", "");
				/*
				System.out.println(id+animalName+"||||"+animalDescription);
				String SQL="insert into animal_sea (animal_id,animal_name_zh,animal_description,source) values ("+id+",'"+animalName+"','"+animalDescription+"','海洋药物与效方')";
				System.out.println(SQL);
	    		db.SQLUpdate(SQL);
	    		String k=name.substring(name.indexOf("//")+2,name.lastIndexOf("效"));
	    		if(k.length()>10){
	    			id=id+1;
	    			String animalName1=k.substring(0, k.indexOf("##"));
					animalName1=DelBlank(animalName1);
					String animalDescription1=k.substring(k.indexOf("##")+2, k.length()-2);
					animalDescription1=animalDescription1.replaceAll("#", "");
					System.out.println(id+animalName1+"||||"+animalDescription1);
					SQL="insert into animal_sea (animal_id,animal_name_zh,animal_description,source) values ("+id+",'"+animalName1+"','"+animalDescription1+"','海洋药物与效方')";
					System.out.println(SQL);
		    		db.SQLUpdate(SQL);
	    		}
	    		*/
	    		name=name.substring(name.lastIndexOf("效")+5,name.length());
			}
			name=name.replaceAll("#", "");
			String source=part[1];
			source=source.replaceAll("#", "");
			String composition=part[2];
			composition=composition.replaceAll("#", "");
			String temp=part[3];
			String preparation=temp.substring(0, temp.indexOf("##"));
			temp=temp.substring(temp.indexOf("##")+2,temp.length());
			String[] list={"功能","主治","禁忌","用法","按语"};
			String t="";
			String function="";
			String treat="";
			String attention="";
			String usage="";
			String note="";
			while(temp.contains("##")){
				t=temp.substring(0,temp.indexOf("##"));
				temp=temp.substring(temp.indexOf("##")+2,temp.length());
				for(int j=0;j<list.length;j++){
					if(t.contains(list[j])){
						String k=t.substring(t.indexOf(list[j])+3,t.length());
						k=k.trim();
						if(j==0)
							function=k;
						else if(j==1)
							treat=k;
						else if(j==2)
							attention=k;
						else if(j==3)
							usage=k;
						else if(j==4)
							note=k;
						//System.out.println(id+":"+list[j]+"||"+k);
						break;
					}
				}
			}
			
			for(int j=0;j<list.length;j++){
				if(temp.contains(list[j])){
					String k=temp.substring(temp.indexOf(list[j])+3,temp.length());
					k=k.trim();
					if(j==0)
						function=k;
					else if(j==1)
						treat=k;
					else if(j==2)
						attention=k;
					else if(j==3)
						usage=k;
					else if(j==4)
						note=k;
					System.out.println(id+":"+list[j]+"||"+k);
					break;
				}
			}
			
			System.out.println(id+name+temp);
			
			
//			String SQL="insert into 常用中药别名速查手册 (id,name,function,usages,bieming,fuyao,category,cate_detail,book) values ("+id+",'"+name+"','"+function+"','"+usages+"','"+bieming+"','"+fuyao+"','"+category+"','"+cateDetail+"','常用中药别名速查手册')";
//			System.out.println(SQL);
//    		db.SQLUpdate(SQL);

			
			id=id+1;
		}
		db.end();
	}
	public static void redealFangZhiChangJianBinDanFangYanFangJingXuan() throws Exception{
		db=new Database();
		BufferedReader reader;
		String inputFilePath="E:\\worktest\\outputForFangZhiChangJianBinDanFangYanFangJingXuan.csv";
		File inputFile=new File(inputFilePath);
		reader=new BufferedReader(new InputStreamReader(new FileInputStream(inputFile),"GBK"));
		String tempString;
		String treat="";
		int i=0;
		int id=0;
		try {
			db.init("10.15.62.29:3306/pianfangyanfang?user=root&password=123&useUnicode=true&characterEncoding=UTF8");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		while((tempString=reader.readLine())!=null){
			String tempTreat="";
			if(i==0){
				i=1;
				continue;
			}
			if(i==1){
				treat="感冒、流行性感冒";
				i=2;
				continue;
			}
			String[] part=tempString.split(",");
			String composition=part[0].trim().replaceAll("#", "");
			String usage=part[1];
			String note="";
			if(usage.contains("//")){
				tempTreat=DelBlank(usage.substring(usage.indexOf("//")+2,usage.indexOf("//")+usage.substring(usage.indexOf("//"),usage.length()).indexOf("##")));
				usage=usage.substring(0,usage.indexOf("//"));
			}
			if(usage.contains("##方"))
				usage=usage.substring(0,usage.indexOf("##方"));
			if(usage.contains("##（"))
				usage=usage.substring(0,usage.indexOf("##（"));
			if(usage.contains("注")){
				note=usage.substring(usage.indexOf("注")+2,usage.length()).trim();
				usage=usage.substring(0,usage.indexOf("注")-2);
			}
			if(usage.contains("附")){
				note=usage.substring(usage.indexOf("附")+2,usage.length()).trim();
				usage=usage.substring(0,usage.indexOf("附")-2);
			}
			if(usage.contains("##"))
				usage=usage.substring(0,usage.indexOf("##"));
			System.out.println(id+usage);
			
			String SQL="insert into 防治常见病单方验方精选 (id,composition,usages,treat,note,book) values ("+id+",'"+composition+"','"+usage+"','"+treat+"','"+note+"','防治常见病单方验方精选')";
			System.out.println(SQL);
    		db.SQLUpdate(SQL);
			if(!tempTreat.equals("")){
				treat=tempTreat;
			}
	
			id=id+1;
		}
		db.end();
	}
	public static void redealChangJianBinDanFangYanFang() throws Exception{
		db=new Database();
		BufferedReader reader;
		String inputFilePath="E:\\worktest\\outputForChangJianBinDanFangYanFang.csv";
		File inputFile=new File(inputFilePath);
		reader=new BufferedReader(new InputStreamReader(new FileInputStream(inputFile),"GBK"));
		String tempString;
		String treat="";
		int i=0;
		int id=0;
		try {
			db.init("10.15.62.29:3306/pianfangyanfang?user=root&password=123&useUnicode=true&characterEncoding=UTF8");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		while((tempString=reader.readLine())!=null){
			String tempTreat="";
			if(i==0){
				i=1;
				continue;
			}
			if(i==1){
				treat="风寒感冒";
				i=2;
				continue;
			}
			String[] part=tempString.split(",");
			String composition=part[0];
			composition=composition.substring(composition.indexOf("】")+1, composition.length());
			composition=s2(composition.trim());
			String usage=part[1];
			if(usage.contains("//")){
				tempTreat=DelBlank(usage.substring(usage.indexOf("//")+2, usage.indexOf("##")));
				usage=usage.substring(0,usage.indexOf("//"));
			}
			usage=usage.trim().replaceAll("#", "");
			System.out.println(usage);

			
			String SQL="insert into 常见病单方·验方 (id,composition,usages,treat,book) values ("+id+",'"+composition+"','"+usage+"','"+treat+"','常见病单方·验方')";
			System.out.println(SQL);
    		db.SQLUpdate(SQL);
			if(!tempTreat.equals("")){
				treat=tempTreat;
			}
			
			id=id+1;
		}
		db.end();
	}
	public static void redealZhuYanYouShuPianYanFang() throws Exception{
		db=new Database();
		BufferedReader reader;
		String inputFilePath="E:\\worktest\\outputForZhuYanYouShuPianYanFang.csv";
		File inputFile=new File(inputFilePath);
		reader=new BufferedReader(new InputStreamReader(new FileInputStream(inputFile),"GBK"));
		String tempString;
		int i=0;
		int id=0;
		while((tempString=reader.readLine())!=null){
			if(i==0){
				i=1;
				continue;
			}
			String[] part=tempString.split(",");
			String name=DelBlank(part[0].trim());
			String source=part[1].trim();
			String function=part[2].trim();
			String composition=part[3].trim();
			String preparation=part[4].trim();
			String bingli="";
			String note="";
			String attention="";
			String usage="";
			String jiajian="";
			if(preparation.contains("例")){
				bingli=preparation.substring(preparation.indexOf("例")+2,preparation.length());
				preparation=preparation.substring(0,preparation.indexOf("例"));
			}
			if(preparation.contains("按")){
				note=preparation.substring(preparation.indexOf("按")+2,preparation.length());
				note=note.replace("#", "");
				preparation=preparation.substring(0,preparation.indexOf("按"));
			}
			if(preparation.contains("禁忌")){
				attention=preparation.substring(preparation.indexOf("禁忌")+3,preparation.length());
				attention=attention.replace("#", "");
				preparation=preparation.substring(0,preparation.indexOf("禁忌"));
			}
			if(preparation.contains("用法")){
				usage=preparation.substring(preparation.indexOf("用法")+3,preparation.length());
				usage=usage.replace("#", "");
				preparation=preparation.substring(0,preparation.indexOf("用法"));
			}
			if(preparation.contains("加减")){
				jiajian=preparation.substring(preparation.indexOf("加减")+3,preparation.length());
				jiajian=jiajian.replace("#", "");
				preparation=preparation.substring(0,preparation.indexOf("加减"));
			}
			preparation=preparation.replaceAll("#", "");
			System.out.println(id+preparation);
		
			try {
				db.init("10.15.62.29:3306/pianfangyanfang?user=root&password=123&useUnicode=true&characterEncoding=UTF8");
			} catch (SQLException e) {
				e.printStackTrace();
			}
			String SQL="insert into 驻颜有术偏验方 (id,name,composition,function,usages,note,attention,preparation,bingli,jiajian,source,book) values ("+id+",'"+name+"','"+composition+"','"+function+"','"+usage+"','"+note+"','"+attention+"','"+preparation+"','"+bingli+"','"+jiajian+"','"+source+"','驻颜有术偏验方 ')";
			System.out.println(SQL);
    		db.SQLUpdate(SQL);
    		
			id=id+1;
		}
	}
	public static void redealNongCunZhongYiDanYanFang500() throws Exception{
		db=new Database();
		BufferedReader reader;
		String inputFilePath="E:\\worktest\\outputForNongCunZhongYiDanYanFang500.csv";
		File inputFile=new File(inputFilePath);
		reader=new BufferedReader(new InputStreamReader(new FileInputStream(inputFile),"GBK"));
		int i=0;
		int id=0;
		String tempString;
		while((tempString=reader.readLine())!=null){
			if(i==0){
				i=1;
				continue;
			}
			if(i==1){
				i=2;
				continue;
			}
			String[] part=tempString.split(",");
			String composition=part[0];
			composition=composition.substring(composition.indexOf(" ")+1,composition.length());
			composition=s2(composition);
			String treat=s2(part[1].trim());
			String function=s2(part[2].trim());
			String usage=part[3];
			usage=s2(usage.trim());
			if(usage.contains("//"))
				usage=usage.substring(0, usage.indexOf("//"));
			//System.out.println(usage);
			
			try {
				db.init("10.15.62.29:3306/pianfangyanfang?user=root&password=123&useUnicode=true&characterEncoding=UTF8");
			} catch (SQLException e) {
				e.printStackTrace();
			}
			String SQL="insert into 农村中医单验方500首 (id,composition,usages,treat,function,book) values ("+id+",'"+composition+"','"+usage+"','"+treat+"','"+function+"','农村中医单验方500首')";
			System.out.println(SQL);
    		db.SQLUpdate(SQL);
			
			id=id+1;
		}
	}
	public static void redealZhongCaoYaoDanFangYanFangXuanBian() throws Exception{
		db=new Database();
		BufferedReader reader;
		String inputFilePath="E:\\worktest\\outputForZhongCaoYaoDanFangYanFangXuanBian.csv";
		File inputFile=new File(inputFilePath);
		reader=new BufferedReader(new InputStreamReader(new FileInputStream(inputFile),"GBK"));
		String tempString;
		String treat="";
		int i=0;
		int id=0;
		while((tempString=reader.readLine())!=null){
			String tempTreat="";
			if(i==0){
				i=1;
				continue;
			}
			if(i==1){
				String[] part=tempString.split(",");
				treat=part[1].substring(part[1].indexOf("//")+2, part[1].indexOf("##"));
				i=2;
				continue;
			}
			String[] part=tempString.split(",");
			String composition=part[0];
			String preparation="";
			if(composition.contains("制法")){
				preparation=composition.substring(composition.indexOf("制法")+2, composition.length()-2);
				preparation=preparation.trim();
				composition=composition.substring(0,composition.indexOf("制法"));
			}
			composition=composition.replaceAll("#", "");
			String usage=part[1];
			String attention="";
			String note="";
			String Linchuangyingyong="";
			String[] nextList={"（一）","（二）","（三）","（四）","（五）","（六）","（七）","（八）","（九）","（十","（二十"};
			for(int j=0;j<nextList.length;j++){
				if(usage.contains(nextList[j])){
					usage=usage.substring(0, usage.indexOf(nextList[j]));
					break;
				}
			}
			if(usage.contains("【")){
				usage=usage.substring(0, usage.indexOf("【"));
			}
			usage=s2(usage);
			
			if(usage.contains("//")){
				tempTreat=usage.substring(usage.lastIndexOf("//")+2, usage.length());
				tempTreat=DelBlank(tempTreat.trim());
				
				usage=usage.substring(0, usage.indexOf("//"));
			}
			if(usage.contains("／／")){
				tempTreat=usage.substring(usage.lastIndexOf("／／")+2, usage.length());
				tempTreat=DelBlank(tempTreat.trim());
				usage=usage.substring(0, usage.indexOf("／／"));
			}
			if(usage.contains("禁忌")){
				attention=usage.substring(usage.lastIndexOf("禁忌")+2, usage.length());
				attention=attention.trim();
				usage=usage.substring(0, usage.indexOf("禁忌")-2);
			}
			if(usage.contains("说明")){
				note=usage.substring(usage.lastIndexOf("说明")+2, usage.length());
				note=Linchuangyingyong.trim();
				usage=usage.substring(0, usage.indexOf("说明")-2);
			}
			if(usage.contains("疗效")){
				Linchuangyingyong=usage.substring(usage.lastIndexOf("疗效")+2, usage.length());
				Linchuangyingyong=Linchuangyingyong.trim();
				usage=usage.substring(0, usage.indexOf("疗效")-2);
			}
			
			if(usage.contains("##注意")){
				attention=attention+usage.substring(usage.lastIndexOf("##注意")+4, usage.length()).trim();
				usage=usage.substring(0, usage.indexOf("##注意"));
			}
			usage=usage.replaceAll("#", "");
			System.out.println(usage);
			//System.out.println(id+composition+"||||||"+preparation);
			
			try {
				db.init("10.15.62.29:3306/pianfangyanfang?user=root&password=123&useUnicode=true&characterEncoding=UTF8");
			} catch (SQLException e) {
				e.printStackTrace();
			}
			String SQL="insert into 中草药单方验方选编 (id,composition,preparation,usages,treat,note,linchuangyingyong,attention,book) values ("+id+",'"+composition+"','"+preparation+"','"+usage+"','"+treat+"','"+note+"','"+Linchuangyingyong+"','"+attention+"','中草药单方验方选编')";
			System.out.println(SQL);
    		db.SQLUpdate(SQL);
			
			if(!tempTreat.equals("")){
				treat=tempTreat;
			}
			id=id+1;
		}
	}
	public static void redealWeiChangBinYanFang() throws Exception{
		db=new Database();
		BufferedReader reader;
		String inputFilePath="E:\\worktest\\outputForWeiChangBinYanFang.csv";
		File inputFile=new File(inputFilePath);
		reader=new BufferedReader(new InputStreamReader(new FileInputStream(inputFile),"GBK"));
		String tempString;
		int i=0;
		int id=0;
		while((tempString=reader.readLine())!=null){
			if(i==0){
				i=1;
				continue;
			}
			String[] part=tempString.split(",");
			String name=part[0].trim();
			name=name.substring(0, name.length()-2);
			if(name.contains("##")){
				name=name.substring(name.indexOf("##")+2,name.length());
			}
			if(name.contains("//")){
				name=name.substring(name.indexOf("//")+2,name.length());
			}
			name=DelBlank(name);
			String composition=part[1].trim();
			String jiajian="";
			if(composition.contains("加减")){
				jiajian=composition.substring(composition.indexOf("加减")+3, composition.length()-2).trim();
				composition=composition.substring(0,composition.indexOf("加减"));
			}
			composition=s2(composition).trim();
			String treat=s2(part[2]).trim();
			String usage=s2(part[3]).trim();
			String Linchuangyingyong=part[4].trim();
			String bingli="";
			if(Linchuangyingyong.contains("病案举例")){
				bingli=Linchuangyingyong.substring(Linchuangyingyong.indexOf("病案举例")+5, Linchuangyingyong.length()-2).trim();
				bingli=bingli.trim();
				Linchuangyingyong=Linchuangyingyong.substring(0,Linchuangyingyong.indexOf("病案举例")-1);
			}
			Linchuangyingyong=s2(Linchuangyingyong);
			String source=part[5].trim();

			String note="";
			if(source.contains("按：")){
				note=source.substring(source.indexOf("按：")+2, source.length()-2);
				note=note.trim();
				source=source.substring(0,source.indexOf("按："));
			}
			
			source=s2(source);
			System.out.println(source);
			
			try {
				db.init("10.15.62.29:3306/pianfangyanfang?user=root&password=123&useUnicode=true&characterEncoding=UTF8");
			} catch (SQLException e) {
				e.printStackTrace();
			}
			String SQL="insert into 胃肠病验方 (id,name,composition,jiajian,usages,treat,note,linchuangyingyong,bingli,source,book) values ("+id+",'"+name+"','"+composition+"','"+jiajian+"','"+usage+"','"+treat+"','"+note+"','"+Linchuangyingyong+"','"+bingli+"','"+source+"','胃肠病验方')";
			System.out.println(SQL);
    		db.SQLUpdate(SQL);
			
			
			id=id+1;
		}
	}
	public static void redealZhiAiYanFang400() throws Exception{
		db=new Database();
		BufferedReader reader;
		String inputFilePath="E:\\worktest\\outputForZhiAiYanFang.csv";
		File inputFile=new File(inputFilePath);
		reader=new BufferedReader(new InputStreamReader(new FileInputStream(inputFile),"GBK"));
		String tempString;
		int i=0;
		int id=0;
		while((tempString=reader.readLine())!=null){
			if(i==0){
				i=1;
				continue;
			}
			String[] part=tempString.split(",");
			String treat=part[0].substring(part[0].indexOf("章")+2, part[0].length());
			treat=DelBlank(treat);
			String name=part[1].trim();
			String source=part[2].trim();
			String usages=part[3].trim();
			String composition="";
			String[] wordList={"熬成","上药","水煎","为细末","共为细末","共研","逐日","每日","每用","搽敷","麻油熬","制成","隔汤","隔水","徐徐","任意食之","上2味","乘热","于1日内","煨熟","放温和","水适量","不拘","共煮","更煎","共捣","熬膏","米酒或开水","研细末"};
			boolean flag=false;
			for(int j=0;j<wordList.length;j++){
				if(usages.indexOf(wordList[j])!=-1){
					flag=true;
					composition=usages.substring(0,usages.indexOf(wordList[j]));
					usages=usages.substring(usages.indexOf(wordList[j]), usages.length());
					usages=usages.trim();
					break;
				}
			}
			if(flag==false){
				if(usages.indexOf("克")!=-1){
					composition=usages.substring(0,usages.lastIndexOf("克")+1);
					usages=usages.substring(usages.lastIndexOf("克")+1, usages.length());
					usages=usages.trim();
				}
				else{
					composition=usages;
					usages="";
				}
			}
			String function=part[4].trim();
			String note="";
			String attention="";
			if(function.indexOf("##［处方来源］")!=-1){
				function=function.substring(0,function.indexOf("##［处方来源］"));
			}
			if(function.indexOf("##［附注］")!=-1){
				note=function.substring(function.indexOf("##［附注］")+"##［附注］".length(), function.length());
				function=function.substring(0,function.indexOf("##［附注］"));
			}
			if(function.indexOf("##［注意］")!=-1){
				attention=function.substring(function.indexOf("##［注意］")+"##［注意］".length(), function.length());
				function=function.substring(0,function.indexOf("##［注意］"));
			}

			//System.out.println(note);
			try {
				db.init("10.15.62.29:3306/pianfangyanfang?user=root&password=123&useUnicode=true&characterEncoding=UTF8");
			} catch (SQLException e) {
				e.printStackTrace();
			}
			String SQL="insert into 治癌验方400 (id,name,composition,function,usages,treat,note,attention,source,book) values ("+id+",'"+name+"','"+composition+"','"+function+"','"+usages+"','"+treat+"','"+note+"','"+attention+"','"+source+"','治癌验方400')";
			System.out.println(SQL);
    		db.SQLUpdate(SQL);
			
			id=id+1;
		}
	}
	public static String DelBlank(String s){
		String news="";
		for(int i=0;i<s.length();i++){
			if(!s.substring(i,i+1).equals(" ")){ 
				news=news+s.substring(i,i+1);
			}
		}
		return news;
	}
	public static String s2(String s){
		s=s.substring(0,s.length()-2);
		return s;
	}
}

