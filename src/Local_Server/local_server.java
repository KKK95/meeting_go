package Local_Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.Enumeration;

import Window.ServerWindow;
import Local_Server.RemoteDataServer;

public class local_server implements Runnable
{
	private static RemoteDataServer sub_thread = null;
	private static Thread remort_port_thread = null;
	private static Thread get_img_thread = null;
	private static Thread send_img_thread = null;
	
	private InetAddress ip;

	private static int remort_port = 0;
	private static int get_img_port = 0; 
	private static int send_img_port = 0;
	
	private static boolean remort_thread_built = false;	
	private static boolean get_img_thread_built = false;	
	
	public local_server (int port1, int port2, int port3, InetAddress ip_address) throws IOException
	{
		
		remort_port = port1;
		send_img_port = port2;
		get_img_port = port3;
		sub_thread = new RemoteDataServer(send_img_port, get_img_port);
		ip = ip_address;
		remort_port_thread = new Thread(this);			 // 產生Thread物件
		remort_port_thread.start();
		
		while (remort_thread_built == false)
			System.out.println("remort_thread_built ? " + remort_thread_built);
		
		send_img_thread = new Thread(this);
		send_img_thread.start();
/*		
		while (remort_thread_built == false)
			System.out.println("get_img_thread_built ? " + get_img_thread_built);
		
		get_img_thread = new Thread(this);
		get_img_thread.start();
*/		
		
		
	}
	
	public void run()
	{
		ServerSocket server = null;
		Socket client = null;
		if (remort_thread_built == false)				
		{
			try {
				server = new ServerSocket(remort_port);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
			remort_thread_built = true;
			System.out.println("remort thread , port = " + server.getLocalPort() );
		}
		else if (remort_thread_built == true)	
		{
			try {
					server = new ServerSocket(send_img_port);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("send img thread , port = " + server.getLocalPort() );
		}
/*		
		else if (get_img_thread_built == false)
		{
			try {
				server = new ServerSocket(get_img_port);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			get_img_thread_built = true;
			System.out.println("get img thread , port = " + server.getLocalPort() );
		}
		else
		{
			try {
				server = new ServerSocket(send_img_port);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("send img thread , port = " + server.getLocalPort() );
		}
*/		
		//======================================================================//
		
		for (int pid=0; true; )
	    {
	    	if (pid < 20)
	    	{
	    		try 
	    		{
					client = server.accept();
					System.out.println(server.getLocalPort());
					System.out.println(client.getPort());
		    		sub_thread.init(client);
					new Thread(sub_thread).start();
				} catch (IOException e) 
	    		{	e.printStackTrace();	}	
	    		
	        	++pid;
	    	}
	    }
		
	}
		
}
