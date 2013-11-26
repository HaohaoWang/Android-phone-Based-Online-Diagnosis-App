package myServices;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

public class FeedbackService {
	
	private List<HashMap<String, String>> messageList = new ArrayList<HashMap<String, String>>();//the key information 
	
	private static ArrayList<String> allMessages = new ArrayList<String>();
	private static ArrayList<String> threadallMessages = new ArrayList<String>();
	private static int topfew = 10, updatedfew = 0, count0=0,count1=0;
	public static int top = 0;
	private ArrayList<String> parameters = new ArrayList<String>();  
    private ArrayList<String> values = new ArrayList<String>();
    private httpSoap Soap = new httpSoap();  
    private String pid = "", did=""; 
    private boolean newMessage  =false;
    public boolean getnewMessage(){
		return this.newMessage;
    }
    public void setnewMessage(){
	    this.newMessage=false;
    }
	//0 initial the messageList
	/*Thread myThread= new Thread(){
		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			while(true){
			try {
				Thread.sleep(4000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
			updatePDnewmessages();
			Message message = new Message();      
            message.what = 1;      
            handler.sendMessage(message);
			}
		}
	};*/
	
	public FeedbackService(String pid0,String did0) {
		super();
		pid = pid0;
		did = did0;
	}

	public List<HashMap<String, String>> getMessagehashlist(){
		return messageList;
	}
	
	public void addPDOld10messages() {
		parameters.clear();
		values.clear();
		parameters.add("pid");  
		parameters.add("did"); 
		parameters.add("startindex"); 
	    values.add(pid); 
	    values.add(did);
	    values.add(String.valueOf(top));
	    allMessages = Soap.getWebservice("selectPDportion10messages", parameters, values);
		for(int i=allMessages.size()-1,j=0;i>=0;i-=3){
			HashMap<String, String> rowHash = new HashMap<String, String>();
			rowHash.put("messagetime", allMessages.get(i-2));
			rowHash.put("messagebody", allMessages.get(i-1));
			rowHash.put("messagedirection", allMessages.get(i));
			messageList.add(j++, rowHash);// for each hashmap add it into list!!
			top++;
		}
	}
	
	public void selectPDtopfewmessages() {
		parameters.clear();
		values.clear();
		parameters.add("pid");  
		parameters.add("did"); 
		parameters.add("topfew"); 
		parameters.add("updateBoth"); 
	    values.add(pid); 
	    values.add(did);
	    values.add( String.valueOf(topfew));
	    values.add("2");
	    allMessages = Soap.getWebservice("selectPDtopfewmessages", parameters, values);
		for(int i=allMessages.size()-1;i>=0;i-=3){
			HashMap<String, String> rowHash = new HashMap<String, String>();
			rowHash.put("messagetime", allMessages.get(i-2));
			rowHash.put("messagebody", allMessages.get(i-1));
			rowHash.put("messagedirection", allMessages.get(i));
			messageList.add(rowHash);// for each hashmap add it into list!!
			top++;
		}
		getMessagecount();
	}
	
	public void getMessagecount() {
		parameters.clear();
		values.clear();
		parameters.add("pid");  
		parameters.add("did");  
	    values.add(pid); 
	    values.add(did);
	    allMessages = Soap.getWebservice("getMessagecount", parameters, values);
	    
	    try{
	    	count1 = Integer.valueOf(allMessages.get(0));
	    }
	    catch(Exception e){
	    }
	    System.out.println("getMessagecount() count1:"+count1 +"  count0:"+count0);
	}
	
	public void updatePDnewmessages() {
		count0 = count1;
		getMessagecount();
		
		if(count1>count0){
			newMessage = true;
			System.out.println("yes updated");
			updatedfew = count1-count0;
			parameters.clear();
			values.clear();
			parameters.add("pid");  
			parameters.add("did"); 
			parameters.add("topfew"); 
			parameters.add("updateBoth"); 
		    values.add(pid); 
		    values.add(did);
		    values.add( String.valueOf(updatedfew));
		    values.add("1");
		    allMessages = Soap.getWebservice("selectPDtopfewmessages", parameters, values);
			for(int i=allMessages.size()-1;i>=0;i-=3){
				HashMap<String, String> rowHash = new HashMap<String, String>();
				rowHash.put("messagetime", allMessages.get(i-2));
				rowHash.put("messagebody", allMessages.get(i-1));
				rowHash.put("messagedirection", allMessages.get(i));
				messageList.add(rowHash);// for each hashmap add it into list!!
				top++;
			}
		}
		else{
			System.out.println("no updated");
		}
	}
	
	public void insertPDmessagefront(String messagebody) {
		HashMap<String, String> rowHash = new HashMap<String, String>();
		SimpleDateFormat s = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		String time = s.format(new Date());
		//1 add a new message into messageList
		rowHash.put("messagetime",time);
		rowHash.put("messagebody", messagebody);
		rowHash.put("messagedirection", "0");//0 means patient sends one message to doctor
		messageList.add(rowHash);
		System.out.println("INSERT FROUNT");
		
	}
	public void insertPDmessageback(String messagebody) {
		//2 insert a new message into server' database
				parameters.clear();
				values.clear();
				parameters.add("pid");  
				parameters.add("did"); 
				parameters.add("messagebody"); 
				parameters.add("messagedirection");
			    values.add(pid); //pid ?
			    values.add(did); //did ?
			    values.add(messagebody);
			    values.add("0");//patient leave a message to doctor :messagedirection = 0 
				Soap.getWebservice("insertPDmessages", parameters, values);
				top++;
	}
}
