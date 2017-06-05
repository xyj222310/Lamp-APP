package com.yjtse.lamp.ui;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;
import com.yjtse.lamp.Config;
import com.yjtse.lamp.R;
import com.yjtse.lamp.adapter.TimerAdapter;
import com.yjtse.lamp.asynchttp.TextNetWorkCallBack;
import com.yjtse.lamp.contentview.ContentWidget;
import com.yjtse.lamp.domain.Cron;
import com.yjtse.lamp.domain.Result;
import com.yjtse.lamp.requests.AsyncRequest;
import com.yjtse.lamp.utils.InjectUtils;
import com.yjtse.lamp.utils.NetAvailable;
import com.yjtse.lamp.utils.SharedPreferencesUtil;
import com.yjtse.lamp.utils.ToastUtils;
import com.yjtse.lamp.widgets.CenterTitleActionBar;
import com.yjtse.lamp.widgets.MyListView;

import org.apache.http.Header;

import java.util.ArrayList;
import java.util.List;

public class TimerActivity extends Activity implements View.OnClickListener, AdapterView.OnItemClickListener {

    public static List<Cron> cron_list;

    private MyListView timer_list;//列表

    @ContentWidget(R.id.timer_btn_add)
    private TextView btn_add_timer;

    @ContentWidget(R.id.timer_btn_delete)
    private TextView btn_delete_timer;

    @ContentWidget(R.id.timer_btn_more)
    private TextView btn_more_timer;

    @ContentWidget(R.id.hint_timer_string)
    private TextView hint_timer_string;

    private TimerAdapter adapter;

//    private CenterTitleActionBar actionBar;//中央标题actionBar组件

    private Dialog dialog = null;

    private boolean isFirstLoading = true;

    private boolean isRefreshing = false;
    /**
     * 预加载标志，默认值为false，设置为true，表示已经预加载完成，可以加载数据
     */
    private boolean isPrepared;

