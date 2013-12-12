package milst2;

import java.io.*;
import java.net.*;
/**
 *
 * @author if12b061 & if12b052
 */
public class Server implements Runnable
{
    private Socket server;
    private String url;

    Server(Socket server)
    {
      this.server=server;
    }

    @Override
    public void run()
    {
        try
        {
            System.out.println("\nThread started: " + Thread.currentThread() + "\n");
            Request req = new Request(server);
            req.readRequest();
            url = req.get_url();
            if(url != null)
            {
                StaticPlugin pm = new StaticPlugin(url);
                String plugin = pm.getSite();
                Response resp = new Response(server, plugin);
                resp.sendResponse();
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
