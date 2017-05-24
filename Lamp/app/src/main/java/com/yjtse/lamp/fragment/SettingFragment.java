package com.yjtse.lamp.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.zxing.client.android.CaptureActivity;
import com.yjtse.lamp.Config;
import com.yjtse.lamp.R;
import com.yjtse.lamp.ui.TabFragmentActivity;

import org.json.JSONArray;
import org.json.JSONException;

import static android.app.Activity.RESULT_OK;

public class SettingFragment extends BaseFragment {


    private TextView gateway_list_information;
    private TabFragmentActivity tabActivity;
    public static SettingFragment instance = null;
    private Dialog dialog = null;
    private boolean isRefreshing = false;
    private boolean isPrepared;
    private boolean isFirstLoading = true;  //是否是第一次加载

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_device_message, container, false);
        return view;
    }

    @Override
    protected void setlazyLoad() {
        super.setlazyLoad();
        if (!isPrepared || !isVisible) {
            return;
        }
        //此处添加数据代码
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        tabActivity = (TabFragmentActivity) getActivity();
        instance = this;


        tabActivity.setActionBarSettingOnClickListener(new TabFragmentActivity.ActionBarSettingOnClickListener() {
            @Override
            public void onMySettingFirstBtnClick() {
                Intent openCameraIntent = new Intent(getActivity(), CaptureActivity.class);
                startActivityForResult(openCameraIntent, 0);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isFirstLoading = true;
    }

    /// /////////////加载数据的弹出框，吧之前的activity改成现在的样子///////////////////////
    private void startDialog(String msg) {
        dialog = new Dialog(getActivity(), R.style.MyDialogStyle);
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
            isRefreshing = false;
        }
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 0) {
            Bundle bundle = data.getExtras();
            String scanResult = bundle.getString("result");
            Message msg = Message.obtain();
            msg.what = Config.MESSAGE_WHAT_ADD_DEVICE;
            msg.obj = scanResult;
        }
    }

    private boolean checkInsideIsOK(JSONArray array1) {
        for (int i = 0; i < array1.length(); i++) {
            try {
                if (array1.getJSONObject(i).getInt("NUM") == 0) {
                    byte mode = (byte) array1.getJSONObject(i).getInt("S1");
                    if ((mode & 0x80) == 0x80) {
                        return true;
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

}
