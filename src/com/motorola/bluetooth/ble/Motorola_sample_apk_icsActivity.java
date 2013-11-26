package com.motorola.bluetooth.ble;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;

import com.motorola.bluetoothle.BluetoothGatt;
import android.bluetooth.BluetoothGattService;
import android.os.ParcelUuid;
import com.motorola.bluetooth.bluetoothle.IBluetoothGattCallback; //This file needs to be created
import android.bluetooth.IBluetoothGattProfile; //This file needs to be created

import com.motorola.bluetooth.ble.R;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.SharedPreferences;

import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Arrays;
import java.util.Iterator;
import java.util.TimeZone;

import android.os.Handler;
import java.util.ArrayList;

import myServices.ConnectionUtil;
import myServices.SensorsService;
import mySqlite.DatabaseHelper;

import org.achartengine.GraphicalView;

import android.os.Message;

public class Motorola_sample_apk_icsActivity extends Activity {
    /** Called when the activity is first created. */
	private static final String  TAG = "BluetoothLe";
    private static final String className = "android.bluetooth.BluetoothGattService";
    private static final boolean DEBUG = true;
     // Bluetooth Intent request codes
    private static final int REQUEST_ENABLE_BT = 2;
    public static final int GATT_CONNECTION_STATE = 3;
    public static final int GATT_READ_VALUE = 4;  
    public static final int GATT_VALUECHANGED = 5; 
  
    // Local Bluetooth adapter
    private BluetoothAdapter mBluetoothAdapter = null;
    private BluetoothDevice mDevice = null;
	private IBluetoothGattCallback mGattCallback = null;
	
	private LineChart accLinechart = new LineChart();
	private LineChartStretch stretchLinechart = new LineChartStretch();
	private LineChartTemp tempLinechart = new LineChartTemp();
	private LinearLayout accLinearLayout,stretchLinearLayout,tempLinearLayout;
	private GraphicalView accGraphicalview,stretchGraphicalview,tempGraphicalview; 
	
	private int axisx,axisy,axisz,stretch,temperature;
	private Context context; 
	
    private TextView   mTvState2,Value,Value_Stretch,Value_Temp,Discover,Broad,Write,Writebutton;
    String uuidpaths="",writeuuidpaths="",valuedata="";

 
    private Context mContext;
	private IntentFilter filter_scan;
	
    private static final int CONNECTED = 1;
    private static final int CONNECTING = 2;
    private static final int DISCONNECTED = 3;
    private static final int DISCONNECTING = 4;
    private static final int LOST_CONNECTION = 6;
    private static final int HAVE_CONNECTION = 7;
    private static final int INIT = -1;
    
	boolean leDisconn = true;
	boolean ifPhoneSupportsLE = false;
	boolean flag_leRcvrReg = false;
	boolean isDevicePickerPending = false;

	private static String  currentDatetime="",  startDatetime ="", endDatetime="";
	private static int connCount= 0, notconnCount= 0;
	private boolean isConncted = true;
	private String pid = "";
	
    public int mLeState = INIT;
    public static final ParcelUuid HRM = ParcelUuid.fromString("0000ffa0-0000-1000-8000-00805f9b34fb");
    public static final ParcelUuid UUID_HEART_RATE = ParcelUuid.fromString("0000ffa3-0000-1000-8000-00805f9b34fb");
    public static final ParcelUuid UUID_SENSOR_LOCATION = ParcelUuid.fromString("0000ffa3-0000-1000-8000-00805f9b34fb");
    //public static final ParcelUuid UUID_HEART_RATE1 = ParcelUuid.fromString("0000ffa4-0000-1000-8000-00805f9b34fb");
    //public static final ParcelUuid UUID_HEART_RATE2 = ParcelUuid.fromString("0000ffa5-0000-1000-8000-00805f9b34fb");
    public static final ParcelUuid ACCEL_ENABLER_UUID = ParcelUuid.fromString("0000ffa1-0000-1000-8000-00805f9b34fb");

    public static final ParcelUuid STRTCH_SERVICE = ParcelUuid.fromString("0000fff0-0000-1000-8000-00805f9b34fb");
    public static final ParcelUuid STRTCH_ENABLER = ParcelUuid.fromString("0000fff1-0000-1000-8000-00805f9b34fb");
    public static final ParcelUuid STRTCH_ROWDATA = ParcelUuid.fromString("0000fff2-0000-1000-8000-00805f9b34fb");
   
