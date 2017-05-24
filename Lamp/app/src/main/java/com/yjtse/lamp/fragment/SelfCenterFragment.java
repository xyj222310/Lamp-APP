package com.yjtse.lamp.fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yjtse.lamp.Config;
import com.yjtse.lamp.R;
import com.yjtse.lamp.ui.LoginActivity;
import com.yjtse.lamp.ui.SuggestionBackActivity;
import com.yjtse.lamp.utils.SharedPreferencesUtil;

public class SelfCenterFragment extends BaseFragment implements OnClickListener {

    /**
     * 退出或者登陆按钮
     */
    private Button user_exit;
    private ImageView self_center_user_logo;
    private TextView self_center_user_login;
    private RelativeLayout relieveDeviceLinearLayout;
    private RelativeLayout helpCenterLinearLayout;
    private RelativeLayout systemVersionLinearLayout;

    /**
     * 判断按钮显示登陆还是退出
     */
    private boolean loginOrExits;
    /**
     * token值
     */
    private String userPass = "";
    private String phoneNum = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_selfcenter, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        user_exit = (Button) getActivity().findViewById(R.id.user_exit);
        self_center_user_logo = (ImageView) getActivity().findViewById(R.id.self_center_user_logo);
        self_center_user_login = (TextView) getActivity().findViewById(R.id.self_center_user_login);
        relieveDeviceLinearLayout = (RelativeLayout) getActivity().findViewById(R.id.relieveDeviceLinearLayout);
        helpCenterLinearLayout = (RelativeLayout) getActivity().findViewById(R.id.helpCenterLinearLayout);
        systemVersionLinearLayout = (RelativeLayout) getActivity().findViewById(R.id.systemVersionLinearLayout);

        userPass = (String) SharedPreferencesUtil.query(getActivity(), Config.KEY_PASSWORD, "String");
        phoneNum = (String) SharedPreferencesUtil.query(getActivity(), Config.KEY_USERNAME, "String");
        if (TextUtils.isEmpty(phoneNum) || phoneNum == null) {
            self_center_user_login.setText(R.string.selfCenterUserNotLogin);
        } else {
            self_center_user_login.setText(phoneNum);
        }
        if (userPass != null && !TextUtils.isEmpty(userPass)) //不为空，说明已经登陆，显示退出
        {
            user_exit.setText(R.string.exit);
            loginOrExits = true;
        } else {
            self_center_user_login.setText(R.string.selfCenterUserNotLogin);
            user_exit.setText(R.string.selfCenterUserLogin);//为空，说明未登陆，显示登陆
            loginOrExits = false;
        }

        user_exit.setOnClickListener(this);
        self_center_user_logo.setOnClickListener(this);
        relieveDeviceLinearLayout.setOnClickListener(this);
        helpCenterLinearLayout.setOnClickListener(this);
        systemVersionLinearLayout.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        switch (v.getId()) {
            case R.id.user_exit:
                if (loginOrExits) //显示退出,未退出
                {
                    loginOrExits = false;
                    self_center_user_login.setText(R.string.selfCenterUserNotLogin);
                    user_exit.setText(R.string.selfCenterUserLogin);
//                    SharedPreferencesUtil.save(getActivity(), Config.KEY_USERNAME, "");
//                    SharedPreferencesUtil.save(getActivity(), Config.KEY_PASSWORD, "");
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    getActivity().finish();
                } else {          //显示登陆，已退出
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    getActivity().finish();
                }
                break;
            case R.id.self_center_user_logo:
                break;
            case R.id.helpCenterLinearLayout:

                builder.setTitle("联系我");
                builder.setMessage("xyj222310@163.com\n1568321****\n1021238535@qq.com");
                builder.setPositiveButton("确定", null);
                builder.create().show();
                break;
            case R.id.relieveDeviceLinearLayout:
                Intent intent1 = new Intent(getActivity(), SuggestionBackActivity.class);
                startActivity(intent1);
                break;
            case R.id.systemVersionLinearLayout:
                builder.setTitle("联系我");
                builder.setMessage("xyj222310@163.com\n1568321****\n1021238535@qq.com");
                builder.setPositiveButton("确定", null);
                builder.create().show();
                break;
            default:
                break;
        }
    }
}
