/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package milst2;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 *
 * @author Kuh
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({milst2.MainTest.class, milst2.StaticPluginTest.class, milst2.ResponseTest.class, milst2.ServerTest.class, milst2.UrlClassTest.class, milst2.RequestTest.class})
public class Milst2Suite {

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }
    
}
