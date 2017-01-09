package com.joy.phomel.common.application;

import android.app.Application;
import android.content.Context;

import com.joy.phomel.common.image.GlideUtils;
import com.joy.phomel.common.network.RetrofitManager;
import com.joy.phomel.common.utils.FileUtils;
import com.joy.phomel.common.utils.L;
import com.joy.phomel.common.utils.NetworkUtils;
import com.joy.phomel.common.utils.SDCardManager;

import java.io.File;

/**
 * Created by phomel on 2017/1/5.
 */

public class BaseApplication extends Application {

    //缓存目录
    public static String CACHE_DIR;

    //图片缓存目录
    public static String IMAGE_DIR;

    //上传图片临时目录
    public static String IMAGE_UPLOAD_TEMP;

    //文件目录
    public static String FILE_DIR;

    //日志目录
    public static String LOG_DIR;

    protected Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        init();
    }

    private void init() {
        if(SDCardManager.isExistSD()) { //SD卡存在且可用
            CACHE_DIR = SDCardManager.getSDCacheDir(this) + File.separator;
        } else { //SD卡不可用，使用内部存储
            CACHE_DIR = this.getCacheDir().getAbsolutePath() + File.separator;
        }
        L.e("缓存目录："+CACHE_DIR);

        IMAGE_DIR = CACHE_DIR + "image" +File.separator;
        IMAGE_UPLOAD_TEMP = CACHE_DIR + "imageUploadTemp" + File.separator;
        FILE_DIR = CACHE_DIR + "file" +  File.separator;
        LOG_DIR = CACHE_DIR + "log" +  File.separator;

        FileUtils.checkDir(CACHE_DIR);
        FileUtils.checkDir(IMAGE_DIR);
        FileUtils.checkDir(IMAGE_UPLOAD_TEMP);
        FileUtils.checkDir(FILE_DIR);
        FileUtils.checkDir(LOG_DIR);

        //初始化Glide图片加载,替换错误图片和加载中图片
        GlideUtils.init(mContext, android.R.mipmap.sym_def_app_icon, android.R.mipmap.sym_def_app_icon);
        //初始化Retrofit，修改base_url
        RetrofitManager.init(this);
        //初始化网络状态
        NetworkUtils.initNetStatus(this);
    }
}
