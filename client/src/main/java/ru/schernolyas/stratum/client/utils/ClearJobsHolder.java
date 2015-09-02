/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.schernolyas.stratum.client.utils;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 *
 * @author schernolyas
 */
public class ClearJobsHolder {

    private static final AtomicBoolean clearJobs = new AtomicBoolean(false);

    public static void setNewValue(boolean newValue) {
        clearJobs.set(newValue);
    }

    public static boolean needClearJobs() {
        return clearJobs.get();
    }

}
