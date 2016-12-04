package Window;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import java.net.*;
import java.util.Collections;
import java.util.Enumeration;

import Local_Server.local_server;

//public class Local_Server_Window extends ServerListener implements ActionListener 
public class Local_Server_Window implements ActionListener 
{
	private static local_server server_running = null;
	private static final boolean DISPLAY_DESKTOP_SHORCUT = true;
	
	private static Map<String, String> form_data;
	private static boolean stendby = false;
	private static boolean send = false;
	//=====================================================================
	private String ipAddress;						
	//=====================================================================
	private static JFrame shortcut = new JFrame("Dekstop Shortcut");
	private static JLabel shortcutLabel = new JLabel(new ImageIcon());
	//=====================================================================		
	
	private JLabel addressLabel = new JLabel("");
	private JTextField ipTxt = new JTextField(10);
	private JLabel get_img_port_label = new JLabel("接收畫面端口: ");
	private JTextField get_img_port_txt = new JTextField(5);
	private JLabel remort_port_label = new JLabel("遠端控制端口 : ");
	private JTextField remort_port_txt = new JTextField(5);
	private JLabel send_img_port_label = new JLabel("送出畫面端口 : ");
	private JTextField send_img_port_txt = new JTextField(5);
	private JButton connectButton = new JButton("Connect");
	private JButton disconnectButton = new JButton("Disconnect");
	
	private JLabel serverMessages = new JLabel("Not Connected");
		
	//=====================================================================	
	
	private JTextArea[] buffers = new JTextArea[4];
	
	private JButton shutdownButton = new JButton("Shutdown");
	
	private JPanel center_page = null;
	
	public Local_Server_Window()
	{
		try{
			InetAddress ip = getIpAddress();
			ipAddress = ip.getHostAddress();
			addressLabel.setText("IP Address: ");
			ipTxt.setText(ipAddress);
		}
		catch(Exception e){addressLabel.setText("IP Address Could Not be Resolved, Try typing in the IP address.");}
		
		
		form_data = new LinkedHashMap();
		init_server_window();
	}
	
	private void init_server_window()
	{
		connectButton.addActionListener(this);
		disconnectButton.addActionListener(this);
		//shutdownButton.addActionListener(this);
		center_page = new JPanel();
		
		int x;
		for(x = 0; x < 4; x++){
			buffers[x] = new JTextArea("", 1, 30);
			buffers[x].setEditable(false);
//			buffers[x].setBackground(window.getBackground());
		}
		
		center_page.add(addressLabel);
		center_page.add(ipTxt);
		center_page.add(buffers[0]);
		center_page.add(remort_port_label);
		remort_port_txt.setText("6060");
		center_page.add(remort_port_txt);

		center_page.add(buffers[1]);
		
		center_page.add(get_img_port_label);
		get_img_port_txt.setText("6080");
		center_page.add(get_img_port_txt);
		
		center_page.add(buffers[2]);
		
		center_page.add(send_img_port_label);
		send_img_port_txt.setText("6090");
		center_page.add(send_img_port_txt);
		
		center_page.add(buffers[1]);
		center_page.add(connectButton);
		center_page.add(disconnectButton);
		center_page.add(buffers[2]);
		center_page.add(serverMessages);
		
		//c.add(shutdownButton);
		ipTxt.setSize(100, 20);
		
	}
	
	private InetAddress getIpAddress() throws Exception
	{
		// this first line generally works on mac and windows
		InetAddress ip = InetAddress.getLocalHost();
		
		// but on linux...
		if(ip.isLoopbackAddress())
		{
			//loop trough all network interfaces
			Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();
            for (NetworkInterface netint : Collections.list(nets))
            {
            	//loop through the ip address associated with the interface
            	Enumeration<InetAddress> inetAddresses = netint.getInetAddresses();
                for (InetAddress inetAddress : Collections.list(inetAddresses)) {
                	
                	// if the address is no the loopback and is not ipv6
                	if(!inetAddress.isLoopbackAddress() && !inetAddress.toString().contains(":"))
                		return inetAddress;
                }
            }
		}
		
		return ip;
	}
	
	public JPanel get_page()
	{	return center_page;	}
	
	public boolean stendby()
	{	return stendby;	}
	
	public void sleeping()
	{	
		stendby = false;
		return ;	
	}
	
	public boolean send_data()
	{	return send;	}
	
	public void sent()
	{	
		send = false;
		return ;	
	}
	
	public Map<String, String> get_form_data()
	{	return form_data;	}
	
	public void actionPerformed(ActionEvent e)		//這裏都跟button有關
	{
		Object src = e.getSource();
		
		if(src instanceof JButton)
		{
			if((JButton)src == connectButton)
			{
				int remort_port = Integer.parseInt(remort_port_txt.getText());
				int get_img_port = Integer.parseInt(get_img_port_txt.getText());
				int send_img_port = Integer.parseInt(send_img_port_txt.getText());
				
				
				try
				{
					form_data.put("ip", ipTxt.getText());
					stendby = true;
					send = true;
					InetAddress ip = InetAddress.getByName(ipTxt.getText());
					
					server_running = new local_server(remort_port, get_img_port, send_img_port, ip);
					
					connectButton.setEnabled(false);
				}catch(UnknownHostException err)
				{
					serverMessages.setText("Error: Check that the ip you have entered is correct.");
				}
			}
				
			else if((JButton)src == disconnectButton)
			{
				closeServer();
				connectButton.setEnabled(true);
			}
			
			else if((JButton)src == shutdownButton)
			{
				closeServer();
				shutdown();
				System.exit(0);
			}
		}
	}
	
	public void closeServer()
	{
	//	server.shutdown();
	//	listener.shutdown();
		setMessage("Disconnected");
	}
	
	public void setMessage(String msg) {
		serverMessages.setText(msg);
	}
	
	
	
	private static void shutdown() 
	{
	    String shutdownCommand;
	    String operatingSystem = System.getProperty("os.name");

	    if ("Linux".equals(operatingSystem) || "Mac OS X".equals(operatingSystem)) {
	        shutdownCommand = "shutdown -h now";
	    }
	    else if ("Windows".equals(operatingSystem)) {
	        shutdownCommand = "shutdown.exe -s -t 0";
	    }
	    else {
	    	shutdownCommand = "null";
	    }

	    Runtime runtime = Runtime.getRuntime();
        try {
			Process proc = runtime.exec(shutdownCommand);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	
	
	
	
	
}
