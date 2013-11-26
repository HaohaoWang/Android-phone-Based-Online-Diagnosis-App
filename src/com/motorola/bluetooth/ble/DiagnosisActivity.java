package com.motorola.bluetooth.ble;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.motorola.bluetooth.ble.LoginActivity.getPersoninfo;

import myServices.httpSoap;
import mySqlite.DatabaseHelper;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.Toast;

public class DiagnosisActivity extends Activity {
 
    List<String> groupList = new ArrayList<String>();
    List<String> childList;
    Map<String, List<String>>  diagnosisCollection = new LinkedHashMap<String,List<String>>();
    ExpandableListView expListView;
    ListView list;
    DiagnosisListAdapter expListAdapter;
    SimpleAdapter adapter = null;
    boolean flage = false;
    private httpSoap soap = new httpSoap();  
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diagnosis_activity);
        getDiagnoses syncTask=new getDiagnoses();
		syncTask.execute();
		
		
        expListView = (ExpandableListView) findViewById(R.id.laptop_list);
        expListView.setCacheColorHint(0);
        expListView.setDividerHeight(0);
        list = (ListView) findViewById(R.id.doctor); 
		list.setCacheColorHint(0);
		list.setDividerHeight(0);
		list.setVisibility(8);//Invisible
        //expListView.setBackgroundResource(R.drawable.activityback3); 
        /*expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {  
            @Override  
        public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {  
            	flage = !flage;
            	if((flage == true)&&(groupPosition == expListAdapter.getGroupCount()-1)){
            		list.setVisibility(8);
            	}
            	if((flage != true)&&(groupPosition == expListAdapter.getGroupCount()-1)){
            		list.setVisibility(0);
            	}
				return true;
            }  
        });*/
        expListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
			
			@Override
			public void onGroupCollapse(int groupPosition) {
            	if(groupPosition == expListAdapter.getGroupCount()-1){
            		list.setVisibility(8);
            	}
			}
		});
        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
			
			@Override
			public void onGroupExpand(int groupPosition) {
				if(groupPosition == expListAdapter.getGroupCount()-1){
            		list.setVisibility(0);
            	}
			}
		});
        
        
        /*expListView.setOnChildClickListener(new OnChildClickListener() {
        	 
            public boolean onChildClick(ExpandableListView parent, View v,
                    int groupPosition, int childPosition, long id) {
                final String selected = (String) expListAdapter.getChild(
                        groupPosition, childPosition);
                Toast.makeText(getBaseContext(), selected, Toast.LENGTH_LONG)
                        .show();
 
                return true;
            }
        });*/
    }
 
    private void setGroupIndicatorToRight() {
        /* Get the screen width */
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
 
        expListView.setIndicatorBounds(width - getDipsFromPixel(35), width
                - getDipsFromPixel(5));
    }
 
    // Convert pixel to dip
    public int getDipsFromPixel(float pixels) {
        // Get the screen's density scale
        final float scale = getResources().getDisplayMetrics().density;
        // Convert the dps to pixels, based on density scale
        return (int) (pixels * scale + 0.5f);
    }
 
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    public class getDiagnoses extends AsyncTask<String,Integer,HashMap<String , String>> {

  		@Override
  		protected HashMap<String, String> doInBackground(String... arg0) {
  			List<String> diagnosisList = new ArrayList<String>();
  			
  			ArrayList<String> parameters = new ArrayList<String>();  
  			ArrayList<String> value = new ArrayList<String>(); 
  			SharedPreferences sharedPreferences = getSharedPreferences("patientcaselogininfo", Context.MODE_PRIVATE);  
  			parameters.add("pid");  
      	    value.add(sharedPreferences.getString("thepid", null));
      	    try {
      	    diagnosisList  = soap.getWebservice("selectDiagnosesJson", parameters, value);
      	    } catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
      	    try {
      	    	
				JSONArray diagnosisCaseArray = new JSONArray(diagnosisList.get(0));
				for (int i = 0; i < diagnosisCaseArray.length(); i++) {  
				    JSONObject diagnosisObj = (JSONObject) diagnosisCaseArray.get(i);  
				    String date0 = diagnosisObj.getString("date");  
				    String date = date0.substring(0, date0.length()-12);
				    String diagnosis = diagnosisObj.getString("diagnosis");  
				    groupList.add(date+"   Diagnosis" );
					childList = new ArrayList<String>();
					childList.add(diagnosis);
					diagnosisCollection.put(date+"   Diagnosis" , childList);
				}  
				groupList.add("Doctor Information");
				childList = new ArrayList<String>();
				childList.add(null);
				diagnosisCollection.put("Doctor Information" , childList);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
  			return null;
  		}

		@Override
		protected void onPostExecute(HashMap<String, String> result) {
			expListAdapter = new DiagnosisListAdapter(DiagnosisActivity.this, groupList, diagnosisCollection);
	        expListView.setAdapter(expListAdapter);
	        doctorAsynctask syncTask1=new doctorAsynctask();
			syncTask1.execute();
		}
      }
public class doctorAsynctask extends AsyncTask<String,Integer,List<HashMap<String, String>>> {
		
		@Override
		protected List<HashMap<String, String>> doInBackground(String... string) {
			// TODO Auto-generated method stub
			SharedPreferences sharedPreferences = getSharedPreferences("patientcaselogininfo", Context.MODE_PRIVATE);  
			String did = sharedPreferences.getString("thedid", null);
			List<String> patientCaselist = new ArrayList<String>();
			ArrayList<String> parameters = new ArrayList<String>();  
			ArrayList<String> value = new ArrayList<String>();  
			parameters.add("did");  
    	    value.add(did); 
    	    
    	    List<HashMap<String, String>> result = new ArrayList<HashMap<String, String>>(); 
    	    HashMap<String, String> rowHash = new HashMap<String, String>();
			try{
				patientCaselist  = soap.getWebservice("selectDoctorcaseJson", parameters, value); 
				JSONObject doctorInfoObj = (JSONObject) new JSONTokener(patientCaselist.get(0)).nextValue();//always 0 for JSON string
				rowHash.put("did",doctorInfoObj.getString("did")); 
				rowHash.put("dname",doctorInfoObj.getString("dname"));
				String pbirthday= doctorInfoObj.getString("dbirthday");
				rowHash.put("dbirthday",pbirthday.substring(0, pbirthday.length()-12));
				rowHash.put("dgender",doctorInfoObj.getString("dgender"));
				rowHash.put("ddepartment",doctorInfoObj.getString("ddepartment"));
				rowHash.put("dmail",doctorInfoObj.getString("dmail"));
				String phoneNumber = doctorInfoObj.getString("dphone");
				rowHash.put("dphone", phoneNumber);
				rowHash.put("dstreet",doctorInfoObj.getString("dstreet"));
				rowHash.put("daddress",doctorInfoObj.getString("daddress"));
				rowHash.put("dcity",doctorInfoObj.getString("dcity"));
				rowHash.put("dcode",doctorInfoObj.getString("dcode"));
				result.add(rowHash);
				expListAdapter.setNumber(phoneNumber);
				return result;
			}
			catch(Exception e){
				System.out.println(e.toString());
				rowHash.put("server error","server error");
				result.add(rowHash);
				return result;
			}
		}

		@Override
		protected void onPostExecute(List<HashMap<String, String>> result) {
			// TODO Auto-generated method stub
    		try{
        	    adapter = new SimpleAdapter(DiagnosisActivity.this, //没什么解释  
        	    		result,//数据来源   
        	            R.layout.doctor_listview,//ListItem的XML实现                                   
        	            //动态数组与ListItem对应的子项          
        	            new String[] {"did", "dname","dbirthday","dgender","ddepartment","dmail","dphone","dstreet","daddress","dcity","dcode"},  
        	            //ListItem的XML文件里面的两个TextView ID  
        	            new int[] {R.id.did,R.id.dname,R.id.dbirthday,R.id.dgender,R.id.ddepartment,R.id.dmail,R.id.dphone,R.id.dstreet,R.id.daddress,R.id.dcity,R.id.dcode});  
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
}