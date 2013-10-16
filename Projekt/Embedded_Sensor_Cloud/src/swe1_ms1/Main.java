/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package swe1_ms1;


import java.io.*;
import java.net.*;
import java.util.logging.*;
//import java.security.*;

/**
 *
 * @author if12b061 & if12b052
 */
public class Main
{
    /**
     * @param args the command line arguments
     */
  private static int port=8080, maxConnections=0;
  // Listen for incoming connections and handle them
    public static void main(String[] args)
    {

    int i=0;

    try
    {
      ServerSocket listener = new ServerSocket(port);
      Socket server;

      while((i++ < maxConnections) || (maxConnections == 0))
      {
        server = listener.accept();
        requestHandler req= new requestHandler(server);
// Thread erzeugen und gleich req starten
        Thread t = new Thread(req);
        t.start();
      }
    } catch (IOException ioe) {
      System.out.println("IOException on socket listen: " + ioe);
    }
  }
}

class responseHandler implements Runnable
{
    private Socket server;
   
    responseHandler(Socket server)
    {
      this.server=server;
    }

    @Override
    public void run ()
    {
      try
      {
        // Get input from the client
//        DataInputStream in = new DataInputStream (server.getInputStream());
//        PrintStream out = new PrintStream(server.getOutputStream());
        PrintWriter out = new PrintWriter(server.getOutputStream());
/*
        while((line = in.readLine()) != null && !line.equals(".")) {
          input=input + line;
          out.println("I got:" + line);
        }
*/
        // Now write to the client
        out.println("HTTP/1.1 200 OK");
        out.println("Content-Type: text/html");
        out.println("connection: close");
        // this blank line signals the end of the header
        out.println("");
        // Send the HTML page
        out.println("<H1>Juchuuu!!! :P</H2>");
        out.flush();

        server.close();
        System.out.println("\nclosed Connection\n");
      }
      catch (IOException ioe)
      {
        System.out.println("IOException on socket listen: " + ioe);
      }
    }
}

class requestHandler implements Runnable
{
    private Socket server;
    private String header = "";

    requestHandler(Socket server)
    {
      this.server=server;
    }

    @Override
    public void run()
    {
        try
        {
            System.out.println("Connection established\n");
            BufferedReader in = new BufferedReader(new InputStreamReader(server.getInputStream()));
            String line;
            while((line = in.readLine()) != null && !line.equals(""))
            {
              header = header + "\n" + line;
            }
            System.out.println(header);
            responseHandler resp = new responseHandler(server);
            resp.run();      
        }catch (UnknownHostException ex)
        {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }catch (IOException ex)
        {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}