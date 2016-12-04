package Local_Server;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.BindException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.Iterator;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

import Window.ServerWindow;
import messages.Constants;

public class ImageReceiver implements Runnable {

	// local settings
	private Object lock_img = new Object();
	private Object lock = new Object();
	private Socket reg_client;
	
	private long startTime = 0;
	private long endTime = 0;
	
	public byte[] buf = null;
	private int length = 0;
	
	public ImageReceiver() {}
	
	public void init(Socket client)
	{
		boolean joined = false;
		while (joined == false)
		{
			synchronized(lock)  		//新增client 
	        {	
				if (reg_client == null)
				{
					reg_client = client;	
					joined = true;   
				}
			}
			System.out.println("init_rec");
		}
	}
	
	public BufferedImage get_image() throws IOException
	{	
		byte[] reg_buf = null;
		synchronized(lock_img)  		//新增client 
        {	
			if (length != 0)
			{
				reg_buf = new byte[length];
				reg_buf = buf;	
			}
		}
		
		if (reg_buf != null)
			return ImageIO.read(new ByteArrayInputStream(reg_buf));	
		else
			return null;
	}
	
	public long delay()
	{    
		endTime = System.currentTimeMillis();    
		if (endTime - startTime != 0)
			return (endTime - startTime)/1000 ;
		return 0;
	}
	
	public void run() 
	{
		boolean connected = false;
		Socket client = null;
		InputStream in = null;
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
		}
		
		try 
		{	in = client.getInputStream();	}
		
		catch(BindException e) 
		{	System.out.println("Can't bind to receive server port");	}
		
		catch(Exception e) 
		{	System.out.println("Create receive server port");	}
		
		while(connected) {
			// get message from sender
			try {
				// store the packets address for sending images out
				// translate and use the message to automate the desktop
				
				byte[] lengthMsg = new byte[4];
                in.read(lengthMsg);
                synchronized(lock_img)
                {
                	length = ByteBuffer.wrap(lengthMsg).asIntBuffer().get();
	                buf = new byte[length];
	                for(int i = 0; i < length; i++)
	                	buf[i] = (byte) in.read();
	                
                }
                ServerWindow.setImage(ImageIO.read(new ByteArrayInputStream(buf)));
                startTime = System.currentTimeMillis();
                
			}catch(Exception e) {
				System.out.println("[IR]" + e);
				connected = false;
			}
		}
	}
	
	private BufferedImage scaleImage(BufferedImage image, int width, int height)
	{
		BufferedImage scaledImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		// Paint scaled version of image to new image
		Graphics2D graphics2D = scaledImage.createGraphics();
		
		graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		graphics2D.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		
		graphics2D.drawImage(image, 0, 0, width, height, null); 
		graphics2D.dispose();
		return scaledImage;
	}
	
	private BufferedImage scaleImage(BufferedImage image, int width, int height, float scale)
	{
		BufferedImage scaledImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		// Paint scaled version of image to new image
		Graphics2D graphics2D = scaledImage.createGraphics();
		AffineTransform xform = AffineTransform.getScaleInstance(scale, scale);
		
		graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		graphics2D.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		
		graphics2D.drawImage(image, xform, null);
		
		graphics2D.dispose();
		return scaledImage;
	}
	
	/*
	 * 
	 * Compression
	 * 
	 */
	private ByteArrayOutputStream compressImage(BufferedImage image, float quality) throws IOException 
	{
		// Get a ImageWriter for jpeg format.
		Iterator<ImageWriter> writers = ImageIO.getImageWritersBySuffix("jpeg");
		if (!writers.hasNext()) throw new IllegalStateException("No writers found");
		ImageWriter writer = (ImageWriter) writers.next();
		
		while(!writer.getDefaultWriteParam().canWriteCompressed() && writers.next() != null)
		{
			writer = writers.next();
		}
		
		
		// Create the ImageWriteParam to compress the image.
		ImageWriteParam param = writer.getDefaultWriteParam();
		
		param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
		param.setCompressionQuality(quality);
		
		// The output will be a ByteArrayOutputStream (in memory)
		ByteArrayOutputStream bos = new ByteArrayOutputStream(32768);
		ImageOutputStream ios = ImageIO.createImageOutputStream(bos);
		writer.setOutput(ios);
		writer.write(null, new IIOImage(image, null, null), param);
		ios.flush();
		
		return bos;
		
		/*
		// otherwise the buffer size will be zero!
		// From the ByteArrayOutputStream create a RenderedImage.
		ByteArrayInputStream in = new ByteArrayInputStream(bos.toByteArray());
		RenderedImage out = ImageIO.read(in);
		int size = bos.toByteArray().length;
		showImage("Compressed to " + quality + ": " + size + " bytes", out); 
		*/
	}
}
