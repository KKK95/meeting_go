package Conn_to_Could.Browser;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import Conn_to_Could.HTTP_Request.http_request;
import Conn_to_Could.Browser.Get_Links;
import Conn_to_Could.Browser.Get_Forms;


public class Water_Crab 
{
	private static HttpClient 	conn_cloud 	= 		null; 
	private static http_request request 	= 		null;
	private static Get_Links 	links 		= 		null;
	private static Get_Forms 	form 		= 		null;
	private static Get_Contents contents 	= 		null;
	private static Get_States 	states 		= 		null;

	static JSONObject json_web_data;
	private static Scanner scanner;
	
	private static String basic_web_link 	= 		"";
	private static String now_web_url 		= 		"";
	
    private static Map<String, String> send_form_to = new LinkedHashMap();
    private static Map<String, String> state 		= new LinkedHashMap();
    
    private static boolean vnc_server_running = false;
    
	public Water_Crab(String link) 
	{
		conn_cloud = new DefaultHttpClient();			
		
		json_web_data = new JSONObject();
		if (link == null)
			basic_web_link = "http://localhost:8080/meeting_cloud/";
		else 
			basic_web_link = link;
		
		scanner = new Scanner(System.in);
		http_request request = new http_request(basic_web_link);
		Get_Links links = new Get_Links(basic_web_link);
		Get_Forms form = new Get_Forms(basic_web_link);
		Get_States states = new Get_States(basic_web_link);
		Get_Contents contents = new Get_Contents();
	}
	
private static void update_web_data (JSONObject json)
{
	send_form_to = form.get(json);
	state = states.get(json);
	return;
}

public static void link(String url)			
{
	try {
		json_web_data = request.link( conn_cloud, url);
		update_web_data(json_web_data);
	} catch (ClientProtocolException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

	return;
}

public static void show_json_data()
{
	System.out.println(json_web_data);
	return;
}

public static String get_form_addr(String key)
{
	String all_key;
	for(Map.Entry<String, String> entry:send_form_to.entrySet())		//用map 記錄表單所有資料, 
    {   
		all_key = entry.getKey();
			
		if (all_key.equals(key))
		{	return entry.getValue();	}
    }
	return "error";
}

public static Map<String, String> get_state()
{	return state;	}

/*
public static String chick_link(int link_num)					
{
	int i = 1;
	String new_url = "";
	for(Map.Entry<String, String> entry:link.entrySet())	 
    {   												
        if (link_num == i)
        {	
        	System.out.println(entry.getKey()+"--->" + entry.getValue());  
        	new_url = entry.getValue() ;	
        	break;	
        }
        else	i++;
    }
	now_web_url = new_url;
	return new_url;
}
*/

public static String post_submit_form(Map<String, String> form_data) 
throws ClientProtocolException, IOException
{
	String new_url = "";
	new_url = request.post_submit_form(conn_cloud, form_data);	

	return new_url;
}


public static void set_state (boolean vnc_server_state)
{
	vnc_server_running = vnc_server_state;
	return;
}


public static String get_now_url()
{	return now_web_url;		}

public static Map<String, String> get_member_list(JSONObject json_data, String member_name, String ip)
{	return contents.get_member_list(json_data, member_name, ip);	}

public static String using_client_ip_to_get_name(String client_ip)
{	return contents.using_client_ip_to_get_name(json_web_data, client_ip);	}



}