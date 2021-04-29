package com.sample;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.instrument.Instrumentation;
import java.lang.management.ManagementFactory;
import java.net.HttpURLConnection;
import java.net.URL;

public class Agent {
    public static void premain(String args, Instrumentation inst) throws Exception {
        getHTML("https://www.example.com");
    }

    public static void main(String[] args) throws Exception {
        getHTML("https://www.example.com");
    }

    public static void getHTML(String urlToRead) throws Exception {
        System.out.println("start");
        StringBuilder result = new StringBuilder();
        URL url = new URL(urlToRead);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(conn.getInputStream()))) {
            for (String line; (line = reader.readLine()) != null; ) {
                result.append(line);
            }
        }
        System.out.println("HTTP response code: " + conn.getResponseCode() + " response len=" + result.length());
        long jvmUpTime = ManagementFactory.getRuntimeMXBean().getUptime();
        System.out.println("end");
        System.out.println("Total execution time MS=" + jvmUpTime);
    }
}