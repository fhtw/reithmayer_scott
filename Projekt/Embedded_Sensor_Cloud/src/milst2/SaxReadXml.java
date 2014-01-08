
package milst2;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
 
public class SaxReadXml {
 
   public static void main(String argv[]) {
 
    try {
 
	SAXParserFactory factory = SAXParserFactory.newInstance();
	SAXParser saxParser = factory.newSAXParser();
 
	DefaultHandler handler = new DefaultHandler() {
 
	boolean bfname = false;
	boolean blname = false;
	boolean bnname = false;
	boolean bsalary = false;
 
        @Override
	public void startElement(String uri, String localName,String qName, Attributes attributes) throws SAXException
        {
 
		//System.out.println("Start Element :" + qName);
/* 
		if (qName.equalsIgnoreCase("way")) {
			bfname = true;
                        System.out.println("Start Element :" + qName);
		}
                if (qName.equalsIgnoreCase("tag") ) {
                        blname = true;
                        System.out.println("Start Element :" + qName);
*/                    for ( int i = 0; i < attributes.getLength(); i++ )
                    {
                        if(attributes.getValue(i).equalsIgnoreCase("place"))
                        {
                            System.out.printf( "Attribut no. %d: %s = %s%n", i, attributes.getQName( i ), attributes.getValue( i ) );
                            System.out.printf( "Attribut no. %d: %s = %s%n", i+1, attributes.getQName( i+1 ), attributes.getValue( i+1 ) );
                        }
                    }
//                    bfname = false;
//                }
 
/*
                if (attributes.getType(qName).equalsIgnoreCase("k")) {
			bnname = true;
		}
 
		if (attributes.getValue(qName).equalsIgnoreCase("highway")) {
			bsalary = true;
		}
*/ 
	}
 
        @Override
	public void endElement(String uri, String localName,
		String qName) throws SAXException {
 
		//System.out.println("End Element :" + qName);
                if (qName.equalsIgnoreCase("way")) {
			//bfname = false;
                        //System.out.println("End Element :" + qName);
		}
                if (qName.equalsIgnoreCase("tag")) {
			//bfname = false;
                        //System.out.println("End Element :" + qName);
		}
	}
 
        @Override
	public void characters(char ch[], int start, int length) throws SAXException {
 
		if (bfname) {
			System.out.println("First Name : " + new String(ch, start, length));
			bfname = false;
                    if (blname) {
                            System.out.println("Last Name : " + new String(ch, start, length));
                            blname = false;
                        if (bnname) {
                                System.out.println("Nick Name : " + new String(ch, start, length));
                                bnname = false;
                        }
                    }
		}
 
 
 
		if (bsalary) {
			System.out.println("Salary : " + new String(ch, start, length));
			bsalary = false;
		}
 
	}
 
     };
 
       saxParser.parse("files/nav/isle-of-man-latest.osm", handler);
 

 
        }catch (SAXException | IOException | ParserConfigurationException ex) {
                Logger.getLogger(SaxReadXml.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
 
}