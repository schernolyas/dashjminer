/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.schernolyas.stratum.client.method;

import org.apache.commons.codec.binary.Hex;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Сергей
 */
public class InitialTest {
    
    public InitialTest() {
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
     * Test of build method, of class Initial.
     */
    @Test
    public void testBuild() throws Exception {
        System.out.println("build");
        String jsonString = " {\"error\": null, \"id\": 1, \"result\": [[\"mining.notify\", \"ae6812eb4cd7735a302a8a9dd95cf71f\"], \"f80079c5\", 4]}";
        Initial initial = Initial.build(jsonString);
        assertNotNull("Must be not null", initial);
        assertNotNull("Must be not null", initial.getMiningNotify());
        assertArrayEquals(Hex.decodeHex("ae6812eb4cd7735a302a8a9dd95cf71f".toCharArray()), initial.getMiningNotify());
        jsonString = "{\"id\":1,\"result\":[[[\"mining.set_difficulty\",\"deadbeefcafebabe26d60d0000000000\"],[\"mining.notify\",\"deadbeefcafebabe26d60d0000000000\"]],\"7ff90f57\",4],\"error\":null}";
        initial = Initial.build(jsonString);
        assertNotNull("Must be not null", initial);
        assertNotNull("Must be not null", initial.getMiningNotify());
        assertArrayEquals(Hex.decodeHex("deadbeefcafebabe26d60d0000000000".toCharArray()), initial.getMiningNotify());
        
        
    }
    
}
