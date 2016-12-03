package Window;
// 	Copyright 2010 Justin Taylor
// 	This software can be distributed under the terms of the
// 	GNU General Public License. 

import javax.imageio.ImageIO;
import javax.swing.*;
import org.omg.CORBA.portable.ApplicationException;
import Window.Login_Window;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.*;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;

import Window.Local_Server_Window;
import Window.Login_Window;
import Conn_to_Could.Browser.Water_Crab;
import Local_Server.RemoteDataServer;
import Local_Server.ServerListener;



public class ServerWindow implements Runnable
{
	private static Map<String, String> form = new LinkedHashMap();
	private static Map<String, String> state = new LinkedHashMap();
	private static final int WINDOW_HEIGHT = 275;
	private static final int WINDOW_WIDTH = 350;

	private static JPanel page = null;
	private CardLayout card = null;
	
	private static Login_Window 		login_window 		= null; 
	private static Local_Server_Window 	local_server_window = null; 
	
	private static JFrame window = null;
	
	private static Water_Crab browser;
	
	private static String window_state = "";
	
//=======================================================================================
	private static String url = "";
	private static String basic_web_link = "";
	
//=======================================================================================	
	
	public ServerWindow() 
	{
		
		window = new JFrame();
		window.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		window.setLocationRelativeTo(null);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		card = new CardLayout(5, 5);
		page = new JPanel();
		page.setLayout(card);
		
		login_window = new Login_Window();
		page.add(login_window.get_page(), "login");
		
		local_server_window = new Local_Server_Window();
		page.add(local_server_window.get_page(), "center");
		
		window.getContentPane().add(page, BorderLayout.CENTER);
		window.setVisible(true);
		window.setResizable(false);
/*		
		if(DISPLAY_DESKTOP_SHORCUT) 
		{
			shortcut.setSize(800, 600);
			shortcut.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

			JScrollPane scrollPane = new JScrollPane(shortcutLabel);//��Image��ilabel��

			shortcut.getContentPane().add(scrollPane);
			shortcut.pack();
			shortcut.setLocationRelativeTo(null);
			shortcut.setVisible(true);
		}
		//		server = new RemoteDataServer();
		//		server.setServerListener(this);
				
		//		listener = new RemoteDataServer();
		//		listener.setServerListener(this);
*/		
		
		
		
	}

	public void run()
	{
		int test;
		String now_page = "";
		url = "device_index.php";
		basic_web_link = "http://localhost:8080/meeting_cloud/";
		browser = new Water_Crab(basic_web_link);
		browser.link(url);
		state = browser.get_state();
		while(true)
		{

			//============================登入===================================
			if (state.containsKey("login"))
			{
				if ( now_page != "login")
				{	now_page = "login";	card.show(page, "login");	}
/*				
				if (login_window != null && login_window.send_data() == 1) 
				{
						System.out.println( "login ----> " + login_window.send_data() );
						
				}
				else if (login_window != null)
				{
					System.out.println( "wrong ----> " + login_window.send_data() );
				}
*/
				if ((login_window != null) && (login_window.send_data()) )
				{
					System.out.println( "login ----> " + login_window.send_data() );
					form = login_window.get_form_data();
					url = browser.get_form_addr("login");
					System.out.println( "login ----> " + url );
					form.put("post_link", url);
					try 
					{
						url = browser.post_submit_form(form);
						browser.link(url);
						state = browser.get_state();
					} catch (IOException e) 
					{	e.printStackTrace();	}
					
					login_window.sent();
				}
				else if (login_window != null)
				{
					System.out.println( "wrong ----> " + login_window.send_data() );
				}
			}
			//============================準備===================================
			else if (state.containsKey("center"))
			{
				if (now_page != "center")
				{	now_page = "center";	card.show(page, "center");	}
				
				
				
				
			}
		}
	}
	
	
	public static void main(String[] args)
	{
		ServerWindow Window = new ServerWindow(); 
		Thread conn_to_cloud = new Thread(Window);			 // 產生Thread物件
		conn_to_cloud.start();
		int i = 0;
		while(true)
		{
			if (i == 0)
			{	
				System.out.println("main running ...");
				i = 1;
			}

		}
	}
}
