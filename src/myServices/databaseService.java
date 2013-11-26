package myServices;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class databaseService {
	//private List<HashMap<String, String>> patientCaseList = new ArrayList<HashMap<String, String>>();
	private ArrayList<String> parameterss = new ArrayList<String>();  
    private ArrayList<String> values = new ArrayList<String>();  
    private ArrayList<String> result = new ArrayList<String>();
    private httpSoap Soap = new httpSoap();  
    
    public databaseService() {
		super();
		//here to call getWebservice is wrong: must call in a thread
		// TODO Auto-generated constructor stub
	}
    
    public List<HashMap<String, String>> selectPatientcase(String pid) { 
    	List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>(); 
    	parameterss.clear();  
    	values.clear();  
       	parameterss.add("pid");  
        values.add(pid);  
    	result.clear(); 
    	HashMap<String, String> headerHash = new HashMap<String, String>();  
    	headerHash.put("pid", "ID:");  
    	headerHash.put("pnane", "Name:");  
    	headerHash.put("pbirtahday", "Birthday:");
    	headerHash.put("pgender", "Gender:");  
    	headerHash.put("pdepartment", "Department:");  
    	headerHash.put("pstate", "State:");  
    	headerHash.put("datein", "Datein:");  
    	headerHash.put("dateout", "Dateout:");  
    	headerHash.put("pjob", "Job:");  
    	headerHash.put("pmail", "Email:");  
    	headerHash.put("pphone", "Phone:");  
    	headerHash.put("pstreet", "Street:");  
    	headerHash.put("paddress", "Address:");  
    	headerHash.put("pcity", "City:");  
    	headerHash.put("pcode", "Zip Code:");  
    	headerHash.put("ppassword", "Password:");  
    	headerHash.put("pbool", "Bool:");  
        //list.add(headerHash); 
    	try{
    		
    		result = Soap.getWebservice("selectPatientcase", parameterss, values);
    	for (int j = 0; j < result.size(); j += 17) { 
            HashMap<String, String> rowHash = new HashMap<String, String>();  
            rowHash.put("pid", 			"ID:"+result.get(j));  
            rowHash.put("pname", 		"Name:"+result.get(j+1));  
            rowHash.put("pbirtahday", 	"Birthday:"+result.get(j+2));
            rowHash.put("pgender", 		"Gender:"+result.get(j+3));  
        	rowHash.put("pdepartment", 	"Department:"+result.get(j+4));  
        	rowHash.put("pstate", 		"State:"+result.get(j+5));  
        	rowHash.put("datein", 		"Datein:"+result.get(j+6));  
        	rowHash.put("dateout",	    "Dateout:"+result.get(j+7));  
        	rowHash.put("pjob", 		"Job:"+result.get(j+8));  
        	rowHash.put("pmail", 		"Email:"+result.get(j+9));  
        	rowHash.put("pphone", 		"Phone:"+result.get(j+10));  
        	rowHash.put("pstreet", 		"Street:"+result.get(j+11));  
        	rowHash.put("paddress", 	"Address:"+result.get(j+12));  
        	rowHash.put("pcity", 		"City:"+result.get(j+13));  
        	rowHash.put("pcode", 		"Zip Code:"+result.get(j+14));  
        	rowHash.put("ppassword", 	"Password:"+result.get(j+15));  
        	rowHash.put("pbool", 		"Bool:"+result.get(j+16));   
            list.add(rowHash); 
        }   
    	}
    	catch(Exception e){ 
    	}  	
    	return list;
    } 
    
    public void deletePatientcase(String pid) {  
  	  
    	parameterss.clear();  
    	values.clear();  
       	parameterss.add("pid");  
        values.add(pid);   
        Soap.getWebservice("deletePatientcase", parameterss, values);  
    } 
    
	public List<HashMap<String, String>> selectAllitems() { 
    	List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>(); 
    	parameterss.clear();  
    	values.clear();  
    	result.clear(); 
    	HashMap<String, String> headerHash = new HashMap<String, String>();  
    	headerHash.put("id", "id");  
    	headerHash.put("name", "name");  
    	headerHash.put("age", "age");  
        list.add(headerHash); 
        
    	try{
    		result = Soap.getWebservice("selectAllitems", parameterss, values);         
    	for (int j = 0; j < result.size(); j += 3) {  
            HashMap<String, String> rowHash = new HashMap<String, String>();  
            rowHash.put("id", result.get(j));  
            rowHash.put("name", result.get(j + 1));  
            rowHash.put("age", result.get(j + 2));  
            list.add(rowHash);  
        }   
    	}
    	catch(Exception e){ 
    	}  	
    	return list;
    } 
    
    public void deleteItem(String id) {  
  	  
    	parameterss.clear();  
    	values.clear();  
       	parameterss.add("id");  
        values.add(id);   
        Soap.getWebservice("deleteItem", parameterss, values);  
    } 
    
    public void insertItem(String name, String age) {  
  	  
    	parameterss.clear();  
    	values.clear();  
       	parameterss.add("name"); 
       	parameterss.add("age"); 
        values.add(name);
        values.add(age);
        Soap.getWebservice("insertItem", parameterss, values);  
    } 

    public void uptateItem (String id,String name, String age) {  
    	  
    	parameterss.clear();  
    	values.clear();  
       	parameterss.add("id"); 
       	parameterss.add("name"); 
       	parameterss.add("age"); 
        values.add(id);
        values.add(name);
        values.add(age);
        Soap.getWebservice("uptateItem", parameterss, values);  
    } 
    
}
