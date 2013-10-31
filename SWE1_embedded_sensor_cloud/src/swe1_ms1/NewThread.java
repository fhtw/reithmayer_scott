/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package swe1_ms1;

import java.io.*;
import java.net.*;
//import java.util.logging.*;

/**
 *
 * @author if12b061
 */
public class NewThread implements Runnable {

    private Socket server;

    NewThread(Socket server) {
        this.server = server;
    }

    public void run() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(server.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null && line.length() != 0) {
                System.out.println(line);
            }
            PrintWriter writer = new PrintWriter(server.getOutputStream(), true);
            String hTag = "<h1>Hallo</h1>";
            writer.println("HTTP/1.1 200 OK");
            writer.println("ContentLength: " + hTag.length());
            writer.println("ContentType: text/html");
            writer.println();
            writer.println(hTag);
            writer.flush();
            
            if(!server.isClosed()){
                server.close();
            }
        } catch (IOException ioe) {
            System.out.println("IOException on socket listen: " + ioe);
        }
    }
}
