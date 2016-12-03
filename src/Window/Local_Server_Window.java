package Window;

import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import Local_Server.ServerListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.*;
import java.util.Collections;
import java.util.Enumeration;

//public class Local_Server_Window extends ServerListener implements ActionListener 
public class Local_Server_Window implements ActionListener 
{
	private static final boolean DISPLAY_DESKTOP_SHORCUT = true;
	
	private static Map<String, String> form_data;
	private static boolean stendby = false;
	//=====================================================================
	private String ipAddress;						
	//=====================================================================
	private static JFrame shortcut = new JFrame("Dekstop Shortcut");
	private static JLabel shortcutLabel = new JLabel(new ImageIcon());
	//=====================================================================		
	
	private JLabel addressLabel = new JLabel("");
	private JTextField ipTxt = new JTextField(10);
	private JLabel portLabel = new JLabel("PORT: ");
	private JTextField portTxt = new JTextField(5);
	private JLabel listenPortLabel = new JLabel("Listen PORT: ");
	private JTextField listenPortTxt = new JTextField(5);
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
		center_page.add(portLabel);
		portTxt.setText("6060");
		center_page.add(portTxt);

		center_page.add(buffers[3]);
		
		center_page.add(listenPortLabel);
		listenPortTxt.setText("6080");
		center_page.add(listenPortTxt);
		
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

	
	
	
	
	public void actionPerformed(ActionEvent e)		//這裏都跟button有關
	{
		Object src = e.getSource();
		
		if(src instanceof JButton)
		{
			if((JButton)src == connectButton)
			{
				int port = Integer.parseInt(portTxt.getText());
				int listenPort = Integer.parseInt(listenPortTxt.getText());
				try
				{
					InetAddress ip = InetAddress.getByName(ipTxt.getText());
	//				runServer(port, listenPort, ip);
				}catch(UnknownHostException err)
				{
					serverMessages.setText("Error: Check that the ip you have entered is correct.");
				}
			}
				
			else if((JButton)src == disconnectButton)
			{
				closeServer();
				setConnectButtonEnabled(true);
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
	
	public void setConnectButtonEnabled(boolean enable) {
		connectButton.setEnabled(enable);
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
	
	
	
	public static void setImage(byte[] image) {
		if(DISPLAY_DESKTOP_SHORCUT) {
			ByteArrayInputStream in = new ByteArrayInputStream(image);
			int height, width;
			try {
				BufferedImage img = ImageIO.read(in);
				height = 500;
				width = 300;
				System.out.println("Img:" + height + " " + width);
				
				Image scaleImage = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
				BufferedImage imageBuff = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
				imageBuff.getGraphics().drawImage(scaleImage, 0, 0, new Color(0, 0, 0), null);
				
				ByteArrayOutputStream buffer = new ByteArrayOutputStream();
				
				ImageIO.write(imageBuff, "jpg", buffer);
				
				System.out.println("DrawImage:" + image.length);
				shortcutLabel.setIcon(new ImageIcon(buffer.toByteArray()));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	
	
}
