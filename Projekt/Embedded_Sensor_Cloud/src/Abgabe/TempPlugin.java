/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Abgabe;

import java.io.*;
import java.net.Socket;
import java.sql.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.*;

/**
 *
 * @author Angelika Reithmayer
 */
public class TempPlugin {
        
    //stellt verbindung zur Datenbank her & ruft entsprechende Funktion zum abrufen der Daten auf
    public void getTemp(Socket sock, UrlClass url) throws IOException, ParseException
    {
        ArrayList<String> parameters = url.getParameters();
        int count = parameters.size();
        String file;
            
        try {
            //Datenbank Treiber laden
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch(ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "MS JDBC Driver not installed\n" + ex);
        }
        
        //********************try statt db.close() od cmd.close()... was besser? oder egal? ***********************************\\
        try (Connection db = DriverManager.getConnection("jdbc:sqlserver://localhost\\sqlexpress;" + "databaseName=SWE1", "swe1", "test")) 

        {
            if (count != 3) //Anzahl der Teile der Url
            {
                this.tempAll(db, url);
                file = "temp.html";
            }else{
                
                this.tempDate(db, url);
                file = "temp.xml";
            }
            
            new Response(sock, file).sendResponse();
            
        }catch (SQLException ex) {
            Logger.getLogger(TempPlugin.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    private void tempDate(Connection db, UrlClass url) throws SQLException, IOException
    {
        try {
            String dateString;
            PreparedStatement cmd;
            ArrayList<String> parameters = url.getParameters();
            
            cmd = db.prepareStatement("SELECT[date], [temp] FROM [temperature] WHERE [date] = ?");
            StringBuilder sb = new StringBuilder();
            sb.append(parameters.get(0));
            sb.append("-");
            sb.append(parameters.get(1));
            sb.append("-");
            sb.append(parameters.get(2));
            dateString = sb.toString();
            cmd.setString(1, dateString);
    //      System.out.println(dateString);
            
            Document xml = newDocument("temperatures");
            Element root = xml.getDocumentElement();
            Node child;
            Attr a;

            try (ResultSet rd = cmd.executeQuery())
            {
                while(rd.next())
                {
                    child = xml.createElement("temperature");
                    a = xml.createAttribute("date");
                    a.setValue(dateString);
                    child.getAttributes().setNamedItem(a);
                    sb = new StringBuilder();
                    sb.append(rd.getFloat(2));
                    child.appendChild(xml.createTextNode(sb.toString()));
                    root.appendChild(child);
                }
            }
                // write the content into xml file
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            DOMSource source = new DOMSource(xml);
            StreamResult result = new StreamResult(new File("files/temp.xml")); //ev. temporäres file verwenden
            transformer.transform(source, result);
                    
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(TempPlugin.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerConfigurationException ex) {
            Logger.getLogger(TempPlugin.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerException ex) {
            Logger.getLogger(TempPlugin.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    private void tempAll(Connection db, UrlClass url) throws SQLException, IOException
    {
        int count = 1, page = 1;
        
        String file = "temp.html";
        File f = new File("files/" + file);//ev. temporäres file verwenden
        FileWriter writer = new FileWriter(f);
        BufferedWriter out = new BufferedWriter(writer);
        out.write("<!DOCTYPE html>\n<html>\n<head>\n<title>SWE1 - Temperatures</title>\n");
        out.write("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n</head>\n<body>\n<H1>Temperatures:</H1>\n<BR/>\n");
        
        if(url.getParameters().size() == 2)    
            page = Integer.parseInt(url.getParameters().get(1));
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
            System.out.println("prev page: " + page);
            out.write("\""+page+"\"");
            out.write(" name=p type=submit />\n");
            page++;
        }
        if(count==20)
        {
            out.write("<input value=");
            page++;
            System.out.println("next page: " + page);
            out.write("\""+page+"\"");
            out.write(" name=p type=submit />\n");
        }
        out.write("</form>");
        out.write("</body>\n</html>");
        out.flush();

    }

    public static Document newDocument(String rootName) throws ParserConfigurationException	{
        
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document xml = db.newDocument();
        Node root = xml.createElement(rootName);
        Attr a = xml.createAttribute("version");
        a.setValue("1.0");
        root.getAttributes().setNamedItem(a);
        xml.appendChild(root);
        return xml;

    }
}
