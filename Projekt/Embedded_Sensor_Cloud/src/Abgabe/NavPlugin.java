/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Abgabe;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author Angelika Reithmayer & Samantha Scott
 */
public class NavPlugin extends PluginClass{
    
    private final String file = "nav.html";
    private HashMap<String, ArrayList<String>> tags;

    @Override
    public void start(Socket socket, UrlClass url)
    {
        ArrayList<String> urlParts = url.getTokens();
        int count = urlParts.size();

            if (count < 3) //Anzahl der Teile der Url
            {
                this.search();
            }else if(urlParts.get(1).equalsIgnoreCase("s"))
            {
               this.result(url);
            }else
            {
                this.load();
                this.search();
            }
            
            new Response(socket, file).sendResponse();
    }

    private void search()
    {
            File f = new File("files/" + file);//ev. temporäres file verwenden
        try (FileWriter writer = new FileWriter(f)) {
            BufferedWriter out = new BufferedWriter(writer);
            out.write("<!DOCTYPE html>\n<html>\n<head>\n<title>SWE1 - Navi</title>\n");
            out.write("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n</head>\n<body>\n<H1>Navi:</H1>\n");
            //reload map button
            out.write("<form method=\"GET\" action=\"Navi\" >\n");
            out.write("<input type=submit value=\"reload map\" name=f />\n");
            out.write("</form>\n");
            //search form
            out.write("<form method=\"GET\" action=\"Navi\" >\n");
            out.write("<p>Straße: </p>\n");
            out.write("<input name=s />\n");
            out.write("<input type=submit value=Suchen />\n");
            out.write("</form>\n</body>\n</html>");
            
            out.flush();
        } catch (IOException ex) {
            Logger.getLogger(NavPlugin.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    
    private void result(UrlClass url)
    {
        String street = url.getTokens().get(2).toString();
        StringTokenizer st = new StringTokenizer(street, "+");
        StringBuilder sb = new StringBuilder();
        sb.append(st.nextToken());
        while(st.hasMoreTokens())
        {
            sb.append(" ");
            sb.append(st.nextToken());
        }
        street = sb.toString();
        
        File f = new File("files/" + file);//ev. temporäres file verwenden
        try (FileWriter writer = new FileWriter(f)) 
        {
            BufferedWriter out = new BufferedWriter(writer);
            out.write("<!DOCTYPE html>\n<html>\n<head>\n<title>SWE1 - Navi</title>\n");
            out.write("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n</head>\n<body>\n<H1>Navi:</H1>\n");
            //reload map button
            out.write("<form method=\"GET\" action=\"Navi\" >\n");
            out.write("<input type=submit value=\"reload map\" name=f />\n");
            out.write("</form>\n");
            //search form
            out.write("<form method=\"GET\" action=\"Navi\" >\n");
            out.write("<p>Straße: </p>\n");
            out.write("<input name=s />\n");
            out.write("<input type=submit value=Suchen />\n</form>\n");
            //Results
            out.write("<br/><br/><h3><b> ");
            out.write(street);
            out.write(":</b></h3>\n");
            if(!tags.containsKey(street))
                out.write("<p>Kein Treffer!</p>\n");
            else
            {
                ArrayList cities = tags.get(street);
                for(int i = 0; i < cities.size(); i++)
                {
                    out.write("<p>");
                    out.write(cities.get(i).toString());
                    out.write("</p>\n");
                }    
            }
            
            out.write("</body>\n</html>");
            out.flush();
        } catch (IOException ex) {
            Logger.getLogger(NavPlugin.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public void load()
    {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            
            DefaultHandler handler = new DefaultHandler() 
            {

                boolean node = false;
                boolean tag = false;
                String key = null;
                String val = null;
                ArrayList<String> values;

                @Override
                public void startDocument() throws SAXException {
                    tags = new HashMap<>();
                }

                @Override
                public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException 
                {
                    if (qName.equalsIgnoreCase("node")) {
                        node = true;
                        //System.out.println("Start Element :" + qName);
                    }
                    if (qName.equalsIgnoreCase("tag")) {
                        tag = true;
                        //System.out.println("Start Element :" + qName);
                    }

                    if(node && tag)
                    {
                        for ( int i = 0; i < atts.getLength(); i++ )
                        {
                            if(atts.getValue(i).equalsIgnoreCase("addr:city"))
                            {
                                val = atts.getValue( i+1 );
                            }
                            if(atts.getValue(i).equalsIgnoreCase("addr:street"))
                            {
                                key = atts.getValue( i+1 );
                            }
                        }

                        if(key != null && val != null)
                        {
                            if((values = tags.get(key)) == null)
                                values = new ArrayList<>();
                            if(values == null || !values.contains(val))
                            {
                                values.add(val);
                                tags.put(key, values);
                            }
                        }

                    }
                }
                @Override
                public void endElement(String uri, String localName, String qName) throws SAXException
                {
                    //System.out.println("End Element :" + qName);
                    if (qName.equalsIgnoreCase("node")) {
                        node = false;
                        //System.out.println("End Element :" + qName);
                    }
                    if (qName.equalsIgnoreCase("tag")) {
                        tag = false;
                        //System.out.println("End Element :" + qName);
                    }
                    if(key != null && val != null)
                    {
                        key=null;
                        val=null;
                    }

                }

                @Override
                public void endDocument() throws SAXException {
                    // ...
                }
            };
            
            saxParser.parse("files/nav/isle-of-man-latest.osm", handler);
            
            System.out.println("NavPlugin geladen");
        } catch (ParserConfigurationException | SAXException | IOException ex) {
            Logger.getLogger(NavPlugin.class.getName()).log(Level.SEVERE, null, ex);
        }


    }
}

