package com.example.rxretifoit.net;


import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Retrofit接口
 */
public interface RestService {


    @GET
    Call<String> get(@Url String url, @QueryMap Map<String, Object> params);

    @FormUrlEncoded
    @POST
    Call<String> post(@Url String url, @FieldMap Map<String, Object> params);

    /**
     * 原始数据
     * @param url
     * @param body
     * @return
     */
    @POST
    Call<String> postRaw(@Url String url, @Body RequestBody body);

    @FormUrlEncoded
    @PUT
    Call<String> put(@Url String url, @FieldMap Map<String, Object> params);

    @PUT
    Call<String> putRaw(@Url String url, @Body RequestBody body);


    @DELETE
    Call<String> delete(@Url String url, @QueryMap Map<String, Object> params);

    /**
     * 这里为了防止内存溢出，默认情况是下载好存储，当文件过大时，不建议采取。
     * 这里改用边下载边存储
     * @param url
     * @param params
     * @return
     */
    @Streaming
    @GET
    Call<ResponseBody> download(@Url String url, @QueryMap Map<String, Object> params);

    /**
     * 多文件上传
     * @param url
     * @param partList
     * @return
     */
    @Multipart
    @POST
    Call<String> upload(@Url String url, @Part List<MultipartBody.Part> partList);

}
