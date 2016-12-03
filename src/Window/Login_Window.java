package Window;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import Conn_to_Could.Browser.Water_Crab;

public class Login_Window implements ActionListener 
{
	private static Map<String, String> form_data;
	private static boolean send = false;
	
	private JLabel id_label = new JLabel("id: ");
	private JTextField id_txt = new JTextField(10);
	private JLabel pw_label = new JLabel("pw: ");
	private JTextField pw_txt = new JTextField(10);
	private JButton login_button = new JButton("login");
	
	private JTextArea[] buffers = new JTextArea[4];
	
	private JFrame this_window = null;
	
	public Login_Window(JFrame window, Map<String, String> form)
	{
		form_data = form;
		this_window = window;
		init_login_window(window);
	}
	
	private void init_login_window(JFrame window)
	{
		
		Container c = window.getContentPane();
		c.setLayout(new FlowLayout());
		
		int x;
		for(x = 0; x < 4; x++){
			buffers[x] = new JTextArea("", 1, 30);
			buffers[x].setEditable(false);
			buffers[x].setBackground(window.getBackground());
		}
		

		c.add(buffers[2]);
		c.add(id_label);
		c.add(id_txt);

		c.add(buffers[3]);
		c.add(pw_label);
		c.add(pw_txt);
		
		c.add(buffers[1]);
		c.add(login_button);
		
		open();
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
	
	public boolean send_data()
	{	return send;	}
	
	public void sent()
	{	
		send = false;
		return ;	
	}
	public void actionPerformed(ActionEvent e)		//這裏都跟button有關
	{
		Object src = e.getSource();
		
		if(src instanceof JButton)
		{
			if((JButton)src == login_button)
			{
				form_data.put("id", id_txt.getText()); 
				form_data.put("pw", pw_txt.getText()); 
				send = true;
			}
		}
	}
	
	
	
}
