package com.cxs.androidlib.fragment.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cxs.androidlib.constant.Constants;
import com.trello.rxlifecycle.components.support.RxFragment;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by _CXS
 * Date:2016/3/30
 * Time:11:30
 */
public abstract class BaseFragment extends RxFragment {

    private View mContentView;
    private Context mContext;
    private ProgressDialog mProgressDialog;
    private Unbinder bind;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        init();
        if (setLayoutResourceID() != 0) {
            mContentView = inflater.inflate(setLayoutResourceID(), container, false);//setContentView(inflater, container);
        }
        mContext = getContext();
        mProgressDialog = new ProgressDialog(getMContext());
        mProgressDialog.setCanceledOnTouchOutside(false);

        return mContentView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bind = ButterKnife.bind(this, view);
        setUpView();
        setUpData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        bind.unbind();
    }

    protected abstract int setLayoutResourceID();

    /**
     * initialize before  setUpView and  setUpData
     */
    protected void init() {

    }

    protected abstract void setUpView();

    protected abstract void setUpData();

    protected <T extends View> T $(int id) {
        return (T) mContentView.findViewById(id);
    }

    protected View getContentView() {
        return mContentView;
    }

    public Context getMContext() {
        return mContext;
    }

    public void refresh() {
    }

    protected ProgressDialog getProgressDialog() {
        return mProgressDialog;
    }

    /**
     * 申请指定的权限.
     */
    public void requestPermission(int code, String... permissions) {

        if (Build.VERSION.SDK_INT >= 23) {
            requestPermissions(permissions, code);
        }
    }

    /**
     * 判断是否有指定的权限
     */
    public boolean hasPermission(String... permissions) {

        for (String permisson : permissions) {
            if (ContextCompat.checkSelfPermission(getActivity(), permisson)
                    != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Constants.HARDWEAR_CAMERA_CODE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    doOpenCamera();
                }
                break;
            case Constants.WRITE_READ_EXTERNAL_CODE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    doWriteSDCard();
                }
                break;
        }
    }

    public void doOpenCamera() {

    }

    public void doWriteSDCard() {

    }
}
