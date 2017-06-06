package com.cxs.androidlib.widget.sectioned;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cxs.androidlib.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by CXS on 2017/4/2.
 * USE:
 */

public class SectionGuid {
//
//        @Override
//        public void finishCreateView(Bundle state)
//        {
//
//            isPrepared = true;
//            lazyLoad();
//        }
//
//        @Override
//        protected void lazyLoad()
//        {
//
//            if (!isPrepared || !isVisible)
//                return;
//
//            initRefreshLayout();
//            initRecyclerView();
//            isPrepared = false;
//        }
//
//        @Override
//        protected void initRecyclerView()
//        {
//
//            mSectionedRecyclerViewAdapter = new SectionedRecyclerViewAdapter();
//            GridLayoutManager mGridLayoutManager = new GridLayoutManager(getActivity(), 3);
//            mGridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup()
//            {
//
//                @Override
//                public int getSpanSize(int position)
//                {
//
//                    switch (mSectionedRecyclerViewAdapter.getSectionItemViewType(position))
//                    {
//                        case SectionedRecyclerViewAdapter.VIEW_TYPE_HEADER:
//                            return 3;
//
//                        default:
//                            return 1;
//                    }
//                }
//            });
//
//            mRecyclerView.setHasFixedSize(true);
//            mRecyclerView.setNestedScrollingEnabled(true);
//            mRecyclerView.setLayoutManager(mGridLayoutManager);
//            mRecyclerView.setAdapter(mSectionedRecyclerViewAdapter);
//            setRecycleNoScroll();
//        }
//
//        @Override
//        protected void initRefreshLayout()
//        {
//
//            mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
//            mSwipeRefreshLayout.post(() -> {
//
//                mSwipeRefreshLayout.setRefreshing(true);
//                mIsRefreshing = true;
//                loadData();
//            });
//
//            mSwipeRefreshLayout.setOnRefreshListener(() -> {
//
//                clearData();
//                loadData();
//            });
//        }
//
//
//        private void clearData()
//        {
//
//            mIsRefreshing = true;
//            banners.clear();
//            bannerList.clear();
//            bangumibobys.clear();
//            bangumiRecommends.clear();
//            newBangumiSerials.clear();
//            seasonNewBangumis.clear();
//            mSectionedRecyclerViewAdapter.removeAllSections();
//        }
//
//
//        @Override
//        protected void loadData()
//        {
//
//            RetrofitHelper.getBangumiAPI()
//                    .getBangumiAppIndex()
//                    .compose(bindToLifecycle())
//                    .flatMap(new Func1<BangumiAppIndexInfo,Observable<BangumiRecommendInfo>>()
//                    {
//
//                        @Override
//                        public Observable<BangumiRecommendInfo> call(BangumiAppIndexInfo bangumiAppIndexInfo)
//                        {
//
//                            banners.addAll(bangumiAppIndexInfo.getResult().getAd().getHead());
//                            bangumibobys.addAll(bangumiAppIndexInfo.getResult().getAd().getBody());
//                            seasonNewBangumis.addAll(bangumiAppIndexInfo.getResult().getPrevious().getList());
//                            season = bangumiAppIndexInfo.getResult().getPrevious().getSeason();
//                            newBangumiSerials.addAll(bangumiAppIndexInfo.getResult().getSerializing());
//                            return RetrofitHelper.getBangumiAPI().getBangumiRecommended();
//                        }
//                    })
//                    .compose(bindToLifecycle())
//                    .map(BangumiRecommendInfo::getResult)
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(resultBeans -> {
//
//                        bangumiRecommends.addAll(resultBeans);
//                        finishTask();
//                    }, throwable -> {
//                        initEmptyView();
//                    });
//        }
//
//        @Override
//        protected void finishTask()
//        {
//
//            mSwipeRefreshLayout.setRefreshing(false);
//            mIsRefreshing = false;
//            hideEmptyView();
//
//            Observable.from(banners)
//                    .compose(bindToLifecycle())
//                    .forEach(bannersBean -> bannerList.add(new BannerEntity(
//                            bannersBean.getLink(), bannersBean.getTitle(), bannersBean.getImg())));
//
//            mSectionedRecyclerViewAdapter.addSection(new HomeBangumiBannerSection(bannerList));
//            mSectionedRecyclerViewAdapter.addSection(new HomeBangumiItemSection(getActivity()));
//            mSectionedRecyclerViewAdapter.addSection(new HomeBangumiNewSerialSection(getActivity(), newBangumiSerials));
//            mSectionedRecyclerViewAdapter.addSection(new HomeBangumiBobySection(getActivity(), bangumibobys));
//            mSectionedRecyclerViewAdapter.addSection(new HomeBangumiSeasonNewSection(getActivity(), season, seasonNewBangumis));
//            mSectionedRecyclerViewAdapter.addSection(new HomeBangumiRecommendSection(getActivity(), bangumiRecommends));
//            mSectionedRecyclerViewAdapter.notifyDataSetChanged();
//        }
//
//        public void initEmptyView()
//        {
//
//            mSwipeRefreshLayout.setRefreshing(false);
//            mCustomEmptyView.setVisibility(View.VISIBLE);
//            mRecyclerView.setVisibility(View.GONE);
//            mCustomEmptyView.setEmptyImage(R.drawable.img_tips_error_load_error);
//            mCustomEmptyView.setEmptyText("加载失败~(≧▽≦)~啦啦啦.");
//            SnackbarUtil.showMessage(mRecyclerView, "数据加载失败,请重新加载或者检查网络是否链接");
//        }
//
//        public void hideEmptyView()
//        {
//
//            mCustomEmptyView.setVisibility(View.GONE);
//            mRecyclerView.setVisibility(View.VISIBLE);
//        }
//
//        private void setRecycleNoScroll()
//        {
//
//            mRecyclerView.setOnTouchListener((v, event) -> mIsRefreshing);
//        }
//    }





//    public class HomeBangumiNewSerialSection extends StatelessSection
//    {
//
//        private Context mContext;
//
//        private List<BangumiAppIndexInfo.ResultBean.SerializingBean> newBangumiSerials;
//
//
//        public HomeBangumiNewSerialSection(Context context, List<BangumiAppIndexInfo.ResultBean.SerializingBean> newBangumiSerials)
//        {
//
//            super(R.layout.layout_home_bangumi_new_serial_head, R.layout.layout_home_bangumi_new_serial_body);
//            this.mContext = context;
//            this.newBangumiSerials = newBangumiSerials;
//        }
//
//        @Override
//        public int getContentItemsTotal()
//        {
//
//            return newBangumiSerials.size();
//        }
//
//        @Override
//        public RecyclerView.ViewHolder getItemViewHolder(View view)
//        {
//
//            return new ItemViewHolder(view);
//        }
//
//        @SuppressLint("SetTextI18n")
//        @Override
//        public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position)
//        {
//
//            ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
//            BangumiAppIndexInfo.ResultBean.SerializingBean serializingBean = newBangumiSerials.get(position);
//
//            Glide.with(mContext)
//                    .load(serializingBean.getCover())
//                    .centerCrop()
//                    .diskCacheStrategy(DiskCacheStrategy.ALL)
//                    .placeholder(R.drawable.bili_default_image_tv)
//                    .dontAnimate()
//                    .into(itemViewHolder.mImage);
//
//            itemViewHolder.mTitle.setText(serializingBean.getTitle());
//            itemViewHolder.mPlay.setText(NumberUtil.converString(serializingBean.getWatching_count()) + "人在看");
//            itemViewHolder.mUpdate.setText("更新至第" + serializingBean.getNewest_ep_index() + "话");
//
//            itemViewHolder.mCardView.setOnClickListener(v -> BangumiDetailsActivity.launch(
//                    (Activity) mContext, serializingBean.getSeason_id()));
//        }
//
//        @Override
//        public RecyclerView.ViewHolder getHeaderViewHolder(View view)
//        {
//
//            return new HomeBangumiNewSerialSection.HeaderViewHolder(view);
//        }
//
//        @SuppressLint("SetTextI18n")
//        @Override
//        public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder)
//        {
//
//            HomeBangumiNewSerialSection.HeaderViewHolder headerViewHolder = (HomeBangumiNewSerialSection.HeaderViewHolder) holder;
//            headerViewHolder.mAllSerial.setOnClickListener(v -> mContext.startActivity(
//                    new Intent(mContext, NewBangumiSerialActivity.class)));
//        }
//
//
//        static class HeaderViewHolder extends RecyclerView.ViewHolder
//        {
//
//            @BindView(R.id.tv_all_serial)
//            TextView mAllSerial;
//
//
//            HeaderViewHolder(View itemView)
//            {
//
//                super(itemView);
//                ButterKnife.bind(this, itemView);
//            }
//        }
//
//        static class ItemViewHolder extends RecyclerView.ViewHolder
//        {
//
//            @BindView(R.id.card_view)
//            LinearLayout mCardView;
//
//            @BindView(R.id.item_img)
//            ImageView mImage;
//
//            @BindView(R.id.item_title)
//            TextView mTitle;
//
//            @BindView(R.id.item_play)
//            TextView mPlay;
//
//            @BindView(R.id.item_update)
//            TextView mUpdate;
//
//
//            public ItemViewHolder(View itemView)
//            {
//
//                super(itemView);
//                ButterKnife.bind(this, itemView);
//            }
//        }
//    }


}
