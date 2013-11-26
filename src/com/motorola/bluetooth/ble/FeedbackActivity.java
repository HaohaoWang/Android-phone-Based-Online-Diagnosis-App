package com.motorola.bluetooth.ble;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;
import myServices.ConnectionUtil;
import myServices.FeedbackService;
import myServices.SensorsService;
import myServices.httpSoap;
import mySqlite.DatabaseHelper;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class FeedbackActivity extends Activity{
	//private SharedPreferences sharedPreferences = getSharedPreferences("patientcaselogininfo", Context.MODE_PRIVATE);  
	private httpSoap soap = new httpSoap(); 
	private static ArrayList<String> allMessages = new ArrayList<String>();
	private static int topfew = 10, startindex = 5;
	private ListView listView;
	private Parcelable state;
	private Button sendBtn;
	private EditText editText;
	private Context mContext;
	private ChartAdapter chartAdapter;
	private FeedbackService feedbackService = null;
	private SensorsService sensorsService = new SensorsService();
	private String message = "";
	private Handler handler = new Handler(){   
        public void handleMessage(Message msg) {  
            switch (msg.what) {      
            case 1:      //handle update UI thread
            	listView.setAdapter(chartAdapter); 
                break; 
            case 2:      //handle update UI thread
				 Toast.makeText(mContext, "LOST Conn insert", Toast.LENGTH_SHORT).show();
                break;  
            }      
            super.handleMessage(msg);  
        }  
    };  

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.feedback);
		mContext = this.getApplicationContext();
		listView = (ListView)findViewById(R.id.chart_listView);
		listView.setBackgroundResource(R.drawable.back3); 
		listView.setCacheColorHint(0);
		listView.setDividerHeight(0);
		listView.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				 if(firstVisibleItem==0&&feedbackService.top>0){//scroll to top
					 getListviewmoredata secondshowTask=new getListviewmoredata();
					 secondshowTask.execute();
				 }
				 if(visibleItemCount+firstVisibleItem==totalItemCount){//scroll to bottom
	                }
			}

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				state = listView.onSaveInstanceState();  
		        switch (scrollState) {  
		        case OnScrollListener.SCROLL_STATE_FLING:  
		              
		            break;  
		        case OnScrollListener.SCROLL_STATE_IDLE:  
		              
		            break;  
		        case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:  
		              
		            break;  
		        default:  
		            break;  
		        }  
		        chartAdapter.notifyDataSetChanged();  
				
			}
        });
		state=listView.onSaveInstanceState();
		
		sendBtn = (Button)findViewById(R.id.sendBtn);
		editText = (EditText)findViewById(R.id.editText);
		SharedPreferences sharedPreferences = getSharedPreferences("patientcaselogininfo", Context.MODE_PRIVATE);  
		String pid = sharedPreferences.getString("thepid", null);
		String did = sharedPreferences.getString("thedid", null);
		feedbackService = new FeedbackService(pid,did);
		
		getListviewdata1 firstshowTask=new getListviewdata1();
		firstshowTask.execute();
	    
		Thread update = new Thread(){
			@Override
			public void run() {
				while(true){
				updateMessages updateTask=new updateMessages();
				updateTask.execute();
				try {
					Thread.sleep(6000);//6s is the time period of polling messages
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
				}
			}
		};
		update.start();
		
		/*Thread insert = new Thread(){
			@Override
			public void run() {
				while(true){
				insertSensorThreadToLocalOrServer updateTask=new insertSensorThreadToLocalOrServer();
				updateTask.execute();
				try {
					Thread.sleep(8000);//6s is the time period of polling messages
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
				}
			}
		};
		insert.start();*/
		sendBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				message = editText.getText().toString();
				if(!message.equals("")){
				feedbackService.insertPDmessagefront(message);
				editText.setText("");
				chartAdapter = new ChartAdapter(getApplicationContext(), feedbackService.getMessagehashlist());//change to "messagelist" hashmaplist
				listView.setAdapter(chartAdapter);
				listView.post(new Runnable() {
			        @Override
			        public void run() {
			            listView.setSelection(listView.getCount());
			        }
			    });
				leaveoneMessage syncTask=new leaveoneMessage();
				syncTask.execute();
				}
				
			}
		});
		}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		System.out.println("onPause false");
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		System.out.println("onResume true");
	}

	private class getListviewdata1 extends AsyncTask<String,Integer,ChartAdapter> {
		@Override
		protected ChartAdapter doInBackground(String... params) {
			// TODO Auto-generated method stub
			try{
				System.out.println("fisrt getting");
				feedbackService.selectPDtopfewmessages();
				chartAdapter = new ChartAdapter(getApplicationContext(), feedbackService.getMessagehashlist());//change to "messagelist" hashmaplist
			}
			catch(Exception e){
				System.out.println("fisrt dont get");
			}	
    	    return chartAdapter;
		}
		@Override
		protected void onPostExecute(ChartAdapter chartAdapter) {
			// TODO Auto-generated method stub
			listView.setAdapter(chartAdapter);
			listView.onRestoreInstanceState(state); 
		}
	}
	private class getListviewmoredata extends AsyncTask<String,Integer,ChartAdapter> {
		@Override
		protected ChartAdapter doInBackground(String... params) {
			// TODO Auto-generated method stub
			try{
				 feedbackService.addPDOld10messages();
				 chartAdapter = new ChartAdapter(getApplicationContext(), feedbackService.getMessagehashlist());
			}
			catch(Exception e){
				System.out.println("more dont get");
			}	
    	    return chartAdapter;
		}
		@Override
		protected void onPostExecute(ChartAdapter chartAdapter) {
			// TODO Auto-generated method stub
			listView.setAdapter(chartAdapter);
			listView.onRestoreInstanceState(state);
		}
	}
	private class leaveoneMessage extends AsyncTask<String,Integer,ChartAdapter> {
	@Override
	protected ChartAdapter doInBackground(String... params) {
		// TODO Auto-generated method stub
		try{
			System.out.println("leaveoneMessage");
			feedbackService.insertPDmessageback(message);
		}
		catch(Exception e){
			System.out.println("leaveoneMessage dont get");
		}	
		return chartAdapter;
	}
	@Override
	protected void onPostExecute(ChartAdapter chartAdapter) {
		// TODO Auto-generated method stub
	}
	}
	private class updateMessages extends AsyncTask<String,Integer,ChartAdapter>{
		@Override
		protected ChartAdapter doInBackground(String... params) {
			// TODO Auto-generated method stub
			try{
				System.out.println("updateing Messages");
				feedbackService.updatePDnewmessages();
				if(feedbackService.getnewMessage()){
					ring();
				}
				feedbackService.setnewMessage();
				chartAdapter = new ChartAdapter(getApplicationContext(), feedbackService.getMessagehashlist());//change to "messagelist" hashmaplist
			}
			catch(Exception e){
				System.out.println("catch update Messages");
			}	
			return chartAdapter;
		}
		@Override
		protected void onPostExecute(ChartAdapter chartAdapter) {
			// TODO Auto-generated method stub
			listView.setAdapter(chartAdapter);
			//listView.onRestoreInstanceState(state);
		}
		}
		//test
	private MediaPlayer ring() {
		MediaPlayer mp = new MediaPlayer();
		mp.reset();                    
		try
		{
			// 短信
			mp.setDataSource(this,RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION ));
					
			// 铃声
			//mp.setDataSource(this,RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE));
					
			// 自定义音频文件
			//mp.setDataSource("/mnt/sdcard/AAAAA_CHENJIAN/test/music/1.mp3");
					
			mp.prepare();
			mp.start();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return mp;
		
	}
}
