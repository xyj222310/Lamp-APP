package com.yjtse.lamp.ui;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;
import com.yjtse.lamp.Config;
import com.yjtse.lamp.R;
import com.yjtse.lamp.asynchttp.TextNetWorkCallBack;
import com.yjtse.lamp.base.BaseActivity;
import com.yjtse.lamp.contentview.ContentWidget;
import com.yjtse.lamp.domain.Result;
import com.yjtse.lamp.requests.AsyncRequest;
import com.yjtse.lamp.utils.InjectUtils;
import com.yjtse.lamp.utils.NetAvailable;
import com.yjtse.lamp.utils.PackageUtils;
import com.yjtse.lamp.utils.SharedPreferencesUtil;
import com.yjtse.lamp.widgets.ClearEditText;

import org.apache.http.Header;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    /**
     关联组件*/
    /**
     * 用户输入手机号框
     */
    @ContentWidget(R.id.et_login_input_number)
    private ClearEditText et_login_input_number;
    /**
     * 用户输入密码框
     */
    @ContentWidget(R.id.et_login_input_password)
    private ClearEditText et_login_input_password;
    /**
     * 用户记住密码复选框
     */
    @ContentWidget(R.id.remenber_password)
    private CheckBox remenber_password;
    /**
     * 用户远程登录按钮
     */
    @ContentWidget(R.id.btn_remote_login)
    private Button btn_remote_login;
    /**
     * 用户忘记密码按钮
     */
    @ContentWidget(R.id.tv_forget_password)
    private TextView tv_forget_password;
    /**
     * 用户注册账号按钮
     */
    @ContentWidget(R.id.tv_regist_account)
    private TextView tv_regist_account;
    /**
     * 错误提示
     */
    @ContentWidget(R.id.hint_string)
    private TextView hint_string;
    @ContentWidget(R.id.tv_version)
    private TextView tv_version;
    /**
     * 是否记住密码
     * 默认为记住密码
     */
    private boolean isRememberPassword = false;

    private ProgressDialog pd = null;

    private Dialog dialog = null;

    public LoginActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(android.R.style.Theme_Light_NoTitleBar);
        setContentView(R.layout.activity_login);
        InjectUtils.injectObject(this, this);
        btn_remote_login.setOnClickListener(this);
        tv_forget_password.setOnClickListener(this);
        tv_regist_account.setOnClickListener(this);
        //接收从注册页面传回的数据，用于在输入框中显示出来，给用户减少输入
        dealBundle(getIntent().getExtras());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        dealBundle(intent.getExtras());
    }

    private void dealBundle(Bundle bundle) {
        if (bundle != null) {
            String phoneNum = bundle.getString(Config.KEY_USERNAME);
            String password = bundle.getString(Config.KEY_PASSWORD);
            if (!TextUtils.isEmpty(phoneNum) && !TextUtils.isEmpty(password)) {
                et_login_input_number.setText(phoneNum);
                et_login_input_password.setText(password);
            }
            remenber_password.setChecked(true);
            isRememberPassword = true;
        } else if ((boolean) SharedPreferencesUtil.query(LoginActivity.this, Config.KEY_REMEMBER_PWD, "boolean")) {
            et_login_input_number.setText((String) SharedPreferencesUtil.query(LoginActivity.this, Config.KEY_USERNAME, "String"));
            et_login_input_password.setText((String) SharedPreferencesUtil.query(LoginActivity.this, Config.KEY_PASSWORD, "String"));
            remenber_password.setChecked(true);
            isRememberPassword = true;
        } else {
            et_login_input_number.setText((String) SharedPreferencesUtil.query(LoginActivity.this, Config.KEY_USERNAME, "String"));
            et_login_input_password.setText("");
            remenber_password.setChecked(false);
            isRememberPassword = false;
        }

        tv_version.setText(PackageUtils.getVersion(getApplicationContext()));

        et_login_input_number.addTextChangedListener(new EditTextTextWatcher());
        et_login_input_password.addTextChangedListener(new EditTextTextWatcher());

        remenber_password.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) { //选中
                    isRememberPassword = true;
                } else {  //未选中
                    isRememberPassword = false;
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_remote_login:
                final String userId = et_login_input_number.getText().toString().trim();
                final String userPass = et_login_input_password.getText().toString().trim();
                if (TextUtils.isEmpty(userId)) {
                    hint_string.setText("id不能为空");
                    return;
                }
                if (userId.length() >= 20) {
                    hint_string.setText("长度别超过20位");
                    return;
                }
                if (TextUtils.isEmpty(userPass)) {
                    hint_string.setText("密码不能为空");
                    return;
                }
                if (userPass.length() < 6) {
                    hint_string.setText("密码长度必须大于6位");
                    return;
                }
                if (!NetAvailable.isNetworkAvailable()) {
                    hint_string.setText("设备无法联网，请打开数据流量或连接WIFI热点");
                    return;
                }

                startDialog("正在登录");
                HashMap<String, String> params = new HashMap<>();
                params.put(Config.KEY_USERNAME, userId);
                params.put(Config.KEY_PASSWORD, userPass);
                params.put(Config.KEY_MOBILE_LOGIN, "true");
                params.put("remenberMe", isRememberPassword + "");

                Config.writeToDebug("login...url " + Config.getRequestURL(Config.ACTION_LOGIN));
                Config.writeToDebug("login...params " + Config.KEY_USERNAME + "=" + userId + Config.KEY_PASSWORD + "=" + userPass + "remenberMe=" + "true");
                sendLoginPostRequest(Config.getRequestURL(Config.ACTION_LOGIN), params, System.currentTimeMillis(), userId, userPass);
                break;

            case R.id.tv_forget_password:
                launchActivity(LoginActivity.this, ForgetPasswordActivity.class);
                break;
            case R.id.tv_regist_account:
                launchActivity(LoginActivity.this, RegistActivity.class);
                break;
            default:
                break;
        }
    }

    private void sendLoginPostRequest(final String url, final Map<String, String> params, final long startTime, final String userId, final String userPass) {

        RequestParams params1 = new RequestParams();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            params1.add(entry.getKey(), entry.getValue());
        }
        AsyncRequest.ClientPost(url, params1, new TextNetWorkCallBack() {
            @Override
            public void onMySuccess(int statusCode, Header[] header, String result) {
                Gson gson = new Gson();
                final Result token = gson.fromJson(result, Result.class);
                Config.writeToDebug(result);
                final long endTime = System.currentTimeMillis();
                if (token != null) {
                    if (token.isSuccess()) {
                        SharedPreferencesUtil.save(LoginActivity.this, Config.KEY_PASSWORD, userPass);
                        SharedPreferencesUtil.save(LoginActivity.this, Config.KEY_REMEMBER_PWD, isRememberPassword);
                        SharedPreferencesUtil.save(LoginActivity.this, Config.KEY_USERNAME, userId);

                        launchActivity(LoginActivity.this, TabFragmentActivity.class);
                        LoginActivity.this.finish();
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                hint_string.setText(token.getError());
                                endDialog();
                            }
                        });
                    }

                } else {
//                            msg.what = Config.MESSAGE_WHAT_HTTP_LOGIN_FAIL;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            hint_string.setText("服务器返回异常，请稍后再试");
                            endDialog();
                        }
                    });
                }
                if (endTime - startTime <= 2000) { //小于两秒
                    try {
                        Thread.sleep(2000 - (endTime - startTime));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onMyFailure(int statusCode, Header[] header, String result, Throwable th) {
                endDialog();
            }
        });

    }


    private void startDialog(String msg) {
        dialog = new Dialog(LoginActivity.this, R.style.MyDialogStyle);
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

    private class EditTextTextWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            hint_string.setText("");
        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    }

}
