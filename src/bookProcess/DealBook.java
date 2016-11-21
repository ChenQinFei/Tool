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
import java.io.Writer;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import common.Database;
public class DealBook{
	private static Database db;
	public static void main(String args[]) throws IOException, Exception{
//		SecondProcessChangYongZhongYao();
//				SecondProcessZhaoSuCha();
//		SecondProcessDaCiDian();
//		uploadData();
//		processBaike();
		processQian();
	}
	public static void SecondProcessDaCiDian() throws IOException{
		String inputFilePath="E:\\processData\\Output\\中药别名大辞典.csv";
		File inputFile=new File(inputFilePath);
		BufferedReader reader=new BufferedReader(new InputStreamReader(new FileInputStream(inputFile),"GBK"));
		//指定输出文件
		String outputFileName="E:\\processData\\Output\\secondPro\\中药别名大辞典re2.csv";
		File outputFile=new File(outputFileName);
		BufferedWriter writer=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile), "GBK"));
		String tempString;
		int i=0;
		try {
			while((tempString=reader.readLine())!=null){
				if(i==0){
					writer.write("标准名,来源,别名,商品名,处方名,注意,性味归经,功效主治,用量,慎忌,附药\r\n");
					i=1;
					continue;
				}
				String[] part=tempString.split(",");
				String name=part[0].trim();
				String sj = " "; 
				String fy = " ";
				if (name.contains("慎忌")){
					sj = name.substring(name.indexOf("慎忌")+3, name.indexOf("//")).trim();
				}
				if(name.contains("//")){
					if(name.substring(0, name.lastIndexOf("//")).contains("附：")){
						fy = name.substring(name.indexOf("附：")+2,name.lastIndexOf("//")).trim();
					}
					name = name.substring(name.lastIndexOf("//")+2, name.length());
				}
				writer.write(","+sj+","+fy+"\r\n");
				name = name.replace(" ", "");
				if(name.contains("附：")){
					name = name.substring(0, name.indexOf("（")).trim();
				}
				System.out.println(name);
				String ly = part[1].trim();
				ly = ly.substring(ly.indexOf("##")+2, ly.indexOf("//")).trim();
				
				String bm = part[2].trim();
				System.out.println(bm);
				bm = bm.substring(bm.indexOf("名")+2, bm.lastIndexOf("//")).trim();
				
				String spm = part[3].trim();
				spm = spm.substring(spm.indexOf("名")+2, spm.lastIndexOf("//")).trim();
				
				String cfm = part[4].trim();
				String zy = "";
				if(cfm.contains("【注意】")){
					zy = cfm.substring(cfm.indexOf("【注意】")+4, cfm.lastIndexOf("//")).trim();
					cfm = cfm.substring(0, cfm.indexOf("【注意】"));
				}
				cfm = cfm.substring(cfm.indexOf("名")+2, cfm.lastIndexOf("//")).trim();
				
				String xwgj = part[5].trim();
				xwgj = xwgj.substring(xwgj.indexOf("经")+2, xwgj.lastIndexOf("//")).trim();
		
				String gx = part[6].trim();
				gx = gx.substring(gx.indexOf("治")+2, gx.lastIndexOf("//")).trim();
				
				String yl = part[7].trim();
				yl = yl.substring(yl.indexOf("【用量】")+4, yl.length()).trim();
				
				writer.write(name + "," + ly + "," + bm + "," + spm +"," + cfm + "," + zy + "," + xwgj + "," + gx + "," + yl);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		reader.close();
		writer.close();
	}
	public static void SecondProcessZhaoSuCha() throws IOException{
		String inputFilePath="E:\\processData\\Output\\中药标准名与别名速查.csv";
		File inputFile=new File(inputFilePath);
		BufferedReader reader=new BufferedReader(new InputStreamReader(new FileInputStream(inputFile),"GBK"));
		//指定输出文件
		String outputFileName="E:\\processData\\Output\\secondPro\\中药标准名与别名速查re2.csv";
		File outputFile=new File(outputFileName);
		BufferedWriter writer=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile), "GBK"));
		String tempString;
		int i=0;
		String bihua="";
		try {
			while((tempString=reader.readLine())!=null){
				if(i==0){
					writer.write("标准名,笔画,来源,别名\r\n");
					i=1;
					continue;
				}
				String[] part=tempString.split(",");
				String name=part[0];
				name = name.replace("（", "(").replace("）", ")");
				System.out.println(name);
				if(name.contains("画")){
					bihua = name.substring(0, name.indexOf("画")+1);
					name = name.substring(name.indexOf("画")+1, name.length());
				}
				String source = name.substring(name.indexOf("(")+1, name.indexOf(")"));
				name = name.substring(0, name.indexOf("("));
				String bieming = part[1].replace("/","").trim();
				writer.write(name + "," + bihua + "," + source + "," + bieming + "\r\n");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		reader.close();
		writer.close();
	}
	public static void SecondProcessChangYongZhongYao() throws IOException {
		String inputFilePath="E:\\processData\\Output\\常用中药别名速查手册.csv";
		File inputFile=new File(inputFilePath);
		BufferedReader reader=new BufferedReader(new InputStreamReader(new FileInputStream(inputFile),"GBK"));
		//指定输出文件
		String outputFileName="E:\\processData\\Output\\secondPro\\常用中药别名速查手册re2.csv";
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
				String fuyao = " ";
				if(name.contains("章") && name.contains("第")){
					category = name.substring(name.indexOf("章")+1,name.indexOf("药",name.indexOf("章"))+1).trim();
					cateDetail="";
				}
				if(name.contains("节") && name.contains("第")){
					cateDetail = name.substring(name.indexOf("节")+1, name.indexOf("药", name.indexOf("节"))+1).trim();
				}
				if(name.contains("附药")){
					fuyao =  name.substring(name.indexOf("附药")+3, name.lastIndexOf("//")).replace("/","").replace("*","").trim();
				}
				writer.write(","+fuyao+"\r\n");
				System.out.println(name);
				name = name.split("[0-9]{3}")[1].replace("#","").trim();
				
				String function = part[1].trim();
				function = function.substring(0, function.length()-2).trim();
				
				String usages = part[2].trim();
				usages = usages.substring(0, usages.length()-2).trim();
				
				writer.write(name+","+category+","+cateDetail+","+function+","+usages);
				
				String bieming = part[3].trim();
				writer.write(","+bieming.substring(0,bieming.indexOf("。")));
				if(bieming.contains("附药")){
					writer.write("," + bieming.substring(bieming.indexOf("附药")+3, bieming.length()).trim());
				}
				if(bieming.contains("章") && bieming.contains("第")){
					category = bieming.substring(bieming.indexOf("章")+1,bieming.indexOf("药",bieming.indexOf("章"))+1).trim();
					cateDetail="";
				}
				if(bieming.contains("节") && bieming.contains("第")){
					cateDetail = bieming.substring(bieming.indexOf("节")+1, bieming.indexOf("药", bieming.indexOf("节"))+1).trim();
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		reader.close();
		writer.close();
	}
	public static void uploadData() throws SQLException, IOException{
		String connectionURL = "jdbc:mysql://10.15.62.29:3306/bieming?user=root&password=123&useUnicode=true&characterEncoding=UTF8";
		Connection conn = null;
		Statement stmt = null;
		String inputFile="E:\\processData\\Output\\secondPro\\中药标准名与别名速查re2.csv";
		BufferedReader reader=new BufferedReader(new InputStreamReader(new FileInputStream(inputFile),"GBK"));
		int id = 0;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(connectionURL);
			System.out.println("connect success");
			stmt = conn.createStatement();
			// String sql =
			// "select plant_name_zh from plant_basic where med_id is not null and med_id != 0";
			String line;
			int i = 0;
			while((line=reader.readLine()) != null){
				if(i == 0){
					i = 1;
					continue;
				}
				String[] part = line.split(",");
//				String sql = "insert into 常用中药别名速查手册 (id,name,category,cate_detail,function,usages,bieming,fuyao,book) values ("+id
//						+",'"+part[0]+"','"+part[1]+"','"+part[2]+"','"+part[3]+"','"+part[4]+"','"+part[5]+"','"+part[6]+"','常用中药别名速查手册')";
//				String sql = "insert into 中药别名大辞典 (id,name,category,cate_detail,source,bieming,shangpinming,chufangming,zhuyi,xingweiguijing,function,usages,shenji,fuyao,book) values("
//						   		+ id +",'"+part[0]+"','"+part[1]+"','"+part[2]+"','"+part[3]+"','"+part[4]+"','"+part[5]+"','"+part[6]+"','"+part[7]+"','"+part[8]+"','"+part[9]
//								+"','"+part[10]+"','"+part[11]+"','"+part[12]+"','中药别名大辞典')";
				String sql = "insert into 中药标准名与别名速查(id,name,bihua,source,bieming,book) values ("
								+id+",'"+part[0]+"','"+part[1]+"','"+part[2]+"','"+part[3]+"','中药标准名与别名速查')";
				System.out.println(sql);
				stmt.executeUpdate(sql);
				id++;
			}
		} catch(Exception e){
			System.out.println("connection error");
			e.printStackTrace();
		} finally{
			if(conn != null)
				conn.close();
			if(stmt != null)
				stmt.close();
			reader.close();
		}
	}
	//处理百科中别名数据
	public static void processBaike() throws IOException, SQLException{
		String connectionURL = "jdbc:mysql://10.15.62.29:3306/bieming?user=root&password=123&useUnicode=true&characterEncoding=UTF8";
		Connection conn = null;
		Statement stmt = null;
		String inputFile="E:\\processData\\baike.csv";
		BufferedReader reader=new BufferedReader(new InputStreamReader(new FileInputStream(inputFile),"GBK"));
		int id = 0;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(connectionURL);
			System.out.println("connect success");
			stmt = conn.createStatement();
			String line;
//			int i = 0;
			while((line=reader.readLine()) != null){
//				if(i == 0){
//					i = 1;
//					continue;
//				}
				String[] part = line.replace("\"","").split(",");
				if(part.length < 3 || !part[1].contains("别名")|| part[1].length() > 254){
					continue;
				}
				System.out.println(line);
				String sql = "insert ignore into baike(id,name,bieming,caijiyuan) values ("
								+id+",'"+part[0]+"','"+part[2]+"','百度百科')";
				System.out.println(sql);
				stmt.executeUpdate(sql);
				id++;
				}
			} catch(Exception e){
				System.out.println("connection error");
				e.printStackTrace();
			} finally{
				if(conn != null)
					conn.close();
				if(stmt != null)
					stmt.close();
				reader.close();
			}
		}

	//处理钱医生提供别名数据
	public static void processQian() throws IOException, SQLException{
		String connectionURL = "jdbc:mysql://10.15.62.29:3306/bieming?user=root&password=123&useUnicode=true&characterEncoding=UTF8";
		Connection conn = null;
		Statement stmt = null;
		String inputFile="E:\\processData\\qian.txt";
		BufferedReader reader=new BufferedReader(new InputStreamReader(new FileInputStream(inputFile),"GBK"));
		int id = 0;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(connectionURL);
			System.out.println("connect success");
			stmt = conn.createStatement();
			String line;
//			int i = 0;
			while((line=reader.readLine()) != null){
//				if(i == 0){
//					i = 1;
//					continue;
//				}
				String part = line.replace("。","").replaceAll("（|<","、").replaceAll("）|>","、").replace("、、","、").trim();
				int index = part.indexOf("、");
		
				String sql = "insert ignore into professor_qian(id,name,bieming,caijiyuan) values ("
								+id+",'"+part.substring(0, index)+"','"+part.substring(index+1)+"','专家-钱俊华')";
				System.out.println(sql);
				stmt.executeUpdate(sql);
				id++;
				}
			} catch(Exception e){
				System.out.println("connection error");
				e.printStackTrace();
			} finally{
				if(conn != null)
					conn.close();
				if(stmt != null)
					stmt.close();
				reader.close();
			}
		}

}

