package com.example.rxretifoit.net.rx;


import android.content.Context;


import com.example.rxretifoit.net.RestCreator;
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
public class RxRestClientBuilder {
    private String mUrl=null;
    private static Map<String, Object> mParams= RestCreator.getParams();
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

    RxRestClientBuilder() {

    }

    public final RxRestClientBuilder url(String url) {
        this.mUrl = url;
        return this;
    }

    public final RxRestClientBuilder params(WeakHashMap<String, Object> params) {
        mParams = params;
        return this;
    }

    public final RxRestClientBuilder params(String key, Object value) {
        mParams.put(key, value);
        return this;
    }

    public final RxRestClientBuilder onRequest(IRequest iRequest) {
        this.mRequest = iRequest;
        return this;
    }

    /**
     * 传入原始数据
     *
     * @param raw
     * @return
     */
    public final RxRestClientBuilder raw(String raw) {
        this.mBody = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), raw);
        return this;
    }

    public final RxRestClientBuilder success(ISuccess iSuccess) {
        this.mSuccess = iSuccess;
        return this;
    }

    public final RxRestClientBuilder failure(IFailure ifailure) {
        this.mFailure = ifailure;
        return this;
    }

    public final RxRestClientBuilder error(Ierror iError) {
        this.mError = iError;
        return this;
    }

    public final RxRestClientBuilder loader(Context context, LoaderStyle style){
        this.mcontext=context;
        this.mLoaderStyle=style;
        return this;
    }

    public final RxRestClientBuilder loader(Context context){
        this.mcontext=context;
        this.mLoaderStyle=LoaderStyle.BallSpinFadeLoaderIndicator;
        return this;
    }

    public final RxRestClientBuilder file(String url, String name){
        File file=new File(url);
        final RequestBody requestBody = RequestBody.create(MediaType.parse(MultipartBody.FORM.toString()), file);
        final MultipartBody.Part body = MultipartBody.Part.createFormData(name, file.getName(), requestBody);
        mbodys.add(body);
        return this;
    }
    public final RxRestClientBuilder file(File file, String name){
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
    public final RxRestClientBuilder dir(String idir) {
        this.mDownloadDir =idir;
        return this;
    }


    public final RxRestClientBuilder extension(String  ietension) {
        this.mExtension = ietension;
        return this;
    }

    public final RxRestClientBuilder name(String  iname) {
        this.mName = iname;
        return this;
    }


    public final RxRestClient build() {
        return new RxRestClient(mUrl, mParams,mBody,mcontext,mLoaderStyle,mbodys);
    }


}
