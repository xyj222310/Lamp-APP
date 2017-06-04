package com.yjtse.lamp.adapter;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.yjtse.lamp.domain.WxCategoryResult;
import com.yjtse.lamp.fragment.ContentFragment;

import java.util.List;

/**
 * Created by yjtse on 2016/6/10.
 */
//继承FragmentStatePagerAdapter
public class TabFragmentAdapter extends FragmentStatePagerAdapter {

    public static final String TAB_TAG = "@dream@";

    private List<WxCategoryResult.ResultBean> mTitles;

    public TabFragmentAdapter(FragmentManager fm, List<WxCategoryResult.ResultBean> titles) {
        super(fm);
        mTitles = titles;
    }


    @Override
    public android.support.v4.app.Fragment getItem(int position) {
        //初始化Fragment数据
//        ContentFragment fragment = new ContentFragment();
//        WxCategoryResult.ResultBean title = mTitles.get(position);
//        fragment.setType(Integer.parseInt(title.getCid()));
//        fragment.setTitle(title.getName());
//        return fragment;
        ContentFragment contentFragment = ContentFragment.newInstance(mTitles.get(position).getName(), mTitles.get(position).getCid());
        contentFragment.setUserVisibleHint(true);
        return contentFragment;
    }

    @Override
    public int getCount() {
        return mTitles.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles.get(position).getName();
    }
}