	public HashMap<ParcelUuid, BluetoothGattService> uuidGattSrvMap = null;
	public HashMap<String, ParcelUuid> objPathUuidMap;
	public HashMap<ParcelUuid, String> uuidObjPathMap;
	private static int[] xyz = new int[3];
	private SensorsService sensorsService = new SensorsService();
	public final Handler msgHandler = new Handler() {
		 @Override
		 public void handleMessage(Message msg) {
			 switch (msg.what) {
			 case GATT_CONNECTION_STATE:
				 String text = (String)msg.obj;
				 mTvState2.setText(text);
				 enableSensor1();
				 enableSensor1Notification();
				 break;
			 case GATT_READ_VALUE:
				 String text1 = (String)msg.obj;
				// mTvRead.setText(text1);
				 break;
			 case GATT_VALUECHANGED:
				 String value = (String)msg.obj;
				 int x, y, z, stretch, temperature;
				 x = hexConvert(Integer.valueOf(value.substring(0, 2),16));
		         y = hexConvert(Integer.valueOf(value.substring(2, 4),16));
		         z = hexConvert(Integer.valueOf(value.substring(4, 6),16));
		         stretch = Integer.valueOf(value.substring(6, 8),16);
		       	 temperature =Integer.valueOf(value.substring(8, 10),16);	
		            
		            xyz[0] = (x+y+z)/3;
		            xyz[1] = stretch;
		            xyz[2] = temperature;
		            Value.setText("X-axis:"+x+"  Y-axis:"+y+"  Z-axis:"+z);//
		            Value_Stretch.setText("Dimension:"+stretch+ " CM");//
		            Value_Temp.setText("Temperature :"+temperature+ " °C");
		            
					accLinechart.updateChart(x,y,z);
					stretchLinechart.updateChart(stretch);
					tempLinechart.updateChart(temperature);
					accGraphicalview.invalidate();
					stretchGraphicalview.invalidate();
					tempGraphicalview.invalidate();    
				//    acclineGraph.addNewPoints(axisx, axisy, axisz); // Add it to our graph
				 break;
			 case HAVE_CONNECTION:      //after gain Internet again send the lost data to server database.
				 if(connCount==1&&notconnCount>0){
				 endDatetime = currentDatetime;
				 insertRemainedSensorDataToServer insertTask=new insertRemainedSensorDataToServer();
				 insertTask.execute();
				 }
				 notconnCount=0;
				 //Toast.makeText(mContext, "LOST Conn insert", Toast.LENGTH_SHORT).show();
				 break;  
			 case LOST_CONNECTION:      //handle update UI thread
				 if(notconnCount==1&&connCount>0){
				 startDatetime = currentDatetime;
				 }
				 connCount=0;
				 Toast.makeText(mContext, "Lost Internet Connnection.", Toast.LENGTH_SHORT).show();
				 break;  
			 }
		 }
	 };
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	SharedPreferences sharedPreferences = getSharedPreferences("patientcaselogininfo", Context.MODE_PRIVATE);  
    	pid = sharedPreferences.getString("thepid", null);
    	mContext = this.getApplicationContext();
        /* Check if Bluetooth Low Energy is supported on phone */
        try 
        {
        Class<?> object = Class.forName(className); 
        ifPhoneSupportsLE = true;

        } catch (Exception e) {
        ifPhoneSupportsLE = false;
        } //End logic to check Low Energy support
        
