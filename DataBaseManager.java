package com.firstbank.model;

import java.io.*;

public class DataBaseManager {
    public static void save(String record) {
        try(PrintWriter pw = new PrintWriter(new FileWriter("accounts.csv", true))) {
            pw.println(record);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
}
