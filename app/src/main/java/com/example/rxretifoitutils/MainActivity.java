package com.example.rxretifoitutils;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.example.rxretifoit.net.RestClient;
import com.example.rxretifoit.net.RestUrlInfo;
import com.example.rxretifoit.net.callback.IRequest;
import com.example.rxretifoit.net.callback.ISuccess;
import com.example.rxretifoit.net.callback.Ierror;
import com.example.rxretifoit.net.download.SaveFileTask;
import com.example.rxretifoit.net.rx.RxRestClient;
import com.example.rxretifoit.ui.LatteLoader;

import java.io.File;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

public class MainActivity extends AppCompatActivity{
    private static int REQUEST_PERMISSION_CODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    private void dowolad() {
        RestUrlInfo.setURL("http://xxx.xxx.xx.xxx/");
        String name="name.xx";       //文件名
        String downloadDir="download";   //默认下载目录 download_log
        String extension="";            //后缀名
        RxRestClient.builder()
                .url("xxx")
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
                        final SaveFileTask task = new SaveFileTask(response -> LatteLoader.stopLoading());
                        //下载目录，后缀名，请求体，文件名
                        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, downloadDir, extension, responseBody, name);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        Log.e("demo","1");
                    }
                })
        ;
    }

    private void setQuxian() {
        // 没有授权，申请权限
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSION_CODE);
            }
        }else{
            dowolad();
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 用户同意的授权请求
                dowolad();
            } else {
                Toast.makeText(this, "用户拒绝权限", Toast.LENGTH_SHORT).show();
                // 如果用户不是点击了拒绝就跳转到系统设置页
            }
        }
    }

}
