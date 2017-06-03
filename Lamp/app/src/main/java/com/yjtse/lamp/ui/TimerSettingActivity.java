package com.yjtse.lamp.ui;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;
import com.yjtse.lamp.Config;
import com.yjtse.lamp.R;
import com.yjtse.lamp.asynchttp.TextNetWorkCallBack;
import com.yjtse.lamp.contentview.ContentWidget;
import com.yjtse.lamp.domain.Result;
import com.yjtse.lamp.requests.AsyncRequest;
import com.yjtse.lamp.utils.InjectUtils;
import com.yjtse.lamp.utils.NetAvailable;
import com.yjtse.lamp.utils.SharedPreferencesUtil;
import com.yjtse.lamp.utils.ToastUtils;
import com.yjtse.lamp.widgets.CenterTitleActionBar;

import org.apache.http.Header;

import java.util.Calendar;

public class TimerSettingActivity extends Activity implements View.OnClickListener {


    /**
     * 控件
     */
    @ContentWidget(R.id.timer_setting_picker)
    private TimePicker time_picker;

    @ContentWidget(R.id.timer_frequence_cb)
    private CheckBox frequence_cb;

    @ContentWidget(R.id.timer_status_tobe_cb)
    private CheckBox status_tobe_cb;

    @ContentWidget(R.id.timer_setting_btn_cancel)
    private Button timer_setting_btn_cancel;

    @ContentWidget(R.id.timer_setting_btn_confirm)
    private Button timer_setting_btn_confirm;

    private Dialog dialog = null;
    /**
     * 变量
     */
    private Integer id = 0;
    private String socketId = "";
    private String ownerId = "";
    private String statusTobe = "";
    private String cron = "";
    private String availale = "";
    private Integer flag = 0;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            RequestParams params = getParamsFromUI();
            switch (msg.what) {
                case Config.MESSAGE_WHAT_UPDATE_TIMER:
                    if (params != null) {
                        startDialog("uploading...");
                        params.put("id", id);
                        requestUpdateTimer(Config.getRequestURL(Config.ACTION_CRON_UPDATE), params);
                    }
                    break;
                case Config.MESSAGE_WHAT_ADD_TIMER:
                    //肯定有网
                    if (params != null) {
                        startDialog("uploading...");
                        requestAddTimer(Config.getRequestURL(Config.ACTION_CRON_ADD), params);
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer_setting);
        /*        获取传过来的数据         */
        Bundle bundle = this.getIntent().getExtras();
        id = bundle.getInt("id");
        socketId = bundle.getString("socketId");
        ownerId = (String) SharedPreferencesUtil.query(getApplicationContext(), Config.KEY_USERNAME, "String");
        statusTobe = bundle.getString("statusTobe");
        cron = bundle.getString("cron");
        availale = bundle.getString("available");
        flag = bundle.getInt("flag");
        /*
        初始界面
         */
        InjectUtils.injectObject(this, this);
        CenterTitleActionBar actionBar = new CenterTitleActionBar(this, getActionBar());
        actionBar.setTitle(flag == Config.MESSAGE_WHAT_ADD_TIMER ? "添加任务" : "修改任务");
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
                TimerSettingActivity.this.finish();
            }
        });
