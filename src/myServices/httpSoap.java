package myServices;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class httpSoap {
	private static HttpURLConnection webserCon = null;
	private OutputStream outStream = null;
	private InputStream inStream = null;
	public ArrayList<String> getWebservice(String methodName, ArrayList<String> Parameters, ArrayList<String> ParValues) {
		 ArrayList<String> Values = new ArrayList<String>();  
		 String serverUrl = "http://195.249.91.39:8080/dbwebServices.asmx";
		 

		 
		 String para, value, paravalue;  
		 String bodyString = ""; 
		 for (int i = 0; i < Parameters.size(); i++) {  
			 para = Parameters.get(i).toString();  
			 value = ParValues.get(i).toString();  
			 paravalue = "<" + para + ">" + value + "</" + para + ">";  
			 bodyString = bodyString + paravalue;  
	        }  
		 
		 String soapAction = "http://195.249.91.39:8060/"+methodName;
		 String soap ="<?xml version=\"1.0\" encoding=\"utf-8\"?>"+
				 	  "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" " +
				 	  "xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" " +
				 	  "xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">"+
				      "<soap:Body>"+
				      "<"+methodName+" xmlns=\"http://195.249.91.39:8060/\">"+
				      bodyString+
				      "</" + methodName + ">"+///here! 
				      "</soap:Body>"+
				      "</soap:Envelope>";

		 try {  
	            URL url = new URL(serverUrl);  
	            webserCon = (HttpURLConnection) url.openConnection();  
	            byte[] bytes = soap.getBytes("utf-8");  
	            webserCon.setDoInput(true);   // 设置是否从httpUrlConnection读入，默认情况下是true;
	            webserCon.setDoOutput(true);   // 设置是否向httpUrlConnection输出，因为这个是post请求，参数要放在  zhengwen
	            webserCon.setUseCaches(false);  // Post 请求不能使用缓存  
	            webserCon.setConnectTimeout(6000);// filenotfound exception
	            webserCon.setReadTimeout(6000);		//sockettimeout exception
	            webserCon.setRequestMethod("POST"); // 设定请求的方法为"POST"，默认是GET   
	           // webserCon.setRequestMethod("soap");
	            webserCon.setRequestProperty("Host", "192.168.1.100");
	            webserCon.setRequestProperty("Content-Type", "text/xml; charset=utf-8");  
	            webserCon.setRequestProperty("SOAPAction", soapAction);  
	            webserCon.setRequestProperty("Content-Length", "" + bytes.length);  
	            webserCon.connect();
	            outStream = webserCon.getOutputStream();  // 现在通过输出流对象构建对象输出流对象，
	            outStream.write(bytes);  
	            outStream.flush();  
	            inStream = webserCon.getInputStream();  

	            Values = inputStreamtovaluelist(inStream, methodName);
	            System.out.println("meicaolesoap");

	            return Values;  
	        } catch (Exception e){ 
	        	 System.out.println("caolesoap e:"+e.toString());
	            return null;  
	        } 
		 	finally{
		 		webserCon.disconnect();
		 		webserCon = null;
			 	try {
		        outStream.close();
				inStream.close();
		        outStream=null;
				inStream=null;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		 	}
	 }
	 
	 public ArrayList<String> inputStreamtovaluelist(InputStream in, String MonthsName) throws IOException {  
	        StringBuffer out = new StringBuffer();  
	        String s1 = "";  
	        byte[] b = new byte[4096];  
	        ArrayList<String> Values = new ArrayList<String>();  
	        Values.clear();  
	        
	        //按照b的大小接收in 分段儿放进out 显示去
	        for (int n; (n = in.read(b)) != -1;) {  
	            s1 = new String(b, 0, n);  
	            out.append(s1);  
		        //System.out.println("s1 :"+s1);
	        }  
	        System.out.println("inputStreamtovaluelist :"+out);  
	        String[] s13 = out.toString().split("><");//原来是s1  
	        String ifString = MonthsName + "Result";  
	        String TS = "";  
	        String vs = "";  

	        Boolean getValueBoolean = false;  
	        for (int i = 0; i < s13.length; i++) {  
	            TS = s13[i];  
	            System.out.println(TS);  
	            int j, k, l;  
	            j = TS.indexOf(ifString);  
	            k = TS.lastIndexOf(ifString);  
	  
	            if (j >= 0) {  
	                System.out.println(j);  
	                if (getValueBoolean == false) {  
	                    getValueBoolean = true;  
	                } else {  
	  
	                }  
	  
	                if ((j >= 0) && (k > j)) {  
	                    System.out.println("FFF" + TS.lastIndexOf("/" + ifString));  
	                    //System.out.println(TS);  
	                    l = ifString.length() + 1;  
	                    vs = TS.substring(j + l, k - 2);  
	                    //System.out.println("fff"+vs);  
	                    Values.add(vs);  
	                    System.out.println("??" + vs);  
	                    getValueBoolean = false;  
	                    return Values;  
	                }  
	  
	            }  
	            if (TS.lastIndexOf("/" + ifString) >= 0) {  
	                getValueBoolean = false;  
	                return Values;  
	            }  
	            if ((getValueBoolean) && (TS.lastIndexOf("/" + ifString) < 0) && (j < 0)) {  
	                k = TS.length();  
	                //System.out.println(TS);  
	                vs = TS.substring(7, k - 8);  
	                //System.out.println("f"+vs);  
	                Values.add(vs);  
	            }  
	  
	        }  
	        
	        return Values;  
	    }  
}
