package com.yjtse.lamp.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;
import com.yjtse.lamp.Config;
import com.yjtse.lamp.R;
import com.yjtse.lamp.asynchttp.TextNetWorkCallBack;
import com.yjtse.lamp.base.BaseActivity;
import com.yjtse.lamp.contentview.ContentWidget;
import com.yjtse.lamp.requests.AsyncRequest;
import com.yjtse.lamp.utils.InjectUtils;
import com.yjtse.lamp.utils.SharedPreferencesUtil;
import com.yjtse.lamp.utils.ToastUtils;
import com.yjtse.lamp.widgets.CenterTitleActionBar;
import com.yjtse.lamp.widgets.CenterTitleActionBar.OnClickActionBarListener;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

public class ReplacePasswordActivity extends BaseActivity {

    @ContentWidget(R.id.et_new_password_input_number)
    private EditText et_new_password_input_number;
    @ContentWidget(R.id.et_new_password_input_number_confirm)
    private EditText et_new_password_input_number_confirm;
    @ContentWidget(R.id.btn_new_password_confirm)
    private Button btn_new_password_confirm;

    private String phoneNum;
    private String newPassword;
    private String newPasswordConform;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_replace_password);
        InjectUtils.injectObject(this, this);
        CenterTitleActionBar actionBar = new CenterTitleActionBar(this, getActionBar());
        actionBar.setTitle("重置密码");
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
                ReplacePasswordActivity.this.finish();
            }
        });
        Bundle bundle = getIntent().getExtras();
        phoneNum = bundle.getString(Config.KEY_USERNAME);

        btn_new_password_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newPassword = et_new_password_input_number.getText().toString().trim();
                newPasswordConform = et_new_password_input_number_confirm.getText().toString().trim();

                if (TextUtils.isEmpty(newPassword)) {
                    ToastUtils.showToast(ReplacePasswordActivity.this, getResources().getString(R.string.passwordCanNotNone), 1);
                    return;
                }
                if (!TextUtils.equals(newPassword, newPasswordConform)) {
                    ToastUtils.showToast(ReplacePasswordActivity.this, getResources().getString(R.string.passwordMustBeSame), 1);
                    return;
                }
                RequestParams params = new RequestParams();
                params.add("userId", phoneNum);
                params.add("newPassword", newPassword);
                String url = Config.getRequestURL(Config.ACTION_REPLACE);
                AsyncRequest.ClientPost(url, params, new TextNetWorkCallBack() {
                    @Override
                    public void onMySuccess(int statusCode, Header[] header, String result) {
                        try {
                            JSONObject object = new JSONObject(result);
                            switch (Integer.valueOf(object.getString(Config.KEY_STATUS))) {
                                case Config.RESULT_STATUS_SUCCESS:
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            ToastUtils.showToast(ReplacePasswordActivity.this, "修改成功", Toast.LENGTH_SHORT);
                                        }
                                    });
                                    SharedPreferencesUtil.save(ReplacePasswordActivity.this, Config.KEY_TOKEN, null);
                                    SharedPreferencesUtil.save(ReplacePasswordActivity.this, Config.KEY_USERNAME, phoneNum);
                                    SharedPreferencesUtil.save(ReplacePasswordActivity.this, Config.KEY_PASSWORD, newPassword);
                                    Bundle bundle1 = new Bundle();
                                    bundle1.putString(Config.KEY_USERNAME, phoneNum);
                                    bundle1.putString(Config.KEY_PASSWORD, newPassword);
                                    launchActivity(ReplacePasswordActivity.this, LoginActivity.class, bundle1);
                                    ReplacePasswordActivity.this.finish();
                                    break;
                                case Config.RESULT_STATUS_FAIL:
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            ToastUtils.showToast(ReplacePasswordActivity.this, "修改失败", Toast.LENGTH_SHORT);
                                        }
                                    });
                                    break;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onMyFailure(int statusCode, Header[] header, String result, Throwable th) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtils.showToast(ReplacePasswordActivity.this, "当前无网络", Toast.LENGTH_SHORT);
                            }
                        });
                    }
                });

            }
        });
    }


}
