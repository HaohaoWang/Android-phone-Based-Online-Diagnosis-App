package com.motorola.bluetooth.ble;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import mySqlite.DatabaseHelper;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class PersonalActivity extends Activity{
	List<HashMap<String, String>> infoList = new ArrayList<HashMap<String, String>>(); 
	SimpleAdapter adapter = null;// Only accessible in main thread ,只用main里能访问
	String pid;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.personal);
		SharedPreferences sharedPreferences = getSharedPreferences("patientcaselogininfo", Context.MODE_PRIVATE);  
		//MyRunnable myRunnable = new MyRunnable();
	    //Thread myThread = new Thread(myRunnable);
	    //myThread.run();
	    myAsynctask syncTask=new myAsynctask();
		syncTask.execute(sharedPreferences.getString("thepid", null));
	}
	
	public class myAsynctask extends AsyncTask<String,Integer,List<HashMap<String, String>>> {
		
		@Override
		protected List<HashMap<String, String>> doInBackground(String... pid) {
			// TODO Auto-generated method stub
			DatabaseHelper dbHelper = new DatabaseHelper(PersonalActivity.this,"ods");
			SQLiteDatabase db = dbHelper.getWritableDatabase();
			try{
				infoList = dbHelper.getPatientlisthash(db, pid[0]);
				System.out.println("HERE:"+pid[0]);
			return infoList;
			}
			catch(Exception e){
				System.out.println(e.toString());
				return null;
			}
			finally{
				dbHelper.close();
				db.close();
			}
		}

		@Override
		protected void onPostExecute(List<HashMap<String, String>> result) {
			// TODO Auto-generated method stub
			//super.onPostExecute(result);
			ListView list = (ListView) findViewById(R.id.listview); 
			list.setCacheColorHint(0);
			list.setDividerHeight(0);
    		try{
    			//adapter = new SimpleAdapter(null, result, 0, null, null);
        	    adapter = new SimpleAdapter(PersonalActivity.this, //没什么解释  
        	    		result,//数据来源   
        	            R.layout.personal_listview,//ListItem的XML实现                                   
        	            //动态数组与ListItem对应的子项          
        	            new String[] {"pid", "pname","pbirthday","pgender","pdepartment","pstate","datein","dateout","pjob","pmail","pphone","pstreet","paddress","pcity","pcode"},  
        	            //ListItem的XML文件里面的两个TextView ID  
        	            new int[] {R.id.pid,R.id.pname,R.id.pbirthday,R.id.pgender,R.id.pdepartment,R.id.pstate,R.id.pdatein,R.id.pdateout,R.id.pjob,R.id.pmail,R.id.pphone,R.id.pstreet,R.id.paddress,R.id.pcity,R.id.pcode});  
        	    		//添加并且显示  
        	    		list.setAdapter(adapter); 
        		}
        		catch(Exception e){
        		System.out.println(e.toString());
        		}
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}
	}
	
    public class MyRunnable implements Runnable{ // my thread 
    	List<HashMap<String, String>> infoList = new ArrayList<HashMap<String, String>>(); 
		ListView list = (ListView) findViewById(R.id.listview); 
        public void run() {  
            // TODO Auto-generated method stub  
    		//infoList = dtaService.selectAllitems();
        	DatabaseHelper dbHelper = new DatabaseHelper(PersonalActivity.this,"ods");
			SQLiteDatabase db = dbHelper.getReadableDatabase();
			try{
				infoList = dbHelper.getPatientlisthash(db, pid);
				adapter = new SimpleAdapter(PersonalActivity.this, //没什么解释  
    	    		infoList,//数据来源   
    	            R.layout.personal_listview,//ListItem的XML实现                                   
    	            //动态数组与ListItem对应的子项          
    	            new String[] {"pid", "pname","pbirtahday","pgender","pdepartment","pstate","datein","dateout","pjob","pmail","pphone","pstreet","paddress","pcity","pcode"},  
    	            //ListItem的XML文件里面的两个TextView ID  
    	            new int[] {R.id.pid,R.id.pname,R.id.pbirthday,R.id.pgender,R.id.pdepartment,R.id.pstate,R.id.pdatein,R.id.pdateout,R.id.pjob,R.id.pmail,R.id.pphone,R.id.pstreet,R.id.paddress,R.id.pcity,R.id.pcode});  
    	    		//添加并且显示  
    	    		list.setAdapter(adapter); 
    		}
    		catch(Exception e){
    		}
        }  
    } 

}

