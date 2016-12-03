package Conn_to_Could.Browser;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONObject;

public class Get_Forms 
{

	private static String basic_web_link = "";
	
	public Get_Forms(String link) 
	{
		if (link == null)
			basic_web_link = "http://localhost:8080/meeting_cloud/device/";
		else 
			basic_web_link = link;
	//	basic_web_link = "http://localhost:8080/meeting_cloud/device/";
	}
	
	public static Map<String, String> get(JSONObject json_data)
	{
		Map<String, String> forms = new LinkedHashMap();
		
		String form_func = "";
		String form_send_to = "";
		String key = "";
	    String reg_form_name = "";
		
	    if (!(json_data.isNull("form")))
	    {
	    	JSONObject formarray = json_data.getJSONObject("form");
	    	JSONObject form;
	    	JSONObject form_textbox;
	    	Iterator form_key = formarray.keys();
	    	while(form_key.hasNext())
			{  
	    		reg_form_name = form_key.next().toString();
				form = formarray.getJSONObject(reg_form_name);
				
				form_func = form.getString("func");
				form_send_to = form.getString("addr");
				forms.put(form_func, form_send_to);
				System.out.println( form_func + "---->" + form_send_to );
			}
	    }
	    
		return forms;
	}
	

}