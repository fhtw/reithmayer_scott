package milst1;

import java.io.*;
import java.net.*;
import java.util.logging.*;
import java.util.StringTokenizer;

/**
 *
 * @author if12b061 & if12b052
 */
public class Request implements Runnable {
    private Socket server;
    private String header = "";
    private String method, url, protocol;

    Request(Socket server)
    {
      this.server=server;
    }

    @Override
    public void run()
    {
        try
        {
            BufferedReader in = new BufferedReader(new InputStreamReader(server.getInputStream()));
            String line = in.readLine();
            StringTokenizer tok = new StringTokenizer(line);
            method = tok.nextToken();
            url = tok.nextToken();
            protocol = tok.nextToken();
/*          //  restlichen header speichern
            while((line = in.readLine()) != null && !line.equals(""))
            {
              header = header + "\n" + line;
            }
            System.out.println("Header:" + header);
*/
            System.out.println("Method: " + method);
            System.out.println("URL: " + url);
            System.out.println("Protocol: " + protocol);
        }
        catch (UnknownHostException ex)
        {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (IOException ex)
        {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public String get_url()
    {
        return this.url;
    }
}
