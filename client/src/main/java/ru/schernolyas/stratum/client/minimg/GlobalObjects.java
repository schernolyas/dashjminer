/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.schernolyas.stratum.client.minimg;

import java.util.LinkedList;
import java.util.logging.Logger;

/**
 *
 * @author schernolyas
 */
public class GlobalObjects {

    private static final Logger LOG = Logger.getLogger(GlobalObjects.class.getName());
    private static String initialJsonString;
    private static String setDifficultyJsonString;
    private static final LinkedList<String> miningNotifyJsonStrings = new LinkedList<>();

    public synchronized static void addNewMiningNotify(String jsonString) {
        miningNotifyJsonStrings.addLast(jsonString);        
    }

    public synchronized static void setInitial(String jsonString) {
       initialJsonString = jsonString;
        
    }
    public synchronized static void setSetDifficulty(String jsonString) {
        setDifficultyJsonString = jsonString;
    }

    public static String getInitialJsonString() {
        return initialJsonString;
    }

    public static String getSetDifficultyJsonString() {
        return setDifficultyJsonString;
    }

    public static LinkedList<String> getMiningNotifyJsonStrings() {
        return miningNotifyJsonStrings;
    }

    
}
