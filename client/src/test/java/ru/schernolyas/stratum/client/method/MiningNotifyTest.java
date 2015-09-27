/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.schernolyas.stratum.client.method;

import org.apache.commons.codec.DecoderException;
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
public class MiningNotifyTest {

    public MiningNotifyTest() {
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
     * Test of build method, of class MiningNotify.
     */
    @Test
    public void testBuild() throws DecoderException {
        System.out.println("build");
        String jsonString = "{\"id\":null,\"method\":\"mining.notify\",\"params\":[\"213\",\"fa45d4f7f7a799e909390acd5be3f41887f6a6700d5ee26f0001442200000000\","
                + "\"01000000010000000000000000000000000000000000000000000000000000000000000000ffffffff2003773d0504b617085608\","
                + "\"0d2f6e6f64655374726174756d2f000000000209ee740c000000001976a914edc3ed0229526ea4c728d714b5289f97e7f63a2188acf9ed740c000000001976a914769b170c50d189b7fe215c7e168a144190aafb3e88ac00000000\",["
                + "\"7623cd82742164001d9af0c926ccd8a83e518f138043c9ae1d7212d87a265e02\","
                + "\"7f643fe39466e0e3477cfcfc9d8828572d8102739fc9bbce09d57a47a3154162\","
                + "\"572a49864bd68fbe1397ead9b28809edb8590e04168525926dd8e43f72b5e4f2\"],\"00000003\",\"1b152c0b\",\"560817b6\",false]}";

        MiningNotify result = MiningNotify.build(jsonString);
        System.out.println("JobId:"+Hex.encodeHexString(result.getJobId()));

        //assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

}
