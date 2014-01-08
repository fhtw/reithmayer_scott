/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package milst2;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Angelika Reithmayer
 */
public class NavPlugin {
    
    String file = "nav.html";
    
    public void start(Socket socket, UrlClass url)
    {
        ArrayList<String> urlParts = url.getTokens();
        int count = urlParts.size();

            if (count != 3) //Anzahl der Teile der Url
            {
                this.search(url);
            }else{
                
                this.result(url);
            }
            
            new Response(socket, "nav.html").sendResponse();
    }

    private void search(UrlClass url)
    {
        FileWriter writer = null;
        try {
            File f = new File("files/" + file);//ev. temporäres file verwenden
            writer = new FileWriter(f);
            BufferedWriter out = new BufferedWriter(writer);
            out.write("<!DOCTYPE html>\n<html>\n<head>\n<title>SWE1 - Navi</title>\n");
            out.write("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n</head>\n<body>\n<H1>Navi:</H1>\n<BR/>\n");
            out.write("<form method=\"GET\" action=\"Navi\" >\n");
            out.write("<p>Straße: </p>\n");
            out.write("<input name=s />\n");
            out.write("<input type=submit value=Suchen />\n");
            out.write("</form>\n</body>\n</html>");
            out.flush();
            
        } catch (IOException ex) {
            Logger.getLogger(NavPlugin.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                writer.close();
            } catch (IOException ex) {
                Logger.getLogger(NavPlugin.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }
    
    
    private void result(UrlClass url)
    {
               String street = url.getTokens().get(2).toString();
               
    }


}

