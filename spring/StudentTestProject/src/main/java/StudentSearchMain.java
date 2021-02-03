import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class StudentSearchMain {

	public static void main(String[] args) {
		String search = null;
		Scanner sc = new Scanner(System.in);
		System.out.println("검색할 학생 정보의 일부분을 입력하세요>");
		search = sc.nextLine();
		
		try {
			search = URLEncoder.encode(search,"utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();

			try {
				String apiUrl = "http://localhost:9999/memberSerach.do?kind=name&search="+search;
				URL url = new URL(apiUrl);
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				String result = "";
				while(true) {
					String str = br.readLine();
					if(str == null) break;
					result += str;
				}
				System.out.println(result);
				JSONObject json = new JSONObject(result);
				if(json.getInt("responseCode") == 500){
					throw new Exception(json.getInt("responseCode") + "\t" + json.getString("responseMessage")+"\n");
				}
				
				JSONArray arr = json.getJSONArray("result");
				for(int i=0;i<arr.length();i++) {
					JSONObject obj = arr.getJSONObject(i);
					System.out.println(obj.getString("sno"));
					System.out.println(obj.getString("name"));
					System.out.println(obj.getString("major"));
					System.out.println(obj.getDouble("score"));
				}
				br.close();
			} catch (MalformedURLException e1) {
				e1.printStackTrace();
			} catch (JSONException e1) {
				e1.printStackTrace();
			} catch (Exception e1) {
				e1.printStackTrace();
				try {
					FileOutputStream fos = new FileOutputStream("error.txt",true);
					PrintWriter pw = new PrintWriter(fos);
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
					Calendar today = Calendar.getInstance();
					String str = sdf.format(today.getTime())+ "\t" + e.getMessage();
					System.out.println(str);
					pw.write(str);
					pw.flush();
					pw.close();
				} catch (FileNotFoundException e2) {
					e2.printStackTrace();
				}
				e.printStackTrace();
			}
		}
	}

}
