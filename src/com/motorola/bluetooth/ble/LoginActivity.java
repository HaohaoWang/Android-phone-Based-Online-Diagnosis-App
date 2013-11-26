package com.motorola.bluetooth.ble;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.json.JSONObject;
import org.json.JSONTokener;

import myServices.ConnectionUtil;
import myServices.httpSoap;
import mySqlite.DatabaseHelper;
import mySqlite.Patient;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
public class LoginActivity extends Activity {
	
	private Button btnLogin;
	private EditText edtCprno = null, edtPassword = null;
	private Intent intent=new Intent();
	private httpSoap soap = new httpSoap();  
	private String pid,ppassword;
	private Context mContext;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setting default screen to login.xml
        mContext = this.getApplicationContext();
        requestWindowFeature(Window.FEATURE_NO_TITLE);  
        //ÉèÖÃÈ«ÆÁ  
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,   
                WindowManager.LayoutParams.FLAG_FULLSCREEN); 
        
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) { 
        	setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        	} 
        	else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) { 
        	setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        	}
        
        setContentView(R.layout.login);
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        btnLogin=(Button)findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(buttonlistener);
        
        edtCprno=(EditText)findViewById(R.id.cprno);
        edtPassword=(EditText)findViewById(R.id.password);
        if(!ConnectionUtil.isConn(getApplicationContext())){
            ConnectionUtil.setNetworkMethod(LoginActivity.this);
            System.out.println("need to enable internet");  
        }
        }
    @Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}
	@Override
	//ÕâÀïÓÃÀ´½ÓÊÜÍË³ö³ÌÐòµÄÖ¸Áî 
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
    	super.onNewIntent(intent);
	}
	
	private Button.OnClickListener buttonlistener= new OnClickListener(){

		public void onClick(View v) {
			// TODO Auto-generated method stub
			List<Patient> patientList = new ArrayList<Patient>();
			boolean pass = false;
			boolean exist = false;
			pid=edtCprno.getText().toString().trim();
			ppassword=edtPassword.getText().toString().trim();
			DatabaseHelper dbHelper = new DatabaseHelper(LoginActivity.this,"ods");
			SQLiteDatabase db = dbHelper.getWritableDatabase();//create a database
			patientList = dbHelper.getPatientMapFromLocalDb(db);//get patient info from local database.
			System.out.println("LOGIN Button:");
			for(int i=0;i<patientList.size();i++){
				if(patientList.get(i).getPid().equals(pid)){
					exist = true;
					if(patientList.get(i).getPpassword().equals(ppassword)){
						pass = true;		
			    	    SharedPreferences sharedPreferences = getSharedPreferences("patientcaselogininfo", Context.MODE_PRIVATE); 
						Editor editor=sharedPreferences.edit();
				        editor.putString("thepid",pid);//store the pid into sharedPreferences, then get it in personal activity
				        editor.putString("thedid",patientList.get(i).getdid());
				        editor.commit();
						System.out.println("LOGIN= for==============:"+patientList.get(i).getPid()+" "+patientList.get(i).getPpassword());
					} 
				}
			}
		
			// the patient's info is already stored in he local database
			if(exist==true&&pass==true){
 				intent.setClass(LoginActivity.this, MainActivity.class);
 				System.out.println("LOGIN localy:");
 				LoginActivity.this.startActivity(intent);
			}
			// the patient's info is already stored in he local database but the password typed is not right
			if(exist==true&&pass==false){
				Toast.makeText(mContext, "Wrong ID or Password.", Toast.LENGTH_SHORT).show();
			}
			// the patient's info is not stored in he local database, means this is the first time patient tries to login
			if(exist==false){
				System.out.println("LOGIN Server:");
				getPersoninfo syncTask=new getPersoninfo();
				syncTask.execute();
			}

	
		}
    	
    };
    //get info from server database
    public class getPersoninfo extends AsyncTask<String,Integer,HashMap<String , String>> {
		
		@Override
		protected HashMap<String , String> doInBackground(String... params) {
			// TODO Auto-generated method stub
			HashMap<String , String> patintInfoMap = new HashMap<String , String>();  
			List<String> patientCaselist = new ArrayList<String>();
			ArrayList<String> parameters = new ArrayList<String>();  
			ArrayList<String> value = new ArrayList<String>();  
			try{
				pid=edtCprno.getText().toString().trim();
				ppassword=edtPassword.getText().toString().trim();
	    	    parameters.add("pid");  
	    	    value.add(pid); 
	    	    //get the json object from server and convert it to a hashmap 
	    	    patientCaselist  = soap.getWebservice("selectPatientcasejson", parameters, value); 
	    	    System.out.println("woqu2:"+patientCaselist.get(0));//
	    	    JSONObject personInfoObj = (JSONObject) new JSONTokener(patientCaselist.get(0)).nextValue();
	    	    patintInfoMap.put("pid", personInfoObj.getString("pid"));
	    	    patintInfoMap.put("pname", personInfoObj.getString("pname"));
	    	    patintInfoMap.put("pbirthday", personInfoObj.getString("pbirthday"));
	    	    patintInfoMap.put("pgender", personInfoObj.getString("pgender"));
	    	    patintInfoMap.put("pdepartment", personInfoObj.getString("pdepartment"));
	    	    patintInfoMap.put("pstate", personInfoObj.getString("pstate"));
	    	    patintInfoMap.put("datein", personInfoObj.getString("datein"));
	    	    patintInfoMap.put("dateout", personInfoObj.getString("dateout"));
	    	    patintInfoMap.put("pjob", personInfoObj.getString("pjob"));
	    	    patintInfoMap.put("pmail", personInfoObj.getString("pmail"));
	    	    patintInfoMap.put("pphone", personInfoObj.getString("pphone"));
	    	    patintInfoMap.put("pstreet", personInfoObj.getString("pstreet"));
	    	    patintInfoMap.put("paddress", personInfoObj.getString("paddress"));
	    	    patintInfoMap.put("pcity", personInfoObj.getString("pcity"));
	    	    patintInfoMap.put("pcode", personInfoObj.getString("pcode"));
	    	    patintInfoMap.put("ppassword", personInfoObj.getString("ppassword"));
	    	    patintInfoMap.put("pbool", personInfoObj.getString("pbool")); 
	    	    String did = personInfoObj.getString("did");
	    	    patintInfoMap.put("did", did);
	    	    
	    	    SharedPreferences sharedPreferences = getSharedPreferences("patientcaselogininfo", Context.MODE_PRIVATE); 
				Editor editor=sharedPreferences.edit();
		        editor.putString("thepid",pid);//store the pid into sharedPreferences, then get it in personal activity
		        editor.putString("thedid",did);
		        editor.commit();
	    	   /* Iterator<Entry<String, String>> entryKeyIterator = patintInfoMap.entrySet().iterator();  
	    	       while (entryKeyIterator.hasNext()) {  
	    	            Entry<String, String> e = entryKeyIterator.next();  
	    	            System.out.println(e.getValue());  
	    	 }  */
			}
			catch(Exception e){
				System.out.println("e:"+e.toString());
			}	
			return patintInfoMap;
		}
		@Override
		protected void onPostExecute(HashMap<String , String> patintInfoMap) {
			// TODO Auto-generated method stub
			try{
				DatabaseHelper dbHelper = new DatabaseHelper(LoginActivity.this,"ods");
				SQLiteDatabase db = dbHelper.getWritableDatabase();//create a database
				if(ppassword.equals(patintInfoMap.get("ppassword"))){
					
	 				ContentValues values = new ContentValues();
	 				values.put("pid", 		patintInfoMap.get("pid"));
	 				values.put("pname", 	patintInfoMap.get("pname"));
	 				values.put("pbirthday", patintInfoMap.get("pbirthday"));
	 				values.put("pgender", 	patintInfoMap.get("pgender"));
	 				values.put("pdepartment", patintInfoMap.get("pdepartment"));
	 				values.put("pstate", 	patintInfoMap.get("pstate"));
	 				values.put("datein", 	patintInfoMap.get("datein"));
	 				values.put("dateout", 	patintInfoMap.get("dateout"));
	 				values.put("pjob", 		patintInfoMap.get("pjob"));
	 				values.put("pmail", 	patintInfoMap.get("pmail"));
	 				values.put("pphone", 	patintInfoMap.get("pphone"));
	 				values.put("pstreet", 	patintInfoMap.get("pstreet"));
	 				values.put("paddress",  patintInfoMap.get("paddress"));
	 				values.put("pcity", 	patintInfoMap.get("pcity"));
	 				values.put("pcode", 	patintInfoMap.get("pcode"));
	 				values.put("ppassword", patintInfoMap.get("ppassword"));
	 				values.put("pbool", 	patintInfoMap.get("pbool"));
	 				values.put("did", 	patintInfoMap.get("did"));
	 				db.insert("patientcase", null, values);//insert this patient info into local database android.database.sqlite.SQLiteConstraintException: error code//
	 				System.out.println("I can login:"+patintInfoMap.get("ppassword"));//
	 				dbHelper.close();
	 				db.close();
	 				intent.setClass(LoginActivity.this, MainActivity.class);
	 				LoginActivity.this.startActivity(intent);
	 				}
	 			else{
	 				System.out.println("can not login");
	 				Toast.makeText(mContext, "Wrong ID or Password!", Toast.LENGTH_SHORT).show();
	 				dbHelper.close();
	 				db.close();
	 	        	}
			}
			catch(Exception e){
			}
		}
	}
	private class createlocalData extends AsyncTask<String,Integer,String> {
	@Override
	protected String doInBackground(String... params) {
		return null;
	}
	@Override
	protected void onPostExecute(String string) {
		// TODO Auto-generated method stub
	}
	}
}