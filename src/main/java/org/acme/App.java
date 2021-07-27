package org.acme;

import java.util.HashMap;
import java.util.Map;

public class App {


  public static void main(String[] args) {
    Map<String, String> connection = new HashMap<>();
    connection.put("dbms", "mysql");
    connection.put("serverName", "37.59.55.185");
    connection.put("portNumber", "3306");
    connection.put("userName", "VvkhTlhNKp");
    connection.put("password", "jbwTRwD6MW");
    connection.put("logFileFolder", "c://Apps/");
    new DemoLog(false,true, true, true, true, true, connection);

    UseDemoLog test = new UseDemoLog();
    test.testLog();

  }
}

