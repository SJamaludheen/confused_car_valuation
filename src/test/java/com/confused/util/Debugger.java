package com.confused.util;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.*;

public class Debugger {
    private static Logger logger;
    private static FileHandler handler = null;

    static {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        int limit = 200000;//200 KB
        int numLogFiles = 5;
        try {
            File logDir = new File("./logs/");
            if (!(logDir.exists())) {
                logDir.mkdir();
            }
            handler = new FileHandler("logs/APPLog_" + sdf.format(new Date()) + ".txt", limit, numLogFiles, true);
            logger = Logger.getLogger("APPLog_");
            handler.setFormatter(new Formatter() {
                public String format(LogRecord rec) {
                    return formatMessage(rec) + '\n';
                }

            });
            logger.addHandler(handler);

        } catch (Exception E) {
            System.out.println("failed to create log file");
        }
    }

    public static void println(String msg) {
        logger.log(Level.INFO, msg);
    }
}