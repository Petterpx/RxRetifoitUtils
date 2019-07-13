package com.example.rxretifoit.net;

public class RestUrlInfo {
    private static String URL=null;

    public static String getURL() {
        if (URL==null){
            new RuntimeException("__________URL==null");
        }
        return URL;
    }

    public static void setURL(String URL) {
        RestUrlInfo.URL = URL;
    }
}