    private String socketIdFromDeviceFm = "";
    private String statusFromDeviceFm = "";

//    private String statusTobe = "";
//    private String cron = "";

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case Config.MESSAGE_WHAT_UPDATE_TIMER_AVAILABLE:
                    //肯定有网
//                    RequestParams params = new RequestParams();
//                    params.put("id", cron_list.get(msg.arg1).getId());
//                    params.put("socketId", cron_list.get(msg.arg1).getSocketId());
//                    params.put("ownerId", cron_list.get(msg.arg1).getOwnerId());
//                    params.put("statusTobe", cron_list.get(msg.arg1).getStatusTobe());
//                    params.put("cron", cron_list.get(msg.arg1).getCron());
//                    params.put("available", msg.arg2);
                    if (msg.obj != null) {
                        startDialog("提交中...");
                        requestUpdateTimer(Config.getRequestURL(Config.ACTION_CRON_UPDATE), (RequestParams) msg.obj);
                    }
                    break;
                case Config.MESSAGE_WHAT_DELETE_TIMER:
                    startDialog("deleting...");
                    String ownerId = (String) SharedPreferencesUtil.query(TimerActivity.this, Config.KEY_USERNAME, "String");
                    RequestParams requestParams = new RequestParams();
                    requestParams.add("id", String.valueOf(cron_list.get(msg.arg1 - 1).getId()));
                    requestParams.add("ownerId", ownerId);
                    requestDeleteTimer(Config.getRequestURL(Config.ACTION_CRON_DELETE_BY_ID), requestParams, msg.arg1 - 1);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
        /*        获取传过来的数据         */
        Bundle bundle = this.getIntent().getExtras();
        socketIdFromDeviceFm = bundle.getString("socketId");
        statusFromDeviceFm = bundle.getString("status");
        /*
        初始界面
         */
        InjectUtils.injectObject(this, this);
        CenterTitleActionBar actionBar = new CenterTitleActionBar(this, getActionBar());
        actionBar.setTitle("定时列表");
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
                TimerActivity.this.finish();
            }
        });

        hint_timer_string.setText("1".equals(statusFromDeviceFm) ? "设备当前状态：on" : "设备当前状态：off");
        timer_list = (MyListView) findViewById(R.id.timer_list);
        cron_list = new ArrayList<>();
        adapter = new TimerAdapter(this.getApplicationContext(), handler, R.layout.item_timer);
        adapter.setData(cron_list);
        timer_list.setAdapter(adapter);
        timer_list.setItemsCanFocus(false);
        /*
        监听事件
         */
        timer_list.setOnRefreshListener(new MyListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                isRefreshing = true;
                if (!NetAvailable.isNetworkAvailable()) {
                    ToastUtils.showToast(getApplicationContext(), "请检查网络链接", Toast.LENGTH_LONG);
                } else {
                    requestAllTimer();
                }
                endDialog();
            }
        });
        btn_add_timer.setOnClickListener(this);
        btn_delete_timer.setOnClickListener(this);
        btn_more_timer.setOnClickListener(this);
        timer_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(TimerActivity.this, TimerSettingActivity.class);
                intent.putExtra("id", cron_list.get(position - 1).getId());
                intent.putExtra("socketId", socketIdFromDeviceFm);
                intent.putExtra("statusTobe", cron_list.get(position - 1).getStatusTobe());
                intent.putExtra("cron", cron_list.get(position - 1).getCron());
                intent.putExtra("available", cron_list.get(position - 1).getAvailable());
                intent.putExtra("flag", Config.MESSAGE_WHAT_UPDATE_TIMER);
                startActivity(intent);
            }
        });

        /**
         * 长按删除监听事件
         @return true if the callback consumed the long click, false otherwise
         */
        timer_list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(TimerActivity.this);
                builder.setTitle("Notice");
                builder.setMessage("确定删除此定时设定？");
                builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Message msg = Message.obtain();
                        msg.arg1 = position;
                        msg.what = Config.MESSAGE_WHAT_DELETE_TIMER;
                        handler.sendMessage(msg);
                    }
                });
                builder.setNegativeButton("no", null);
                builder.create().show();
                return false;
            }
        });
        /*
        加载数据
         */
        InjectUtils.injectObject(this, this);
        isPrepared = true;
        if (isFirstLoading)

        {
            isFirstLoading = false;
            startDialog("正在加载.....");
        }
        if (!NetAvailable.isConnect(this))

        {
            ToastUtils.showToast(this, "请检查网络链接", Toast.LENGTH_LONG);
            endDialog();
        } else

        {
            requestAllTimer();
        }

    }

    private void requestUpdateTimer(String url, RequestParams params) {
        AsyncRequest.ClientPost(url, params, new TextNetWorkCallBack() {
            @TargetApi(Build.VERSION_CODES.KITKAT)
            @Override
            public void onMySuccess(int statusCode, Header[] header, String result) {
                try {
                    final Result res = new Gson().fromJson(result, Result.class);
                    if (res != null) {
                        if (res.isSuccess()) {
                            ToastUtils.showToast(getApplicationContext(), "设置成功", Toast.LENGTH_SHORT);
                            endDialog();
                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(TimerActivity.this);
                            builder.setTitle("Notice");
                            builder.setMessage("更新失败：" + res.getError());
                            builder.setPositiveButton("确定", null);
                            builder.create().show();
                            endDialog();
                        }
                    }
                } catch (Exception e) {
                    responseError(e);
                }
            }

            @Override
            public void onMyFailure(int statusCode, Header[] header, String result, Throwable
                    th) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
                builder.setTitle("Alert");
                builder.setMessage("网络数据更新失败！");
                builder.create().show();
                endDialog();
            }
        });
    }

    private void requestAllTimer() {
        String url = Config.getRequestURL(Config.ACTION_CRON_GET_ALL_BY_SOCKET_ID);
        RequestParams params = new RequestParams();
        params.add("socketId", socketIdFromDeviceFm);
        AsyncRequest.ClientGet(url, params, new TextNetWorkCallBack() {
            @TargetApi(Build.VERSION_CODES.KITKAT)
            @Override
            public void onMySuccess(int statusCode, Header[] header, String result) {
                try {
                    final List<Cron> list = new Gson().fromJson(result, new TypeToken<List<Cron>>() {
                    }.getType());
                    if (!TextUtils.isEmpty(result) && list != null) {
                        ToastUtils.showToast(getApplicationContext(), "列表已更新", Toast.LENGTH_LONG);
                        cron_list = new ArrayList<>();
                        cron_list = list;
                        adapter.setData(cron_list);
                        adapter.notifyDataSetChanged();
                        endDialog();
                    } else {
                        ToastUtils.showToast(getApplicationContext(), "网络请求失败", Toast.LENGTH_LONG);
                        endDialog();
                    }
                } catch (Exception e) {
                    responseError(e);
                }
            }

            @Override
            public void onMyFailure(int statusCode, Header[] header, String result, Throwable th) {
                ToastUtils.showToast(getApplicationContext(), "网络请求失败", Toast.LENGTH_LONG);
                endDialog();
            }
        });
    }

    public void requestDeleteTimer(final String url, final RequestParams params, final int position) {
        AsyncRequest.ClientPost(url, params, new TextNetWorkCallBack() {
            @Override
            public void onMySuccess(int statusCode, Header[] header, String result) {
                Gson gson = new Gson();
                try {
                    Result result1 = gson.fromJson(result, Result.class);
                    if (result1.isSuccess()) {
                        ToastUtils.showToast(TimerActivity.this, "成功删除", Toast.LENGTH_LONG);
                        cron_list.remove(position);
                        adapter.setData(cron_list);
                        adapter.notifyDataSetChanged();
                        endDialog();
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(TimerActivity.this);
                        builder.setTitle("Notice");
                        builder.setMessage("删除失败：" + result1.getError());
                        builder.setPositiveButton("确定", null);
                        builder.create().show();
                        endDialog();
                    }
                } catch (Exception e) {
                    responseError(e);
                }
            }

            @Override
            public void onMyFailure(int statusCode, Header[] header, String result, Throwable th) {
                AlertDialog.Builder builder = new AlertDialog.Builder(TimerActivity.this);
                builder.setTitle("Notice");
                builder.setMessage("删除失败");
                builder.show();
                endDialog();
            }
        });
    }

    private void responseError(Exception e) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Notice");
        if (!NetAvailable.isNetworkAvailable()) {
            builder.setMessage("操作失效，\n 无法访问网络" + e);
        } else {
            builder.setMessage("操作失效，\n 网络请求返回数据格式有误！" + e);
        }
        builder.setPositiveButton("确定", null);
        builder.create().show();
        endDialog();
    }

    private void startDialog(String msg) {
        dialog = new Dialog(this, R.style.MyDialogStyle);
        dialog.setContentView(R.layout.loading);
        dialog.setCanceledOnTouchOutside(false);
        TextView message = (TextView) dialog.getWindow().findViewById(R.id.load_msg);
        if (dialog != null && !dialog.isShowing()) {
            message.setText(msg);
            dialog.show();
        }
    }

    private void endDialog() {
        if (isRefreshing) {
            timer_list.onRefreshComplete();
            isRefreshing = false;
        }
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!NetAvailable.isConnect(this)) {
            ToastUtils.showToast(this, "请检查网络链接", Toast.LENGTH_LONG);
        } else {
            requestAllTimer();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.timer_btn_add:
                Intent intent = new Intent(this, TimerSettingActivity.class);
                intent.putExtra("id", 0);//
                intent.putExtra("socketId", socketIdFromDeviceFm);//
                intent.putExtra("statusTobe", "");
                intent.putExtra("cron", "");
                intent.putExtra("available", "");
                intent.putExtra("flag", Config.MESSAGE_WHAT_ADD_TIMER);
                startActivity(intent);
                break;
            case R.id.timer_btn_delete:
                ToastUtils.showToast(getApplicationContext(), "请长按删除", Toast.LENGTH_LONG);
                break;
            case R.id.timer_btn_more:
                ToastUtils.showToast(getApplicationContext(), "别乱点，这功能没用", Toast.LENGTH_LONG);
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
