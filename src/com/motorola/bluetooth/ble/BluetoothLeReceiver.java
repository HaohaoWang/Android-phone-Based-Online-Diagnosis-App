package com.motorola.bluetooth.ble;


import android.os.Bundle;
import android.util.Log;
import android.content.Intent;
import android.bluetooth.BluetoothDevice;
//import android.bluetooth.BluetoothUuid;

//import android.bluetooth.BluetoothDevicePicker;
import android.content.IntentFilter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.bluetooth.BluetoothDevice;


public class BluetoothLeReceiver extends BroadcastReceiver {
	private String TAG = "BluetoothLeREceiver";

   public static boolean isDevicePickerPending = false;
   public static BluetoothDevice mPendingDevice = null;
	
	@Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
       
        Log.d(TAG, "Bluetooth LE receiver intnet action: " + action);
        BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
         
		 if (action.equals("android.bluetooth.devicepicker.action.DEVICE_SELECTED")) {
       
                if (device != null) {
               	   Intent intent1 = new Intent("com.test.StartConnection");
               	   intent1.putExtra(BluetoothDevice.EXTRA_DEVICE, device);
                   context.sendBroadcast(intent1);
					
                }

            }
	}
}

