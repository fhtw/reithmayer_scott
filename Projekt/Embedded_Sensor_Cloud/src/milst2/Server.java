package milst2;

import java.io.*;
import java.net.*;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author if12b061 & if12b052
 */
public class Server implements Runnable
{
    private Socket server;
    private UrlClass url;

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
            url = req.readRequest();
            if(url != null)
            {
                PluginManager pm = new PluginManager(server);
                pm.loadPlugin(url);
                pm.startPlugin(url);
//                StaticPlugin sp = new StaticPlugin(url.getUrl());
//                String plugin = sp.getSite();
//                Response resp = new Response(server, plugin);
//                resp.sendResponse();
            }
            server.close();
            System.out.println("\nclosed Connection " + Thread.currentThread() + "\n");
        }
        catch (IOException ioe)
        {
            System.out.println("IOException on socket listen: " + ioe);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
