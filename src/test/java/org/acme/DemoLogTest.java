package org.acme;

import org.junit.Rule;
import org.junit.contrib.java.lang.system.SystemOutRule;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.util.HashMap;
import java.util.Map;





class DemoLogTest {

    @Rule
    public final SystemOutRule log = new SystemOutRule().enableLog();




    @Test
    void testCreateFile() {
        Map<String, String> connection = new HashMap<>();
        connection.put("logFileFolder", "c://Apps/");

        File tmpDir = new File("c://Apps/logFile.txt");
        if(tmpDir.exists()) {
            tmpDir.delete();
        }
        new DemoLog(true,false, false,
            true, true, true, connection);
        DemoLog.logMessage("Prueba", true, true, false);

        boolean exists = tmpDir.exists();
        Assertions.assertTrue( exists);

    }



}
