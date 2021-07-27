package org.acme;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Properties;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DemoLog {
    private static boolean logToFile;
    private static boolean logToConsole;
    private static boolean logMessage;
    private static boolean logWarning;
    private static boolean logError;
    private static boolean logToDatabase;
    private static Map<String, String> dbParams;
    private static Logger logger = Logger.getLogger("MyLog");

    DemoLog(boolean logToFileParam, boolean logToConsoleParam, boolean logToDatabaseParam,
                   boolean logMessageParam, boolean logWarningParam, boolean logErrorParam, Map<String, String> dbParamsMap) {
        logError = logErrorParam;
        logMessage = logMessageParam;
        logWarning = logWarningParam;
        logToDatabase = logToDatabaseParam;
        logToFile = logToFileParam;
        logToConsole = logToConsoleParam;
        dbParams = dbParamsMap;
    }

    public static void logMessage(String messageText, boolean message, boolean warning, boolean error) {
        messageText = messageText.trim();
        if (messageText == null || messageText.length() == 0) {
            return;
        }
        if (!logToConsole && !logToFile && !logToDatabase) {
            throw new IllegalArgumentException("Invalid configuration");
        }
        if ((!logError && !logMessage && !logWarning) || (!message && !warning && !error)) {
            throw new IllegalArgumentException("Error or Warning or Message must be specified");
        }
        int t = 0;
        String l = null;
        if (error && logError) {
            l = l + "error " + DateFormat.getDateInstance(DateFormat.LONG).format(new Date()) + messageText;
            t = 2;
        }
        if (warning && logWarning) {
            l = l + "warning " + DateFormat.getDateInstance(DateFormat.LONG).format(new Date()) + messageText;
            t = 3;
        }
        if (message && logMessage) {
            l = l + "message " + DateFormat.getDateInstance(DateFormat.LONG).format(new Date()) + messageText;
            t = 1;
        }
        logConsole(messageText);
        logFile(messageText);
        logDB(l, t);
    }
    private static void logDB(String messageText, int code) {
        if(!logToDatabase) return;
        Connection connection = null;
        Statement stmt = null;
        Properties connectionProps = new Properties();
        connectionProps.put("user", dbParams.get("userName"));
        connectionProps.put("password", dbParams.get("password"));
        try {


        connection = DriverManager.getConnection("jdbc:" + dbParams.get("dbms") + "://" + dbParams.get("serverName")
            + ":" + dbParams.get("portNumber") + "/VvkhTlhNKp", connectionProps);
        stmt = connection.createStatement();
        stmt.executeUpdate("insert into Log values('" + messageText + "', " + code + ")");
        } catch (SQLException sqlException) {
            logConsole(sqlException.getSQLState() + " - "+sqlException.getMessage());
            logFile(sqlException.getSQLState() + " - "+sqlException.getMessage());
        } finally {
            if (connection != null) try { connection.close(); } catch (SQLException ignore) {
                logConsole(ignore.getSQLState());
                logFile(ignore.getSQLState());
            }
            if (stmt != null) try { stmt.close(); } catch (SQLException ignore) {
                logConsole(ignore.getSQLState());
                logFile(ignore.getSQLState());
            }
        }
    }

    private static void logFile(String messageText)  {
        if(!logToFile) return;
        try {
            File logFile = new File(dbParams.get("logFileFolder") + "/logFile.txt");
            if (logFile.createNewFile()) {
                logConsole(logFile.getName() + "- " + "File created" );
            } else {
                logConsole("File already exists");
            }
            FileHandler fh = new FileHandler(dbParams.get("logFileFolder") + "/logFile.txt");
            logger.addHandler(fh);
            logger.log(Level.INFO, messageText);
        } catch (IOException e) {
            logConsole(e.getMessage());
        }
    }

    private static void logConsole(String messageText) {
        if(!logToConsole) return;
        ConsoleHandler ch = new ConsoleHandler();
        logger.addHandler(ch);
        logger.log(Level.INFO, messageText);
    }

}
