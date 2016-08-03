package patent;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PatentCollect {
	private static String connectionURL;
	private static Connection conn = null;
	private static Statement stmt = null;
	private static Statement stmt2 = null;
	private static Statement stmt3 = null;
	private static int id = 1;

	public static void main(String[] args) throws SQLException, IOException {
		filter();
	}

	public static void filter() throws SQLException, IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(
				new FileInputStream("E://dic//herb.txt"), "GBK"));
		List<String> dicList = new ArrayList<String>();
		String line;
		while ((line = br.readLine()) != null) {
			dicList.add(line);
		}
		br.close();
		connectionURL = "jdbc:mysql://localhost:3306/tcm?user=root&password=123456&useUnicode=true&characterEncoding=UTF8";
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(connectionURL);
			System.out.println("connect success");
			stmt = conn.createStatement();
			stmt2 = conn.createStatement();
			stmt3 = conn.createStatement();
			// 插入数据库
			for (int i = 2; i < 96; i++) {
				System.out.println(i);
				String sql = "select * from patent_all  limit " + i * 10000
						+ ", 10000";
				ResultSet rs = stmt.executeQuery(sql);
				while (rs.next()) {
					System.out.println(rs.getString("pub_no"));
					for (String dic : dicList) {
						if (rs.getString("summary").contains(dic)) {
							String sql2 = "select * from patent_filter where pub_no='"
									+ rs.getString("pub_no") + "'";
							ResultSet rs2 = stmt2.executeQuery(sql2);
							if (!rs2.next()) {
								String updateSql = "insert ignore into patent_filter (id,old_id,url,app_no,date,patent_name,pub_no,pub_date,category,origin_no,class_no,issue_date,priority,applier,address,authors,int_patent,int_pub,int_date,agency,agent,summary,domain) values ('"
										+ id
										+ "','"
										+ rs.getInt(1)
										+ "','"
										+ rs.getString(2)
										+ "','"
										+ rs.getString(3)
										+ "','"
										+ rs.getString(4)
										+ "','"
										+ rs.getString(5)
										+ "','"
										+ rs.getString(6)
										+ "','"
										+ rs.getString(7)
										+ "','"
										+ rs.getString(8)
										+ "','"
										+ rs.getString(9)
										+ "','"
										+ rs.getString(10)
										+ "','"
										+ rs.getString(11)
										+ "','"
										+ rs.getString(12)
										+ "','"
										+ rs.getString(13)
										+ "','"
										+ rs.getString(14)
										+ "','"
										+ rs.getString(15).replaceAll("'", "·")
										+ "','"
										+ rs.getString(16)
										+ "','"
										+ rs.getString(17)
										+ "','"
										+ rs.getString(18)
										+ "','"
										+ rs.getString(19)
										+ "','"
										+ rs.getString(20)
										+ "','"
										+ rs.getString(21)
										+ "','"
										+ rs.getString(22) + "')";
								System.out.println(updateSql);
								stmt3.executeUpdate(updateSql);
								id++;
							}
							break;
						}
					}
				}
			}
		} catch (Exception e) {
			System.out.println("connection error");
			e.printStackTrace();
		} finally {
			if (stmt != null)
				stmt.close();
			if (stmt2 != null)
				stmt2.close();
			if (stmt3 != null)
				stmt3.close();
			if (conn != null) {
				conn.close();
				System.out.println("connection closed");
			}
		}
	}

	public static void merge() throws SQLException {
		connectionURL = "jdbc:mysql://localhost:3306/tcm?user=root&password=123456&useUnicode=true&characterEncoding=UTF8";
		try {
			long a = System.currentTimeMillis();
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(connectionURL);
			System.out.println("connect success");
			stmt = conn.createStatement();
			stmt2 = conn.createStatement();

			// 插入数据库
			for (int i = 0; i < 29; i++) {
				String sql = "select * from patent_copy2  limit " + i * 10000
						+ ", 10000";
				ResultSet rs = stmt.executeQuery(sql);
				while (rs.next()) {
					String sql2 = "select * from patent_all where pub_no='"
							+ rs.getString("pub_no") + "'";
					stmt3 = conn.createStatement();
					ResultSet rs2 = stmt3.executeQuery(sql2);
					if (!rs2.next()) {
						String updateSql = "insert ignore into patent_all (id,url,app_no,date,patent_name,pub_no,pub_date,category,origin_no,class_no,issue_date,priority,applier,address,authors,int_patent,int_pub,int_date,agency,agent,summary,domain) values ('"
								+ id
								+ "','"
								+ rs.getString(2)
								+ "','"
								+ rs.getString(3)
								+ "','"
								+ rs.getString(4)
								+ "','"
								+ rs.getString(5)
								+ "','"
								+ rs.getString(6)
								+ "','"
								+ rs.getString(7)
								+ "','"
								+ rs.getString(8)
								+ "','"
								+ rs.getString(9)
								+ "','"
								+ rs.getString(10)
								+ "','"
								+ rs.getString(11)
								+ "','"
								+ rs.getString(12)
								+ "','"
								+ rs.getString(13)
								+ "','"
								+ rs.getString(14)
								+ "','"
								+ rs.getString(15)
								+ "','"
								+ rs.getString(16)
								+ "','"
								+ rs.getString(17)
								+ "','"
								+ rs.getString(18)
								+ "','"
								+ rs.getString(19)
								+ "','"
								+ rs.getString(20)
								+ "','"
								+ rs.getString(21)
								+ "','"
								+ rs.getString(22)
								+ "')";
						System.out.println(updateSql);
						stmt2.executeUpdate(updateSql);
						id++;
					}
				}
			}
			long b = System.currentTimeMillis();
			System.out.println("耗时： " + (b - a) / 60000 + "分钟");
		} catch (Exception e) {
			System.out.println("connection error");
			e.printStackTrace();
		} finally {
			if (stmt != null)
				stmt.close();
			if (stmt2 != null)
				stmt2.close();
			if (stmt3 != null)
				stmt3.close();
			if (conn != null) {
				conn.close();
				System.out.println("connection closed");
			}
		}
	}
}
