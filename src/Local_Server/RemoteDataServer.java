package Local_Server;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.BindException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import Window.ServerWindow;
import messages.Constants;

public class RemoteDataServer implements Runnable 
{		
		private Object lock = new Object();
		private Object lock_sender = new Object();
		private Object lock_receiver = new Object();
		private Object lock_img = new Object();
		
		private Socket reg_client;
		
		private ImageSender sender = null;
		private ImageReceiver receiver = null;
		
		private long startTime = 0;
		private long endTime = 0;
		
		public BufferedImage image = null;
		
		private AutoBot bot = null;
	
		public RemoteDataServer() 
		{	
			if (sender == null)	sender = new ImageSender();
			if (receiver == null)	receiver = new ImageReceiver();
			if (bot == null)	bot = new AutoBot();
		}
		
		public void init(Socket client)
		{
			boolean joined = false;
			while (joined == false )
			{
				synchronized(lock)  		//新增client 
		        {	
					if (reg_client == null)
					{
						reg_client = client;	
						joined = true;   
					}
				}	
				System.out.println("init");
			}	
		}
		
		public void run() 
		{
			String message;
			boolean init_img_receiver = false;
			boolean connected = false;
			BufferedReader in = null;
			PrintWriter out = null;
			Socket client = null;

			while (connected == false )
			{
				synchronized(lock)  		//新增client 
		        {	
					if (reg_client != null)
					{
						client = reg_client;	
						reg_client = null;	
						connected = true;   
					}
				}	
				System.out.println("init_run");
			}
			
			try {
				in = new BufferedReader(new InputStreamReader(client.getInputStream()));
				out = new PrintWriter(new OutputStreamWriter(client.getOutputStream()));
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			while(connected) 
			{
				System.out.println("connected");
				try 
				{
					message = in.readLine();
					
					System.out.println(message);
					
					if (message.equals("Connectivity"))
					{
						System.out.println("Trying to Connect");
						out.println(message); //echo the message back
					}
					
					else if(message.equals("Connected"))
					{
						out.println(message); //echo the message back
					}
					
					else if(message.equals("Close"))
					{
						System.out.println("Controller has Disconnected. Trying to reconnect."); //echo the message back
					}
					
					else if(message.charAt(0) == Constants.REQUESTIMAGE)
					{
						String[] arr = message.split("" + Constants.DELIMITER);
						System.out.print("Request msg:" + arr[1]+" "+arr[2]+"\n");
						get_img(Integer.parseInt(arr[1]), Integer.parseInt(arr[2]));
						sendImage(client, Integer.parseInt(arr[1]), Integer.parseInt(arr[2]));
					}
					
					else if(message.charAt(0) == Constants.PROVIDEIMAGE)
					{
						System.out.println("Provide Image");
						
						if (init_img_receiver == false)
						{
							init_receiveImage(client);
							init_img_receiver = true;
						}
						synchronized(lock_img)  		//最後一次收到圖片的時間 
				        {	
							startTime = System.currentTimeMillis();		
							image = receiver.get_image();
				        }
					}
					
					else
					{
						System.out.print("Touch:" + message);
						System.out.println("Connected to Controller");
						bot.handleMessage(message);
					}
					synchronized(lock_img)  			//計時看看5秒內有沒有收到圖片
			        {
						endTime = System.currentTimeMillis();
						if (image != null)	ServerWindow.setImage(image);
			        }
				}catch(Exception e) {
					System.out.println("[Server]" + e);
					System.out.println("Disconnected");
					System.out.println(false);
					connected = false;
				}
			}
		}
	
		public void get_img(int width, int height) throws IOException
		{
			synchronized(lock_img)  		//新增client 
	        {	
				image = receiver.get_image();
				if ((endTime - startTime)/1000 > 5 || image == null)	//計時看看5秒內有沒有收到圖片
					image = bot.getScreenCap(width, height);
	        }
		}
		
		public void sendImage(Socket client, int width, int height) 
		{	
			Thread send_image_thread = null;
			if(sender != null) 
			{
				if(width == 0 && height == 0) 
				{
					System.out.println("Receive 0 rectangle");
					return;
				}
				
				float scale = 0.5f;
				if(width > height) {
					scale = ImageSender.SIZETHRESHOLD / width;
				}else{
					scale = ImageSender.SIZETHRESHOLD / height;
				}
				synchronized(lock_img)  		//新增image
		        {	sender.setImage(image);		}
				
				synchronized(lock_sender)  		//新增client 
		        {	
					sender.init(client);
					send_image_thread = new Thread(sender);
					send_image_thread.start();
				}
			}
		}

		public void init_receiveImage(Socket client) 
		{
			System.out.println("Call receive image, start receive server");
			synchronized(lock_receiver)  		//新增client 
	        {	
				receiver.init(client);
				Thread receive_image_thread = new Thread(receiver);
				receive_image_thread.start();
	        }
		}
		
		

}



