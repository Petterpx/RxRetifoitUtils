package com.example.rxretifoit.net;


import android.content.Context;


import com.example.rxretifoit.net.callback.IFailure;
import com.example.rxretifoit.net.callback.IRequest;
import com.example.rxretifoit.net.callback.ISuccess;
import com.example.rxretifoit.net.callback.Ierror;
import com.example.rxretifoit.ui.LoaderStyle;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;
import java.util.WeakHashMap;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * 建造者
 */
public class RestClientBuilder {
    private String mUrl=null;
    private static Map<String, Object> mParams=RestCreator.getParams();
    private IRequest mRequest=null;
    private ISuccess mSuccess=null;
    private IFailure mFailure=null;
    private Ierror mError=null;
    private RequestBody mBody=null;
    private Context mcontext=null;
    private LoaderStyle mLoaderStyle=null;
    private String mDownloadDir = null;
    private String mExtension = null;
    private String mName = null;
    private ArrayList<MultipartBody.Part> mbodys=new ArrayList<>();

    RestClientBuilder() {

    }

    public final RestClientBuilder url(String url) {
        this.mUrl = url;
        return this;
    }

    public final RestClientBuilder params(WeakHashMap<String, Object> params) {
        mParams = params;
        return this;
    }

    public final RestClientBuilder params(String key, Object value) {
        mParams.put(key, value);
        return this;
    }

    public final RestClientBuilder onRequest(IRequest iRequest) {
        this.mRequest = iRequest;
        return this;
    }

    /**
     * 传入原始数据
     *
     * @param raw
     * @return
     */
    public final RestClientBuilder raw(String raw) {
        this.mBody = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), raw);
        return this;
    }

    public final RestClientBuilder success(ISuccess iSuccess) {
        this.mSuccess = iSuccess;
        return this;
    }

    public final RestClientBuilder failure(IFailure ifailure) {
        this.mFailure = ifailure;
        return this;
    }

    public final RestClientBuilder error(Ierror iError) {
        this.mError = iError;
        return this;
    }

    public final RestClientBuilder loader(Context context,LoaderStyle style){
        this.mcontext=context;
        this.mLoaderStyle=style;
        return this;
    }

    public final RestClientBuilder loader(Context context){
        this.mcontext=context;
        this.mLoaderStyle=LoaderStyle.BallSpinFadeLoaderIndicator;
        return this;
    }

    public final RestClientBuilder file(String url,String name){
        File file=new File(url);
        final RequestBody requestBody = RequestBody.create(MediaType.parse(MultipartBody.FORM.toString()), file);
        final MultipartBody.Part body = MultipartBody.Part.createFormData(name, file.getName(), requestBody);
        mbodys.add(body);
        return this;
    }
    public final RestClientBuilder file(File file,String name){
        final RequestBody requestBody = RequestBody.create(MediaType.parse(MultipartBody.FORM.toString()), file);
        final MultipartBody.Part body = MultipartBody.Part.createFormData(name, file.getName(), requestBody);
        mbodys.add(body);
        return this;
    }

    /**
     * 文件下载存放位置
     * @param idir
     * @return
     */
    public final RestClientBuilder dir(String idir) {
        this.mDownloadDir =idir;
        return this;
    }


    public final RestClientBuilder extension(String  ietension) {
        this.mExtension = ietension;
        return this;
    }

    public final RestClientBuilder name(String  iname) {
        this.mName = iname;
        return this;
    }


    public final RestClient build() {
        return new RestClient(mUrl, mParams, mRequest, mSuccess, mFailure, mError, mBody,mcontext,mLoaderStyle,mbodys,mDownloadDir,mExtension,mName);
    }


}
