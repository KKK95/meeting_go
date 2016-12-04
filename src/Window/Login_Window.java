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
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import Conn_to_Could.Browser.Water_Crab;

public class Login_Window implements ActionListener 
{
	private static Map<String, String> form_data;
	private static boolean send;
	
	
	private JLabel id_label = new JLabel("id: ");
	private JTextField id_txt = new JTextField(10);
	private JLabel pw_label = new JLabel("pw: ");
	private JTextField pw_txt = new JTextField(10);
	public JButton login_button = new JButton("login");
	
	private JTextArea[] buffers = new JTextArea[4];
	
	private JPanel login_page = null;
	
	public Login_Window()
	{
		send = false;
		form_data = new LinkedHashMap();;
		init_login_window();
	}
	
	private void init_login_window()
	{
		login_button.addActionListener(this);
		login_page = new JPanel();
		int x;
		for(x = 0; x < 4; x++){
			buffers[x] = new JTextArea("", 1, 30);
			buffers[x].setEditable(false);
		}
		

		login_page.add(buffers[2]);
		login_page.add(id_label);
		login_page.add(id_txt);

		login_page.add(buffers[3]);
		login_page.add(pw_label);
		login_page.add(pw_txt);
		
		login_page.add(buffers[1]);
		login_page.add(login_button);
		
		id_txt.setText("lsaa");
		pw_txt.setText("12");
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
				System.out.println("send : " + send);
			}
		}
	}
	
	public JPanel get_page()
	{	return login_page;	}
	
	public Map<String, String> get_form_data()
	{	return form_data;	}
	
	public boolean send_data()
	{	return send;	}
	
	public void sent()
	{	
		send = false;
		return ;	
	}
	
}
