package com.yjtse.lamp.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yjtse.lamp.Config;
import com.yjtse.lamp.R;
import com.yjtse.lamp.adapter.TabFragmentAdapter;
import com.yjtse.lamp.domain.JuheResult;
import com.yjtse.lamp.fragment.childFrag.ContentFragment;
import com.yjtse.lamp.fragment.childFrag.ContentFragment2;
import com.yjtse.lamp.fragment.childFrag.ContentFragment3;
import com.yjtse.lamp.fragment.childFrag.ContentFragment4;
import com.yjtse.lamp.fragment.childFrag.ContentFragment5;

import java.util.ArrayList;
import java.util.List;


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
        //获取标签数据
        List<JuheResult.CategoryBean> titleList = new ArrayList<>();
        titleList = getCategory();
        //创建一个viewpager的adapter
//        requestCategory();
        List<Fragment> fragmentList = new ArrayList<>();
        for (int i = 0; i < titleList.size(); i++) {
            Fragment fragment = new Fragment();
            switch (i) {
                case 0:
                    fragment = new ContentFragment();
                    break;
                case 1:
                    fragment = new ContentFragment2();
                    break;
                case 2:
                    fragment = new ContentFragment3();
                    break;
                case 3:
                    fragment = new ContentFragment4();
                    break;
                case 4:
                    fragment = new ContentFragment5();
                    break;
//                case 5:
//                    fragment = new ContentFragment6();
//                    break;
            }
            fragmentList.add(i, fragment);
            Bundle bundle = new Bundle();
            bundle.putString("type", titleList.get(i).getCid());
            bundle.putString("title", titleList.get(i).getName());
            fragment.setArguments(bundle);
        }
        adapter = new TabFragmentAdapter(getChildFragmentManager(), titleList, fragmentList);
        this.vp_essence.setAdapter(adapter);
        vp_essence.setCurrentItem(1);
//        将TabLayout和ViewPager关联起来
        this.tab_essence.setupWithViewPager(this.vp_essence);
        adapter.notifyDataSetChanged();
        return view;
    }

    public List<JuheResult.CategoryBean> getCategory() {
        String[] strings = getResources().getStringArray(R.array.home_video_tab);
        List<JuheResult.CategoryBean> categoryBeanList = new ArrayList<>();
        for (String s : strings) {
            String[] strings1 = s.split(Config.TAB_TAG);
            categoryBeanList.add(new JuheResult.CategoryBean(strings1[1], strings1[0]));
        }
        return categoryBeanList;
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
