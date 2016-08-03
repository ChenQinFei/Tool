package dangdang;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.jetty.log.OutputStreamLogSink;

public class dangInfo {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		dang();
	}

	public static void dang() throws IOException {
		// 获取当前关键词搜索结果总页数
		String filePath = "E://bookName.csv";
		String outputFile = "E://bookDang2.csv";
		BufferedReader br = new BufferedReader(new InputStreamReader(
				new FileInputStream(filePath), "GBK"));
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(outputFile), "GBK"));
		String line;
		List<String> bookList = new ArrayList<String>();
		// 读入方剂书名
		while ((line = br.readLine()) != null) {
			line = line.split(",")[0];
			line = line.replaceAll("(方出|《|》|，)", "").trim();
			bookList.add(line);
		}
		br.close();
		for (int i = 0; i < bookList.size(); i++) {
			String keyword = bookList.get(i);
			System.out.println(keyword);
			try {
				String url = "http://search.dangdang.com/?key=" + keyword
						+ "&show=list&act=input";
				Document adoc = Jsoup.parse(new URL(url).openStream(), "GBK",
						url);
				bw.write(keyword);
				// 获取当当上前两本书信息
				for (int j = 0; j < 2; j++) {
					try {
						Element e = adoc.select(".search_left li").get(j);
						String author, dynasty;
						// if(e.select(".search_book_author a") != null){
						try {
							author = e.select(".search_book_author a").first()
									.attr("title").replace(",", "，");
							if (author.indexOf("·") != -1) {
								dynasty = author.substring(0,
										author.indexOf("·"));
							} else if (author.indexOf("〔") != -1) {
								dynasty = author.substring(
										author.indexOf("〔") + 1,
										author.indexOf("〕"));
							} else if (author.indexOf("(") != -1) {
								dynasty = author.substring(
										author.indexOf("(") + 1,
										author.indexOf(")"));
							} else if (author.indexOf("【") != -1) {
								dynasty = author.substring(
										author.indexOf("【") + 1,
										author.indexOf("】"));
							} else if (author.indexOf("[") != -1) {
								dynasty = author.substring(
										author.indexOf("[") + 1,
										author.indexOf("]"));
							} else if (author.indexOf("（") != -1) {
								dynasty = author.substring(
										author.indexOf("（") + 1,
										author.indexOf("）"));
							} else {
								dynasty = "";
							}
							System.out.println(dynasty);
						} catch (Exception e2) {
							author = "";
							dynasty = "";
						}
						// } else {
						// }
						String title = e.select("a.pic").attr("title")
								.replace(",", "，");
						String href = e.select("a.pic").attr("abs:href")
								.replace(",", "，");
						bw.write("," + dynasty + "," + title + "," + author
								+ "," + href);
					} catch (Exception e3) {
						break;
					}
				}
				bw.write("\r\n");
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}
		}
		bw.close();
	}

}
