package milst1;

import java.io.*;
import java.net.*;
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
                PluginManager pm = new PluginManager(url);
                String plugin = pm.getPlugin();
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
