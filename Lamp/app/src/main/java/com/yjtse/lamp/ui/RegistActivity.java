package com.yjtse.lamp.ui;

import android.os.Bundle;
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

import com.loopj.android.http.RequestParams;
import com.yjtse.lamp.Config;
import com.yjtse.lamp.R;
import com.yjtse.lamp.asynchttp.TextNetWorkCallBack;
import com.yjtse.lamp.base.BaseActivity;
import com.yjtse.lamp.contentview.ContentWidget;
import com.yjtse.lamp.parser.JsonParser;
import com.yjtse.lamp.requests.AsyncRequest;
import com.yjtse.lamp.utils.InjectUtils;
import com.yjtse.lamp.widgets.CenterTitleActionBar;

import org.apache.http.Header;


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
    /**
     * 短信内容观察者
     */
    //private SmsObserver mObserver;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            //这里判断从短息观察者那里发送过来的消息，更新UI
            switch (msg.what) {
                case Config.MESSAGE_WHAT_SMS_OBSERVER_REGIST:
                    String code = (String) msg.obj;
                    if (!TextUtils.isEmpty(code)) {
                        et_regist_input_identifying_code.setText(code);
                    }
                    break;
                case Config.MESSAGE_WHAT_HTTP_FIND_USERNAME_EXIST:
                    hint_regist_string.setText("用户已存在，无需多次注册\n您可以点击忘记密码重新找回密码");
                    break;
                case Config.MESSAGE_WHAT_HTTP_FIND_USERNAME_NOT_EXIST:
                    String number = (String) msg.obj;
                    if (!TextUtils.isEmpty(number) && number.length() == 11) {
                        request(number);
                    } else {
                        hint_regist_string.setText("手机号码输入错误");
                    }
                    break;
                case Config.MESSAGE_WHAT_HTTP_REGISTER_SUCCESS:
                    launchActivity(RegistActivity.this, LoginActivity.class, bundle1);
                    RegistActivity.this.finish();
                    break;
                case Config.MESSAGE_WHAT_HTTP_REGISTER_FAIL:
                    hint_regist_string.setText("注册失败，请稍后再试");
                    break;
                default:
                    break;
            }
        }

    };

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

        tv_get_identifying_code.setOnClickListener(this);
        btn_regist_confirm.setOnClickListener(this);
        et_regist_input_number.addTextChangedListener(new EditTextTextWatcher());
        et_regist_input_password.addTextChangedListener(new EditTextTextWatcher());
        //   mObserver = new SmsObserver(this, handler);
        //   Uri uri = Uri.parse("content://sms");
        //   getContentResolver().registerContentObserver(uri, true, mObserver);

    }


    @Override
    protected void onPause() {
        super.onPause();
        //   getContentResolver().unregisterContentObserver(mObserver);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_get_identifying_code:
                final String phonenum = et_regist_input_number.getText().toString().trim();
                final String pwd = et_regist_input_password.getText().toString().trim();

                if (TextUtils.isEmpty(phonenum)) {
                    hint_regist_string.setText("手机号码不能为空");
                    return;
                }
                if (!phonenum.substring(0, 1).equals("1")) {
                    hint_regist_string.setText("手机号码不符合大陆规范");
                    return;
                }
                if (phonenum.length() != 11) {
                    hint_regist_string.setText("手机号码长度不符合大陆规范");
                    return;
                }
                if (TextUtils.isEmpty(pwd)) {
                    hint_regist_string.setText("密码不能为空");
                    return;
                }
                if (pwd.length() < 6) {
                    hint_regist_string.setText("密码长度必须大于6位");
                    return;
                }
                RequestParams params = new RequestParams();
                params.add(Config.KEY_USERNAME, phonenum);
                tv_get_identifying_code.setClickable(false);
                AsyncRequest.ClientPost(Config.getRequestURL(Config.ACTION_FIND), params, new TextNetWorkCallBack() {
                    @Override
                    public void onMySuccess(int statusCode, Header[] header, String result) {
                        boolean res = JsonParser.parseUsernameIsExist(result);
                        Message msg = Message.obtain();
                        if (!res) {  //不存在该用户
                            msg.what = Config.MESSAGE_WHAT_HTTP_FIND_USERNAME_NOT_EXIST;
                            msg.obj = phonenum;
                        } else {
                            msg.what = Config.MESSAGE_WHAT_HTTP_FIND_USERNAME_EXIST;
                        }
                        tv_get_identifying_code.setClickable(true);
                        handler.sendMessage(msg);
                    }

                    @Override
                    public void onMyFailure(int statusCode, Header[] header, String result, Throwable th) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                hint_regist_string.setText("网络访问错误，请检查网络是否联网");
                                tv_get_identifying_code.setClickable(true);
                            }
                        });
                    }
                });
                break;
            case R.id.btn_regist_confirm:
                final String phoneNumber = et_regist_input_number.getText().toString().trim();
                final String password = et_regist_input_password.getText().toString().trim();
                final String identifying_code = et_regist_input_identifying_code.getText().toString().trim();//所获取的验证码

                if (TextUtils.isEmpty(phoneNumber)) {
                    hint_regist_string.setText("手机号码不能为空");
                    return;
                }
                if (!phoneNumber.substring(0, 1).equals("1")) {
                    hint_regist_string.setText("手机号码不符合大陆规范");
                    return;
                }
                if (phoneNumber.length() != 11) {
                    hint_regist_string.setText("手机号码长度不符合大陆规范");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    hint_regist_string.setText("密码不能为空");
                    return;
                }
                if (password.length() < 6) {
                    hint_regist_string.setText("密码长度必须大于6位");
                    return;
                }
                if (identifying_code.length() != 6) {
                    hint_regist_string.setText("验证码格式错误");
                    return;
                }
                break;
            default:
                break;
        }
    }

    public void request(final String phoneNumber) {
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

}
