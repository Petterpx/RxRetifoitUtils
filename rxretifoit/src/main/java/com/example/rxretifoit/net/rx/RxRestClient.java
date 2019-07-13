package com.example.rxretifoit.net.rx;

import android.annotation.SuppressLint;
import android.content.Context;


import com.example.rxretifoit.net.HttpMethod;
import com.example.rxretifoit.net.RestCreator;
import com.example.rxretifoit.ui.LatteLoader;
import com.example.rxretifoit.ui.LoaderStyle;

import java.util.ArrayList;
import java.util.Map;
import java.util.WeakHashMap;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

/**
 * 网络请求框架
 * 建造者模式用来处理更好
 */
public class RxRestClient {
    private final String URL;
    private static final WeakHashMap<String, Object> PARAMS = RestCreator.getParams();
    private final RequestBody BODY;
    private final LoaderStyle LOADER_STYLE;
    private final Context CONTEXT;
    private final ArrayList<MultipartBody.Part> BODYS = new ArrayList<>();

    public RxRestClient(String url, Map<String, Object> params, RequestBody body, Context context,
                        LoaderStyle loaderStyle, ArrayList<MultipartBody.Part> bodys) {
        this.URL = url;
        PARAMS.putAll(params);
        this.BODY = body;
        this.CONTEXT = context;
        this.LOADER_STYLE = loaderStyle;
        this.BODYS.addAll(bodys);
    }

    public String getURL() {
        return URL;
    }

    public Map<String, Object> getPARANS() {
        return PARAMS;
    }

    public RequestBody getBODY() {
        return BODY;
    }

    public static RxRestClientBuilder builder() {
        return new RxRestClientBuilder();
    }

    @SuppressLint("CheckResult")
    private Observable<String> request(HttpMethod method) {
        final RxRestService service = RestCreator.getRxRestService();
        Observable<String> observable = null;
        if (LOADER_STYLE != null) {
            LatteLoader.showLoading(CONTEXT, LOADER_STYLE);
        }
        switch (method) {
            case GET:
                observable = service.get(URL, PARAMS);
                break;
            case POST:
                observable = service.post(URL, PARAMS);
                break;
            case POST_RAN:
                observable = service.postRaw(URL, BODY);
                break;
            case PUT:
                observable = service.put(URL, PARAMS);
                break;
            case PUT_RAW:
                observable = service.putRaw(URL, BODY);
                break;
            case DELETE:
                observable = service.get(URL, PARAMS);
                break;
            case UPLOAD:
                observable = service.upload(URL, BODYS);
                break;
            default:
                break;
        }
        return observable;
    }


    public final Observable<String> get() {
        return request(HttpMethod.GET);
    }

    public final Observable<String> post() {
        if (BODY == null) {
            return request(HttpMethod.POST);
        } else {
            if (!PARAMS.isEmpty()) {
                throw new RuntimeException("params null");
            } else {
                return request(HttpMethod.POST_RAN);
            }
        }
    }

    public final Observable<String> put() {
        if (BODY == null) {
            return request(HttpMethod.PUT);
        } else {
            if (!PARAMS.isEmpty()) {
                throw new RuntimeException("params null");
            } else {
                return request(HttpMethod.PUT_RAW);
            }
        }
    }

    public final Observable<String> delete() {
        return request(HttpMethod.DELETE);
    }

    public final Observable<String> upload() {
        return request(HttpMethod.UPLOAD);
    }

    public final Observable<ResponseBody> download() {
        final Observable<ResponseBody> responseBodyObservable = RestCreator.getRxRestService().download(URL, PARAMS);
        return responseBodyObservable;
    }


}
