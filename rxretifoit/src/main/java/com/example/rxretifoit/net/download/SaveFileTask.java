package com.example.rxretifoit.net.download;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import com.example.rxretifoit.net.callback.IRequest;
import com.example.rxretifoit.net.callback.ISuccess;
import com.example.rxretifoit.util.file.FileUtil;

import java.io.File;
import java.io.InputStream;

import okhttp3.ResponseBody;


public class SaveFileTask extends AsyncTask<Object,Void, File> {

    //请求回调
    private  final IRequest REQUEST;
    //成功的回调
    private  final ISuccess SUCCESS;

    public SaveFileTask(IRequest REQUEST, ISuccess SUCCESS) {
        this.REQUEST = REQUEST;
        this.SUCCESS = SUCCESS;
    }

    /**
     *   必须重写的方法，异步执行后台线程要完成的任务，耗时操作将在此方法中执行
     * @param objects
     * @return
     */

    @Override
    protected File doInBackground(Object... objects) {
        //在数组中取出 下载目录
        String downloadDir= (String) objects[0];
        //后缀
        String extension= (String) objects[1];
        //请求体
        final ResponseBody body= (ResponseBody) objects[2];
        //文件名
        final  String name= (String) objects[3];
        //将请求体的到的数据转成输入流写入到文件
        final InputStream  is= body.byteStream();
        //判断下载目录是否为空
        if (downloadDir==null|| downloadDir.equals("")){
            downloadDir="down_loads";
        }
        //判断后缀是否为空
        if (extension==null || extension.equals("")){
            extension="";
        }
        //如果 没有文件名传入 那么就 调用 下面方法 生成文件
        if (name==null){
            return FileUtil.writeToDisk(is,downloadDir,extension.toUpperCase(),extension);
        }else {
            //如果有文件名 传入 就调用下面方法
            return  FileUtil.writeToDisk(is,downloadDir,name);
        }
    }

    /**
     * 执行完异步回到主线程的操作  通过从方法进行更新ui的操作
     * @param file
     */
    @Override
    protected void onPostExecute(File file) {
        super.onPostExecute(file);
        //执行成功的回调
        if (SUCCESS!=null){
            SUCCESS.OnSuccess(file.getPath());
        }
        //并关闭请求
        if (REQUEST!=null){
            REQUEST.onRequestEnd();
        }
        //自动安装apk文件
//        autoTnstallApk(file);
    }

    /**
     * 自动安装AKP代码
     * @param file
     */
    private  void  autoTnstallApk(File file, Context context){
        if (FileUtil.getExtension(file.getPath()).equals("apk"));
        final Intent intent=new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file),"application/vnd.android.package-archive");
        context.startActivity(intent);
    }
}
