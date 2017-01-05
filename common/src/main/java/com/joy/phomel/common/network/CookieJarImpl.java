package com.joy.phomel.common.network;

import android.content.Context;

import com.zhy.http.okhttp.cookie.store.CookieStore;
import com.zhy.http.okhttp.cookie.store.PersistentCookieStore;

import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

/**
 * 使用PersistentCookieStore管理cookies
 */

public class CookieJarImpl implements CookieJar {

    private CookieStore cookieStore;
    private Context mContext;

    public CookieJarImpl(Context context) {
        mContext = context;
        this.cookieStore = new PersistentCookieStore(mContext.getApplicationContext());
    }

    @Override
    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        cookieStore.add(url, cookies);
    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl url) {
        return cookieStore.get(url);
    }
}
