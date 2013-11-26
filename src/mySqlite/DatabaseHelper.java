package mySqlite;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper{

	private static final int VERSION = 3;
	public DatabaseHelper(Context context, String name, CursorFactory factory,int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}
	public DatabaseHelper(Context context, String name, int version) {
		this(context, name, null, version);
		// TODO Auto-generated constructor stub
	}
	public DatabaseHelper(Context context, String name) {
		this(context, name, null, VERSION);
		// TODO Auto-generated constructor stub
	}
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		
		String sqlpatient="create table  IF NOT EXISTS patientcase(pid char(11) not null primary key,pname char(30),pbirthday char(25),pgender char(6),pdepartment char(40),pstate smallint,datein char(25),dateout char(25),pjob varchar(80),pmail varchar(40),pphone varchar(15),pstreet varchar(20),paddress varchar(20), pcity varchar(20),pcode char(4),ppassword varchar(12),pbool char(5),did char(11));";
		db.execSQL(sqlpatient); 
		String sqlsensor="create table IF NOT EXISTS sensors(pid char(11), time datetime, data1 char(10), data2 char(10), data3 char(10));";
		db.execSQL(sqlsensor); 
		System.out.println("now create 2 databases table oncreate");//IF NOT EXISTS//
		
	}

	@Override
	public void onOpen(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		super.onOpen(db);
		
		String sqlpatient="create table  IF NOT EXISTS patientcase(pid char(11) not null primary key,pname char(30),pbirthday char(25),pgender char(6),pdepartment char(40),pstate smallint,datein char(25),dateout char(25),pjob varchar(80),pmail varchar(40),pphone varchar(15),pstreet varchar(20),paddress varchar(20), pcity varchar(20),pcode char(4),ppassword varchar(12),pbool char(5),did char(11));";
		db.execSQL(sqlpatient); 
		String sqlsensor="create table IF NOT EXISTS sensors(pid char(11), time datetime, data1 char(10), data2 char(10), data3 char(10));";
		db.execSQL(sqlsensor); 
		System.out.println("now create 2 databases table onopen");//IF NOT EXISTS//
		
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		System.out.println("update a database");
	}
	
	//insert data to local database
	public void insertSensordataToLocalDb(String pid,String datetime,String data1,String data2,String data3){
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("insert into sensors (pid,time,data1,data2,data3) values(?,?,?,?,?)", new Object[] { pid, datetime,data1,data2,data3});  
	}
	
	public String getRemainedSensordataList(String pid, String startDatetime,String endDatetime){
		SQLiteDatabase db = this.getWritableDatabase();
		String remainedjson="";
		JSONArray dataList = new JSONArray();  
		try {
			Cursor cursor = db.rawQuery("select * from sensors where pid=?  and time>=Datetime(?) and time<Datetime(?)",new String[] {pid,startDatetime,endDatetime});  
			cursor.moveToFirst();
			int i=0;
			while (cursor.isAfterLast() == false){
				
				JSONObject data = new JSONObject(); 
				data.put("pid", pid);
				data.put("time", cursor.getString(cursor.getColumnIndex("time")));
				data.put("data1", cursor.getString(cursor.getColumnIndex("data1")));
				data.put("data2", cursor.getString(cursor.getColumnIndex("data2")));
				data.put("data3", cursor.getString(cursor.getColumnIndex("data3")));
				dataList.put(data);
				
				cursor.moveToNext();
			}
			
			remainedjson=dataList.toString();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		System.out.println("json=======:"+remainedjson);
		return remainedjson;
	}
	//Patientcsae table part:: 
	//get patient info from local database
	public List<Patient> getPatientMapFromLocalDb(SQLiteDatabase db){
		List<Patient> patientList = new ArrayList<Patient>();
		Cursor cursor = db.rawQuery("select * from patientcase",null);  
		cursor.moveToFirst();
		while (cursor.isAfterLast() == false) 
		{
			Patient p = new Patient(
					cursor.getString(cursor.getColumnIndex("pid")), cursor.getString(cursor.getColumnIndex("pname")), 
					cursor.getString(cursor.getColumnIndex("pbirthday")), cursor.getString(cursor.getColumnIndex("pgender")), 
					cursor.getString(cursor.getColumnIndex("pdepartment")), cursor.getString(cursor.getColumnIndex("pstate")), 
					cursor.getString(cursor.getColumnIndex("datein")), cursor.getString(cursor.getColumnIndex("dateout")), 
					cursor.getString(cursor.getColumnIndex("pjob")), cursor.getString(cursor.getColumnIndex("pmail")), 
					cursor.getString(cursor.getColumnIndex("pphone")), cursor.getString(cursor.getColumnIndex("pstreet")), 
					cursor.getString(cursor.getColumnIndex("paddress")), cursor.getString(cursor.getColumnIndex("pcity")), 
					cursor.getString(cursor.getColumnIndex("pcode")), cursor.getString(cursor.getColumnIndex("ppassword")), 
					cursor.getString(cursor.getColumnIndex("pbool")), cursor.getString(cursor.getColumnIndex("did")));
			patientList.add(p);
		    cursor.moveToNext();
		}
           
		return patientList;
	}
	//get patient info in to a list, then show it in the personal activity
	public List<HashMap<String, String>> getPatientlisthash(SQLiteDatabase db,String pid){
		List<HashMap<String, String>> patientListhash = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> rowHash = new HashMap<String, String>();//Spouse to put this line in to for loop
		//Cursor cursor = db.query("patientcase", new String[]{"pid","pname","pbirthday","pgender","pdepartment","pstate","datein","dateout","pjob","pmail","pphone","pstreet","paddress","pcity","pcode","ppassword","pbool"}, "pid=?", new String[]{pid} , null, null, null);
		//Cursor cursor = db.rawQuery("select pid,pname,pbirthday,pgender,pdepartment,pstate,datein,dateout,pjob,pmail,pphone,pstreet,paddress,pcity,pcode,ppassword,pbool from patientcase where pid=?", new String[]{pid});//select * from patientcase where pid=pid;
		Cursor cursor = db.rawQuery("select * from patientcase where pid=?",new String[] {pid});  
		try {	
			cursor.moveToFirst();
			String pbirthday = cursor.getString(cursor.getColumnIndex("pbirthday"));
			String pdatein = cursor.getString(cursor.getColumnIndex("datein"));
			String pdateout = cursor.getString(cursor.getColumnIndex("dateout"));
			
			rowHash.put("pid",cursor.getString(cursor.getColumnIndex("pid"))); 
			rowHash.put("pname",cursor.getString(cursor.getColumnIndex("pname")));
			rowHash.put("pbirthday",pbirthday.substring(0, pbirthday.length()-12));
			rowHash.put("pgender",cursor.getString(cursor.getColumnIndex("pgender")));
			rowHash.put("pdepartment",cursor.getString(cursor.getColumnIndex("pdepartment")));
			rowHash.put("pstate",cursor.getString(cursor.getColumnIndex("pstate")));
			rowHash.put("datein",pdatein.substring(0,pdatein.length()-12));
			rowHash.put("dateout",pdateout.substring(0, pdateout.length()-12));
			rowHash.put("pjob",cursor.getString(cursor.getColumnIndex("pjob")));
			rowHash.put("pmail",cursor.getString(cursor.getColumnIndex("pmail")));
			rowHash.put("pphone",cursor.getString(cursor.getColumnIndex("pphone")));
			rowHash.put("pstreet",cursor.getString(cursor.getColumnIndex("pstreet")));
			rowHash.put("paddress",cursor.getString(cursor.getColumnIndex("paddress")));
			rowHash.put("pcity",cursor.getString(cursor.getColumnIndex("pcity")));
			rowHash.put("pcode",cursor.getString(cursor.getColumnIndex("pcode")));
			//rowHash.put("ppassword",cursor.getString(cursor.getColumnIndex("ppassword")));
			//rowHash.put("pbool",cursor.getString(cursor.getColumnIndex("pbool")));
			//rowHash.put("did",cursor.getString(cursor.getColumnIndex("did")));
			patientListhash.add(rowHash);
			db.close();
			//System.out.println("can get loacal database:"+patientListhash.get(0).get("pid"));
			System.out.println("I I can get local database did:"+cursor.getString(cursor.getColumnIndex("did")));
			return patientListhash;
		}
		catch(Exception e){
			db.close();
			System.out.println("can not get loacal database;");
			return patientListhash;
        }
	}
	
	public boolean tabbleIsExist(String tableName,String dbName){
        boolean result = false;
        if(tableName == null){
                return false;
        }
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
                db = this.getReadableDatabase();
                String sql = "select count(*) as c from "+dbName+" where type ='table' and name ='"+tableName.trim()+"' ";
                cursor = db.rawQuery(sql, null);
                if(cursor.moveToNext()){
                        int count = cursor.getInt(0);
                        if(count>0){
                                result = true;
                        }
                }
                
        } catch (Exception e) {
                // TODO: handle exception
        }               
        return result;
	}
	
	public boolean regi1ster(SQLiteDatabase db,String cprno0,String password0,String email0){
		int flag=0;
		Cursor cursor = db.query("users", new String[]{"id","cprno","password","email"}, null, new String[]{} , null, null, null);	
		try {	
			cursor.moveToFirst();
			String cprno = cursor.getString(cursor.getColumnIndex("cprno"));
			String email = cursor.getString(cursor.getColumnIndex("email"));
				if(cprno0.equals(cprno)||email.equals(email0)){
					flag=1;
					System.out.println("already exsit no:"+cprno+" email:"+email);
					}
		while(cursor.moveToNext()){
			cprno = cursor.getString(cursor.getColumnIndex("cprno"));
			email = cursor.getString(cursor.getColumnIndex("email"));
				if(cprno0.equals(cprno)||email.equals(email0)){
					flag=1;
					System.out.println("already exsit no:"+cprno+" email:"+email);
					}			
			}
		cursor.close();
			} catch (Exception e) {
			} finally {
			}
		
        if (flag == 1) {
            System.out.println("can not register a new one:");
            db.close();///new
            return false;
        } else {
        	ContentValues values = new ContentValues();
			values.put("cprno", cprno0);
			values.put("password", password0);
			values.put("email", email0);
			db.insert("users", null, values);
			System.out.println("now register a new one:");
			db.close();///new
            return true;
        }
        
		
	}

}
