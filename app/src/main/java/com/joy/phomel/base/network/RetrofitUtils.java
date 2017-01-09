package com.joy.phomel.base.network;

import com.joy.phomel.common.network.RetrofitManager;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by phomel on 2017/1/9.
 */

public class RetrofitUtils {

    public static ApiService sApiService = RetrofitManager.getInstance().getRetrofit().create(ApiService.class);

    public static ApiService create() {
        return sApiService;
    }

    /**
     *建立简单的订阅关系
     * @param observable
     * @param subscriber
     */
    public static void toSubcribe(Observable observable, Subscriber subscriber) {
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }
}
