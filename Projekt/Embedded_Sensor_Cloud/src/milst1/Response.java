package milst1;

import java.io.*;
import java.net.*;

//import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
//import java.io.FileOutputStream;
import java.io.IOException;
//import java.net.Socket;
//import java.net.UnknownHostException;

/**
 *
 * @author if12b061 & if12b052
 */
public class Response implements Runnable{
    private Socket server;

    Response(Socket server)
    {
        this.server=server;
    }

    @Override
    public void run()
    {
        try
        {
            PrintWriter out = new PrintWriter(server.getOutputStream());
            // write to client
            out.println("HTTP/1.1 200 OK");
            out.println("Content-Type: text/html");
            out.println("connection: close");
            // blank line signals the end of the header
            out.println("");
            // Send the HTML page
            out.println("<H1>Juchuuu!!! :P</H1>");  //plugin
            out.println("<BR/>");  //plugin
            out.println("<a href='/test'>testlink</a>");  //plugin
            out.println("<a href='/test2'>testlink2</a>");  //plugin
            out.println("<a href='/fileTest'>fileTest</a>");  //plugin
            out.flush();
        }
        catch (IOException ioe)
        {
            System.out.println("IOException on socket listen: " + ioe);
        }
    }

    public void test()
    {
        try
        {
            PrintWriter out = new PrintWriter(server.getOutputStream());
            // write to client
            out.println("HTTP/1.1 200 OK");
            out.println("Content-Type: text/html");
            out.println("connection: close");
            // blank line signals the end of the header
            out.println("");
            // Send the HTML page
            out.println("<H1>Testseite</H2>");  //plugin
            out.println("<BR/>");  //plugin
            out.println("<a href='/'>start</a>");  //plugin
            out.flush();
        }
        catch (IOException ioe)
        {
            System.out.println("IOException on socket listen: " + ioe);
        }
    }
    public void test2()
    {
        try
        {
            PrintWriter out = new PrintWriter(server.getOutputStream());
            // write to client
            out.println("HTTP/1.1 200 OK");
            out.println("Content-Type: text/html");
            out.println("connection: close");
            // blank line signals the end of the header
            out.println("");
            // Send the HTML page
            out.println("<H1>Testseite2</H2>");  //plugin
            out.println("<BR/>");  //plugin
            out.println("<a href='/'>start</a>");  //plugin
            out.flush();
        }
        catch (IOException ioe)
        {
            System.out.println("IOException on socket listen: " + ioe);
        }
    }
    public void unknown()
    {
        try
        {
            PrintWriter out = new PrintWriter(server.getOutputStream());
            // write to client
            out.println("HTTP/1.1 404 Not Found");
    //        out.println("Content-Type: text/html");
            out.println("connection: close");
            // blank line signals the end of the header
            out.println("");
            out.flush();

        }
        catch (IOException ioe)
        {
            System.out.println("IOException on socket listen: " + ioe);
        }
    }


    public void fileTest()
    {
        try
        {
            PrintWriter out = new PrintWriter(server.getOutputStream());
            // write to client
            out.println("HTTP/1.1 200 OK");
            out.println("Content-Type: text/html");
            out.println("connection: close");
            // blank line signals the end of the header
            out.println("");
            File f = new File("files/test.html");
            FileInputStream input = new FileInputStream(f);
            BufferedOutputStream socketOut = new BufferedOutputStream(server
                    .getOutputStream());
            System.out.println(f.getAbsolutePath());
            int read = 0;
            while ((read = input.read()) != -1) {
                socketOut.write(read);
                System.out.println("write " + read + " to socket");
            }
            out.flush();
            socketOut.flush();

//            server.close();
//            this.serverSocket.close();
        }
        catch(Exception e)
        {
            System.err.println("sth was wrong");
            e.printStackTrace();
        }
    }
}
