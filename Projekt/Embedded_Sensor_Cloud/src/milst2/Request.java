package milst2;

import java.io.*;
import java.net.*;
import java.util.logging.*;
import java.util.StringTokenizer;

/**
 *
 * @author if12b061 & if12b052
 */
public class Request {
    private Socket server;
    private String method = "", protocol = "";

    Request(Socket server)
    {
      this.server=server;
    }

    public UrlClass readRequest()
    {
        try
        {
            BufferedReader in = new BufferedReader(new InputStreamReader(server.getInputStream()));
            String line = in.readLine();
            if(line != null)
            {
                StringTokenizer tok = new StringTokenizer(line);
                method = tok.nextToken();
                UrlClass url = new UrlClass(tok.nextToken());
                protocol = tok.nextToken();
//                System.out.println("Method: " + method);
//                System.out.println("URL: " + url.getUrl());
//                System.out.println("Protocol: " + protocol);
                return url;
            }
        }
        catch (UnknownHostException ex)
        {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (IOException ex)
        {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
}
