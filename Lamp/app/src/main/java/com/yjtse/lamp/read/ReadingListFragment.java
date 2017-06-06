package com.yjtse.lamp.read;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cxs.androidlib.activity.WebViewActivity;
import com.cxs.androidlib.fragment.base.BaseFragment;
import com.cxs.androidlib.rx.retrofit.RxUtils;
import com.cxs.androidlib.widget.CircleProgressView;
import com.yjtse.lamp.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import rx.Subscriber;


/**
 * Created by _SOLID
 * GitHub:https://github.com/burgessjp
 * Date:2017/3/18
 * Time:16:16
 * Desc:
 */

public class ReadingListFragment extends BaseFragment {

    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerView;

    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @BindView(R.id.circle_progress)
    CircleProgressView mProgressView;


    private String category;
    private int pageIndex = 1;
    private XianDuAdapter mAdapter;
    private List<XianDuItem> listdata = new ArrayList<>();

    public static ReadingListFragment newInstance(String category) {

        Bundle args = new Bundle();
        args.putString("category", category);
        ReadingListFragment fragment = new ReadingListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        category = getArguments().getString("category");
    }

    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_base_recyclerview;
    }

    @Override
    protected void setUpView() {
//        initRefreshLayout();
//        initRecycleView();
    }

    protected void initRefreshLayout() {
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
//                requestDataFromNet();
            }
        });

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                clearData();
                requestDataFromNet();
            }
        });
    }

    private void initRecycleView() {
        mAdapter = new XianDuAdapter(listdata);
        final LinearLayoutManager manager = new LinearLayoutManager(getMContext());
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                WebViewActivity.start(getMContext(), listdata.get(position).getTitle(), listdata.get(position).getUrl());
            }
        });

    }

    @Override
    protected void setUpData() {

    }

    private void requestDataFromNet() {
        XianDuService.getData(category, pageIndex)
                .compose(RxUtils.<List<XianDuItem>>defaultSchedulers())
                .subscribe(new Subscriber<List<XianDuItem>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(List<XianDuItem> list) {
//                        onDataSuccessReceived(pageIndex, list);
                        listdata.addAll(list);
                        notifyDataRecycleView();
                    }
                });
    }

    protected void notifyDataRecycleView() {
        mSwipeRefreshLayout.setRefreshing(false);
        mAdapter.notifyDataSetChanged();
    }


}
