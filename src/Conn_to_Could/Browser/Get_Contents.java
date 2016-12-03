package Conn_to_Could.Browser;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

public class Get_Contents {

	public static Map<String, String> get_member_list(JSONObject json_data, String member_name, String ip)
	{
		Map<String, String> member_list = new HashMap();
		
		String content = "";
		String content_array_name = "";
		String reg_content = "";
		
		int map_links_ptr = 1;
		int array_length = 1;
	    if (!(json_data.isNull("content")))
	    {

	    	JSONObject ContentObject = json_data.getJSONObject("content");			//嚙踐嚙踝��� object
	    	JSONObject content_object;												//��bject ��豰蕭嚙踝蕭 ���ject
	    	JSONArray content_object_array;											//���ject ��豰蕭嚙踝蕭 array
			Iterator Content_Key = ContentObject.keys();
			Iterator content_object_key;
			
			while(Content_Key.hasNext())
			{   
				content = Content_Key.next().toString();
				if ( (content.indexOf("obj_meeting_member_list", 0)) != -1)			//嚙踝蕭��j, obj ����rray
				{
					content_object = ContentObject.getJSONObject(content);
					content_object_key = content_object.keys();
					for (int j = 0, array_start = 0; j < array_length; j++, array_start = 0)
					{		
						while(content_object_key.hasNext())
						{
							content_array_name = content_object_key.next().toString();			//嚙踝蕭謘潘蕭謖斟ray 嚙踝蕭��蕭嚙�
							content_object_array = content_object.getJSONArray(content_array_name);	//嚙踐嚙踐�蕭��蕭嚙� 嚙踝蕭謘潘蕭嚙� array
							reg_content = content_object_array.getString(j);					//�豱ray ���蕭謘潘蕭謅蕭�嚙踝蕭
							
							if (array_start == 0)
							{	member_name = reg_content;	}
							else if (reg_content.equals(ip))
							{
								member_list.put(member_name, reg_content);
							}
							if ( j == 0 )	array_length = content_object_array.length();
							array_start = 1;
						}
						content_object_key = content_object.keys();
					}
				}
	        }
	    }
	    
		return member_list;
	}
	public static void show(JSONObject json_data)
	{ 
	    Map<String, String> contents = new LinkedHashMap();
	
		String content = "";
		String content_array_name = "";
		String reg_content = "";
		
		int map_links_ptr = 1;
		int array_length = 1;
	    if (!(json_data.isNull("content")))
	    {
	    	System.out.println("-----------------------Contents-----------------------");
	    	JSONObject ContentObject = json_data.getJSONObject("content");
	    	JSONObject content_set;
	    	JSONArray content_set_array;
			Iterator content_key = ContentObject.keys();
			Iterator content_set_key;
			
			while(content_key.hasNext())
			{  
				content = content_key.next().toString();
				if ( (content.indexOf("obj_", 0)) != -1)			//嚙踝蕭��j, obj ����rray
				{
					content_set = ContentObject.getJSONObject(content);
					content_set_key = content_set.keys();
					for (int j = 0, array_start = 0; j < array_length; j++, array_start = 0)
					{		
						while(content_set_key.hasNext())
						{
							content_array_name = content_set_key.next().toString();			//嚙踝蕭謘潘蕭謖斟ray 嚙踝蕭��蕭嚙�
							content_set_array = content_set.getJSONArray(content_array_name);	//嚙踐嚙踐�蕭��蕭嚙� 嚙踝蕭謘潘蕭嚙� array
							reg_content = content_set_array.getString(j);					//�豱ray ���蕭謘潘蕭謅蕭�嚙踝蕭
							if (array_start == 0)
							{
								System.out.print(reg_content);
								System.out.print("------------------------------------");
							}
							else
							{
								System.out.println("  " + reg_content); 
							}
							if ( j == 0 )	array_length = content_set_array.length();
							array_start = 1;
						}
						content_set_key = content_set.keys();
						System.out.println(" ");
					}
				}
	        }
			System.out.println("-----------------------Contents End-----------------------");
	    }
	    return ; 
	} 
	
	
	public static String using_client_ip_to_get_name(JSONObject json_data, String client_ip)
	{
		String client_name = null;
		
		String content = "";
		String content_array_name = "";
		String reg_content = "";
		
		int map_links_ptr = 1;
		int array_length = 1;
	    if (!(json_data.isNull("content")))
	    {

	    	JSONObject ContentObject = json_data.getJSONObject("content");			//嚙踐嚙踝��� object
	    	JSONObject content_object;												//��bject ��豰蕭嚙踝蕭 ���ject
	    	JSONArray content_object_array;											//���ject ��豰蕭嚙踝蕭 array
			Iterator Content_Key = ContentObject.keys();
			Iterator content_object_key;
			
			while(Content_Key.hasNext())
			{   
				content = Content_Key.next().toString();
				if ( (content.indexOf("obj_meeting_member_list", 0)) != -1)			//嚙踝蕭��j, obj ����rray
				{
					content_object = ContentObject.getJSONObject(content);
					content_object_key = content_object.keys();
					for (int j = 0, array_start = 0; j < array_length; j++, array_start = 0)
					{		
						while(content_object_key.hasNext())
						{
							content_array_name = content_object_key.next().toString();			//嚙踝蕭謘潘蕭謖斟ray 嚙踝蕭��蕭嚙�
							content_object_array = content_object.getJSONArray(content_array_name);	//嚙踐嚙踐�蕭��蕭嚙� 嚙踝蕭謘潘蕭嚙� array
							reg_content = content_object_array.getString(j);					//�豱ray ���蕭謘潘蕭謅蕭�嚙踝蕭
							
							if (array_start == 0)
							{	client_name = reg_content;	}
							else if (reg_content.equals(client_ip))
							{
								return client_name;
							}
							if ( j == 0 )	array_length = content_object_array.length();
							array_start = 1;
						}
						content_object_key = content_object.keys();
					}
				}
	        }
	    }
		
		return client_name;
	}



}
