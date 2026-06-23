package com.firstbank.model;
import java.time.Year;
import java.util.HashMap;
public class AccountNumberGenerator {
    private static HashMap<String,Integer> counters=new HashMap<>();

    public static String generate(String branch) {
        String code = switch (branch) {
            case "Kampala" -> "KLA";
            case "Gulu" -> "GUL";
            case "Mbarara" -> "MBR";
            case "Jinja" -> "JIN";
            default -> "MBL";
        };
        String key = code + Year.now().getValue();
        int n = counters.getOrDefault(key, 0) + 1;
        counters.put(key, n);
        return
                String.format("%s-%d-%06", code, Year.now().getValue(), n);
    }
}
