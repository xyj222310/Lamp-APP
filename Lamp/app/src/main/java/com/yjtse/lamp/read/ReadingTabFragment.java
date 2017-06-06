package com.yjtse.lamp.read;


import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.cxs.androidlib.fragment.base.BaseFragment;
import com.cxs.androidlib.rx.retrofit.RxUtils;
import com.cxs.androidlib.utils.ViewUtils;
import com.yjtse.lamp.R;

import java.util.List;

import rx.Subscriber;


/**
 * Created by _SOLID
 * Date:2016/11/29
 * Time:17:01
 * Desc:闲读
 */

public class ReadingTabFragment extends BaseFragment {

    private TabLayout tab_layout;
    private ViewPager view_pager;
    private ImageView tab_more;
    private ProgressBar mProgress;
    private XianDuTabAdapter mAdapter;

    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_stroke_layout;
    }

    @Override
    protected void setUpView() {
        tab_layout = $(R.id.tab_layout);
        view_pager = $(R.id.view_pager);
        ViewCompat.setElevation(tab_layout, ViewUtils.dp2px(getContext(), 4));
//        setUpData();
    }

    @Override
    protected void setUpData() {
        XianDuService.getCategorys()
                .compose(RxUtils.<List<XianDuCategory>>defaultSchedulers())
                .subscribe(new Subscriber<List<XianDuCategory>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(List<XianDuCategory> list) {
                        mAdapter = new XianDuTabAdapter(getChildFragmentManager(), list);
                        view_pager.setAdapter(mAdapter);
                        tab_layout.setupWithViewPager(view_pager);
                    }
                });
    }

    //: 点击返回时现将列表滚动到顶部
    public boolean scrollToTop() {

        return true;
    }
}
