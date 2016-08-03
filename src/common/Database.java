package common;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
	private String connectionURL;
	private Connection conn = null;

	public void init(String url) throws SQLException {
		connectionURL = "jdbc:mysql://" + url
				+ "&useUnicode=true&characterEncoding=UTF8";
		try {
			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("connection success");
			conn = DriverManager.getConnection(connectionURL);
		} catch (Exception e) {
			System.out.println("connection error");
			e.printStackTrace();
		}
	}

	public void end() throws SQLException {
		if (conn != null) { // 关闭连接对象
			try {
				conn.close();
				System.out.println("connection closed");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public ResultSet SQLQuery(String sql) throws SQLException {
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		stmt.close();
		return rs;
	}

	public void SQLUpdate(String sql) throws SQLException {
		Statement stmt = conn.createStatement();
		stmt.executeUpdate(sql);
		stmt.close();
	}
}