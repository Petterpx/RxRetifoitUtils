package com.example.rxretifoit.net.callback;

import android.os.Handler;

import com.example.rxretifoit.ui.LatteLoader;
import com.example.rxretifoit.ui.LoaderStyle;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RequestCallBacks implements Callback<String> {
    private final IRequest REQUEST;
    private final ISuccess SUCCESS;
    private final IFailure FAILURE;
    private final Ierror ERROR;
    private final LoaderStyle LOADER_STYPE;
    private static final Handler HANDLER=new Handler();

    public RequestCallBacks(IRequest request, ISuccess success, IFailure failure, Ierror ierror,LoaderStyle loaderStyle) {
        this.REQUEST = request;
        this.SUCCESS = success;
        this.FAILURE = failure;
        this.ERROR = ierror;
        this.LOADER_STYPE=loaderStyle;

    }

    @Override
    public void onResponse(Call<String> call, Response<String> response) {
        if (response.isSuccessful()){
            if (call.isExecuted()){
                if (SUCCESS!=null){
                    SUCCESS.OnSuccess(response.body());
                }
            }
        }else{
            if (ERROR!=null){
                ERROR.onError(response.code(),response.message());
            }
        }

        stopLoading();
    }

    private void stopLoading() {
        if (LOADER_STYPE!=null){
            HANDLER.postDelayed(new Runnable() {
                @Override
                public void run() {
                    LatteLoader.stopLoading();
                }
            },1000);
        }
    }

    @Override
    public void onFailure(Call<String> call, Throwable t) {
        if (FAILURE!=null){
            FAILURE.onFailure();
        }
        if (REQUEST!=null){
            REQUEST.onRequestEnd();
        }
        stopLoading();
    }
}
