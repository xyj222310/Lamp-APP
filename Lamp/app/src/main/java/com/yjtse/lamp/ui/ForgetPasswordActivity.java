package com.yjtse.lamp.ui;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.yjtse.lamp.R;
import com.yjtse.lamp.base.BaseActivity;
import com.yjtse.lamp.utils.InjectUtils;
import com.yjtse.lamp.widgets.CenterTitleActionBar;
import com.yjtse.lamp.widgets.CenterTitleActionBar.OnClickActionBarListener;


public class ForgetPasswordActivity extends BaseActivity implements OnClickListener {

//    @ContentWidget(R.id.et_forget_input_number)
//    private EditText et_forget_input_number;
//    @ContentWidget(R.id.et_forget_input_identifying_code)
//    private EditText et_forget_input_identifying_code;
//    @ContentWidget(R.id.tv_forget_get_identifying_code)
//    private TextView tv_forget_get_identifying_code;
//    @ContentWidget(R.id.btn_forget_confirm)
//    private Button btn_forget_confirm;
//    @ContentWidget(R.id.hint_forget_string)
//    private TextView hint_forget_string;

    private String phoneNumber = "";
    private String IdentifyCode = "";

    /**
     * 短信内容观察者
     */
    // private SmsObserver mObserver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_forget_password);
        InjectUtils.injectObject(this, this);

        CenterTitleActionBar actionBar = new CenterTitleActionBar(this, getActionBar());
        actionBar.setTitle("忘记密码");
        actionBar.setCustomActionBar();
        actionBar.setOnClickActionBarListener(new OnClickActionBarListener() {
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
                ForgetPasswordActivity.this.finish();
            }
        });

//        tv_forget_get_identifying_code.setOnClickListener(this);
//        btn_forget_confirm.setOnClickListener(this);

        //    mObserver = new SmsObserver(this, handler);
        //    Uri uri = Uri.parse("content://sms");
        //    getContentResolver().registerContentObserver(uri, true, mObserver);

    }

    @Override
    protected void onPause() {
        super.onPause();
        //   getContentResolver().unregisterContentObserver(mObserver);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_forget_confirm:
                /**
                 * 网络验证获得重置密码的权限
                 */
                Bundle bundle = new Bundle();
                launchActivity(ForgetPasswordActivity.this, ReplacePasswordActivity.class, bundle);
                break;
            default:
                break;
        }
    }


}
