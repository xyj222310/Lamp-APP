package com.yjtse.lamp.ui;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.yjtse.lamp.R;
import com.yjtse.lamp.base.BaseActivity;
import com.yjtse.lamp.utils.InjectUtils;
import com.yjtse.lamp.widgets.CenterTitleActionBar;

public class NewsActivity extends BaseActivity {

    WebView mWebview;
    WebSettings mWebSettings;
    TextView loading;
    ProgressBar progressBar;

    //变量
    private String url = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        Bundle bundle = this.getIntent().getExtras();
        url = bundle.getString("url");

        InjectUtils.injectObject(this, this);
        CenterTitleActionBar actionBar = new CenterTitleActionBar(this, getActionBar());
        actionBar.setTitle("详情");
        actionBar.setCustomActionBar();
        actionBar.setOnClickActionBarListener(new CenterTitleActionBar.OnClickActionBarListener() {
            @Override
            public void onTitleClick() {
            }

            @Override
            public void onSecondBtnClick() {
            }

            @Override
            public void onFirstBtnClick() {
            }

            @Override
            public void onBackBtnClick() {
                NewsActivity.this.finish();
            }
        });
        /**
         * 设置webview
         */
        mWebview = (WebView) findViewById(R.id.news_web_view);
        loading = (TextView) findViewById(R.id.text_Loading);
        progressBar = (ProgressBar) findViewById(R.id.news_progressBar);

        mWebSettings = mWebview.getSettings();
        mWebview.loadUrl(url);

        //设置不用系统浏览器打开,直接显示在当前Webview
        mWebview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                loading.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                return true;
            }
        });

        //设置WebChromeClient类
        mWebview.setWebChromeClient(new WebChromeClient() {
            //获取网站标题
            @Override
            public void onReceivedTitle(WebView view, String title) {
                System.out.println("标题在这里");
                loading.setText(title);
            }

            //获取加载进度
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
//                if (newProgress < 100) {
                String progress = newProgress + "%";
                progressBar.setProgress(newProgress);
                loading.setText(progress);
//                } else if (newProgress == 100) {
//                    String progress = newProgress + "%";
//                    loading.setText(progress);
//                }
            }
        });
        //设置WebViewClient类
        mWebview.setWebViewClient(new WebViewClient() {
            //设置加载前的函数
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                System.out.println("开始加载了");

            }

            //设置结束加载函数
            @Override
            public void onPageFinished(WebView view, String url) {
                loading.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    //点击返回上一页面而不是退出浏览器
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && mWebview.canGoBack()) {
            mWebview.goBack();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    //销毁Webview
    @Override
    protected void onDestroy() {
        if (mWebview != null) {
            mWebview.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            mWebview.clearHistory();

            ((ViewGroup) mWebview.getParent()).removeView(mWebview);
            mWebview.destroy();
            mWebview = null;
        }
        super.onDestroy();
    }

}
