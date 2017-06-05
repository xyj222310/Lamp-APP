package com.yjtse.lamp.ui;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
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
        InjectUtils.injectObject(this, this);

        actionBar = new CenterTitleActionBar(SuggestionBackActivity.this, getActionBar());
        actionBar.setTitle("联系我们");
        actionBar.setCustomActionBar();
        actionBar.setOnClickActionBarListener(new CenterTitleActionBar.OnClickActionBarListener() {
            @Override
            public void onBackBtnClick() {
                SuggestionBackActivity.this.finish();
            }

            @Override
            public void onTitleClick() {
            }

            @Override
            public void onFirstBtnClick() {
            }

            @Override
            public void onSecondBtnClick() {
            }
        });

        feedback_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastUtils.showToast(getApplicationContext(), "back to the back", Toast.LENGTH_LONG);
                SuggestionBackActivity.this.finish();
            }
        });

    }
}