        if (!ifPhoneSupportsLE) {
        	
        	String message = "Bluetooth Low Energy is not supported on this phone !";
            Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show(); 
            //finish();
            return;
        } else {
     
        mDevice = null;
        // Set up the window layout
        setContentView(R.layout.main);
 
        // Get local Bluetooth adapter
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        accLinearLayout = (LinearLayout)findViewById(R.id.acchatView);
        stretchLinearLayout = (LinearLayout)findViewById(R.id.stretchView);
        tempLinearLayout = (LinearLayout)findViewById(R.id.tempchartView);
        mTvState2 = (TextView)findViewById(R.id.m_state2);
      //  Write = (TextView)findViewById(R.id.Write);
      //  Writebutton = (TextView)findViewById(R.id.Writebutton);
        Value = (TextView)findViewById(R.id.Value);
        Value_Stretch = (TextView)findViewById(R.id.Value1);
        Value_Temp = (TextView)findViewById(R.id.Value2);
        //Broad= (TextView)findViewById(R.id.Broad);
       // Discover = (TextView)findViewById(R.id.Discover);
        
      flag_leRcvrReg = true;
      mContext = this.getApplicationContext(); 
      uuidObjPathMap = new HashMap<ParcelUuid, String>();
      objPathUuidMap = new HashMap<String, ParcelUuid>();
      uuidGattSrvMap = new HashMap<ParcelUuid, BluetoothGattService>();
      filter_scan = new IntentFilter();
      filter_scan.addAction("com.test.StartConnection");
 	  filter_scan.addAction(BluetoothDevice.ACTION_GATT);
 	  registerReceiver(mConn_Receiver, filter_scan);  
 	  
 	  accGraphicalview = accLinechart.getView(this);
 	  accLinearLayout.addView(accGraphicalview);
 	  stretchGraphicalview = stretchLinechart.getView(this);
 	  stretchLinearLayout.addView(stretchGraphicalview);
 	  tempGraphicalview = tempLinechart.getView(this);
	  tempLinearLayout.addView(tempGraphicalview);
 	  
 	  Thread insertSensoo1 = new Thread(){
			@Override
			public void run() {
				while(true){
				insertSensorThreadToLocalOrServer insertTask=new insertSensorThreadToLocalOrServer();
				insertTask.execute();
				try {
					Thread.sleep(10000);//
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
				}
			}
		};
		insertSensoo1.start();
      }
     // thread.start();
    }
  
