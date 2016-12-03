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
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import Local_Server.ServerListener;


//public class Local_Server_Window extends ServerListener implements ActionListener 
public class Local_Server_Window implements ActionListener 
{
	private static final boolean DISPLAY_DESKTOP_SHORCUT = true;
	
	private static Map<String, String> form_data;
	private static boolean send = false;
	
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
	
	private JFrame this_window = null;
	
	public Local_Server_Window(JFrame window, Map<String, String> form)
	{
		form_data = form;
		this_window = window;
		init_server_window(window);
	}
	
	private void init_server_window(JFrame window)
	{
		
		connectButton.addActionListener(this);
		disconnectButton.addActionListener(this);
		//shutdownButton.addActionListener(this);
		
		Container c = window.getContentPane();
		c.setLayout(new FlowLayout());
		
		int x;
		for(x = 0; x < 4; x++){
			buffers[x] = new JTextArea("", 1, 30);
			buffers[x].setEditable(false);
			buffers[x].setBackground(window.getBackground());
		}
		
		c.add(addressLabel);
		c.add(ipTxt);
		c.add(buffers[0]);
		c.add(portLabel);
		portTxt.setText("6060");
		c.add(portTxt);

		c.add(buffers[3]);
		
		c.add(listenPortLabel);
		listenPortTxt.setText("6080");
		c.add(listenPortTxt);
		
		c.add(buffers[1]);
		c.add(connectButton);
		c.add(disconnectButton);
		c.add(buffers[2]);
		c.add(serverMessages);
		
		//c.add(shutdownButton);
		ipTxt.setSize(100, 20);
		
		close();
		
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
	
	public void open()
	{
		this_window.setLocationRelativeTo(null);
		this_window.setVisible(true);
		this_window.setResizable(false);
		
		return;
	}
	
	public void close()
	{
		this_window.setLocationRelativeTo(null);
		this_window.setVisible(false);
		this_window.setResizable(false);
		
		return;
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
