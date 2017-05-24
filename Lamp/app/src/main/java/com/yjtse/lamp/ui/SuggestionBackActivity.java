package com.yjtse.lamp.ui;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.yjtse.lamp.R;
import com.yjtse.lamp.base.BaseActivity;
import com.yjtse.lamp.contentview.ContentWidget;
import com.yjtse.lamp.utils.InjectUtils;
import com.yjtse.lamp.utils.ToastUtils;
import com.yjtse.lamp.widgets.CenterTitleActionBar;

public class SuggestionBackActivity extends BaseActivity {

    CenterTitleActionBar actionBar;
    @ContentWidget(R.id.feedback_content)
    private EditText et_feedback_content;
    @ContentWidget(R.id.feedback_submit)
    private Button feedback_submit;
    private Dialog dialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggestion_back);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        InjectUtils.injectObject(this,this);

        actionBar = new CenterTitleActionBar(SuggestionBackActivity.this, getActionBar());
        actionBar.setTitle("意见反馈");
        actionBar.setCustomActionBar();
        actionBar.setOnClickActionBarListener(new CenterTitleActionBar.OnClickActionBarListener() {
            @Override
            public void onBackBtnClick() {
                SuggestionBackActivity.this.finish();
            }
            @Override
            public void onTitleClick() {            }
            @Override
            public void onFirstBtnClick() {            }
            @Override
            public void onSecondBtnClick() {            }
        });

        feedback_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = et_feedback_content.getText().toString().trim();
                if(TextUtils.isEmpty(message)){
                    ToastUtils.showToast(SuggestionBackActivity.this,"您未输入任何意见或者建议", Toast.LENGTH_SHORT);
                    return;
                }
                startDialog("正在提交数据");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(2000);
                            endDialog();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ToastUtils.showToast(SuggestionBackActivity.this,"反馈成功，我们将第一时间处理您的意见", Toast.LENGTH_LONG);
                                    et_feedback_content.setText("");
                                }
                            });
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });

    }

    private void startDialog(String msg) {
        dialog = new Dialog(SuggestionBackActivity.this, R.style.MyDialogStyle);
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
