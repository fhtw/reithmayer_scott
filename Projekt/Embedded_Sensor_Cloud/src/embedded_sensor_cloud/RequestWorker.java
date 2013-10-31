/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package embedded_sensor_cloud;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

/**
 *
 * @author Sam
 */
public class RequestWorker extends Thread
{

	private Socket socket;
	private static final String ROOT_PATH = new java.io.File("root").getAbsolutePath();
	private static final String DEFAULT_PAGE = "index.html";

	public RequestWorker(Socket socket)
	{
		this.socket = socket;
	}
	
	/**
	 * handling the users request
	 */
	public void run()
	{
		OutputStream os = null;
		InputStream is;
		
		try 
		{
			os = socket.getOutputStream();
			is = socket.getInputStream();
			
			System.out.println("Handling client: " + socket.getRemoteSocketAddress());
	
			String request = convertStreamToString(is);
		
			requestHandler(request, os);
		}
		catch (HTTPException ex)
		{
			try 
			{
				os.write(ex.getMessage().getBytes());
			} 
			catch (IOException e) {}
			
		}
		catch (Exception ex) 
		{
			System.out.println("Failed handling client: "+socket.getRemoteSocketAddress());
			System.out.println(ex.getMessage());
		}
		finally
		{
			try 
			{
				os.close();
			} 
			catch (IOException e) {}
		}
	}

	/**
	 * Handles the actual HTTP request.
	 * @param request The request sent by the users browser
	 * @param os The output stream related with the request.
	 * @throws HTTPException When the request is invalid, this exception is thrown.
	 * @throws IOException 
	 */
	private void requestHandler(String request, OutputStream os) throws HTTPException, IOException
	{
		//split header into lines
		String[] header = request.split("\n");
		
		//get the line containing the requested file
		String requestPath = header[0];
		
		//remove everything except the requested path 
		requestPath=requestPath.replaceAll("HTTP/\\d.\\d", "");
		requestPath=requestPath.replaceAll("GET|POST", "");
		requestPath = requestPath.trim();
		  
		//convert to relative windows path
		requestPath = requestPath.replace("/", "\\");
		requestPath = ROOT_PATH + requestPath;
		
		File file = new File(requestPath);
		
		//check if the file exists
		if(!file.exists())
			throw new HTTPException(Status.HTTP_NOT_FOUND);
		
		//if the path points to a directory, we look for the DEFAULT_PAGE.
		if(file.isDirectory())
		{
			requestPath+=DEFAULT_PAGE;
			file = new File(requestPath);
			
			if(!file.exists())
				throw new HTTPException(Status.HTTP_NOT_FOUND);			
		}
		
		//get read/write related stuff
		FileInputStream fis = new FileInputStream(file);
		BufferedInputStream  bis = new BufferedInputStream(fis);
		DataInputStream dis = new DataInputStream(bis);
		
		//write response header
		String responseHeader = "HTTP/1.1 200 OK\n";
		 
		String mimeType = MimeTypeResolver.types.get(getFileExtension(requestPath));
		if(mimeType!=null)
			responseHeader += "Content-Type: " + mimeType + "\n"; 
		  
		responseHeader += "\n";
		os.write(responseHeader.getBytes());
		  
		//write the actual file
		byte[] buffer = new byte[256];
		int len;
		while (dis.available() != 0) {
			len=dis.read(buffer, 0, 255);
			os.write(buffer, 0, len);
		}
	}	
	
	/**
	 * Converts the content of an InputString to a String. The reader stops reading, as soon as an empty line is found.
	 * @param is
	 * @return
	 * @throws Exception
	 */
	public static String convertStreamToString(InputStream is) throws Exception 
	{
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		String line = null;
		while ((line = reader.readLine()) != null) 
		{
			if(line.length()==0)
				break;
			else
				sb.append(line + "\n");
		}
		return sb.toString();
	}

	/**
	 * Finds the last occurrence of "." in a String, and cuts off the file extension.
	 * @param f
	 * @return
	 */
	private String getFileExtension(String f)
	{
		int i = f.lastIndexOf(".");
		if(i==-1)
			return "";
		String ret = f.substring(i+1);
		return ret;
	}
}