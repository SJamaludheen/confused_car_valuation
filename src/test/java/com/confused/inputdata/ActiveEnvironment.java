package com.confused.inputdata;

public class ActiveEnvironment {
    private static String currentEnvironment;
    private static String currentBrowser;
    public static boolean isSnapshotRequired;

    public static void setCurrentEnvironment(String currentEnvironment) {
        ActiveEnvironment.currentEnvironment = currentEnvironment;
        String snapshot = System.getProperty("snapshot");
        isSnapshotRequired = snapshot != null && !snapshot.equalsIgnoreCase("No");
    }

    public static String getCurrentEnvironment() {
        return currentEnvironment;
    }

    public static void setCurrentBrowser(String currentBrowser) {
        ActiveEnvironment.currentBrowser = currentBrowser;
    }

    public static String getCurrentBrowser() {
        return currentBrowser;
    }
}