    @Override
    protected void onStart() {
    	super.onStart();

        if (DEBUG) Log.i(TAG, "onStart()");
        
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        } else {
            // TODO: 
        }        
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        if (DEBUG) Log.i(TAG, "onResume()");
      }
    
    @Override
    protected void onPause() {
    	
        super.onPause();
        if (DEBUG) Log.i(TAG, "onPause()");
    }
    
    @Override
    protected void onStop() {
    	
        super.onStop();
        if (DEBUG) Log.i(TAG, "onStop()");
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (DEBUG) Log.i(TAG, "onDestroy()");
    	
        if(!leDisconn){
        	if(mLeState == CONNECTED){
        		
        		if (mDevice != null) {
        			mLeState = DISCONNECTING;
        			Log.i(TAG, "disconnecting LE");
      				disconnectLe(mDevice);
                }
        	}
      	    leDisconn = true;
        }
        if(flag_leRcvrReg)
        {
        	flag_leRcvrReg = false;
        	unregisterReceiver(mConn_Receiver);
        }
     }
   
    public void startConnection(BluetoothDevice device) {
    	Log.i(TAG,"enter startConnection and value of devicePicker is " + isDevicePickerPending );
    	if (isDevicePickerPending ){
        	isDevicePickerPending = false;
       		connectIfPending(device);
 	    }
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (DEBUG) Log.i(TAG, "onActivityResult() code: " + resultCode);
        
        switch (requestCode) {

        case REQUEST_ENABLE_BT :
            if (resultCode == Activity.RESULT_OK) {
              
            }
            else {
                Log.e(TAG, "onActivityResult(): BT not enabled");
                Toast.makeText(this, R.string.bt_not_enabled_leaving, Toast.LENGTH_SHORT).show();
                finish();
            }
            break;
        }
    }
    
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
    	case R.id.menu_conn_dev2:
    		//thread.resume();
     		startDevicePicker();
    		return true;

		case R.id.menu_exit:
				if(!leDisconn){
				if(mLeState == CONNECTED){
			   	     if (mDevice != null) {
			   	    	 mLeState = DISCONNECTING;
			             disconnectLe(mDevice);
			         }
			   	}
         		leDisconn = true;
			}
			finish();			
			return true;
						
        }
        return false;
    }
 
    private void startDevicePicker() {

        Log.v(TAG, "BT already enabled and launching Settings menu!! "); 
      
        Intent in1 = new Intent("android.bluetooth.devicepicker.action.LAUNCH");
        in1.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        in1.putExtra("android.bluetooth.devicepicker.extra.NEED_AUTH", true);
        in1.putExtra("android.bluetooth.devicepicker.extra.FILTER_TYPE", 0);
        in1.putExtra("android.bluetooth.devicepicker.extra.LAUNCH_PACKAGE", "com.motorola.bluetooth.ble");
        in1.putExtra("android.bluetooth.devicepicker.extra.DEVICE_PICKER_LAUNCH_CLASS", BluetoothLeReceiver.class.getName());
        
        this.startActivity(in1);
      
    }
    private void disconnectLe (BluetoothDevice device) {
   
    	BluetoothGattService gattService = uuidGattSrvMap.get(HRM);
    	if (gattService != null) {
    		try {
    		Log.i(TAG, "Calling gattService.close()");
    		gattService.close();
    		uuidGattSrvMap.remove(HRM);
    		uuidObjPathMap.clear();
    		objPathUuidMap.clear();
    		mDevice = null;
    		mLeState = DISCONNECTED;
    		String text = "Not Connected";
    		//Message message = msgHandler.obtainMessage(GATT_CONNECTION_STATE, text);
    		//msgHandler.sendMessage(message);
    	    } catch (Exception e) {
    	    	Log.e(TAG, "************Error while closing the Gatt Service");
                e.printStackTrace();
    	    }
    	}
     }
    
    private void connectIfPending(BluetoothDevice device){
    
    	if (device == null ) {
    		String message = "Connection failed !";
    		Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
    		return; 
    	}
    	if (mLeState == CONNECTED) {
     		Toast.makeText(mContext,"Device already connected", Toast.LENGTH_SHORT).show();
    		return;
    	}
    	
       	if ((mDevice != null ) && (mDevice.getAddress().equals(device.getAddress()))) {
          	String objPath = uuidObjPathMap.get(HRM);
       		if (objPath != null) {
       			getBluetoothGattService(objPath, HRM);//decide here can change to acc from battery
       		} else {
       			Log.d(TAG, "action GATT has not been received for uuid : " + HRM);
       			getGattServices(HRM, device);
       		}
       	} else {
          mDevice = device;
          if (getGattServices(HRM, device))
              Log.v(TAG,"primary service was successful");
          else
        	  Toast.makeText(mContext, "Connection Failed", Toast.LENGTH_SHORT).show();
       	}         
     }                        

    
    /* This class is used to pass data from the Profile to an Application if needed */
    private class callback extends IBluetoothGattCallback.Stub {
    	private String service;
        public callback(String serv) {
        	service = serv;

        }

        public void indicationGattCb(BluetoothDevice device, String service, String characterstic_handle, String[] data) {
        	Log.i(TAG,"indicationLeCb" );
        }

        public void notificationGattCb(BluetoothDevice device, String service, String characterstic_handle,
        		byte[] data) {
        	Log.i(TAG,"notificationLeCb" );
          }
    }
  
    private final BroadcastReceiver mConn_Receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			String action = intent.getAction();
			BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				
		if (action.equals(BluetoothDevice.ACTION_GATT)) {
				BluetoothDevice remoteDevice = intent
						.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				ParcelUuid uuid = (ParcelUuid)intent.getParcelableExtra(BluetoothDevice.EXTRA_UUID);
				String[] ObjectPathArray = (String[])intent.getStringArrayExtra(BluetoothDevice.EXTRA_GATT);
					
				if (ObjectPathArray != null) {
					ArrayList<String> gattDataList = new ArrayList<String>(
							Arrays.asList(ObjectPathArray));
					int size = gattDataList.size();
					Log.d(TAG, "GATT Service data list len : " + size);
					String selectedServiceObjPath = gattDataList.get(0);
					mLeState = CONNECTING;
					uuidObjPathMap.put(uuid, selectedServiceObjPath );uuidpaths=uuidpaths+"\n uuid="+uuid +" path="+selectedServiceObjPath;
					objPathUuidMap.put(selectedServiceObjPath, uuid);
					String ops="";
					for(int i=0;i<ObjectPathArray.length;i++){
						ops=ops+"\n"+ObjectPathArray[i];
					}
					//Broad.setText("Broadcast:"+ops+ObjectPathArray.length+"\n suuid:"+uuid+"\n spath:"+selectedServiceObjPath+"\n"+remoteDevice.getAddress());
					getBluetoothGattService(selectedServiceObjPath, uuid);
					
				}
			}
		
		if (action.equals("com.test.StartConnection")) {
			if (device != null) {
				isDevicePickerPending = true;
				startConnection(device);
			}
		}
		}
	};

	private final IBluetoothGattProfile.Stub btGattCallback = new IBluetoothGattProfile.Stub() {
		
		public void onDiscoverCharacteristicsResult(String path, boolean result) {
			writeuuidpaths=writeuuidpaths+"Discover:"+path+"\n";
			Log.d(TAG, "onDiscoverCharacteristicsResult : " + "path : " + path + " result : "
                    + result);
			ParcelUuid srvUUID = objPathUuidMap.get(path);
			BluetoothGattService gattService = uuidGattSrvMap.get(srvUUID);
			if (result) {
                if (gattService != null) {
                    Log.d(TAG, "gattService.getServiceUuid() ======= " + srvUUID.toString());
                   /* try {
						gattService.registerWatcher();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} */
                    	String[] charObjPathArray = gattService.getCharacteristics();
                    	if (charObjPathArray != null) {
                            for (String objPath : Arrays.asList(charObjPathArray)) {
                            ParcelUuid parcelUUID = gattService.getCharacteristicUuid(objPath);
                            uuidObjPathMap.put(parcelUUID, objPath);uuidpaths=uuidpaths+"\n uuid="+parcelUUID +" path="+objPath;
                            objPathUuidMap.put(objPath, parcelUUID);
                            Log.d(TAG, " Map with key UUID : " + parcelUUID + " value : " + objPath);
                            }
                            Log.d(TAG, "Created map with size : " + uuidObjPathMap.size());
                            mLeState = CONNECTED;
                            String text = "Connected";
                            leDisconn = false;
                            Message message = msgHandler.obtainMessage(GATT_CONNECTION_STATE, text);
                            msgHandler.sendMessage(message);
                            return;
                     	}else {
                            Log.e(TAG, " gattService.getCharacteristics() returned null");
                       }
                
                } else {
                    Log.e(TAG, " gattService is null");
                }
            } else {
                Log.e(TAG, "Discover characterisitcs failed ");
            }
			mLeState = DISCONNECTED;
		}
		public void onSetCharacteristicValueResult(String path, boolean result) {
			//do nothing for now  write some configrations
			writeuuidpaths=writeuuidpaths+"SetValueResult:"+path+"\n";

			/*BluetoothGattService gattService = uuidGattSrvMap.get(HRM);
			Toast.makeText(mContext,"thread!", Toast.LENGTH_SHORT).show();
			byte bs[] = new byte[] {(byte)0x01};
			try {
				if(gattService.writeCharacteristicRaw(path, bs, true)){
					Toast.makeText(mContext,"TRUETRUETRUETRUETRUE!", Toast.LENGTH_SHORT).show();
					//Write.setText("TRUE!!!");
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Toast.makeText(mContext,"FALSEFALSEFALSEFALSEFALSE!", Toast.LENGTH_SHORT).show();
				//Write.setText("FALSE!!!");
			}*/
			
		}
		public void onSetCharacteristicCliConfResult(String path, boolean result) {
			//do nothing for now x's path ffa3 = 003a
			writeuuidpaths=writeuuidpaths+"NotiCliConfResult:"+path+"\n";

			/*BluetoothGattService gattService = uuidGattSrvMap.get(HRM);
			Toast.makeText(mContext,"thread!", Toast.LENGTH_SHORT).show();
			byte bs[] = new byte[] {(byte)0x01,(byte)0x00};
			try {
				if(gattService.writeCharacteristicRaw(notihandle, bs, true)){
					Toast.makeText(mContext,"down noti success!", Toast.LENGTH_SHORT).show();
					//Write.setText("TRUE!!!");
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Toast.makeText(mContext,"dow noti false!", Toast.LENGTH_SHORT).show();
				//Write.setText("FALSE!!!");
			}*/
		}
		
		public void onValueChanged(String path, String value) {  
			//do nothing for now
			writeuuidpaths=writeuuidpaths+"ValueChanged:"+path+"\n";
            
            String objPathx = uuidObjPathMap.get(UUID_HEART_RATE);
            Message message  = null;
            
            message = msgHandler.obtainMessage(GATT_VALUECHANGED, value);
            msgHandler.sendMessage(message);
		}
		
		public void onUpdateCharacteristicValueResult(String arg0, boolean arg1) {
			writeuuidpaths=writeuuidpaths+"Update:"+arg0+"\n";
           //no read here, all in onValueChanged.
		}
	};

	  private void getBluetoothGattService(String objPath, ParcelUuid uuid) {
			Log.v(TAG, "getBluetoothGattService");
			if (mDevice != null) {
				Log.d(TAG,
	                    " ++++++ Creating BluetoothGattService with device = "
	                            + mDevice.getAddress() + " uuid " + uuid.toString()
	                            + " objPath = " + objPath);
				/*Instantiating object of BluetoothGattService. The BluetoothGattService starts the Characteristic
				 * Discovery process , the result of which is handled in onDiscoverCharacteristicsResult function
				 */
				BluetoothGattService gattService = new BluetoothGattService(mDevice, uuid,
						objPath, btGattCallback);
				if (gattService != null) {
					uuidGattSrvMap.put(uuid, gattService);
					Log.d(TAG, "Adding gatt service to map for : " + uuid + "size :" + uuidGattSrvMap.size());
					return;
				}else {
	                Log.e(TAG, "Gatt service is null for UUID : " + uuid.toString());
	            }
	        } else {
	            Log.e(TAG, " mDevice is null");
	        }
		    mLeState = DISCONNECTED;
		    Toast.makeText(mContext,"Connection Failed", Toast.LENGTH_SHORT).show();
		}
	   
	  private boolean getGattServices(ParcelUuid uuid, BluetoothDevice btDevice) {
      Log.d(TAG, "Calling  btDevice.getGattServices");
	        return btDevice.getGattServices(uuid.getUuid()); 
	 
	  }
	  private class insertRemainedSensorDataToServer extends AsyncTask<String,Integer,String> {

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			DatabaseHelper dbHelper = new DatabaseHelper(Motorola_sample_apk_icsActivity.this,"ods");
			sensorsService.insertSensor1dataJson(dbHelper.getRemainedSensordataList(pid,startDatetime,endDatetime));
			return null;
		}
		  
	  }

	  private class insertSensorThreadToLocalOrServer extends AsyncTask<String,Integer,String> {
			@Override
			protected String doInBackground(String... params) {
			try{
				SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");       
				Date curDatetime = new Date(System.currentTimeMillis());//get current date time   
				formatter.setTimeZone(TimeZone.getTimeZone("GMT+02:00"));
				currentDatetime  =formatter.format(curDatetime);  
				
				 //send to server and local database 
				 if(ConnectionUtil.isConn(getApplicationContext())){ 
					 connCount++;
					 
			         sensorsService.insertSensor1dataNotJson(pid,currentDatetime, String.valueOf(xyz[0]),String.valueOf(xyz[1]),String.valueOf(xyz[2]));
			         Message message  = null;
			         message = msgHandler.obtainMessage(HAVE_CONNECTION, null);
			         msgHandler.sendMessage(message);	
					 //DatabaseHelper dbHelper = new DatabaseHelper(Motorola_sample_apk_icsActivity.this,"ods");
					 //dbHelper.insertSensordataToLocalDb("140687-3443",datetime, "1", "200"); 
			    }
				//only send to local database
				 else{
					 //startDatetime = datetime;////////////////////////////////////////ppp
					 notconnCount++;
					 
					 DatabaseHelper dbHelper = new DatabaseHelper(Motorola_sample_apk_icsActivity.this,"ods");
					 dbHelper.insertSensordataToLocalDb(pid,currentDatetime, String.valueOf(xyz[0]), String.valueOf(xyz[1]), String.valueOf(xyz[2])); 

					 Message message  = null;
			         message = msgHandler.obtainMessage(LOST_CONNECTION, null);
			         msgHandler.sendMessage(message);
				}
				}
				catch(Exception e){
				}
				return null;
				}
			@Override
			protected void onPostExecute(String string) {
				// TODO Auto-generated method stub
			}
		  }
	  private void enableSensor1() {
			String objPath = uuidObjPathMap.get(ACCEL_ENABLER_UUID);
			BluetoothGattService gattService = uuidGattSrvMap.get(HRM);
			byte bs[] = new byte[] {(byte)0x01};
			try {
				if(gattService.writeCharacteristicRaw(objPath, bs, true)){
					Toast.makeText(mContext, "BSN Enabled.", Toast.LENGTH_SHORT).show();
				}
				else{
					Toast.makeText(mContext, "BSN Enabled Failed!", Toast.LENGTH_SHORT).show();
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	  }
	  private void enableSensor1Notification() {
			BluetoothGattService gattService = uuidGattSrvMap.get(HRM);
			String objPath = uuidObjPathMap.get(UUID_HEART_RATE);
			//String objPath1 = uuidObjPathMap.get(UUID_HEART_RATE1);
			//String objPath2 = uuidObjPathMap.get(UUID_HEART_RATE2);
			try {
				// no 1
				if(gattService.registerWatcher()){//onValueChanged call it??? no?/
					//Writebutton.setText("registerWatcher is ok!"+gattService.getCharacteristicClientConf(objPath));
				}
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				//Writebutton.setText("registerWatcher is Not ok!");
				e1.printStackTrace();
			}
			boolean status = false;
  			String hex = "0001";
  					  int value = Integer.parseInt(hex, 16);  
  					  int confVal = ((1 << 8) & 0xff00);
  				try {// no 2
  			         status = gattService.setCharacteristicClientConf(objPath, confVal);//CAOCAOCAO onSetCharacteristicCliConfResult call it
  			         //status = gattService.setCharacteristicClientConf(objPath1, confVal);
  			         //status = gattService.setCharacteristicClientConf(objPath2, confVal);
  				} catch (Exception e) {
  			         status = false;
  			         e.printStackTrace();
  			    }
       		    if (status == true) {
  					Toast.makeText(mContext,"Notification Enabled.", Toast.LENGTH_SHORT).show();
  			    } else {
  					Toast.makeText(mContext,"Notification Enabled Failed!", Toast.LENGTH_SHORT).show();
  				}
	  }
	  private class disableSensor1Notification extends AsyncTask<String,Integer,String> {
			@Override
			protected String doInBackground(String... params) {
			BluetoothGattService gattService = uuidGattSrvMap.get(HRM);
			String objPath = uuidObjPathMap.get(UUID_HEART_RATE);
			//String objPath1 = uuidObjPathMap.get(UUID_HEART_RATE1);
			//String objPath2 = uuidObjPathMap.get(UUID_HEART_RATE2);
			try {
				// no 1
				if(gattService.registerWatcher()){//onValueChanged call it??? no?/
					//Writebutton.setText("registerWatcher is ok!"+gattService.getCharacteristicClientConf(objPath));
				}
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				//Writebutton.setText("registerWatcher is Not ok!");
				e1.printStackTrace();
			}
			boolean status = false;
			int confVal = ((0 << 8) & 0xff00);
			String hex = "0001";
				  int value = Integer.parseInt(hex, 16); 
  			try {
  				// no 3
				status = gattService.setCharacteristicClientConf(objPath, confVal);
				//status = gattService.setCharacteristicClientConf(objPath1, confVal);
				//status = gattService.setCharacteristicClientConf(objPath2, confVal);
			} catch (Exception e) {
				Log.e(TAG, "mBtNoti", e);
				status = false;
				e.printStackTrace();
			}
  			if (status == true) {
				   Toast.makeText(mContext,"Notification disabled ok!"+gattService.getCharacteristicClientConf(objPath), Toast.LENGTH_SHORT).show();
			   } else {
				   Toast.makeText(mContext,"Notification disabling failed !", Toast.LENGTH_SHORT).show();
			   } 
  			return null;
		}
	  }
	  private int hexConvert(int hex){
			double acceleration=0.0;
			if(hex<128){
				acceleration = (hex*2); 
			}
			else{
				acceleration = (double)((255-hex)*2); 
			}
			
			return (int) acceleration;
		}
}