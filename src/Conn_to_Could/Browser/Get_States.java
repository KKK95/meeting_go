package Conn_to_Could.Browser;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONObject;

public class Get_States 
{

	private static String basic_web_link = "";
	
	public Get_States(String link) 
	{
		if (link == null)
			basic_web_link = "http://localhost:8080/meeting_cloud/device/";
		else 
			basic_web_link = link;
	//	basic_web_link = "http://localhost:8080/meeting_cloud/device/";
	}
	
	public static Map<String, String> get(JSONObject json_data)
	{
		Map<String, String> states = new LinkedHashMap();
		
		String state = "";
		String key = "";
	    String state_name = "";
		
	    if (!(json_data.isNull("state")))
	    {
	    	JSONObject formarray = json_data.getJSONObject("state");
	    	Iterator form_key = formarray.keys();
	    	while(form_key.hasNext())
			{  
	    		state_name = form_key.next().toString();
	    		state = formarray.getString(state_name);
				
				states.put(state_name, state);
				System.out.println( state_name + "---->" + state );
			}
	    }
	    
		return states;
	}
	

}