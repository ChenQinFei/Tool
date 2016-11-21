package dicProcess;

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
import java.util.ArrayList;
import java.util.List;

import com.sun.imageio.plugins.common.InputStreamAdapter;

public class dicFileProcess {

	/**
	 * 此工具主要为对输出的词表进行二次处理，去除括号后内容，去除空白行……
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("E:\\dic\\bieming.txt"),"UTF-8"));
	
		
		List<String> dic = new ArrayList<>();
		
		String line;
		while((line = br.readLine()) != null ){
			line = line.replace("《","(").replace("（","(").replace("[","(").replace("）","(").trim();
			if(line.contains("(")){
				line = line.substring(0, line.indexOf('(')).trim();
			}
			if( line.equals("") || line.equals("null")){
				continue;
			}
			dic.add(line);
		}
		br.close();
	
		writeToFile("E:\\dic\\newBieming.txt", dic);
	}
	
	public static void writeToFile(String fileName, List<String> list) throws IOException{
		File filePath= new File(fileName);
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath),"UTF-8"));
		for(String word : list){
			bw.write(word + "\r\n");
		}
		bw.close();
	}
}
