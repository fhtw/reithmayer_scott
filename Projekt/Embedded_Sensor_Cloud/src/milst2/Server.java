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
    private Socket sock;
    private UrlClass url;
    private PluginManager _pm;

    Server(Socket server, PluginManager pm)
    {
      this.sock=server;
      this._pm = pm;
    }

    @Override
    public void run()
    {
        try
        {
            System.out.println("\nThread started: " + Thread.currentThread() + "\n");
            Request req = new Request(sock);
            url = req.readRequest();
            if(url != null)
            {
                //PluginManager pm = new PluginManager(sock);
                //pm.loadPlugin(url);
                _pm.startPlugin(url, sock);
            }
            sock.close();
            System.out.println("\nclosed Connection " + Thread.currentThread() + "\n");
        }
        catch (IOException ioe)
        {
            System.out.println("IOException on socket listen: " + ioe);
        } catch (ParseException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
