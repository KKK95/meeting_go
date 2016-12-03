package Local_Server;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.Enumeration;

public class local_server implements Runnable
{

	private Thread sThread; //server thread
	private Thread lThread; //listen thread
	
	public RemoteDataServer server;
	public RemoteDataServer listener;
	
	private String ipAddress;
	
	public local_server ()
	{
		
	}
	
	public void run()
	{}
	
	public void runServer(int port, int listenerPort, InetAddress ip)
	{
		if(port < 65535){
			server.setPort(port);
			server.setIP(ip);
			sThread = new Thread(server);
			sThread.start();
			
			listener.setPort(listenerPort);
			lThread = new Thread(listener);
			lThread.start();
			serverMessages.setText("Waiting for connection on " + ip);
			connectButton.setEnabled(false);
		}else{
			serverMessages.setText("The port Number must be less than 65535");
			connectButton.setEnabled(true);
		}
	}
		
}
