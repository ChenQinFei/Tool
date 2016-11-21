package patentProcess;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
//import org.openqa.selenium.firefox.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jsoup.Connection;

import patentProcess.PatentItem;
import common.Database;

public class Petent {

	/**
	 * @param args
	 * @throws IOException
	 * @throws SQLException
	 */
	private static int id = 85582;

	public static void main(String[] args) throws IOException, SQLException {
		// TODO Auto-generated method stub
		jsoupPatent();
	}

	
	// 使用jsoup爬取页面
	public static void jsoupPatent() throws IOException, SQLException {
		//读取词表
		String inputFileName = "E:\\dic\\disease.txt";
		File inputFile = new File(inputFileName);
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				new FileInputStream(inputFile), "GBK"));
		// 连接数据库
		Database db = new Database();
		HashMap<Integer, Integer> notConnect = new HashMap<Integer, Integer>();
		try {
			db.init("10.15.62.29:3306/patent?user=root&password=123");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<String> dict = new ArrayList<String>();
		String line;
		while ((line = reader.readLine()) != null) {
			dict.add(line);
		}
		reader.close();
		// System.out.println(dict.indexOf(" "));
		// 获取词表
		// List<String> dict = getDict();
		// //获取关键词对应位置
		// for(int i = 0; i<dict.size();i++){
		// if(dict.get(i).equals("内脏感觉")){
		//
		// System.out.print(i);
		// return;
		// }
		// }
		// 遍历词表
		for (int j = 0; j < dict.size(); j++) {
			String keyword = dict.get(j);
			//处理关键词，去除括号及括号内内容
			keyword = keyword.replaceAll("\\(|\\[","（").replaceAll("\\)|\\]", "）");
			if(keyword.contains("（") && keyword.contains("）")){
				keyword = keyword.substring(0, keyword.indexOf('（')) + keyword.substring(keyword.indexOf('）'));
				keyword = keyword.replace("）","").trim();
			}
			System.out.println(keyword + ":" + j);
			
			// 当关键词为单字的时候，跳过
			if (keyword.length() == 1) {
				System.out.println("单个字跳过");
				continue;
			}
			if(j<549){
				continue;
			}
			// System.out.println(keyword+" : 第"+j);
			// 获取当前关键词搜索结果总页数
			Document adoc = null;
			try {
				adoc = Jsoup
						.connect("http://epub.sipo.gov.cn/patentoutline.action")
						.timeout(100000)
						.data("showType", "1",
								"strSources", "",
								"strWhere","PA,IN,AGC,AGT+='%"+keyword+"%' or PAA,TI,ABH+='"+keyword+"'",
								"numSortMethod", "4",
								"strLicenseCode","",
								"numlp","",
								"numlpc","",
								"numlg","",
								"numlgc","",
								"numlgd","",
								"numUg","",
								"numUgc","",
								"numUgd","",
								"numDg","",
								"numDgc","",
								"pageSize","10",
								"pageNow","1"
								).post();
			} catch (Exception e) {
				System.out.println(j);
				notConnect.put(j, 1);
				break;
			}
			String tolString;
			try {
				tolString = adoc.select(".lxxz_dl").get(0).select("li>a")
						.first().text();

			} catch (Exception e) {
				System.out.println(keyword + ": 当前搜索结果为零。");
				// writer.write(keyword+",0"+"/n");
				// writer.newLine();
				continue;
			}

			tolString = tolString.split("：")[1].trim();
			tolString = tolString.substring(0, tolString.length() - 1);
			// System.out.print(tolString);
			int tol = Integer.parseInt(tolString);
			tol = (int) Math.ceil((double) tol / 10);
			System.out.println(keyword + ":" + tol + "页");
			// 遍历搜索结果页面
			for (int i = 1; i <= tol; i++) {
				try {
					Document doc = Jsoup
							.connect(
									"http://epub.sipo.gov.cn/patentoutline.action")
							.timeout(100000)
							.data("showType", "1",
									"strSources", "",
									"strWhere","PA,IN,AGC,AGT+='%"+keyword+"%' or PAA,TI,ABH+='"+keyword+"'",
									"numSortMethod", "4",
									"strLicenseCode","",
									"numlp","",
									"numlpc","",
									"numlg","",
									"numlgc","",
									"numlgd","",
									"numUg","",
									"numUgc","",
									"numUgd","",
									"numDg","",
									"numDgc","",
									"pageSize","10",
									"pageNow",Integer.toString(i)
									).post();

					// 分析获取到的页面body
					parseJsoup(doc.body(), keyword, db);
				} catch (Exception e) {
					e.printStackTrace();
					// notConncet.add(Integer.valueOf(i));
					notConnect.put(Integer.valueOf(j), Integer.valueOf(i));
					// writer.write(keyword+","+tol+",爬至"+i+"\n");
					// writer.newLine();
					System.out.println("关键词、位置、搜到页码：" + keyword + ", " + j
							+ ", " + i);
					System.out.println("id:" + id);
					break;
				}
			}
			// writer.write(keyword+","+tol+"\n");

		}
		// writer.close();
		db.end();
		System.out.println(notConnect.toString());
		System.out.println("id:" + id);

	}

	// 解析爬取页面
	public static void parseJsoup(Element ele, String keyword, Database db)
			throws IOException, SQLException {
		Elements eles = ele.select(".cp_box");
		for (Element e : eles) {
			//判断标题和摘要中是否含有关键词
			if (e.select("h1").text().contains(keyword)
					|| e.select(".cp_jsh").text().contains(keyword)) {
				PatentItem patentItem = new PatentItem();
				// 设置url
				patentItem.setUrl("http://epub.sipo.gov.cn/");
				// 获取专利名
				patentItem.setPatentName(e.select("h1").text().split("]")[1]);
				// 获取摘要
				patentItem.setSummary(e.select(".cp_jsh").text()
						.replaceAll("摘要：|全部", ""));
				// 获取公布号……
				Elements lis = e.select(".cp_linr>ul>li");
				for (Element li : lis) {
					String[] name = li.text().split("：");
					// System.out.print(name[0]);
					switch (name[0]) {
					case "申请公布号":
						patentItem.setPubNo(name[1]);
						break;
					case "申请公布日":
						patentItem.setPubDate(name[1]);
						break;
					case "申请号":
						patentItem.setAppNo(name[1]);
						break;
					case "申请日":
						patentItem.setDate(name[1]);
						break;
					case "申请人":
						patentItem.setApplier(name[1]);
						break;
					case "发明人":
						patentItem.setAuthors(name[1]);
						break;
					case "地址":
						patentItem.setAddress(name[1]);
						break;
					// Todo:进一步分类分类号中的数据
					case "分类号":
						parseCategory(patentItem, li);
						break;// patentItem.setCategory(li.text())
					default:
						;
						break;
					}
					// System.out.println();
				}
				System.out.println(patentItem.getPubNo());
				// 插入数据库
//				String query = "select * from patent where pub_no='"
//						+ patentItem.getPubNo() + "'";
//				ResultSet rs = db.SQLQuery(query);
//				if (!rs.next()) {
					String sql = "insert ignore into patent_web_disease(id,url,app_no,date,patent_name,pub_no,pub_date,category,origin_no,class_no,issue_date,priority,applier,address,authors,int_patent,int_pub,int_date,agency,agent,summary) values ('"
							+id
							+ "','"
							+patentItem.getUrl()
							+ "','"
							+ patentItem.getAppNo()
							+ "','"
							+ patentItem.getDate()
							+ "','"
							+ patentItem.getPatentName()
							+ "','"
							+ patentItem.getPubNo()
							+ "','"
							+ patentItem.getPubDate()
							+ "','"
							+ patentItem.getCategory()
							+ "','"
							+ patentItem.getOriginNo()
							+ "','"
							+ patentItem.getClassNo()
							+ "','"
							+ patentItem.getIssueDate()
							+ "','"
							+ patentItem.getPriority()
							+ "','"
							+ patentItem.getApplier()
							+ "','"
							+ patentItem.getAddress()
							+ "','"
							+ patentItem.getAuthors()
							+ "','"
							+ patentItem.getIntPatent()
							+ "','"
							+ patentItem.getIntPub()
							+ "','"
							+ patentItem.getIntDate()
							+ "','"
							+ patentItem.getAgency()
							+ "','"
							+ patentItem.getAgent()
							+ "','"
							+ patentItem.getSummary() + "')";
					System.out.println(sql);
					db.SQLUpdate(sql);
					id++;
//				}
			}
		}
	}

	public static void parseCategory(PatentItem patentItem, Element li) {
		String category = li.text().split("全部")[0].split("：")[1];
		patentItem.setCategory(category.split("; ")[0]);
		Elements lis = li.select("div>ul>li");
		if (lis.size() != 0) {
			if (!lis.get(0).text().contains("：")) {
				patentItem.setClassNo(category + lis.get(0).text());
			}
			for (Element liMore : lis) {
				String[] name = liMore.text().split("：");
				switch (name[0]) {
				case "专利代理机构":
					patentItem.setAgency(name[1]);
					break;
				case "代理人":
					patentItem.setAgent(name[1]);
					break;
				case "优先权":
					patentItem.setPriority(name[1]);
					break;
				default:
					;
					break;
				}
			}
		} else {
			patentItem.setClassNo(category);
		}
	}

	// 获取词表
	public static List<String> getDict() throws SQLException {
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
		List<String> dict = new ArrayList<String>();
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
				// System.out.println(className);
				tol++;
				dict.add(className);
			}
		}
		System.out.println("dict-tol:" + tol);
		db.end();
		return dict;
	}
	
	public static void chromePatent() {
		System.setProperty("webdriver.chrome.driver",
				"E:\\Code\\JarLib\\chromedriver.exe");
		WebDriver driver = new ChromeDriver();
		// System.setProperty("webdriver.firefox.bin",
		// "D:\\Program Files (x86)\\Mozilla Firefox\\firefox.exe");
		// WebDriver driver = new FirefoxDriver();
		// 隐式等待时间 30s
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		driver.get("http://www.pss-system.gov.cn/sipopublicsearch/search/searchHome-searchIndex.shtml");
		// 搜索操作
		WebElement radio = driver.findElement(By
				.id("radioTypeCongregateTIVIEW"));
		radio.click();
		WebElement textarea = driver.findElement(By.id("searchInfo0"));
		textarea.sendKeys("中风");
		WebElement button = driver.findElement(By.id("executeSearch0"));
		button.click();
		// 查看文献详细信息
		List<WebElement> links = driver.findElements(By
				.cssSelector(".sipo_link>a:first-child"));

		for (WebElement e : links) {
			// e.click();
			// ((JavascriptExecutor)driver).executeScript("$(arguments[0]).click()",
			// e);
			Actions action = new Actions(driver);
			action.moveToElement(e).click().perform();
			System.out.println(e.getText());
		}
		// //得到当前窗口的句柄
		// String currentWindow = driver.getWindowHandle();
		// //得到所有窗口的句柄
		// Set<String> handles = driver.getWindowHandles();
		// Iterator<String> it = handles.iterator();
		// while(it.hasNext()){
		// String handle = it.next();
		// if(currentWindow.equals(handle)) continue;
		// WebDriver window = driver.switchTo().window(handle);
		// System.out.println("title,url = "+window.getTitle()+","+window.getCurrentUrl());
		// }
	}

}
