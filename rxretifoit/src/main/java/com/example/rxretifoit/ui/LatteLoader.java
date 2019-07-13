package com.example.rxretifoit.ui;

import android.content.Context;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatDialog;


import com.example.rxretifoit.R;
import com.example.rxretifoit.util.DimenUtil;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;

public class LatteLoader {

    //宽高比例缩放
    private static final int LOADER_SIZE_SCALE=8;
    private static final int LOADER_OFFSET_SCALE=10;
    private static final ArrayList<AppCompatDialog> LOADERS=new ArrayList<>();
    private static final String DEFAULT_LOADER=LoaderStyle.BallSpinFadeLoaderIndicator.name();


    public static void showLoading(Context context,Enum<LoaderStyle> type){
        showLoading(context,type.name());
    }

    public static void showLoading(Context context,String type){
        final AppCompatDialog dialog=new AppCompatDialog(context, R.style.dialog);

        final AVLoadingIndicatorView avLoadingIndicatorView=LoaderCreator.create(type,context);
        dialog.setContentView(avLoadingIndicatorView);

        int deviceWidth= DimenUtil.getScreenWidth(context);
        int deviceHeight=DimenUtil.getScreenHeight(context);

        final Window dialogWindow=dialog.getWindow();
        if (dialogWindow!=null){
            WindowManager.LayoutParams lp=dialogWindow.getAttributes();
            lp.width=deviceWidth/LOADER_SIZE_SCALE;
            lp.height=deviceHeight/LOADER_SIZE_SCALE;
            lp.height=lp.height+deviceHeight/LOADER_OFFSET_SCALE;
            lp.gravity= Gravity.CENTER;
        }
        LOADERS.add(dialog);
        dialog.show();
    }

    public static void showLoading(Context context){
        showLoading(context,DEFAULT_LOADER);
    }
    public static void stopLoading(){
        for (AppCompatDialog dialog:LOADERS){
            if (dialog != null) {
                if (dialog.isShowing()){
                    dialog.cancel();
                }
            }
        }
    }
}
