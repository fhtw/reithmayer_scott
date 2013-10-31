/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package swe1_ms1;

import java.io.*;
import java.net.*;
import java.util.logging.*;


/**
 *
 * @author if12b061
 */
public class Response implements Runnable{
    private Socket server;

    Response(Socket server)
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

//        server.close();
        System.out.println("\nclosed Connection\n");
      }
      catch (IOException ioe)
      {
        System.out.println("IOException on socket listen: " + ioe);
      }
    }
}
