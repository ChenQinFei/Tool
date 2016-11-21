package bookProcess;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class dataReprocess {
	private static String connectionURL = "jdbc:mysql://10.15.62.29:3306/bieming?user=root&password=123&useUnicode=true&characterEncoding=UTF8";
	private static Connection conn = null;
	private static Statement st =null;
	private static Statement st2 =null;
	private static int id = 0;
	public static void main(String[] args) throws SQLException, IOException{
//		merge();
//		filter();
		export();
	}
	public static void filter() throws SQLException{
		try{
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(connectionURL);
		} catch(Exception e){
			System.out.println("connection error");
			e.printStackTrace();
		}
		try {
			st = conn.createStatement();
			st2 = conn.createStatement();
			String sql = "select * from bieming_all";
			ResultSet rs = st.executeQuery(sql);
			while(rs.next()){
				String book = rs.getString("book");
				if(book.equals("null") || book.equals("") || book == null){
					continue;
				}
				String source = rs.getString("source");
				if(source.equals("null") || source.equals("") || source == null){
					source = book;
				}
//				if(!book.contains("《")){
//					book = "《" + book +"》";
//				}
//				if(!source.contains("《")){
//					source = "《" + source + "》";
//				}
				String bieming = rs.getString("bieming").replace(",","、").replace("。","").replace("，", "、");
				if(bieming.equals("null") || bieming.equals("") || bieming == null) 
					continue;
				System.out.println(bieming);
				String sql2 = "insert into bieming_all_filter (id, name, source, bieming, book) values ("
						+id+",'"+rs.getString("name")+"','"+source+"','"+bieming+"','"+book+"')";
				System.out.println(sql2);
				st2.executeUpdate(sql2);
				id++;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			if(conn != null){
				conn.close();
			}
			if(st != null){
				st.close();
			}
			if(st2 != null){
				st2.close();
			}
		}
		
	}
	public static void merge() throws SQLException{
		try{
			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("connection success");
			conn = DriverManager.getConnection(connectionURL);
		}catch(Exception e){
			System.out.println("connection error");
			e.printStackTrace();
		}
		try{
			st = conn.createStatement();
			String sql = "select name, source, bieming, book from 中药别名大辞典";
//			String sql = "select name, source, bieming, book from 常用中药别名速查手册";
//			String sql = "select name, source, bieming, book from 中药标准名与别名速查";
//			String sql = "select med_name_zh, zhailu, med_name_alias from quanguozhongcaoyaohuibian";
//			String sql = "select med_name_zh, zhailu, med_name_alias from zhongguoyaodian05";
//			String sql = "select med_name_zh, zhailu, med_name_alias from zhonghuabencao";
//			String sql = "select med_name_zh,chuchu, med_name_alias from zhongyaodacidian";
			ResultSet rs= st.executeQuery(sql);
			st2 = conn.createStatement();
			while(rs.next()){
				String source ="《中药别名大辞典》";
				String chuchu = rs.getString("source");
//				if( chuchu == null || chuchu.equals("null") || chuchu.equals("") ){
//					chuchu = source;
//				}
				if(chuchu.contains("《")){
					chuchu = chuchu.substring(chuchu.indexOf("《"),chuchu.indexOf("》")+1);
				} else{
					chuchu = source;
				}
				String sql2 = "insert into bieming_all(id, name, source, bieming, book) values("
							+id+",'"+rs.getString("name")+"','"+chuchu+"','"+rs.getString("bieming")+"','"+source+"')";
				System.out.println(sql2);
				st2.executeUpdate(sql2);
				id++;
			}
		} catch(Exception e){
			e.printStackTrace();
		} finally{
			if(conn != null){
				conn.close();
			}
			if(st != null){
				st.close();
			}
			if(st2 != null){
				st2.close();
			}
		}
	}

	//导出别名与标准名词表
	public static void export() throws SQLException, IOException{
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("bieming"),true)));
		try{
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(connectionURL);
		} catch(Exception e){
			System.out.println("connection error");
			e.printStackTrace();
		}
		String sql = "select * from bieming_all limit 1000 offset ?";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		Set<String> herb = new HashSet<String>();
		int index = 0;
		while(index < 20){
			pstmt.setInt(1, index*1000);
			index++;
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()){
				String name = rs.getString("name").replace("(","（").trim();
				if(name.contains("（")){
					name = name.substring(0, name.indexOf("（")).trim();
				}
				if(!herb.contains(name) && name != "" && name !="null"){
					herb.add(name);
				}
				String bieming = rs.getString("bieming").replace("。","").replace("，", "、").replace("[", "（").replace("(","（").replace("《", "（");
				String[] part = bieming.split("、");
				for(int i = 0; i < part.length; i++){
					part[i] = part[i].trim();
					if(part[i].contains("（")){
						part[i] = part[i].substring(0, part[i].indexOf("（")).trim();
					}
					if(part[i].contains("(")){
						part[i] = part[i].substring(0, part[i].indexOf("(")).trim();
					}
					System.out.println(part[i]);
					if(!herb.contains(part[i]) && part[i] != "" && part[i] != "null"){
						herb.add(part[i]);
					}
				}
				
			}
			rs.close();
		}
		Iterator<String> it = herb.iterator();
			while(it.hasNext()){
				String str = it.next();
				bw.write(str + "\r\n");
			}
			bw.close();
			pstmt.close();
			conn.close();
	}

}
