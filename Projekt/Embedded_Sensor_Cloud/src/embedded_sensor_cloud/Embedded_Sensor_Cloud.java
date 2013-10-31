/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package embedded_sensor_cloud;

/**
 *
 * @author Sam
 */

import java.io.*;
import java.net.*;


class Embedded_Sensor_Cloud extends Thread 
{

	private static int PORT = 9090;
	private ServerSocket serverSocket;
	private boolean exit = false;
	
	/**
	 * Binding my socket to the specified port.
	 */
	public Embedded_Sensor_Cloud()
	{
		try 
		{
		    serverSocket = new ServerSocket(PORT);
		} 
		catch (IOException e) {
		    System.out.println("Could not bind on port: " + PORT);
		    System.exit(-1);
		}
	}
	
	/**
	 * Entry point of the thread. It is called when the Thread(server).start() is called.
	 */
        @Override
	public void run() 
	{
		while(!exit)
		{
			//wait for incoming connection
			try 
			{
				Socket socket = serverSocket.accept();
				RequestWorker worker = new RequestWorker(socket);
				worker.start();
			} 
			catch (IOException e) 
			{
				System.out.println("Socket.accpet failed... continuing.");
			}
		}
	}	
	
	/**
	 * Calling this function would end the thread.
	 */
	public void shutdown()
	{
		this.exit=true;
	}
	
	/**
	 * Main entry point. No args required.
	 * @param args
	 */
	public static void main(String[] args) 
	{
		//create and start a new Server thread
		Embedded_Sensor_Cloud server = new Embedded_Sensor_Cloud();
		new Thread(server).start();
		System.out.println("Server started");
	}		
}