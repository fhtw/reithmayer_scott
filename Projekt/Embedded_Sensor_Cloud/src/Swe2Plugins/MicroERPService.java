/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Swe2Plugins;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import Abgabe.Response;
import Abgabe.UrlClass;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.DriverManager;
import javax.swing.JOptionPane;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 *
 * @author Angelikuh
 */
public class MicroERPService {

    public void getData(Socket socket, UrlClass url) {
        ArrayList<String> parameters = url.getParameters();
//        System.out.println(parameters);
        String file = "erp.xml";

        try {
            //Datenbank Treiber laden
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "MS JDBC Driver not installed\n" + ex);
        }

        try (Connection db = DriverManager.getConnection("jdbc:sqlserver://localhost\\sqlexpress;" + "databaseName=SWE1", "swe1", "test")) {
            PreparedStatement cmd;

            cmd = db.prepareStatement("SELECT * FROM [contacts]");

//            System.out.println("prams size: " + parameters.size());
            if (!parameters.isEmpty()) { //update contact
                cmd = this.prepareSqlStatement(parameters, cmd, db);
                if (parameters.get(0).equalsIgnoreCase("edit") || parameters.get(0).equalsIgnoreCase("new")) {
                    try {
                        int rd = cmd.executeUpdate();
                        System.out.println("rd: " + rd);
                        //direkt antworten das gespeichert wurde
                        PrintWriter socketOut = new PrintWriter(socket.getOutputStream());
                        socketOut.println("HTTP/1.1 200 OK");
                        socketOut.println("Content-Type: text/xml");
                        socketOut.println("connection: close");
                        socketOut.println("");
                        socketOut.write("gespeichert!");
                        socketOut.flush();
                    } catch (SQLException | IOException ex) {
                        Logger.getLogger(MicroERPService.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    return;
                }
            }

            new Response(socket, file).sendResponse();

        } catch (ParserConfigurationException ex) {
            System.out.println("ParserConfigurationExeption");
            Logger.getLogger(MicroERPService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerConfigurationException ex) {
            System.out.println("TransformerConfigurationExeption");
            Logger.getLogger(MicroERPService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerException ex) {
            System.out.println("TransformerExeption");
            Logger.getLogger(MicroERPService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (com.microsoft.sqlserver.jdbc.SQLServerException ex) {
            System.out.println("SqlServerExeption");
            Logger.getLogger(MicroERPService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(MicroERPService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private PreparedStatement prepareSqlStatement(ArrayList<String> parameters, PreparedStatement cmd, Connection db) throws SQLException, ParserConfigurationException, TransformerException {
        StringBuilder sb = new StringBuilder();

        if (parameters.get(0).equalsIgnoreCase("search")) {
            if (parameters.get(1).equalsIgnoreCase("contact")) {
                if (parameters.size() > 3) {
                    cmd = db.prepareStatement("SELECT * FROM [contacts] WHERE [name] LIKE ? OR [compName] LIKE ?");
                    sb.append("%");
                    sb.append(parameters.get(3));
                    sb.append("%");
                    cmd.setString(1, sb.toString());
                    cmd.setString(2, sb.toString());
                } else {
                    cmd = db.prepareStatement("SELECT * FROM [contacts]");
                }
                buildContactXml(cmd);
            } else if (parameters.get(1).equalsIgnoreCase("company")) {
                if (parameters.size() > 3) {
                    if (parameters.get(2).equalsIgnoreCase("compId")) {
                        cmd = db.prepareStatement("SELECT * FROM [contacts] WHERE [id] = ?");
                        sb.append(parameters.get(3));
                        cmd.setString(1, sb.toString());
                    } else {
                        cmd = db.prepareStatement("SELECT * FROM [contacts] WHERE [compName] IS NOT NULL AND [compName] LIKE ?");
                        sb.append("%");
                        sb.append(parameters.get(3));
                        sb.append("%");
                        cmd.setString(1, sb.toString());
                    }
                } else {
                    cmd = db.prepareStatement("SELECT * FROM [contacts] WHERE [compName] IS NOT NULL");
                }
                buildContactXml(cmd);
            } else if (parameters.get(1).equalsIgnoreCase("invoice")) { //TODO unterschied suche nach contactname oder datum
                if (parameters.size() > 3) {
                    if (parameters.get(2).equalsIgnoreCase("searchKey")) {
                        cmd = db.prepareStatement("SELECT * FROM [invoice] WHERE [contactId] = ANY ( SELECT [id] FROM [contacts] WHERE [name] LIKE ? OR [compName] LIKE ? )");
                        sb.append("%");
                        sb.append(parameters.get(3));
                        sb.append("%");
                        cmd.setString(1, sb.toString());
                        cmd.setString(2, sb.toString());
                    } else if (parameters.get(2).equalsIgnoreCase("dateFrom") && parameters.size() == 6) { //invoices werden nach Datum durchsucht
                        if (parameters.get(3).equalsIgnoreCase("all") && parameters.get(5).equalsIgnoreCase("all")) {    //keine Werte für dateFrom && dateTo -> alle Rechnungen
                            cmd = db.prepareStatement("SELECT * FROM [invoice]");
                        } else if (!parameters.get(3).equalsIgnoreCase("all") && !parameters.get(5).equalsIgnoreCase("all")) {   //dateFrom && dateTo haben Werte
                            cmd = db.prepareStatement("SELECT * FROM [invoice] WHERE [date] >= ? AND [date] <= ?");
                            cmd.setString(1, parameters.get(3));    //Wert von dateFrom
                            cmd.setString(2, parameters.get(5));    //Wert von dateTo                  
                        } else {    //entweder dateFrom oder dateTo hat einen Wert
                            if (parameters.get(3).equalsIgnoreCase("all")) { //nur dateTo hat einen Wert
                                cmd = db.prepareStatement("SELECT * FROM [invoice] WHERE [date] <= ?");
                                cmd.setString(1, parameters.get(5));    //Wert von dateTo                  
                            } else {    //nur dateFrom hat einen Wert
                                cmd = db.prepareStatement("SELECT * FROM [invoice] WHERE [date] >= ?");
                                cmd.setString(1, parameters.get(3));    //Wert von dateFrom                                
                            }

                        }
                    }
                } else {
                    cmd = db.prepareStatement("SELECT * FROM [invoice]");
                }
                buildInvoiceXml(cmd);
            } else if (parameters.get(1).equalsIgnoreCase("invoiceItem")) {
                if (parameters.size() > 3) {
                    cmd = db.prepareStatement("SELECT * FROM [invoiceItems] WHERE [invoiceId] = ?");
                    cmd.setString(1, parameters.get(3));
                } else {
                    cmd = db.prepareStatement("SELECT * FROM [invoiceItems]");
                }
                buildInvoiceItemXml(cmd);
            }
        } else if (parameters.get(0).equalsIgnoreCase("edit")) {
            if (parameters.get(1).equalsIgnoreCase("company")) {
                cmd = db.prepareStatement("UPDATE [contacts] SET [uid] = ?, [compName] = ?, [compId] = -1, [address] = ?, [plz] = ?, [city] = ? WHERE [id] = ?");
                cmd.setString(1, parameters.get(5));
                cmd.setString(2, parameters.get(7));
                cmd.setString(3, parameters.get(13));
                cmd.setString(4, parameters.get(15));
                cmd.setString(5, parameters.get(17));
                cmd.setString(6, parameters.get(3));
            } else if (parameters.get(1).equalsIgnoreCase("person")) {
                cmd = db.prepareStatement("UPDATE [contacts] SET [firstName] = ?, [name] = ?, [compId] = ?, [address] = ?, [plz] = ?, [city] = ? WHERE [id] = ?");
                cmd.setString(1, parameters.get(7));
                cmd.setString(2, parameters.get(9));
                cmd.setString(3, parameters.get(11));
                cmd.setString(4, parameters.get(13));
                cmd.setString(5, parameters.get(15));
                cmd.setString(6, parameters.get(17));
                cmd.setString(7, parameters.get(3));
            }

        } else if (parameters.get(0).equalsIgnoreCase("new")) {
            if (parameters.get(1).equalsIgnoreCase("company")) {
                cmd = db.prepareStatement("INSERT INTO [contacts] ([uid], [compName], [address], [plz], [city]) VALUES (?, ?, ?, ?, ?)");
                cmd.setString(1, parameters.get(5));
                cmd.setString(2, parameters.get(7));
                cmd.setString(3, parameters.get(13));
                cmd.setString(4, parameters.get(15));
                cmd.setString(5, parameters.get(17));
            } else if (parameters.get(1).equalsIgnoreCase("person")) {
                cmd = db.prepareStatement("INSERT INTO [contacts] ([firstName], [name], [compId], [address], [plz], [city]) VALUES (?, ?, ?, ?, ?, ?)");
                cmd.setString(1, parameters.get(7));
                cmd.setString(2, parameters.get(9));
                cmd.setString(3, parameters.get(11));
                cmd.setString(4, parameters.get(13));
                cmd.setString(5, parameters.get(15));
                cmd.setString(6, parameters.get(17));
            }
        }
        return cmd;
    }

    public static Document newDocument(String rootName) throws ParserConfigurationException {

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

    private void buildContactXml(PreparedStatement cmd) throws ParserConfigurationException, TransformerConfigurationException, TransformerException {
        Document xml = newDocument("contacts");
        Element root = xml.getDocumentElement();
        Node child, childUid, childCompName, childFirstName, childName, childCompId, childBirth, childAddr, childPlz, childCity;
        Attr a;
        String stringBuffer;
        try (ResultSet rd = cmd.executeQuery()) {
            while (rd.next()) {
                child = xml.createElement("contact");
                a = xml.createAttribute("id");
                a.setValue(rd.getString(1));
                child.getAttributes().setNamedItem(a);
                if (rd.getString(2) != null) {
                    childUid = xml.createElement("uid");
                    childUid.appendChild(xml.createTextNode(rd.getString(2)));
                    child.appendChild(childUid);
                }
                if (rd.getString(3) != null) {
                    childCompName = xml.createElement("compName");
                    childCompName.appendChild(xml.createTextNode(rd.getString(3)));
                    child.appendChild(childCompName);
                }
                childFirstName = xml.createElement("firstName");
                childFirstName.appendChild(xml.createTextNode(rd.getString(4) == null ? "" : rd.getString(4)));
                child.appendChild(childFirstName);
                if (rd.getString(5) != null) {
                    childName = xml.createElement("name");
                    childName.appendChild(xml.createTextNode(rd.getString(5)));
                    child.appendChild(childName);
                }
                if (rd.getString(6) != null) {
                    childCompId = xml.createElement("compId");
                    childCompId.appendChild(xml.createTextNode(rd.getString(6)));
                    child.appendChild(childCompId);
                }
                if (rd.getString(7) != null) {
                    childBirth = xml.createElement("birth");
                    childBirth.appendChild(xml.createTextNode((rd.getString(7))));
                    child.appendChild(childBirth);
                }
                if (rd.getString(8) != null) {
                    childAddr = xml.createElement("address");
                    childAddr.appendChild(xml.createTextNode(rd.getString(8)));
                    child.appendChild(childAddr);
                }
                if (rd.getString(9) != null) {
                    childPlz = xml.createElement("plz");
                    childPlz.appendChild(xml.createTextNode(rd.getString(9)));
                    child.appendChild(childPlz);
                }
                if (rd.getString(10) != null) {
                    childCity = xml.createElement("city");
                    childCity.appendChild(xml.createTextNode(rd.getString(10)));
                    child.appendChild(childCity);
                }
                root.appendChild(child);
            }

        } catch (SQLException ex) {
            Logger.getLogger(MicroERPService.class.getName()).log(Level.SEVERE, null, ex);
        }
        // write the content into xml file
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        DOMSource source = new DOMSource(xml);
        StreamResult result = new StreamResult(new File("files/erp.xml")); //ev. temporäres file verwenden
        transformer.transform(source, result);
    }

    private void buildInvoiceXml(PreparedStatement cmd) throws ParserConfigurationException, TransformerConfigurationException, TransformerException {
        Document xml = newDocument("invoices");
        Element root = xml.getDocumentElement();
        Node child, childDate, childContactId, childDueDate, childComment, childMessage;
        Attr attrId;

        try (ResultSet rd = cmd.executeQuery()) {
            while (rd.next()) {
                child = xml.createElement("invoice");
                attrId = xml.createAttribute("id");
                attrId.setValue(rd.getString(1));
                child.getAttributes().setNamedItem(attrId);
                childDate = xml.createElement("date");
                childDate.appendChild(xml.createTextNode(rd.getString(2)));
                child.appendChild(childDate);
                childContactId = xml.createElement("contactId");
                childContactId.appendChild(xml.createTextNode(rd.getString(3)));
                child.appendChild(childContactId);
                childDueDate = xml.createElement("dueDate");
                childDueDate.appendChild(xml.createTextNode(rd.getString(4)));
                child.appendChild(childDueDate);
                if (rd.getString(5) != null) {
                    childComment = xml.createElement("comment");
                    childComment.appendChild(xml.createTextNode(rd.getString(5)));
                    child.appendChild(childComment);
                }
                if (rd.getString(6) != null) {
                    childMessage = xml.createElement("message");
                    childMessage.appendChild(xml.createTextNode((rd.getString(6))));
                    child.appendChild(childMessage);
                }
                root.appendChild(child);
            }

        } catch (SQLException ex) {
            Logger.getLogger(MicroERPService.class.getName()).log(Level.SEVERE, null, ex);
        }
        // write the content into xml file
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        DOMSource source = new DOMSource(xml);
        StreamResult result = new StreamResult(new File("files/erp.xml")); //ev. temporäres file verwenden
        transformer.transform(source, result);
    }

    private void buildInvoiceItemXml(PreparedStatement cmd) throws ParserConfigurationException, TransformerConfigurationException, TransformerException {
        Document xml = newDocument("invoiceItems");
        Element root = xml.getDocumentElement();
        Node child, childDescription, childAmount, childPrice, childUst, childInvoiceId;
        Attr attrId;

        try (ResultSet rd = cmd.executeQuery()) {
            while (rd.next()) {
                child = xml.createElement("item");
                attrId = xml.createAttribute("id");
                attrId.setValue(rd.getString(1));
                child.getAttributes().setNamedItem(attrId);
                childDescription = xml.createElement("description");
                childDescription.appendChild(xml.createTextNode(rd.getString(2)));
                child.appendChild(childDescription);
                childAmount = xml.createElement("amount");
                childAmount.appendChild(xml.createTextNode(rd.getString(3)));
                child.appendChild(childAmount);
                childPrice = xml.createElement("price");
                childPrice.appendChild(xml.createTextNode(rd.getString(4)));
                child.appendChild(childPrice);
                childUst = xml.createElement("ust");
                childUst.appendChild(xml.createTextNode(rd.getString(5)));
                child.appendChild(childUst);
                childInvoiceId = xml.createElement("invoiceId");
                childInvoiceId.appendChild(xml.createTextNode(rd.getString(6)));
                child.appendChild(childInvoiceId);
                root.appendChild(child);
            }

        } catch (SQLException ex) {
            Logger.getLogger(MicroERPService.class.getName()).log(Level.SEVERE, null, ex);
        }
        // write the content into xml file
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        DOMSource source = new DOMSource(xml);
        StreamResult result = new StreamResult(new File("files/erp.xml")); //ev. temporäres file verwenden
        transformer.transform(source, result);
    }

}
