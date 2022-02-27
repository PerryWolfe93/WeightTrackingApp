package com.fitness_app;

import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConSQL {

    public static Connection connectionClass(String username, String password) {
        Connection con = null;
        String ip = "10.0.0.224", port = "1433", databasename = "fitnessAppDB";
        StrictMode.ThreadPolicy tp = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(tp);
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            String  connectionUrl = "jdbc:jtds:sqlserver://" + ip + ":" + port + ";databaseName=" + databasename + ";User=" + username + ";password=" + password + ";";
            con = DriverManager.getConnection(connectionUrl);
        }
        catch (Exception exception) {
            Log.e("Error", exception.getMessage());
        }

        return con;
    }
}
