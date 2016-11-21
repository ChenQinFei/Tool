package patentProcess;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import common.Database;

public class computeTotal {

	public static void main(String args[]) throws SQLException {
		Database db = new Database();
		try {
			db.init("10.15.62.29:3306/tcm?user=root&password=123");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ResultSet rs = null;
		int num;
		int tol = 0;
		String base = "TD27,005,";
		String SQL = "";
		String classCode = "";
		HashMap<String, String> funcClass = new HashMap<String, String>();
		for (num = 5; num <= 165; num = num + 5) {
			String numString = String.valueOf(num);
			String className = "";
			for (int j = numString.length(); j < 3; j++) {
				numString = "0" + numString;
			}
			classCode = base + numString;
			SQL = "select name from dic_word where category like '%"
					+ classCode + ",%'and source='《中医药主题词表》'";
			rs = db.SQLQuery(SQL);
			while (rs.next()) {
				className = rs.getString("name");
				System.out.println(className);
				tol++;
			}
			funcClass.put(className, classCode);
		}
		System.out.print(tol);
	}
}
