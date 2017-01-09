package com.joy.phomel.common.listener;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import rx.Subscriber;

/**
 * 下拉刷新的subcribe
 * Created by phomel on 2017/1/9.
 */

public class CustSubscriber<T> extends Subscriber<T> implements ProgressCancelListener {

    private SubscriberOnNextListener<T> mListener;
    private Context mContext;
    private SwipeRefreshLayout mRefreshLayout;

    public CustSubscriber(Context context, SwipeRefreshLayout mRefreshLayout, SubscriberOnNextListener<T> listener) {
        this.mListener = listener;
        this.mContext = context;
        this.mRefreshLayout = mRefreshLayout;
    }

    private void showProgress() {
        if (mRefreshLayout != null) {
            mRefreshLayout.setRefreshing(true);
        }
    }

    private void dismissProgress() {
        if (mRefreshLayout != null) {
            mRefreshLayout.setRefreshing(false);
        }
        mRefreshLayout = null;
    }


    /**
     * 订阅开始时调用
     * 显示ProgressDialog
     */
    @Override
    public void onStart() {
        super.onStart();
        showProgress();
    }

    @Override
    public void onCompleted() {
        dismissProgress();
        Toast.makeText(mContext, "获取数据完成！", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onError(Throwable e) {
        if (e instanceof SocketTimeoutException || e instanceof ConnectException) {
            Toast.makeText(mContext, "网络中断，请检查您的网络状态", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(mContext, "error:" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        dismissProgress();
    }

    @Override
    public void onNext(T t) {
        if (mListener != null) {
            mListener.onNext(t);
        }
    }

    @Override
    public void onCancelProgress() {
        if (!this.isUnsubscribed()) {
            this.unsubscribe();
        }
    }
}
