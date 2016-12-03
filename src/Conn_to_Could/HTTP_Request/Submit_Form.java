package Conn_to_Could.HTTP_Request;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

public class Submit_Form {

	private static String basic_web_link = "";
	
	public Submit_Form(String link) 
	{
		if (link == null)
			basic_web_link = "http://localhost:8080/meeting_cloud/device/";
		else 
			basic_web_link = link;
	//	basic_web_link = "http://localhost:8080/meeting_cloud/device/";
	}
	
	public static String post( HttpClient conn_cloud, Map<String, String> form_data) 
		throws ClientProtocolException, IOException
		{
			String url = basic_web_link + form_data.get("post_link");
		    HttpPost post = new HttpPost(url);
		    
		    ArrayList<NameValuePair> post_form = new ArrayList<NameValuePair>();
		    for(Map.Entry<String, String> entry:form_data.entrySet())		//嚙踐�ap �謢綽蕭���蕭謘蕭嚙踝蕭嚙踝嚙踝嚙踝蕭, ���蕭��蕭嚙踝蕭�嚙踝嚙踐�嚙踝��蕭
		    {   
			    	if (entry.getKey() != "post_link")
			    	post_form.add( new BasicNameValuePair( entry.getKey(), entry.getValue() ));	
			    	System.out.println(entry.getKey()+"--->" + entry.getValue());
		    }   
		    post.setEntity( new UrlEncodedFormEntity(post_form, "UTF-8"));

		    HttpResponse response = conn_cloud.execute(post);

		    post.abort();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
										response.getEntity().getContent(), "UTF-8"));
	        String sResponse;
	        String s = "";
	
	        while ((sResponse = reader.readLine()) != null) {
	            s = s + sResponse + '\n';
	        }
	        
	        System.out.println(s);
		    if (response.getStatusLine().getStatusCode() == 302) 		  	//302 �����甇�
		    {  	url = response.getLastHeader("Location").getValue();    }  
		    
		    return url;
		  }
}