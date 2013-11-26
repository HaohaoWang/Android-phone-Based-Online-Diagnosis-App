package myServices;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

public class SensorsService {
	private ArrayList<String> parameters = new ArrayList<String>();  
    private ArrayList<String> values = new ArrayList<String>();
    private httpSoap Soap = new httpSoap();  
    
    public SensorsService() {
		super();
		// TODO Auto-generated constructor stub
	}
    //insert data to server database
	/*public void insertSensor1data(int[] data){
    	parameters.clear();
		values.clear();
		parameters.add("pid");  
		parameters.add("sensor"); 
		parameters.add("data");
	    values.add("140687-3443"); //pid ?
	    values.add("1"); //did ?
	    values.add(String.valueOf(data[0]));
		Soap.getWebservice("insertSensor1data", parameters, values);
    }*/
	public void insertSensor1dataNotJson(String pid,String datetime,String data1,String data2,String data3){//dai xu!!
		
    	parameters.clear();
		values.clear();
		parameters.add("pid"); 
		parameters.add("time"); 
		parameters.add("data1");
		parameters.add("data2");
		parameters.add("data3");
	    values.add(pid);
	    values.add(datetime);
	    values.add(String.valueOf(data1));
	    values.add(String.valueOf(data2));
	    values.add(String.valueOf(data3));
		Soap.getWebservice("insertSensor1data", parameters, values);
    }
	public void insertSensor1dataJson(String dataList){//dai xu!!
		
    	parameters.clear();
		values.clear();
		parameters.add("dataList");
	    values.add(dataList);
		Soap.getWebservice("insertSensor1dataJson", parameters, values);
		System.out.println("inset data list!!!");
    }
}
