package com.example.rxretifoit.util;

import android.annotation.SuppressLint;
import android.content.Context;

public class ApplicationContext {
    @SuppressLint("StaticFieldLeak")
    private static Context context=null;

    public static Context getContext() {
        if (context==null){
            try {
                throw new Exception("context,Null");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return context;
    }

    public static void setContext(Context context) {
        ApplicationContext.context = context;
    }
}
