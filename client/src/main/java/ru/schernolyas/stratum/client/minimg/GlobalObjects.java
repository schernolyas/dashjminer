/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.schernolyas.stratum.client.minimg;

import java.util.LinkedList;
import java.util.TreeMap;
import java.util.logging.Logger;

/**
 *
 * @author schernolyas
 */
public class GlobalObjects {

    private static final Logger LOG = Logger.getLogger(GlobalObjects.class.getName());
    private static boolean testMode = false;
    private static String initialJsonString;
    private static String setDifficultyJsonString;
    public final static TreeMap<String, String> lastMiningNotifyJsonStringJobIdMap = new TreeMap<>();
    
    public static void addNewMiningNotify(String jobId, String jsonString) {
        synchronized (lastMiningNotifyJsonStringJobIdMap) {
            lastMiningNotifyJsonStringJobIdMap.put(jobId, jsonString);
            if (lastMiningNotifyJsonStringJobIdMap.size() > 10) {
                String firstKey = lastMiningNotifyJsonStringJobIdMap.firstKey();
                lastMiningNotifyJsonStringJobIdMap.remove(firstKey);
            }
        }
    }

    public static String getLastMiningNotifyJsonString() {
        String result = null;
        synchronized (lastMiningNotifyJsonStringJobIdMap) {
            if (!lastMiningNotifyJsonStringJobIdMap.isEmpty()) {
                String lastJobId = lastMiningNotifyJsonStringJobIdMap.lastKey();
                result = lastMiningNotifyJsonStringJobIdMap.get(lastJobId);
            }
        }
        return result;
    }

    public static String getLastMiningNotifyJsonString(String jobId) {
        String result = null;
        synchronized (lastMiningNotifyJsonStringJobIdMap) {
            result = lastMiningNotifyJsonStringJobIdMap.get(jobId);
            if (result == null) {
                String lastJobId = lastMiningNotifyJsonStringJobIdMap.lastKey();
                result = lastMiningNotifyJsonStringJobIdMap.get(lastJobId);
            }
        }
        return result;
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

    public static boolean isTestMode() {
        return testMode;
    }

    public static void setTestMode(boolean testMode) {
        GlobalObjects.testMode = testMode;
    }

}
