package myServices;

import com.motorola.bluetooth.ble.ChartAdapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

public class updateThread extends Thread{
	private ChartAdapter chartAdapter1;
	private FeedbackService feedbackService;
	private Handler handler;
	private Context mContext;
	private Boolean updateFlag = true;
	
	
	public updateThread(Context mContext,FeedbackService feedbackService,Handler handler){
		super();
		this.mContext = mContext;
		this.feedbackService = feedbackService;
		this.handler = handler;
	}

	public ChartAdapter getChartAdapter() {
		return chartAdapter1;
	}

	public void setUpdateFlag(Boolean updateFlag) {
		this.updateFlag = updateFlag;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		while(updateFlag){
        	System.out.println("updateThread is running");
        	feedbackService.updatePDnewmessages();
			chartAdapter1 = new ChartAdapter(mContext, feedbackService.getMessagehashlist());//change to "messagelist" hashmaplist
			Message message = new Message();      
            message.what = 1;      
            handler.sendMessage(message);  
			try {
				Thread.sleep(8000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	}
	}

	@Override
	public synchronized void start() {
		// TODO Auto-generated method stub
		super.start();
	}

}
