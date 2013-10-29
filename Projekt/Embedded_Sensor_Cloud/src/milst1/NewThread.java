package milst1;

import java.io.*;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author if12b061 & if12b052
 */
public class NewThread implements Runnable
{
    private Socket server;
    private String url;

    NewThread(Socket server)
    {
      this.server=server;
    }

    public void run()
    {
        try
        {
            System.out.println("\nThread started: " + Thread.currentThread() + "\n");
            Request req = new Request(server);
            req.run();
            url = req.get_url();
            Response resp = new Response(server);
            if(url.equals("/"))
            {
                resp.run();
            }
            else if(url.equals("/test"))
            {
                resp.test();
            }
            else if(url.equals("/test2"))
            {
                resp.test2();
            }
            else if(url.equals("/fileTest"))
            {
                resp.fileTest();
            }
            else
            {
                resp.unknown();
            }
            server.close();
            System.out.println("\nclosed Connection " + Thread.currentThread() + "\n");
        }
        catch (IOException ioe)
        {
            System.out.println("IOException on socket listen: " + ioe);
        }
    }

}
