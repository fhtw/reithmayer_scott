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
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Angelika Reithmayer
 */
public class TempPlugin {
        
    public void getTemp(Socket sock, UrlClass url) throws IOException, ParseException
    {
        ArrayList<String> urlParts = url.getTokens();
        int count = urlParts.size();
        
        String dateString, file = "temp.html";
        File f = new File("files/" + file);
        FileWriter writer = new FileWriter(f);
        BufferedWriter out = new BufferedWriter(writer);
        out.write("<!DOCTYPE html>\n<html>\n<head>\n<title>SWE1 - Temperatures</title>\n");
        out.write("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n</head>\n<body>\n<H1>Temperatures:</H1>\n<BR/>\n");
            
        try {
            //Datenbank Treiber laden
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch(ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "MS JDBC Driver not installed\n" + ex);
        }
        
        //********************try statt db.close() od cmd.close()... was besser? oder egal? ***********************************\\
        try (Connection db = DriverManager.getConnection("jdbc:sqlserver://localhost\\sqlexpress;" + "databaseName=SWE1", "swe1", "test")) 

        {
            PreparedStatement cmd;
              // Parameter setzen
            if (count != 4)
            {
                this.tempAll(db, out, url);
            }else{
                cmd = db.prepareStatement("SELECT[date], [temp] FROM [temperature] WHERE [date] = ?");
                StringBuilder sb = new StringBuilder();
                sb.append(urlParts.get(1));
                sb.append("-");
                sb.append(urlParts.get(2));
                sb.append("-");
                sb.append(urlParts.get(3));
                dateString = sb.toString();
                cmd.setString(1, dateString);
//                System.out.println(dateString);
            
                try (ResultSet rd = cmd.executeQuery())
                {
                    while(rd.next())
                    {
                        sb = new StringBuilder();
                        sb.append(rd.getDate(1));
                        sb.append(": ");
                        sb.append(rd.getFloat(2));

                        out.write(sb.toString());
                        out.write("<br/>\n");
                    }
                }
            }      
        }catch (SQLException ex) {
            Logger.getLogger(TempPlugin.class.getName()).log(Level.SEVERE, null, ex);
        }
            
            out.write("</body>\n</html>");
            out.flush();
            new Response(sock, file).sendResponse();

    }
    
    private void tempAll(Connection db, BufferedWriter out, UrlClass url) throws SQLException, IOException
    {
        int count = 1, page = 1;
        
        if(url.getTokens().size() > 1)    
            page = Integer.parseInt(url.getTokens().get(2).toString());
        PreparedStatement cmd = db.prepareStatement("SELECT[date], [temp] FROM [temperature] WHERE [id] >= ? AND [id] < ?");
        cmd.setInt(1, page*20-19);
        cmd.setInt(2, page*20);

        try (ResultSet rd = cmd.executeQuery())
        {
            while(rd.next())
            {
                StringBuilder sb = new StringBuilder();
                sb.append(rd.getDate(1));
                sb.append(": ");
                sb.append(rd.getFloat(2));

                out.write(sb.toString());
                out.write("<br/>\n");
                count++;
            }
        }

        out.write("<form method=\"GET\" action=\"GetTemperature\" >\n");
        if(page!=1)
        {
            out.write("<input value=");
            page--;
            System.out.println(page);
            out.write("\""+page+"\"");
            out.write(" name=p type=submit />\n");
            page++;
        }
        if(count==20)
        {
            out.write("<input value=");
            page++;
            System.out.println(page);
            out.write("\""+page+"\"");
            out.write(" name=p type=submit />\n");
        }
        out.write("</form>");

    }

    
    
    //die macht nix, war nur zum probieren
    public static void main(String[] args)
    {
        try {
            //Datenbank Treiber laden
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch(ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "MS JDBC Driver not installed\n" + ex);
        }
        
        //********************try statt db.close() od cmd.close()... was besser? oder egal? ***********************************\\
        try (Connection db = DriverManager.getConnection("jdbc:sqlserver://localhost\\sqlexpress;" + "databaseName=SWE1", "swe1", "test"); 
            PreparedStatement cmd = db.prepareStatement("SELECT[id], [date], [temp] FROM [temperature] WHERE [date] = ?"))
        {
            // Parameter setzen
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date date = format.parse("2008-12-12");
            cmd.setDate(1, new java.sql.Date(date.getTime()));
            try (ResultSet rd = cmd.executeQuery())
            {
                while(rd.next())
                {
                    StringBuilder sb = new StringBuilder();
                    sb.append(rd.getInt(1));
                    sb.append(' ');
                    sb.append(rd.getDate(2));
                    sb.append(' ');
                    sb.append(rd.getFloat(3));

                    System.out.println(sb);
                }
            }
        } catch (SQLException | ParseException ex) {
            Logger.getLogger(TempPlugin.class.getName()).log(Level.SEVERE, null, ex);
        }



    
    }

}
