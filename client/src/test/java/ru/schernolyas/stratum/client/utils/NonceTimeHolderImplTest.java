/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.schernolyas.stratum.client.utils;

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
public class NonceTimeHolderImplTest {

    public NonceTimeHolderImplTest() {
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
     * Test of getNonce method, of class NonceTimeHolderImpl.
     */
    @Test
    public void testGetNonce() {
        System.out.println("getNonce");
        NonceTimeHolderImpl instance = new NonceTimeHolderImpl();
        byte[] expResult1 = new byte[]{0, 0, 0, 0};
        assertArrayEquals(expResult1, instance.getNonce());
        byte[] expResult2 = new byte[]{0, 0, 0, 1};
        assertArrayEquals(expResult2, instance.getNonce());

    }

}
