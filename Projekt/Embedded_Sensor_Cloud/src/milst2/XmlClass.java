/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package milst2;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.*;
import org.w3c.dom.*;

/**
 *
 * @author Angelika Reithmayer
 * 
 * Wird nicht verwendendet, war nur zum ausprobieren...
 * 
 * 
 */
public class XmlClass {
    
            
    public static void main(String[] args) throws ParserConfigurationException
    {
        try {
            
            Document xml = newDocument("temperatures");
            Element root = xml.getDocumentElement();
            Node child = xml.createElement("temperature");
            Attr a = xml.createAttribute("date");
            a.setValue("2006-06-12");
            child.getAttributes().setNamedItem(a);
            child.appendChild(xml.createTextNode("26.394875"));
            root.appendChild(child);
            
            child = xml.createElement("temperature");
            a = xml.createAttribute("date");
            a.setValue("2010-10-22");
            child.getAttributes().setNamedItem(a);
            child.appendChild(xml.createTextNode("6.394875"));
            root.appendChild(child);
            
            
            
            // write the content into xml file
		Transformer transformer = TransformerFactory.newInstance().newTransformer();
		DOMSource source = new DOMSource(xml);
		StreamResult result = new StreamResult(new File("files/test.xml"));
      		transformer.transform(source, result);

      /*      
                String file = "test.xml";
                File f = new File("files/" + file);
                FileWriter writer = new FileWriter(f);
                BufferedWriter out = new BufferedWriter(writer);
                out.write(xml.toString());
                out.flush();
      */      
            
            System.out.println(xml);
            System.out.println(root);
            System.out.println(child);
            
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = builder.parse(new File("files/test.xml"));

            XPath xpath = XPathFactory.newInstance().newXPath();
            String expression = "/widgets/widget";
            Node widgetNode = (Node) xpath.evaluate(expression, document, XPathConstants.NODE);
        } catch (XPathExpressionException | SAXException | IOException | ParserConfigurationException ex) {
            Logger.getLogger(XmlClass.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerConfigurationException ex) {
            Logger.getLogger(XmlClass.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerException ex) {
            Logger.getLogger(XmlClass.class.getName()).log(Level.SEVERE, null, ex);
        }

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
