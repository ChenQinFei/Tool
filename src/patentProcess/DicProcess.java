package patentProcess;
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
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DicProcess {
	private static String connectionURL;
	private static Connection conn = null;
	private static Statement stmt = null;

	public static void main(String[] args) throws SQLException, IOException {
		// TODO Auto-generated method stub
		dicDatabase();
		// dicIntegrate();

	}

	// 获取数据库中 词表
	public static void dicDatabase() throws UnsupportedEncodingException,
			FileNotFoundException, SQLException {
		// File outputFileName = new File("E://plant_basic.txt");
		File outputFileName = new File("E://bookName.csv");
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(outputFileName), "GBK"));
		connectionURL = "jdbc:mysql://10.15.82.58:3306/tcm?user=root&password=123&useUnicode=true&characterEncoding=UTF8";
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(connectionURL);
			System.out.println("connect success");
			stmt = conn.createStatement();
			// String sql =
			// "select plant_name_zh from plant_basic where med_id is not null and med_id != 0";
			String sql = "select pre_simple_book from prescription";
			ResultSet rs = stmt.executeQuery(sql);
			Set<String> bookSet = new HashSet<String>();
			while (rs.next()) {
				String r = rs.getString("pre_simple_book");
				if (!bookSet.contains(r)) {
					bookSet.add(r);
					writer.write(r + "\r\n");
				}
			}
		} catch (Exception e) {
			System.out.println("connection error");
			e.printStackTrace();
		} finally {
			if (stmt != null)
				stmt.close();
			if (conn != null) {
				conn.close();
				System.out.println("connection closed");
			}
		}
	}

	// 讲不同词表合在一起
	public static void dicIntegrate() throws IOException {
		File outputFileName = new File("E://dic_todo.txt");
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(outputFileName, true), "GBK"));
		// 待处理词表
		File inputFileName = new File("E://plant_basic.txt");
		// File inputFileName = new File("E://med_basic.txt");
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				new FileInputStream(inputFileName), "GBK"));
		// 已爬取词表
		File inputFileName1 = new File("E://patent_already.txt");
		BufferedReader reader1 = new BufferedReader(new InputStreamReader(
				new FileInputStream(inputFileName1), "GBK"));
		// 存储词表
		List<String> dict = new ArrayList<String>();
		Set<String> dictAlready = new HashSet<String>();
		String line;
		while ((line = reader.readLine()) != null) {
			dict.add(line);
		}
		while ((line = reader1.readLine()) != null) {
			dictAlready.add(line.split(",")[0]);
		}
		reader.close();
		reader1.close();
		for (String s : dict) {
			if (!dictAlready.contains(s)) {
				writer.write(s + "\r\n");
			}
		}
		writer.close();

	}
}
