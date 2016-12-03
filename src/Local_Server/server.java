package Local_Server;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.Enumeration;

public class server {

	private Thread sThread; //server thread
	private Thread lThread; //listen thread
	
	public RemoteDataServer server;
	public RemoteDataServer listener;
	
	private String ipAddress;
	
	public server ()
	{
		try
		{
			InetAddress ip = getIpAddress();
			ipAddress = ip.getHostAddress();
			addressLabel.setText("IP Address: ");
			ipTxt.setText(ipAddress);
		}
		catch(Exception e){addressLabel.setText("IP Address Could Not be Resolved, Try typing in the IP address.");}
		
		int port = Integer.parseInt(portTxt.getText());
		int clientPort = Integer.parseInt(listenPortTxt.getText());

		try{
			InetAddress ip = InetAddress.getByName(ipTxt.getText());
		}catch(UnknownHostException err){
			serverMessages.setText("Error: Check that the ip you have entered is correct.");
		}
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
