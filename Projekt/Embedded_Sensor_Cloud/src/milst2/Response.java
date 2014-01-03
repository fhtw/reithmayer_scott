package milst2;

import java.io.*;
import java.net.*;

//import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
//import java.io.FileOutputStream;
import java.io.IOException;
//import java.net.Socket;
//import java.net.UnknownHostException;

/**
 *
 * @author if12b061 & if12b052
 */
public class Response{
    private Socket server;
    private String file;

    Response(Socket server, String file)
    {
        this.server = server;
        this.file = file;
    }

    public void sendResponse()
    {
        try
        {
            PrintWriter out = new PrintWriter(server.getOutputStream());
            // write to client
            out.println("HTTP/1.1 200 OK");
            out.println("Content-Type: text/html");
            out.println("connection: close");
            // blank line signals the end of the header
            out.println("");
            File f = new File("files/" + file);
            FileInputStream input = new FileInputStream(f);
            BufferedOutputStream socketOut = new BufferedOutputStream(server.getOutputStream());
            System.out.println(f.getAbsolutePath());
            int read = 0;
            while ((read = input.read()) != -1) {
                socketOut.write(read);
//                System.out.println("write " + read + " to socket");
            }
            out.flush();
            socketOut.flush();
        }
        catch(Exception e)
        {
            System.err.println("sth was wrong");
            e.printStackTrace();
        }
    }
    
}
