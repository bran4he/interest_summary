package interest_summary;

import java.text.DecimalFormat;
import java.text.Format;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AppTest {

	public static void main(String[] args) throws ParseException {
		// TODO Auto-generated method stub
//		getSignedListTest();
		
//		getNumTest2("￥1.01");
//		getNumTest2("￥0.00");
//		getNumTest2("￥2.00");
//		sortListTest();
		getPageNumTest();
	}
	
	public static void getPageNumTest(){
		String str = "共12页/当前为第1页 首页 上一页 下一页 尾页";
		String regex = "\\d{1,2}";
		
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(str);
		
		if(m.find()){
			System.out.println(m.group(0));
		}
//		while(m.find()){
//			System.out.println(m.group());
//		}
	}

	public static void sortListTest(){
		List<String> dateList = new ArrayList<String> ();
		dateList.add("2017-01-02");
		dateList.add("2017-02-02");
		dateList.add("2013-01-02");
		dateList.add("2017-04-02");
		dateList.add("2015-02-23");
		System.out.println(dateList);
		System.out.println("==================");
		Collections.sort(dateList);
		System.out.println(dateList);
		
	}
	
	public static void getNumTest(){
		Format f=new DecimalFormat("$#,###.00");
		double n=12345.678;
		System.out.println(f.format(n));	//$12,345.68
	}
	
	public static void getNumTest2() throws ParseException{
		Format f=new DecimalFormat("￥#.##");
		String str ="￥1.01";
		Long d = null;
		d = (Long) f.parseObject(str);
		System.out.println(d);	//$12,345.68
	}
	
	public static void getNumTest2(String str) throws ParseException{
		Format f=new DecimalFormat("￥#");
		Object d =  f.parseObject(str);
		System.out.println(d);	//$12,345.68
		
		if(d instanceof Long){
			System.out.println("Long:" +(Long)d);
		}else if(d instanceof Double){
			Double t = (Double)d;
			System.out.println("Double1:" + t.longValue());
		}else{
			Double t = (Double)d;
			System.out.println("Double2:" + t.longValue());
		}
		
		
	}
	
	public static void getSignedListTest(){
		String data = "[5,6,7,8,9,11,16,25,26,27,28,29,30,]";
		
		String regex = "\\d{1,2}";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(data);
		while(m.find()){
			System.out.println(m.group());
		}
	}
}
