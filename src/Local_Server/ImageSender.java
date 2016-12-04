package Local_Server;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.Iterator;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

import Window.ServerWindow;

public class ImageSender implements Runnable
{
	private Object lock = new Object();
	private InetAddress clientAddr;
	private Socket reg_socket;
	
	public static final float SIZETHRESHOLD = 100f;
	
	private BufferedImage image;
	
	public ImageSender() {}
	
	public void init(Socket socket) 
	{
		boolean joined = false;
		while (joined == false)
		{
			synchronized(lock)  		//新增client 
	        {	
				if (reg_socket == null)
				{
					reg_socket = socket;	
					joined = true;   
				}
			}
			System.out.println("sender init : " + joined);
		}
	}
	
	public void setImage(BufferedImage img)
	{	image = img;	}
	
	public void run()
	{
		OutputStream out = null;
		boolean connected = false;
		Socket socket = null;
		while (connected == false )
		{
			synchronized(lock)  		//新增client 
	        {	
				if (reg_socket != null)
				{
					socket = reg_socket;	
					reg_socket = null;	
					connected = true;   
				}
			}	
			System.out.println("sender run connected : " + connected);
		}
		try {
			out = socket.getOutputStream();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		ByteArrayOutputStream buffer;
		
		try{
			ByteArrayOutputStream tmp = compressImage(image, 1.0f);
		    ImageIO.write(image, "jpeg", tmp);
		    tmp.close();
		    
		    int contentLength = tmp.size();
		    float compress = 64000.0f/contentLength;
		    System.out.println("Compress size "+compress);
		    
		    if(compress > 1.0) {
		    	buffer = tmp;
		    } else {
		    	buffer = compressImage(image, compress);
		    }

		}catch(IOException e){
			System.out.println(e);
			return;
		}
		
		byte[] data = buffer.toByteArray();
        //byte[] data = (new String("hi")).getBytes();
		
        try {
        	byte[] dataLength = ByteBuffer.allocate(4).putInt(data.length).array();
        	out.write(dataLength);
			out.write(data);
			out.flush();
			System.out.println("Data Length = "+data.length);
//			ServerWindow.setImage(data);
			buffer.close();
		} catch (IOException e) {
			System.out.println("Data Length = "+data.length);
			e.printStackTrace();
		} catch (NullPointerException e) {
			System.out.println("Data Length = " + data.length);
			e.printStackTrace();
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
