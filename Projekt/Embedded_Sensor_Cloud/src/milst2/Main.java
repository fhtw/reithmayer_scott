package milst2;

import java.io.*;
import java.net.*;

/**
 *
 * @author if12b061 & if12b052
 */
public class Main
{
    private static int port=8080;

    public static void main(String[] args)
    {
        try
        {
            ServerSocket listener = new ServerSocket(port, 100);
            Socket server;


            
            while(true) //endlos
            {
                server = listener.accept();
                System.out.println("Connection established\n");
                // Thread erzeugen und gleich NewThread starten
                Thread t = new Thread(new Server(server));
                t.start();
            }
        }
        catch (IOException ioe)
        {
            System.out.println("IOException on socket listen: " + ioe);
        }
    }
}
