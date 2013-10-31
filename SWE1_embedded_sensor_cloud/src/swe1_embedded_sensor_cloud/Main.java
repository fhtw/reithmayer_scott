/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package swe1_embedded_sensor_cloud;


import java.io.*; //Importiert alle Klassen aus dem I/O Paket
import java.net.*;//Klassen die TCP/IP verbindungen supporten
/**
 *
 * @author if12b061
 * @author if12b052
 */
public class Main {

    /**
     * @param args the command line arguments
     */
  private static int port=8080, maxConnections=0;
  // Listen for incoming connections and handle them
    public static void main(String[] args) {

    int i=0;

    try{
      ServerSocket listener = new ServerSocket(port);
      Socket server;

      while((i++ < maxConnections) || (maxConnections == 0)){
        server = listener.accept();
       doComms conn_c= new doComms(server);
        Thread t = new Thread((Runnable) conn_c);
        t.start();
      }
    } catch (IOException ioe) {
      System.out.println("IOException on socket listen: " + ioe);
      ioe.printStackTrace();
    }
  }

}

