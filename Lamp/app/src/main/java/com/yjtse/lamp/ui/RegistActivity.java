package com.yjtse.lamp.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
import com.yjtse.lamp.utils.ToastUtils;
import com.yjtse.lamp.widgets.CenterTitleActionBar;

import org.apache.http.Header;

import java.lang.reflect.TypeVariable;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.RequestSMSCodeListener;
import cn.bmob.v3.listener.VerifySMSCodeListener;


public class RegistActivity extends BaseActivity implements OnClickListener {

    @ContentWidget(R.id.et_regist_input_number)
    private EditText et_regist_input_number;
    @ContentWidget(R.id.et_regist_input_password)
    private EditText et_regist_input_password;
    @ContentWidget(R.id.et_regist_input_identifying_code)
    private EditText et_regist_input_identifying_code;
    @ContentWidget(R.id.tv_get_identifying_code)
    private TextView tv_get_identifying_code;
    @ContentWidget(R.id.btn_regist_confirm)
    private Button btn_regist_confirm;
    @ContentWidget(R.id.hint_regist_string)
    private TextView hint_regist_string;
    @ContentWidget(R.id.et_regist_input_id)
    private TextView et_regist_input_id;

    private Dialog dialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        InjectUtils.injectObject(this, this);
        CenterTitleActionBar actionBar = new CenterTitleActionBar(this, getActionBar());
        actionBar.setTitle("注册");
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
                RegistActivity.this.finish();
            }
        });
        initBmob();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Alert");
        builder.setMessage("您可以\n1.不设置手机号\n2.设置手机号作为登录名");
        builder.setPositiveButton("明白", null);
        builder.create().show();
        tv_get_identifying_code.setOnClickListener(this);
        btn_regist_confirm.setOnClickListener(this);
        et_regist_input_number.addTextChangedListener(new EditTextTextWatcher());
        et_regist_input_id.addTextChangedListener(new EditTextTextWatcher());
        et_regist_input_password.addTextChangedListener(new EditTextTextWatcher());
        //   mObserver = new SmsObserver(this, handler);
        //   Uri uri = Uri.parse("content://sms");
        //   getContentResolver().registerContentObserver(uri, true, mObserver);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_get_identifying_code:
                final String userId1 = et_regist_input_id.getText().toString().trim();
                final String phoneNumber1 = et_regist_input_number.getText().toString().trim();
                final String userPass1 = et_regist_input_password.getText().toString().trim();
                if (TextUtils.isEmpty(phoneNumber1)) {
                    hint_regist_string.setText("手机号码不能为空");
                    return;
                }
                if (!phoneNumber1.substring(0, 1).equals("1")) {
                    hint_regist_string.setText("手机号码格式不对");
                    return;
                }
                if (phoneNumber1.length() != 11) {
                    hint_regist_string.setText("手机号码格式不对");
                    return;
                }
                tv_get_identifying_code.setClickable(false);
                RequestParams requestParams = new RequestParams();
                requestParams.add(Config.KEY_USERNAME, phoneNumber1);
                requestParams.add("phone", phoneNumber1);
                request(phoneNumber1);
                break;
            case R.id.btn_regist_confirm:
                final String phoneNumber2 = et_regist_input_number.getText().toString().trim();
                final String userId2 = et_regist_input_id.getText().toString().trim();
                final String userPass2 = et_regist_input_password.getText().toString().trim();
                final String identifying_code = et_regist_input_identifying_code.getText().toString().trim();//所获取的验证码
                if (TextUtils.isEmpty(userPass2)) {
                    hint_regist_string.setText("密码不能为空");
                    return;
                }
                if (userPass2.length() < 6) {
                    hint_regist_string.setText("密码长度必须大于6位");
                    return;
                }
                if (!TextUtils.isEmpty(phoneNumber2) || !TextUtils.isEmpty(identifying_code)) {
                    if (TextUtils.isEmpty(phoneNumber2)) {
                        hint_regist_string.setText("手机号码不能为空");
                        return;
                    }
                    if (!phoneNumber2.substring(0, 1).equals("1") || phoneNumber2.length() != 11) {
                        hint_regist_string.setText("手机号码格式不对");
                        return;
                    }
                    if (identifying_code.length() != 6) {
                        hint_regist_string.setText("验证码格式错误");
                        return;
                    }
                    startDialog("注册中");
//                    verify(phoneNumber2, userPass2, identifying_code);
                    RequestParams params = new RequestParams();
                    params.add(Config.KEY_USERNAME, phoneNumber2);
                    params.add("phone", phoneNumber2);
                    params.add(Config.KEY_PASSWORD, userPass2);
                    requestRegister(params, phoneNumber2, userPass2);
                } else {
                    if (TextUtils.isEmpty(userId2)) {
                        hint_regist_string.setText("请设置登录名");
                        return;
                    }
                    startDialog("注册中");
                    RequestParams params = new RequestParams();
                    params.add(Config.KEY_USERNAME, userId2);
                    params.add(Config.KEY_PASSWORD, userPass2);
                    requestRegister(params, userId2, userPass2);
                }
                break;
            default:
                break;
        }
    }

    public void verify(final String phone, final String userPass, String identifying_code) {
        BmobSMS.verifySmsCode(this, phone, identifying_code, new VerifySMSCodeListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) { //验证成功
                    RequestParams params = new RequestParams();
                    params.add(Config.KEY_USERNAME, phone);
                    params.add("phone", phone);
                    params.add(Config.KEY_PASSWORD, userPass);
                    requestRegister(params, phone, userPass);
                } else {//验证失败
                    hint_regist_string.setText("验证码有误,或网络连接有问题！！！");
                    endDialog();
                }
            }
        });
    }

    public void requestRegister(RequestParams params, final String userId, final String userPass) {
        AsyncRequest.ClientPost(Config.getRequestURL(Config.ACTION_ADD_USER), params, new TextNetWorkCallBack() {
            @Override
            public void onMySuccess(int statusCode, Header[] header, String result) {
                //检测返回结果
                Result res = new Gson().fromJson(result, Result.class);
                Message msg = Message.obtain();
                if (res.isSuccess()) { //成功注册
                    Bundle bundle = new Bundle();
                    bundle.putString(Config.KEY_USERNAME, userId);
                    bundle.putString(Config.KEY_PASSWORD, userPass);
                    ToastUtils.showToast(getApplicationContext(), "注册成功！", Toast.LENGTH_LONG);
                    endDialog();
                    launchActivity(RegistActivity.this, LoginActivity.class, bundle);
                    RegistActivity.this.finish();
                } else {  //注册失败
                    hint_regist_string.setText("注册失败" + res.getError());
                    endDialog();
                }
            }

            @Override
            public void onMyFailure(int statusCode, Header[] header, String result, Throwable th) {
                hint_regist_string.setText("网络访问错误，请检查网络是否联网");
                endDialog();
            }
        });
    }

    public void request(final String phoneNumber) {
        BmobSMS.requestSMSCode(this, phoneNumber, "lamp", new RequestSMSCodeListener() {
            @Override
            public void done(Integer integer, BmobException e) {
                if (e == null) {
                    //发送成功时，让获取验证码按钮不可点击，且为灰色
                    Toast.makeText(RegistActivity.this, "发送验证码成功", Toast.LENGTH_SHORT).show();
                    /**
                     * 倒计时1分钟操作
                     * 说明：
                     * new CountDownTimer(60000, 1000),第一个参数为倒计时总时间，第二个参数为倒计时的间隔时间
                     * 单位都为ms，其中必须要实现onTick()和onFinish()两个方法，onTick()方法为当倒计时在进行中时，
                     * 所做的操作，它的参数millisUntilFinished为距离倒计时结束时的时间，以此题为例，总倒计时长
                     * 为60000ms,倒计时的间隔时间为1000ms，然后59000ms、58000ms、57000ms...该方法的参数
                     * millisUntilFinished就等于这些每秒变化的数，然后除以1000，把单位变成秒，显示在textView
                     * 或Button上，则实现倒计时的效果，onFinish()方法为倒计时结束时要做的操作，此题可以很好的
                     * 说明该方法的用法，最后要注意的是当new CountDownTimer(60000, 1000)之后，一定要调用start()
                     * 方法把该倒计时操作启动起来，不调用start()方法的话，是不会进行倒计时操作的
                     */
                    new CountDownTimer(60000, 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            tv_get_identifying_code.setText("(已发送)" + millisUntilFinished / 1000 + "秒");
                        }

                        @Override
                        public void onFinish() {
                            tv_get_identifying_code.setClickable(true);
                            tv_get_identifying_code.setText("重新发送");
                        }
                    }.start();
                } else {
                    tv_get_identifying_code.setClickable(true);
                    hint_regist_string.setText("验证码发送失败"+e);
                }
            }
        });
    }

    public void initBmob() {
        Bmob.initialize(this, Config.BmobSMSToken);//初始化SDK
    }

    private class EditTextTextWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            hint_regist_string.setText("");
        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
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
