/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Abgabe;

import java.io.File;
import java.net.Socket;
import java.util.StringTokenizer;

/**
 *
 * @author if12b061 & if12b052
 */
public class StaticPlugin{

    private String url;
    private final Socket socket;

    StaticPlugin(Socket s, String url)
    {
      this.url = url;
      this.socket = s;
    }
    
    public void start()
    {
        if(url.isEmpty() || url.equalsIgnoreCase("static")) {
            url = "index.html";
        }
//        System.out.println(url);
        Response resp = null;

        File dir = new File("files");
        File[] listFiles = dir.listFiles();
        for(File f : listFiles)
        {
//            System.out.println(f.getName());
            if(url.equals(f.getName()))
            {
                resp = new Response(socket, url);
                break;
            }
        }

        if(resp == null)
            resp = new Response(socket, "error.html");

        resp.sendResponse();
//        System.out.println(url);
        
        
    }
    
}
