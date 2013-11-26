package com.motorola.bluetooth.ble;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ChartAdapter extends BaseAdapter  {
	
	private LayoutInflater mInflater;
	private List<HashMap<String, String>> messageList = new ArrayList<HashMap<String, String>>();
	
	public ChartAdapter(Context context,List<HashMap<String, String>> messageList ){
		this.mInflater = LayoutInflater.from(context);
		this.messageList = messageList;
		// get the inflater
	}

	public int getCount() {
		// TODO Auto-generated method stub
		return this.messageList.size();
	}

	public HashMap<String, String> getItem(int index) {
		// TODO Auto-generated method stub
		return this.messageList.get(index);
	}

	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	 /*private List<HashMap<String, String>> getListdata() {
		for(int i=allMessages.size()-1;i>=0;i-=3){
			HashMap<String, String> rowHash = new HashMap<String, String>();
			rowHash.put("messagetime", allMessages.get(i-2));
			rowHash.put("messagebody", allMessages.get(i-1));
			rowHash.put("messagedirection", allMessages.get(i));
			messageList.add(rowHash);// for each hashmap add it into list!!
		}
		
		return messageList;
	 }*/

	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		boolean messagedirection;
		 if (convertView == null) {  
	            holder = new ViewHolder();  
	            convertView = mInflater.inflate(R.layout.item_chart, null);  
	            holder.chartitem = (LinearLayout) convertView.findViewById(R.id.chartitem);
	            holder.chartcontent = (TextView) convertView.findViewById(R.id.chartcontent);  
	            convertView.setTag(holder);  
	        } else {  
	            holder = (ViewHolder) convertView.getTag();  
	        } 
		 if(messageList.get(position).get("messagedirection").equals("0")){
			 messagedirection = false;// patient sends: Gravity right
		 }
		 else{
			 messagedirection = true;// doctor sends: Gravity left
		 }
		 
		 holder.chartitem.setGravity(messagedirection ? Gravity.LEFT : Gravity.RIGHT); 
		 holder.chartcontent.setText((String)messageList.get(position).get("messagebody"));
		 holder.chartcontent.setBackgroundResource(messagedirection ? R.drawable.bubble_yellow : R.drawable.bubble_green);//background
		 return convertView;
	}
	public final class ViewHolder {
		public LinearLayout chartitem;
        public TextView chartcontent;  
    }  

}
