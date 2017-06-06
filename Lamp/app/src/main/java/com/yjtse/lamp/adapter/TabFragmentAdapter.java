package com.yjtse.lamp.adapter;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;
import com.yjtse.lamp.Config;
import com.yjtse.lamp.asynchttp.TextNetWorkCallBack;
import com.yjtse.lamp.domain.JuheResult;
import com.yjtse.lamp.requests.AsyncRequest;

import org.apache.http.Header;

import java.util.List;

/**
 * Created by yjtse on 2016/6/10.
 */
//继承FragmentStatePagerAdapter
public class TabFragmentAdapter extends FragmentPagerAdapter {

    public static final String TAB_TAG = "@dream@";

    //    private List<WxCategoryResult.ResultBean> mTitles;
    private List<JuheResult.CategoryBean> mTitles;
    private List<Fragment> mFragments;

    public TabFragmentAdapter(FragmentManager fm, List<JuheResult.CategoryBean> titles, List<Fragment> fragments) {
        super(fm);
        mTitles = titles;
        mFragments = fragments;
    }


    @Override
    public Fragment getItem(int position) {
        //初始化Fragment数据
//        String[] title = mTitles.get(position).split(TAB_TAG);
//        ContentFragment fragment = new ContentFragment();
//        WxCategoryResult.ResultBean title = mTitles.get(position);
//        fragment.setType(Integer.parseInt(title.getCid()));
//        fragment.setTitle(title.getName());
//        return fragment;
        return mFragments.get(position);
//        ContentFragment contentFragment = ContentFragment.newInstance(mTitles.get(position).getName(), mTitles.get(position).getCid());
//        contentFragment.setUserVisibleHint(true);
//        return contentFragment;
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles.get(position).getName();
    }


    public static void requestByCategory(String page, String size, String cid, final Handler handler) {
        String url = Config.getApiUrl();
        RequestParams params = new RequestParams();
        params.add("key", Config.NEWS_APP_KEY);
        params.add("page", page);
        params.add("size", size);
        params.add("cid", cid);
        params.add("type", cid);
        AsyncRequest.ClientGet(url, params, new TextNetWorkCallBack() {
            @TargetApi(Build.VERSION_CODES.KITKAT)
            @Override
            public void onMySuccess(int statusCode, Header[] header, final String result) {
                Message message = Message.obtain();
                try {
                    JuheResult JuheResult = new Gson().fromJson(result, JuheResult.class);
                    message.obj = JuheResult;
                    message.what = Config.MESSAGE_WHAT_SET_NEWS;
//                          news_list.clear();
//                        news_list.addAll(JuheResult.getResult().getData());
//                        adapter.setData(news_list);
//                        tab_content_list.setAdapter(adapter);
                    handler.sendMessage(message);
//                        System.out.println(news_list.toString());
//                        endDialog();
//                        ToastUtils.showToast(getActivity(), "已更新", Toast.LENGTH_LONG);

                } catch (Exception e) {
                    message.obj = null;
                    handler.sendMessage(message);
//                    responseError(e);
                }
            }

            @Override
            public void onMyFailure(int statusCode, Header[] header, String result, Throwable
                    th) {
//                ToastUtils.showToast(getActivity(), "查询错误，请检查网络", Toast.LENGTH_LONG);
//                endDialog();
            }
        });
    }

}

