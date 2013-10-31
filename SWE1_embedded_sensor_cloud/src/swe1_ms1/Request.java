/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package swe1_ms1;

import java.io.*;
import java.net.*;
import java.util.logging.*;

/**
 *
 * @author if12b061
 */
public class Request implements Runnable {
    private Socket server;
    private String header = "";

    Request(Socket server)
    {
      this.server=server;
    }

    @Override
    public void run()
    {
        try
        {
            System.out.println("Connection established\n");
            BufferedReader in = new BufferedReader(new InputStreamReader(server.getInputStream()));
            String line;
            while((line = in.readLine()) != null && !line.equals(""))
            {
              header = header + "\n" + line;
            }
            System.out.println(header);
            Response resp = new swe1_ms1.Response(server);
            resp.run();
        }catch (UnknownHostException ex)
        {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }catch (IOException ex)
        {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
