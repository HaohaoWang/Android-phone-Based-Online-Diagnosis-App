package myServices;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import mySqlite.DatabaseHelper;

import com.motorola.bluetooth.ble.PersonalActivity;
import com.motorola.bluetooth.ble.R;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class ConnectionUtil {

	public static boolean isConn(Context context){
	    boolean bisConnFlag=false;
	    ConnectivityManager conManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo network = conManager.getActiveNetworkInfo();
	    //Internet is available return true
	    if(network!=null&& network.isConnected() ){
	        bisConnFlag=conManager.getActiveNetworkInfo().isAvailable();
	    }
	    //return false
	    return bisConnFlag;
		}
	 /** 打开设置网络界面
	 * */
	public static void setNetworkMethod(final Context context){
	    //提示对话框
	    AlertDialog.Builder builder=new Builder(context);
	    builder.setTitle("Network Settings Reminder").setMessage("Internet is disconnected.Turn on the internet, allow?").setNegativeButton("Yes", new DialogInterface.OnClickListener() {
	        
	        @Override
	        public void onClick(DialogInterface dialog, int which) {
	            // TODO Auto-generated method stub
	            Intent intent=null;
	            //判断手机系统的版本  即API大于10 就是3.0或以上版本 
	            if(android.os.Build.VERSION.SDK_INT>10){
	                intent = new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS);
	            }else{
	                intent = new Intent();
	                ComponentName component = new ComponentName("com.android.settings","com.android.settings.WirelessSettings");
	                intent.setComponent(component);
	                intent.setAction("android.intent.action.VIEW");
	            }
	            context.startActivity(intent);
	        }
	    }).setPositiveButton("No", new DialogInterface.OnClickListener() {
	        
	        @Override
	        public void onClick(DialogInterface dialog, int which) {
	            // TODO Auto-generated method stub
	            dialog.dismiss();
	        }
	    }).show();
		}
	
	private Runnable detectIntenet(final Context context){
	    Runnable aRunnable = new Runnable(){
	        public void run(){
	        	final ConnectivityManager conManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
	        	final NetworkInfo activeNetwork = conManager.getActiveNetworkInfo();
	        	if (activeNetwork != null && activeNetwork.isConnected()) {
	        	    System.out.println("online");
	        	} else {
	        		System.out.println("offline");
	        	} 
	        }
	    };
	    return aRunnable;
	}
	
}
