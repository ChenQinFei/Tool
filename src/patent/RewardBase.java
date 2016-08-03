package patent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;

import common.Database;

public class RewardBase {

	// final static String url =
	// "http://115.236.19.70:18180/metaApi/queryReward";
	final static String url = "http://115.236.19.70:18180/metaApi/queryPatent";
	// final static String params = "{\"org\": \"浙江大学\",\"size\":1,\"from\":1}";
	private static Database db;

	// private static int id = 1;

	public static String post(String strURL, String params) throws Exception {
		// System.out.println(strURL);
		BufferedReader in = null;
		// System.out.println(params);
		try {
			URL url = new URL(strURL);// 创建连接
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setConnectTimeout(300000);
			connection.setReadTimeout(300000);
			connection.setDoOutput(true);
			connection.setDoInput(true);
			// connection.setUseCaches(false);
			connection.setInstanceFollowRedirects(true);
			connection.setRequestMethod("POST"); // 设置请求方式
			// connection.setRequestProperty("Accept", "application/json"); //
			// 设置接收数据的格式
			connection.setRequestProperty("content-type", "application/json"); // 设置发送数据的格式
			connection.setRequestProperty("x-api-key",
					"fea32a73bd564ab7ba31dc7d102aff13");
			connection.connect();
			OutputStreamWriter out = new OutputStreamWriter(
					connection.getOutputStream(), "UTF-8"); // utf-8编码
			out.append(params);
			out.flush();
			out.close();
			// 读取响应
			Thread.sleep(1000);
			int temp = connection.getResponseCode();
			System.out.print(temp);
			if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
				StringBuffer content = new StringBuffer();
				String tempStr = "";
				in = new BufferedReader(new InputStreamReader(
						connection.getInputStream()));
				while ((tempStr = in.readLine()) != null) {
					content.append(tempStr);
				}
				return content.toString();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "error"; // 自定义错误信息
	}

	public static void dealJSON(String JSONString) throws SQLException {
		int index1 = find(JSONString, "}", 0);
		int index2 = find(JSONString, "}", index1 + 1);
		int start = 0;
		while (index1 != -1) {
			String dealString = JSONString.substring(start, index1);
			PatentJson o = new PatentJson(dealString);
			start = index1;
			index1 = index2;
			index2 = find(JSONString, "}", index1 + 1);
			String SQL = "insert ignore into patent_copy2(id,url,app_no,date,patent_name,pub_no,pub_date,category,origin_no,class_no,issue_date,priority,applier,address,authors,int_patent,int_pub,int_date,agency,agent,summary,domain) values ("
					+ o.id
					+ ",'"
					+ o.url
					+ "','"
					+ o.appNo
					+ "','"
					+ o.date
					+ "','"
					+ o.patentName
					+ "','"
					+ o.pubNo
					+ "','"
					+ o.pubDate
					+ "','"
					+ o.category
					+ "','"
					+ o.originNo
					+ "','"
					+ o.classNo
					+ "','"
					+ o.issueDate
					+ "','"
					+ o.priority
					+ "','"
					+ o.applier
					+ "','"
					+ o.address
					+ "','"
					+ o.authors
					+ "','"
					+ o.intPatent
					+ "','"
					+ o.intPub
					+ "','"
					+ o.intDate
					+ "','"
					+ o.agency
					+ "','"
					+ o.agent + "','" + o.summary + "','" + o.domain + "')";
			// System.out.println(SQL);
			db.SQLUpdate(SQL);
			// id++;
			// System.out.println(SQL);
		}
	}

	public static int find(String m, String f, int i) {
		int s = i;
		int flag = 0;
		while (s < m.length() - 1) {
			String t = m.substring(s, s + 1);
			if (flag == 0) {
				if (t.equals(f))
					return s;
				if (t.equals("\""))
					flag = 1;
			} else {
				if (t.equals("\""))
					flag = 0;
			}
			s = s + 1;
		}
		return -1;
	}

	public static void main(String[] args) throws Exception {
		db = new Database();
		try {
			// db.init("10.15.62.29:3306/tcm?user=root&password=123");
			db.init("localhost:3306/tcm?user=root&password=123456");
			// db.init("10.15.62.29:3306/tcm?user=root&password=123&useUnicode=true&characterEncoding=UTF8");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// String dealedFilePath="E:\\vocabulary.txt";
		String dealedFilePath = "E:\\dic_todo.txt";
		BufferedReader br = new BufferedReader(new InputStreamReader(
				new FileInputStream(new File(dealedFilePath)), "GBK"));
		/*
		 * FileReader fr=new FileReader(dealedFilePath); BufferedReader br=new
		 * BufferedReader(fr);
		 */
		String word;
		int num = 0;
		while ((word = br.readLine()) != null) {
			num = num + 1;

			if (word.length() == 1)
				continue;
			if (num < 5013)
				continue;
			System.out.println("num: " + num);
			System.out.println("word: " + word);
			// String word="三七";
			String params = "{\"keywords\":\"" + word
					+ "\",\"size\":10,\"from\":0}";
			// System.out.println(params);
			String m = post(url, params);
			if (m.equals("error")) {
				System.out
						.println("count: error!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
				continue;
			}
			// System.out.println(m);
			int countIndex = m.indexOf("count");
			int count = Integer
					.parseInt(m.substring(m.indexOf(":", countIndex) + 1,
							m.indexOf(",", countIndex)));
			System.out.println("count:" + count);
			int from = 0;
			while (from < count) {
				String newParams = "{\"keywords\":\"" + word
						+ "\",\"size\":10,\"from\":" + String.valueOf(from)
						+ "}";
				// System.out.println(newParams);
				String JSONString = post(url, newParams);
				JSONString = JSONString.replaceAll("'", "");
				// JSONString=JSONString.replaceAll("\"", "");
				JSONString = JSONString.replaceAll("\\\\", "");
				JSONString = JSONString.replaceAll("}ln}", "现");
				// System.out.println(JSONString);
				dealJSON(JSONString);
				from = from + 10;
			}
		}
		db.end();
	}

}

class PatentJson {
	public int id;
	public String url;
	public String appNo;
	public String date;
	public String patentName;
	public String pubNo;
	public String pubDate;
	public String category;
	public String originNo;
	public String classNo;
	public String issueDate;
	public String priority;
	public String applier;
	public String address;
	public String authors;
	public String intPatent;
	public String intPub;
	public String intDate;
	public String agency;
	public String agent;
	public String summary;
	public String domain;

	public PatentJson(String message) {
		this.url = "http://115.236.19.70:18180/metaApi/queryPatent";
		this.id = Integer.parseInt(getMessage(message, "id"));
		this.appNo = getMessage(message, "sqh").replaceAll("\"", "").trim();
		this.date = getMessage(message, "sqrq").replaceAll("\"", "").trim();
		this.patentName = getMessage(message, "mc").replaceAll("\"", "").trim();
		this.pubNo = getMessage(message, "gkh").replaceAll("\"", "").trim();
		this.pubDate = getMessage(message, "gkrq").replaceAll("\"", "").trim();
		this.category = getMessage(message, "zflh").replaceAll("\"", "").trim();
		this.originNo = getMessage(message, "faysqh").replaceAll("\"", "")
				.trim();
		this.classNo = getMessage(message, "flh").replaceAll("\"", "").trim();
		this.issueDate = getMessage(message, "bzr").replaceAll("\"", "").trim();
		this.priority = getMessage(message, "yxq").replaceAll("\"", "").trim();
		this.applier = getMessage(message, "sqr").replaceAll("\"", "").trim();
		this.address = getMessage(message, "dz").replaceAll("\"", "").trim();
		this.authors = getMessage(message, "fmr").replaceAll("\"", "").trim();
		this.intPatent = getMessage(message, "gjsq").replaceAll("\"", "")
				.trim();
		this.intPub = getMessage(message, "gjgb").replaceAll("\"", "").trim();
		this.intDate = getMessage(message, "jrgjrq").replaceAll("\"", "")
				.trim();
		this.agency = getMessage(message, "dljg").replaceAll("\"", "").trim();
		this.agent = getMessage(message, "dlr").replaceAll("\"", "").trim();
		this.summary = getMessage(message, "zy").replaceAll("\"", "").trim();
		this.domain = getLastMessage(message, "domain").replaceAll("\"", "")
				.trim();
	}

	public String getMessage(String message, String word) {
		// System.out.println(word);
		// System.out.println(message);
		int index = message.indexOf("\"" + word + "\"");
		return message.substring(message.indexOf(":", index) + 1,
				message.indexOf(",", index));
	}

	public String getLastMessage(String message, String word) {
		int index = message.indexOf(word);
		return message.substring(message.indexOf(":", index) + 1,
				message.length());
	}
}

class JSON {
	public int id;
	public String title;
	public String classifications;
	public String type;
	public int year;
	public String keywords;
	public String remark;
	public String unit;
	public String author;
	public String fClassifications;
	public String subject;
	public String subjectName;
	public String partUnit;
	public String province;
	public String city;

	public JSON(String message) {
		// System.out.println(message);
		this.id = Integer.parseInt(getMessage(message, "id"));
		this.title = getMessage(message, "title");
		this.classifications = getMessage(message, "classifications");
		this.type = getMessage(message, "type");
		this.year = Integer.parseInt(getMessage(message, "year"));
		this.keywords = getMessage(message, "keywords");
		this.remark = getMessage(message, "remark");
		this.unit = getMessage(message, "unit");
		this.author = getMessage(message, "author");
		this.fClassifications = getMessage(message, "fClassifications");
		this.subject = getMessage(message, "subject");
		this.subjectName = getMessage(message, "subjectName");
		this.partUnit = getMessage(message, "partUnit");
		this.province = getMessage(message, "province");
		this.city = getLastMessage(message, "city");

	}

	public String getMessage(String message, String word) {
		// System.out.println(word);
		// System.out.println(message);
		int index = message.indexOf("\"" + word + "\"");
		return message.substring(message.indexOf(":", index) + 1,
				message.indexOf(",", index));
	}

	public String getLastMessage(String message, String word) {
		int index = message.indexOf(word);
		return message.substring(message.indexOf(":", index) + 1,
				message.length());
	}
}