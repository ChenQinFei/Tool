package paper;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import common.Database;

public class PaperGcy {

	// final static String url =
	// "http://115.236.19.70:18180/metaApi/queryReward";
	final static String url = "http://115.236.19.70:14980/univ/queryResources";
	final static String zjUrl = "http://115.236.19.70:14980/univ/queryExperts";
	private static int id = 0;
	// final static String params = "{\"org\": \"浙江大学\",\"size\":1,\"from\":1}";
	private static Database db;

	private static int year = 2015;
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
			connection.setDoInput(true);
			connection.setDoOutput(true);
			// connection.setUseCaches(false);
			connection.setInstanceFollowRedirects(true);
			connection.setRequestMethod("POST"); // 设置请求方式
			// connection.setRequestProperty("Accept", "application/json"); //
			// 设置接收数据的格式
			connection.setRequestProperty("content-type", "application/json"); // 设置发送数据的格式
//			connection.setRequestProperty("x-api-key",
//					"fea32a73bd564ab7ba31dc7d102aff13");
			connection.setRequestProperty("x-api-key",
					"c915041c0cc4c59adc8f30c05fec4513");
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
			System.out.println(params);
		}
		return "error"; // 自定义错误信息
	}

	public static void dealJSON(String JSONString,int num) throws SQLException {
		int index1 = find(JSONString, "}", 0);
		int index2 = find(JSONString, "}", index1 + 1);
		int start = 0;
		while (index1 != -1) {
			String dealString = JSONString.substring(start, index1);
			PaperJson o = new PaperJson(dealString);
//			ExpertJson o = new ExpertJson(dealString);
//			String query = "select * from paper_info where title = '"+o.title+"' and issn = '"+o.issn+"'";
//			ResultSet rs = db.SQLQuery(query);
			start = index1;
			index1 = index2;
			index2 = find(JSONString, "}", index1 + 1);
//			if(rs.next()){
//				continue;
//			}
			//插入查询到的专家数据
//			String SQL = "insert ignore into expert(id,url,keywords, province, city, kId, subjects, organization, name, altName) values("
//					+ id + ",'"
//					+ o.url + "','"
//					+ o.keywords + "','"
//					+ o.province + "','"
//					+ o.city + "','"
//					+ o.kId + "','"
//					+ o.subjects + "','"
//					+ o.organization + "','"
//					+ o.name + "','"
//					+ o.altName + "')";
			//插入文献数据
			String SQL = "insert ignore into paper_info_expert(id,url,title,titleAlt,creator,creatorAlt,creatorAll,abstract,abstractAlt,"
					+ "keyword,keywordAlt,institute,instituteAll,year,subject,province,city,language,"
					+"resourceCode,page,doi,issue,creatorId,sourceUrl,isCore,conferenceName,address,"
					+ "issn,isbn,tutor,degree,publisher,patentNo,owner,applicationNo,type,applicationDate,publicationDate,"
					+ "volume,source,journal) values ('" 
					+ o.id + "','"
					+ o.url + "','"
					+ o.title +"','"
					+ o.titleAlt +"','"
					+ o.creator +"','"
					+ o.creatorAlt +"','"
					+ o.creatorAll +"','"
					+ o.abstractT +"','"
					+ o.abstractAlt +"','"
					+ o.keyword +"','"
					+ o.keywordAlt +"','"
					+ o.insitute +"','"
					+ o.instituteAll +"','"
					+ o.year +"','"
					+ o.subject +"','"
					+ o.province +"','"
					+ o.city +"','"
					+ o.language +"','"
					+ o.resourceCode +"','"
					+ o.page +"','"
					+ o.doi +"','"
					+ o.issue +"','"
					+ o.creatorId +"','"
					+ o.sourceUrl +"','"
					+ o.isCore +"','"
					+ o.conferenceName +"','"
					+ o.address +"','"
					+ o.issn +"','"
					+ o.isbn +"','"
					+ o.tutor +"','"
					+ o.degree +"','"
					+ o.publisher +"','"
					+ o.patentNo +"','"
					+ o.owner +"','"
					+ o.applicationNo +"','"
					+ o.type +"','"
					+ o.applicationDate +"','"
					+ o.publicationDate +"','"
					+ o.volume +"','"
					+ o.source +"','"
					+ o.journal
					+ "')";
			 System.out.println(SQL);
			 try{
				 db.SQLUpdate(SQL);
//				 id++;
			 } catch(Exception e){
				 e.printStackTrace();
				 System.out.println("num:"+num);
			 }
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
			db.init("10.15.62.29:3306/paper_db?user=root&password=123");
		} catch (SQLException e) {
			e.printStackTrace();
		}
//		queryExpert();
		queryExpertPaper();
		db.end();
	}
	/**
	 *  从词表中读取中药等关键词进行爬取
	 * @throws NumberFormatException
	 * @throws IOException
	 * @throws SQLException
	 * @throws Exception
	 */
	public static void queryPaper() throws NumberFormatException, IOException, SQLException, Exception{
		
		
		String dealedFilePath = "E:\\dic\\herb.txt";
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
			if (word.length() <= 1)
				continue;
			//五万到六万的词有时间再爬一边,18万词表爬到7万5
			if (num < 0)
				continue;
			word = word.replaceAll("\\(|\\[","（").replaceAll("\\)|\\]", "）");
			if(word.contains("（") && word.contains("）")){
				word = word.substring(0, word.indexOf('（')) + word.substring(word.indexOf('）'));
				word = word.replace("）","").trim();
			}
			System.out.println("num: " + num);
			System.out.println("word: " + word);
			// String word="三七";
			String params = "{\"keywords\":\"" + word
					+ "\",\"year\":" + year
					+ ",\"size\":10,\"from\":0}";
			// System.out.println(params);
			String m = post(url, params);
			if (m.equals("error")) {
				System.out.println("count: error!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
				break;
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
						+ "\",\"year\":" + year
						+ ",\"size\":10,\"from\":" + String.valueOf(from)
						+ "}";
				// System.out.println(newParams);
				String JSONString = post(url, newParams);
				JSONString = JSONString.replaceAll("'", "");
				// JSONString=JSONString.replaceAll("\"", "");
				JSONString = JSONString.replaceAll("\\\\", "");
				JSONString = JSONString.replaceAll("}ln}", "现");
				// System.out.println(JSONString);
				dealJSON(JSONString,num);
				from = from + 10;
			}
		}
	}
	/**
	 * 读取专家id，爬取文献相关信息
	 * @throws Exception 
	 *
	 */
	public static void queryExpertPaper() throws Exception{
		
		String query = "select kId from expert";
		ResultSet rs = db.SQLQuery(query);
		
		int num = 0;
		while(rs.next()){
			System.out.println("num: "+num);
			String params = "{\"kid\":" + rs.getInt("kId")
					+",\"year\":" + year + "}";
			// System.out.println(params);
			String JSONString = post(url, params);
			if (JSONString.equals("error")) {
				System.out.println("count: error!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
				break;
			}
			System.out.println(JSONString);
			
			JSONString = JSONString.replaceAll("'", "");
			// JSONString=JSONString.replaceAll("\"", "");
			JSONString = JSONString.replaceAll("\\\\", "");
			JSONString = JSONString.replaceAll("}ln}", "现");
			// System.out.println(JSONString);
			dealJSON(JSONString,num);
			num++;	
		}
	}
	/**
	 * 爬取专家信息
	 * @throws Exception
	 */
	public static void queryExpert() throws Exception{
		String[] keywords = { "中草药", "中医" };
		int num = 0;
		for(int i = 0; i < keywords.length; i++){
			String params = "{\"keyword\":\"" + keywords[i]
					+ "\",\"size\":10,\"from\":0}";
			// System.out.println(params);
			String m = post(zjUrl, params);
			System.out.println(m);
			if (m.equals("error")) {
				System.out.println("count: error!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
				break;
			}
			// System.out.println(m);
			int countIndex = m.indexOf("count");
			int count = Integer
					.parseInt(m.substring(m.indexOf(":", countIndex) + 1,
							m.indexOf(",", countIndex)));
			System.out.println("count:" + count);
			int from = 0;
			while (from < count) {
				String newParams = "{\"keyword\":\"" + keywords[i]
						+ "\",\"size\":10,\"from\":" + String.valueOf(from)
						+ "}";
				// System.out.println(newParams);
				String JSONString = post(zjUrl, newParams);
				JSONString = JSONString.replaceAll("'", "");
				// JSONString=JSONString.replaceAll("\"", "");
				JSONString = JSONString.replaceAll("\\\\", "");
				JSONString = JSONString.replaceAll("}ln}", "现");
				// System.out.println(JSONString);
				dealJSON(JSONString, num);
				from = from + 10;
			}
		}
	}

}
class ExpertJson {
	public String url;
	public String keywords;
	public String province;
	public String city;
	public int kId;
	public String subjects;
	public String organization;
	public String name;
	public String altName;
	public String getMessage(String message, String word) {
		if(!message.contains("\"" + word + "\"")){
			return "";
		}
		int index = message.indexOf("\"" + word + "\"");
		return message.substring(message.indexOf(":", index) + 1,
				message.indexOf(",\"", index)).replace("\"", "");
	}

	public String getLastMessage(String message, String word) {
		if(!message.contains("\"" + word + "\"")){
			return "";
		}
		int index = message.indexOf("\"" + word + "\"");
		return message.substring(message.indexOf(":", index) + 1,
				message.length()).replace("\"", "");
	}
	public ExpertJson(String message){
//		message = message.replace("\"", "");
		this.url = "http://115.236.19.70:14980/univ/queryExperts";
		this.keywords = getMessage(message, "keywords");
		this.province = getMessage(message, "province");
		this.city = getMessage(message, "city");
		this.kId = Integer.parseInt(getMessage(message, "kId"));
		this.subjects = getMessage(message, "subjects");
		this.organization = getMessage(message, "organization");
		this.name = getMessage(message, "name");
		this.altName = getLastMessage(message, "altName");
	}
}
class PaperJson {
	public String id;
	public String url;
	public String title;
	public String titleAlt;
	public String creator;
	public String creatorAlt;
	public String creatorAll;
	public String abstractT;
	public String abstractAlt;
	public String keyword;
	public String keywordAlt;
	public String insitute;
	public String instituteAll;
	public String year;
	public String subject;
	public String province;
	public String city;
	public String language;
	public String resourceCode;
	public String page;
	public String doi;
	public String issue;
	public String creatorId;
	public String sourceUrl;
	public String isCore;
	public String conferenceName;
	public String address;
	public String issn;
	public String isbn;
	public String tutor;
	public String degree;
	public String publisher;
	public String patentNo;
	public String owner;
	public String applicationNo;
	public String type;
	public String applicationDate;
	public String publicationDate;
	public String volume;
	public String source;
	public String journal;
	
	public String getMessage(String message, String word) {
		// System.out.println(word);
		// System.out.println(message);
		if(!message.contains("\"" + word + "\"")){
			return "";
		}
		int index = message.indexOf("\"" + word + "\"");
		String sub = message.substring(message.indexOf(":",index)+1);
		//判断当前属性后是否跟随其他属性
		if(sub.contains(",\"")){
			return sub.substring(0, sub.indexOf(",\"")).replace("\"", "");
		}else 
			return sub.substring(0,sub.length()).replace("\"", "");
	}

	public PaperJson(String message){
//		message = message.replace("\"", "");
		System.out.println(message);
		this.id = getMessage(message, "id");
		this.url = "http://115.236.19.70:18180/metaApi/queryJournalPaper";
		this.title = getMessage(message, "title");
		this.titleAlt = getMessage(message, "title_alt");
		this.creator = getMessage(message, "creator");
		this.creatorAlt = getMessage(message, "creator_alt");
		this.creatorAll = getMessage(message, "creator_all");
		this.abstractT = getMessage(message, "abstract");
		this.abstractAlt = getMessage(message, "abstract_alt");
		this.keyword = getMessage(message, "keyword");
		this.keywordAlt = getMessage(message, "keyword_alt");
		this.insitute = getMessage(message, "insitute");
		this.instituteAll = getMessage(message, "insitute_all");
		this.year = getMessage(message, "year");
		this.subject = getMessage(message, "subject");
		this.province = getMessage(message, "province");
		this.city = getMessage(message, "city");
		this.language = getMessage(message, "language");
		this.resourceCode = getMessage(message, "resource_code");
		this.page = getMessage(message, "page");
		this.doi = getMessage(message, "doi");
		this.issue = getMessage(message, "issue");
		this.creatorId = getMessage(message, "creator_id");
		this.sourceUrl = getMessage(message, "url");
		this.isCore = getMessage(message, "is_core");
		this.conferenceName = getMessage(message, "conference_name");
		this.address = getMessage(message, "address");
		this.issn = getMessage(message, "issn");
		this.isbn = getMessage(message, "isbn");
		this.tutor = getMessage(message, "tutor");
		this.degree = getMessage(message, "degree");
		this.publisher = getMessage(message, "publisher");
		this.patentNo = getMessage(message, "patent_no");
		this.owner = getMessage(message, "owner");
		this.applicationNo = getMessage(message, "application_no");
		this.type = getMessage(message, "type");
		this.applicationDate = getMessage(message, "application_date");
		this.publicationDate = getMessage(message, "publication_date");
		this.volume = getMessage(message, "volume");
		this.source = getMessage(message, "source");
		this.journal = getMessage(message, "journal");
	}
}