//        timePicker.setIs24HourView(true);
        if (!TextUtils.isEmpty(cron)
                && flag == Config.MESSAGE_WHAT_UPDATE_TIMER) {
            initUI();
        }
        /**
         *监听事件
         */
        timer_setting_btn_cancel.setOnClickListener(this);
        timer_setting_btn_confirm.setOnClickListener(this);
        frequence_cb.setOnCheckedChangeListener(frequenceListener);
        status_tobe_cb.setOnCheckedChangeListener(statusTobeListener);
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void initUI() {
        /*
        设置time picker
         */
        String[] strings = cron.split("\\s+");
        String second = strings[0];
        String minute = strings[1];
        String hour = strings[2];
        String day = strings[3];
        String month = strings[4];
        String week = strings[5];
        String year = strings[6];
        if (!TextUtils.isEmpty(cron)) {
            if (time_picker.is24HourView()) {
                time_picker.setHour(Integer.parseInt(strings[2]));
            } else {
                time_picker.setIs24HourView(true);
                time_picker.setCurrentHour(Integer.valueOf(strings[2]));
                time_picker.setIs24HourView(false);
            }
            time_picker.setCurrentMinute(Integer.parseInt(strings[1]));
        }
//        frequence_cb.setText(("*".equals(strings[3]) && "*".equals(strings[4])) ? "每天重复" : "不重复");
        frequence_cb.setChecked(("*".equals(month) && "*".equals(day) && "?".equals(week)));
        frequence_cb.setText(frequence_cb.isChecked() ? "每天重复" : "不重复");
        frequence_cb.setTextColor(frequence_cb.isChecked() ? this.getResources().getColor(R.color.theme_color)
                : this.getResources().getColor(R.color.gray));
        status_tobe_cb.setChecked("1".equals(statusTobe));
        status_tobe_cb.setText(status_tobe_cb.isChecked() ? "已设置为届时打开" : "已设置为届时关闭");
        status_tobe_cb.setTextColor(status_tobe_cb.isChecked() ? this.getResources().getColor(R.color.theme_color)
                : this.getResources().getColor(R.color.gray));
    }

    CheckBox.OnCheckedChangeListener frequenceListener = (buttonView, isChecked) -> {
        frequence_cb.setText(isChecked ? "每天重复" : "不重复");
        frequence_cb.setTextColor(isChecked ? this.getResources().getColor(R.color.theme_color) : this.getResources().getColor(R.color.gray));
    };
    CheckBox.OnCheckedChangeListener statusTobeListener = (buttonView, isChecked) -> {
        status_tobe_cb.setText(isChecked ? "已设置为届时打开" : "已设置为届时关闭");
        status_tobe_cb.setTextColor(isChecked ? this.getResources().getColor(R.color.theme_color) : this.getResources().getColor(R.color.gray));
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.timer_setting_btn_cancel:
                TimerSettingActivity.this.finish();
                break;
            case R.id.timer_setting_btn_confirm:
                if (flag == Config.MESSAGE_WHAT_ADD_TIMER) {
                    Message msg = Message.obtain();
                    msg.what = Config.MESSAGE_WHAT_ADD_TIMER;
                    handler.sendMessage(msg);
                    break;
                }
                if (flag == Config.MESSAGE_WHAT_UPDATE_TIMER) {
                    Message msg = Message.obtain();
                    msg.what = Config.MESSAGE_WHAT_UPDATE_TIMER;
                    handler.sendMessage(msg);
                    break;
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
                builder.setTitle("Notice");
                builder.setMessage("不知道你怎么进来的，请重新进来一次：");
                builder.setPositiveButton("确定", null);
                builder.create().show();
                break;
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
                            TimerSettingActivity.this.finish();
                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
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

    private void requestAddTimer(String url, RequestParams params) {
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
                            TimerSettingActivity.this.finish();
                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
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

    private RequestParams getParamsFromUI() {
        Calendar calendar = Calendar.getInstance();
        time_picker.setIs24HourView(true);
        String second = "00 ";
        String minute = String.valueOf(time_picker.getCurrentMinute()) + " ";
        String hour = String.valueOf(time_picker.getCurrentHour()) + " ";
        String day = "";
        String month = "";
        String week = "?" + " ";
        String year = String.valueOf(calendar.get(Calendar.YEAR));
        RequestParams params = new RequestParams();
        params.add("socketId", socketId);
        params.add("ownerId", ownerId);
        params.add("statusTobe", status_tobe_cb.isChecked() ? "1" : "0");
        if (frequence_cb.isChecked()) {
            month = "*" + " ";
            day = "*" + " ";
        } else {
            month = calendar.get(Calendar.MONTH) + 1 + " ";//本月
            if (time_picker.getCurrentHour() < calendar.getTime().getHours()) {
                //设定的时间小于当前时间
                day = calendar.get(Calendar.DAY_OF_MONTH) + 1 + " ";//明天
            }
            if (time_picker.getCurrentHour() > calendar.getTime().getHours()) {
                //设定的时间大于当前时间
                day = calendar.get(Calendar.DAY_OF_MONTH) + " ";//今天
            }
            if (time_picker.getCurrentHour() == calendar.getTime().getHours()) {
                if (time_picker.getCurrentMinute() > calendar.get(Calendar.MINUTE)) {
                    //设定的分钟时间大于当前分钟
                    day = calendar.get(Calendar.DAY_OF_MONTH) + " ";//今天
                } else {
                    day = calendar.get(Calendar.DAY_OF_MONTH) + 1 + " ";//明天
                }
            }
        }
        if (time_picker.getCurrentHour() == calendar.getTime().getHours()
                && time_picker.getCurrentMinute() > calendar.get(Calendar.MINUTE)
                && time_picker.getCurrentMinute() - calendar.get(Calendar.MINUTE) < 2) {
            AlertDialog.Builder builder = new AlertDialog.Builder(TimerSettingActivity.this);
            builder.setTitle("Notice");
            builder.setMessage("请至少设置在2分钟之后");
            builder.setPositiveButton("确定", null);
            builder.create().show();
            time_picker.setIs24HourView(false);
            return null;
        }
        time_picker.setIs24HourView(false);
        params.add("cron", second + minute + hour + day + month + week + year);
        params.add("available", "0");
        return params;
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
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }
}
