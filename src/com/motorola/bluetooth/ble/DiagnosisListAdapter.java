package com.motorola.bluetooth.ble;

import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class DiagnosisListAdapter extends BaseExpandableListAdapter {
 
    private Activity context;
    private Map<String, List<String>> laptopCollections;
    private List<String> laptops;
	private String phoneNumber="";
 
    public DiagnosisListAdapter(Activity context, List<String> laptops,Map<String, List<String>> laptopCollections) {
        this.context = context;
        this.laptopCollections = laptopCollections;
        this.laptops = laptops;
    }
 
    public void setNumber(String number){
    	this.phoneNumber = number;
    }
    public Object getChild(int groupPosition, int childPosition) {
        return laptopCollections.get(laptops.get(groupPosition)).get(childPosition);
    }
 
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }
 
    public View getChildView(final int groupPosition, final int childPosition,
            boolean isLastChild, View convertView, ViewGroup parent) {
        final String laptop = (String) getChild(groupPosition, childPosition);
        LayoutInflater inflater = context.getLayoutInflater();
 
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.diagnosis_child, null);
        }
        ImageView call = (ImageView) convertView.findViewById(R.id.call_button);
        if(groupPosition == getGroupCount()-1){
        	call.setVisibility(0);
        }
        else{
        	call.setVisibility(8);
        }
        TextView item = (TextView) convertView.findViewById(R.id.laptop);
 
        call.setOnClickListener(new OnClickListener() {
        	 
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Call Your Doctor?");
                builder.setCancelable(false);
                builder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                	public void onClick(DialogInterface dialog, int id) {
            		//Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+ phoneNumber));
                		try{
                		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                			if (tm.getSimState() == TelephonyManager.SIM_STATE_ABSENT){
                				Intent phoneIntent = new Intent("android.intent.action.CALL",Uri.parse("tel:" + phoneNumber));
                             	context.startActivity(phoneIntent);
                			} else {
                				Toast.makeText(context, "Calling: "+phoneNumber+".  No Mobile Telecommunication Service.", Toast.LENGTH_SHORT).show();
                			}                		
                		}
                		catch(Exception e){
                		}
                       	}
                        });
                builder.setNegativeButton("No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
 
        item.setText(laptop);
        return convertView;
    }
        
    public int getChildrenCount(int groupPosition) {
        return laptopCollections.get(laptops.get(groupPosition)).size();
    }
 
    public Object getGroup(int groupPosition) {
        return laptops.get(groupPosition);
    }
 
    public int getGroupCount() {
        return laptops.size();
    }
 
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }
 
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String laptopName = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.diagnosis_group,null);
        }
        TextView item = (TextView) convertView.findViewById(R.id.laptop);
        item.setTypeface(null, Typeface.NORMAL);
        item.setText(laptopName);
        return convertView;
    }
 
    public boolean hasStableIds() {
        return true;
    }
 
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
