package com.example.rxretifoit.net;

import android.content.Context;


import com.example.rxretifoit.net.callback.IFailure;
import com.example.rxretifoit.net.callback.IRequest;
import com.example.rxretifoit.net.callback.ISuccess;
import com.example.rxretifoit.net.callback.Ierror;
import com.example.rxretifoit.net.callback.RequestCallBacks;
import com.example.rxretifoit.net.download.DownloadHandler;
import com.example.rxretifoit.ui.LatteLoader;
import com.example.rxretifoit.ui.LoaderStyle;

import java.util.ArrayList;
import java.util.Map;
import java.util.WeakHashMap;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * 网络请求框架
 */
public class RestClient {
    private final String URL;
    private static final WeakHashMap<String, Object> PARAMS = RestCreator.getParams();
    private final IRequest REQUEST;
    private final ISuccess SUCCESS;
    private final IFailure FAILURE;
    private final Ierror ERROR;
    private final RequestBody BODY;
    private final String DOWLOAD_DIR;
    private final String EXTENSION;
    private final String NAME;
    private final LoaderStyle LOADER_STYLE;
    private final Context CONTEXT;
    private final ArrayList<MultipartBody.Part> BODYS = new ArrayList<>();

    public RestClient(String url, Map<String, Object> params, IRequest request, ISuccess success,
                      IFailure failure, Ierror error, RequestBody body, Context context,
                      LoaderStyle loaderStyle, ArrayList<MultipartBody.Part> bodys,String dowload,String extension,String name) {
        this.URL = url;
        PARAMS.putAll(params);
        this.REQUEST = request;
        this.SUCCESS = success;
        this.FAILURE = failure;
        this.ERROR = error;
        this.BODY = body;
        this.CONTEXT = context;
        this.LOADER_STYLE = loaderStyle;
        this.BODYS.addAll(bodys);
        this.DOWLOAD_DIR=dowload;
        this.EXTENSION=extension;
        this.NAME=name;
    }

    public String getURL() {
        return URL;
    }

    public Map<String, Object> getPARANS() {
        return PARAMS;
    }

    public IRequest getREQUEST() {
        return REQUEST;
    }

    public ISuccess getSUCCESS() {
        return SUCCESS;
    }

    public IFailure getFAILURE() {
        return FAILURE;
    }

    public Ierror getERROR() {
        return ERROR;
    }

    public RequestBody getBODY() {
        return BODY;
    }

    public static RestClientBuilder builder() {
        return new RestClientBuilder();
    }

    private void request(HttpMethod method) {
        final RestService service = RestCreator.getRestService();
        Call<String> call = null;

        if (REQUEST != null) {
            REQUEST.onRequestStart();
        }
        if (LOADER_STYLE != null) {
            LatteLoader.showLoading(CONTEXT, LOADER_STYLE);
        }
        switch (method) {
            case GET:
                call = service.get(URL, PARAMS);
                break;
            case POST:
                call = service.post(URL, PARAMS);
                break;
            case POST_RAN:
                call = service.postRaw(URL, BODY);
                break;
            case PUT:
                call = service.put(URL, PARAMS);
                break;
            case PUT_RAW:
                call = service.putRaw(URL, BODY);
                break;
            case DELETE:
                call = service.get(URL, PARAMS);
                break;
            case UPLOAD:
                call = service.upload(URL, BODYS);
                break;
            default:
                break;
        }
        if (call != null) {
            call.enqueue(getequestCallback());
        }
    }

    private Callback<String> getequestCallback() {
        return new RequestCallBacks(REQUEST, SUCCESS, FAILURE, ERROR, LOADER_STYLE);
    }

    public final void get() {
        request(HttpMethod.GET);
    }

    public final void post() {
        if (BODY == null) {
            request(HttpMethod.POST);
        } else {
            if (!PARAMS.isEmpty()) {
                throw new RuntimeException("params null");
            } else {
                request(HttpMethod.POST_RAN);
            }
        }
    }

    public final void put() {
        if (BODY == null) {
            request(HttpMethod.PUT);
        } else {
            if (!PARAMS.isEmpty()) {
                throw new RuntimeException("params null");
            } else {
                request(HttpMethod.PUT_RAW);
            }
        }
    }

    public final void delete() {
        request(HttpMethod.DELETE);
    }

    public final void upload(){
        request(HttpMethod.UPLOAD);
    }

    public final void download(){
        new DownloadHandler(URL,REQUEST,SUCCESS,FAILURE,ERROR,DOWLOAD_DIR,EXTENSION,NAME)
                .handleDownload();
    }


}
