# RxJava + Retriofit + AvLoader

一个通用的高效网络请求框架，自带请求转圈状态。内部两套请求逻辑Retrofit+RxJava，即可以使用Retrofit，又可以使用Rx,动态选择最合适的请求。

## 使用方法：

将其添加到存储库末尾的根build.gradle中：

```css
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```

添加依赖项

```css
	dependencies {
	        implementation 'com.github.Petterpx:RxRetifoitUtils:v1.4'
	}
```



支持以下请求**：

```java
@GET
Call<String> get(@Url String url, @QueryMap Map<String, Object> params);

@FormUrlEncoded
@POST
Call<String> post(@Url String url, @FieldMap Map<String, Object> params);

@POST
Call<String> postRaw(@Url String url, @Body RequestBody body);

@FormUrlEncoded
@PUT
Call<String> put(@Url String url, @FieldMap Map<String, Object> params);

@PUT
Call<String> putRaw(@Url String url, @Body RequestBody body);


@DELETE
Call<String> delete(@Url String url, @QueryMap Map<String, Object> params);

@Streaming
@GET
Call<ResponseBody> download(@Url String url, @QueryMap Map<String, Object> params);

@Multipart
@POST
Call<String> upload(@Url String url, @Part List<MultipartBody.Part> partList);
```



## 简单使用方法：

### Get使用方法1 (Rx)：

```java
//配置全局Host
RestUrlInfo.setURL("https://www.baidu.com");
RxRestClient.builder()
        .url("")
        .loader(this)
        .build()
        .get()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(String s) {
                Log.e("Demo",s);
                //停止动画
                LatteLoader.stopLoading();
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
```

### Get使用方法2 (Rx)：

```java
//配置全局Host
RestUrlInfo.setURL("https://www.baidu.com");
//尾部
final String url = "";
//请求体，键值对形式
final WeakHashMap<String, Object> params = new WeakHashMap<>();
RestCreator.getRxRestService()
        .get(url, params)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(String s) {
                Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        })
;
```

### Get使用 (Retrofit-异步)：

```java
//配置全局Host
RestUrlInfo.setURL("https://www.baidu.com");
RestClient.builder()
        .loader(this)
        .url("")
        .success(new ISuccess() {
            @Override
            public void OnSuccess(String response) {
                Log.e("demo",response);
            }
        })
        .build()
        .get();
```



### 文件上传(Rx)：

```java
//设置全局Host
RestUrlInfo.setURL("http://192.168.1.101");
        File file = new File(Environment.getExternalStorageDirectory() + "/" + "test.text1");
        RxRestClient.builder()
                .url("/API/upload.php")
                .loader(this)
                .file(file,"name1")
                .file(file,"name2")
                .build()
                .upload()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {

                    @Override
                    public void onSubscribe(Disposable d) {
                        
                    }

                    @Override
                    public void onNext(String s) {
                        LatteLoader.stopLoading();
                        Log.e("demo",s);
                    }

                    @Override
                    public void onError(Throwable e) {
                        LatteLoader.stopLoading();
                        Log.e("demo",e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
```



### 文件下载(Rx)：

```java
//设置全局Host
RestUrlInfo.setURL("http://xxx.xxx.xx.xxx/");
        String name="T1.ext";       //文件名
        String downloadDir="download";   //默认下载目录 download_log
        String extension="";            //后缀名
        RxRestClient.builder()
                .url("1.exe")
                .loader(this)
                .build()
                .download()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        Log.e("demo","s");
                        final SaveFileTask task = new SaveFileTask(response -> LatteLoader.stopLoading());
                        //下载目录，后缀名，请求体，文件名
                        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, downloadDir, extension, responseBody, name);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                    }
                })
        ;
```



### 文件下载(Retrofit):

```java
 RestUrlInfo.setURL("http://101.132.64.249/");
        RestClient.builder()
                .loader(this)
                .url("1.exe")
                .success(response ->Log.e("demo", response))
                .error((code, msg) -> Log.e("demo", msg))
                .failure(() -> {
                    
                })
                .name("T1.exe")
                .build().download();
```



### 文件下载(Rx)

```java
RestUrlInfo.setURL("http://101.132.64.249/");
String name="T1.ext";       //文件名
String downloadDir="download";   //默认下载目录 download_log
String extension="";            //后缀名
RxRestClient.builder()
        .url("1.exe")
        .loader(this)
        .build()
        .download()
        .subscribeOn(Schedulers.io())
        .observeOn(Schedulers.newThread())
        .subscribe(new Observer<ResponseBody>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(ResponseBody responseBody) {
                Log.e("demo","s");
                final SaveFileTask task = new SaveFileTask(response -> LatteLoader.stopLoading());
                //下载目录，后缀名，请求体，文件名
                task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, downloadDir, extension, responseBody, name);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
            }
        })
;
```
