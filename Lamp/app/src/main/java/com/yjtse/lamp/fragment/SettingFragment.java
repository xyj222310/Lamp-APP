package com.yjtse.lamp.fragment;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;
import com.yjtse.lamp.Config;
import com.yjtse.lamp.R;
import com.yjtse.lamp.adapter.TabFragmentAdapter;
import com.yjtse.lamp.asynchttp.TextNetWorkCallBack;
import com.yjtse.lamp.domain.WxCategoryResult;
import com.yjtse.lamp.requests.AsyncRequest;
import com.yjtse.lamp.utils.NetAvailable;
import com.yjtse.lamp.utils.ToastUtils;

import org.apache.http.Header;

import java.util.ArrayList;
import java.util.List;

import static android.support.v7.widget.StaggeredGridLayoutManager.TAG;

public class SettingFragment extends BaseFragment implements ViewPager.OnPageChangeListener {
    /**
     * 控件
     */
    private TabLayout tab_essence;

    private ViewPager vp_essence;

    private Dialog dialog = null;

    /**
     * 变量
     */
    public TabFragmentAdapter getAdapter() {
        return adapter;
    }

    public void setAdapter(TabFragmentAdapter adapter) {
        this.adapter = adapter;
    }

    private TabFragmentAdapter adapter;
    private List<WxCategoryResult.ResultBean> resultBeanList = new ArrayList<>();
    private boolean isRefreshing = false;
    private boolean isPrepared;
    private boolean isFirstLoading = true;  //是否是第一次加载

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_device_message, container, false);
        /*
         * 只能这么写
         */
        this.tab_essence = (TabLayout) view.findViewById(R.id.tab_essence);
        this.vp_essence = (ViewPager) view.findViewById(R.id.vp_essence);
        /*
         * initUI
         */
//        MobAPI.initSDK(getContext(), Config.MOB_APP_KEY);
        //获取标签数据

//        String[] titles = requestCategory();
//        titles.add(0,new WxCategoryResult.ResultBean("1","nmb"));
//        titles.add(0,new WxCategoryResult.ResultBean("1","nmb"));
//        titles = ;
//                requestCategory();
//        getResources().getStringArray(R.array.home_video_tab);
        //创建一个viewpager的adapter

      /*  if (requestCategory().size() > 0) {
//        resultBeanList.add(0,new WxCategoryResult.ResultBean("1","时尚"));
//        resultBeanList.add(1,new WxCategoryResult.ResultBean("2","热点"));
//        resultBeanList.add(2,new WxCategoryResult.ResultBean("3","健康"));
//        resultBeanList.add(3,new WxCategoryResult.ResultBean("5","百科"));

        }*/


        requestCategory();
        return view;
    }

    private List<WxCategoryResult.ResultBean> requestCategory() {
        String url = Config.getMobCategoryApiUrl();
        RequestParams params = new RequestParams();
        params.add("key", Config.MOB_APP_KEY);
        AsyncRequest.ClientGet(url, params, new TextNetWorkCallBack() {
            @TargetApi(Build.VERSION_CODES.KITKAT)
            @Override
            public void onMySuccess(int statusCode, Header[] header, final String result) {
                try {
                    WxCategoryResult wxCategoryResult = new Gson().fromJson(result, WxCategoryResult.class);
                    if ("200".equals(wxCategoryResult.getRetCode())
                            && "success".equals(wxCategoryResult.getMsg())) {
                        resultBeanList = new ArrayList<>();
                        resultBeanList = wxCategoryResult.getResult();
                        Log.i(TAG, "onMySuccess: " + resultBeanList.toString());
                        SetAdapter();
                        endDialog();
                    }
                } catch (Exception e) {
                    responseError(e);
                }
            }

            @Override
            public void onMyFailure(int statusCode, Header[] header, String result, Throwable
                    th) {
                ToastUtils.showToast(getActivity(), "查询错误，请检查网络", Toast.LENGTH_LONG);
                endDialog();
            }
        });
        return resultBeanList;
    }

    void SetAdapter() {
        adapter = new TabFragmentAdapter(getChildFragmentManager(), resultBeanList);
        this.vp_essence.setAdapter(adapter);
//        this.vp_essence.setCurrentItem(0);
//        this.tab_essence.setSelected(true);

//        将TabLayout和ViewPager关联起来
        this.tab_essence.setupWithViewPager(this.vp_essence);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
//        Message msg = Message.obtain();
//        msg.arg1 = position;
//        msg.what = Config.MESSAGE_WHAT_UPDATE_DEVICE_STATUS;
//
//        handler.sendMessage(msg);
//        adapter.getItem(position);
        switch (position) {
            case 0:
                break;
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                break;
            case 5:
                break;
            case 6:
                break;
            case 7:
                break;
            case 8:
                break;
            default:
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private void responseError(Exception e) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Notice");
        if (!NetAvailable.isNetworkAvailable()) {
            builder.setMessage("操作失效，\n 无法访问网络" + e);
        } else {
            builder.setMessage("操作失效，\n 网络请求返回数据格式有误！" + e);
        }
        builder.setPositiveButton("确定", null);
        builder.create().show();
        endDialog();
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
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
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
    public void onDestroy() {
        super.onDestroy();
        isFirstLoading = true;
    }
}
