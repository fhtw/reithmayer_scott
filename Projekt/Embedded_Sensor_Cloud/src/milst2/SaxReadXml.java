
package milst2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
 
public class SaxReadXml {
         private static HashMap<String, ArrayList> tags;

   public static void main(String argv[]) {
 
    try {
 
	SAXParserFactory factory = SAXParserFactory.newInstance();
	SAXParser saxParser = factory.newSAXParser();
 
	DefaultHandler handler = new DefaultHandler() {
 
	boolean bfname = false;
	boolean blname = false;
	boolean bnname = false;
	boolean bsalary = false;
        String key = null;
        String val = null;
        ArrayList values;
        
        @Override
        public void startDocument() throws SAXException {
    tags = new HashMap<>();
}

        @Override
        public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException 
        {


    		if (qName.equalsIgnoreCase("node")) {
			bfname = true;
                        //System.out.println("Start Element :" + qName);
                }
    		if (qName.equalsIgnoreCase("tag")) {
			blname = true;
                        //System.out.println("Start Element :" + qName);
                }
    
    
    
    if(bfname && blname)
    {
                    for ( int i = 0; i < atts.getLength(); i++ )
                    {
                        if(atts.getValue(i).equalsIgnoreCase("addr:city"))
                        {
                            //System.out.printf( "Attribut no. %d: %s = %s%n", i, atts.getQName( i ), atts.getValue( i ) );
                            //System.out.printf( "city: %s = %s%n", atts.getQName( i+1 ), atts.getValue( i+1 ) );
                            val = atts.getValue( i+1 );
                        }
                        if(atts.getValue(i).equalsIgnoreCase("addr:street"))
                        {
                            //System.out.printf( "Attribut no. %d: %s = %s%n", i, atts.getQName( i ), atts.getValue( i ) );
                            //System.out.printf( "street: %s = %s%n", atts.getQName( i+1 ), atts.getValue( i+1 ) );
                            key = atts.getValue( i+1 );
                        }
                    }
            
        if(key != null && val != null)
        {
            if((values = tags.get(key)) == null)
                values = new ArrayList();
            if(values == null || !values.contains(val))
            {
                values.add(val);
                tags.put(key, values);
            }
        }
        
    }
}
         @Override
	public void endElement(String uri, String localName,
		String qName) throws SAXException {
 
		//System.out.println("End Element :" + qName);
                if (qName.equalsIgnoreCase("node")) {
			bfname = false;
                        //System.out.println("End Element :" + qName);
		}
                if (qName.equalsIgnoreCase("tag")) {
			blname = false;
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
 
       saxParser.parse("files/nav/austria-latest.osm", handler);
    System.out.println(tags);
    
    for (ArrayList e : tags.values()) {

            System.out.println(e);
        } 
 
 
        }catch (SAXException | IOException | ParserConfigurationException ex) {
                Logger.getLogger(SaxReadXml.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
 
}