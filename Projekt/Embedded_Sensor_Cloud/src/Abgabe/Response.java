package Abgabe;

import java.io.*;
import java.net.*;

//import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
//import java.io.FileOutputStream;
import java.util.StringTokenizer;
//import java.net.Socket;
//import java.net.UnknownHostException;

/**
 *
 * @author if12b061 & if12b052
 */
public class Response{
    private final Socket server;
    private final String file;

    Response(Socket server, String file)
    {
        this.server = server;
        this.file = file;
    }

    public void sendResponse()
    {
        try
        {
            String conType= "text/html", fileType = "html";
            StringTokenizer st = new StringTokenizer(this.file, ".");
            while(st.hasMoreTokens())
                fileType = st.nextToken();
            System.out.println(fileType);
            
            switch(fileType.toLowerCase())
            {
                case "html":
                    conType = "text/html";
                    break;
                case "xml":
                    conType = "text/xml";
                    break;
                case "ico":
                    conType = "image/x-icon";
                    break;
                case "jpg":
                case "jpeg":
                    conType = "image/jpeg";
                    break;
            }
            
            PrintWriter out = new PrintWriter(server.getOutputStream());
            // write to client
            out.println("HTTP/1.1 200 OK");
            out.println("Content-Type: " + conType);
            out.println("connection: close");
            // blank line signals the end of the header
            out.println("");
            File f = new File("files/" + file);
            FileInputStream input = new FileInputStream(f);
            BufferedOutputStream socketOut = new BufferedOutputStream(server.getOutputStream());
            System.out.println(f.getAbsolutePath());
            int read;
            while ((read = input.read()) != -1) {
                socketOut.write(read);
//                System.out.println("write " + read + " to socket");
            }
            out.flush();
            socketOut.flush();
        }
        catch(IOException e)
        {
            System.err.println("sth was wrong");
        }
    }
    
}
