package com.example.rxretifoit.net.download;

import android.os.AsyncTask;


import com.example.rxretifoit.net.RestCreator;
import com.example.rxretifoit.net.callback.IFailure;
import com.example.rxretifoit.net.callback.IRequest;
import com.example.rxretifoit.net.callback.ISuccess;
import com.example.rxretifoit.net.callback.Ierror;

import java.util.WeakHashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DownloadHandler {
    private final String URL;
    private static final WeakHashMap<String, Object> PARAMS = RestCreator.getParams();
    private final IRequest REQUEST;
    private final ISuccess SUCCESS;
    private final IFailure FAILURE;
    private final Ierror ERROR;
    private final String DOWLOAD_DIR;
    private final String EXTENSION;
    private final String NAME;

    public DownloadHandler(String url, IRequest request, ISuccess success,
                           IFailure failure, Ierror ierror, String download_dir,
                           String extension, String name) {
        this.URL = url;
        this.REQUEST = request;
        this.SUCCESS = success;
        this.FAILURE = failure;
        this.ERROR = ierror;
        this.DOWLOAD_DIR = download_dir;
        this.EXTENSION = extension;
        this.NAME = name;
    }
    public final void handleDownload(){
        if (REQUEST!=null){
            REQUEST.onRequestStart();
        }
        RestCreator.getRestService().download(URL,PARAMS).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                final ResponseBody responseBody=response.body();

                final SaveFileTask task=new SaveFileTask(REQUEST,SUCCESS);
                task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,DOWLOAD_DIR,EXTENSION,responseBody,NAME);

                //注意判断,否则下载不全
                if (task.isCancelled()){
                    if (REQUEST!=null){
                        REQUEST.onRequestEnd();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (FAILURE != null) {
                    FAILURE.onFailure();
                }
            }
        });
    }
}
