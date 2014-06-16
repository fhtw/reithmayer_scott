/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Swe2Plugins;

import Abgabe.UrlClass;
import java.net.Socket;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.w3c.dom.Document;

/**
 *
 * @author Angelikuh
 */
public class MicroERPServiceTest {
    
    public MicroERPServiceTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getData method, of class MicroERPService.
     */
    @Test
    public void testGetData() {
        System.out.println("getData");
        Socket socket = null;
        UrlClass url = null;
        MicroERPService instance = new MicroERPService();
        instance.getData(socket, url);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of newDocument method, of class MicroERPService.
     */
    @Test
    public void testNewDocument() throws Exception {
        System.out.println("newDocument");
        String rootName = "";
        Document expResult = null;
        Document result = MicroERPService.newDocument(rootName);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
