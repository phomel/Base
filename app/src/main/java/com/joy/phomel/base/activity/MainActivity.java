package com.joy.phomel.base.activity;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.joy.phomel.base.R;
import com.joy.phomel.common.image.GlideUtils;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.security.PublicKey;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
