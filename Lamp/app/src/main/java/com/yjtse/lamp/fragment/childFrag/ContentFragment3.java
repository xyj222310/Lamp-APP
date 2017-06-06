package com.yjtse.lamp.fragment.childFrag;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.yjtse.lamp.Config;
import com.yjtse.lamp.R;
import com.yjtse.lamp.adapter.NewsAdapter;
import com.yjtse.lamp.adapter.TabFragmentAdapter;
import com.yjtse.lamp.contentview.ContentWidget;
import com.yjtse.lamp.domain.JuheResult;
import com.yjtse.lamp.fragment.BaseFragment;
import com.yjtse.lamp.ui.NewsActivity;
import com.yjtse.lamp.ui.TabFragmentActivity;
import com.yjtse.lamp.utils.NetAvailable;
import com.yjtse.lamp.utils.ToastUtils;
import com.yjtse.lamp.widgets.MyListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by janiszhang on 2016/6/6.
 */

public class ContentFragment3 extends BaseFragment {
    /**
     * 控件
     */
    private View viewContent;

    private MyListView tab_content_list;
    private TabFragmentActivity tabActivity;
    private Dialog dialog = null;

    @ContentWidget(R.id.item_news_icon)
    private ImageView item_news_icon;

    @ContentWidget(R.id.item_news_title)
    private TextView item_news_title;

    @ContentWidget(R.id.item_news_author)
    private TextView item_news_author;

    @ContentWidget(R.id.item_news_date)
    private TextView item_news_date;
    TextView textView;
    /**
     * 变量
     */
    private NewsAdapter adapter;
    private List<JuheResult.ResultBean.DataBean> news_list;
    private boolean isRefreshing = false;
    private boolean isPrepared;
    private boolean isFirstLoading = true;  //是否是第一次加载
    private String type, title;
    private String page = "1";
    private String size = "20";
    Bundle bundle = null;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case Config.MESSAGE_WHAT_SET_NEWS:
                    news_list.clear();
                    JuheResult juheResult = (JuheResult) msg.obj;
                    if (msg.obj != null) {
                        if (0 == juheResult.getError_code()) {
                            news_list.addAll(juheResult.getResult().getData());
                            adapter.notifyDataSetChanged();
                            ToastUtils.showToast(getActivity(), "已更新", Toast.LENGTH_LONG);
                            Log.v("TAG", news_list.get(0).getAuthor_name());
                            endDialog();
                        }
                        ToastUtils.showToast(getActivity(), "已经最新了", Toast.LENGTH_LONG);
                        endDialog();
                    } else {
                        ToastUtils.showToast(getActivity(), "出错了", Toast.LENGTH_LONG);
                        endDialog();
                    }
//                    adapter = new NewsAdapter(getActivity(), handler, R.layout.item_news);
//                    adapter.setData(news_list);
//                    tab_content_list.setAdapter(adapter);
            }
        }
    };

    public static ContentFragment3 newInstance(String title, String type) {
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("type", type);
        ContentFragment3 fragment = new ContentFragment3();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //布局文件中只有一个居中的TextView
        viewContent = inflater.inflate(R.layout.fragment_content3, container, false);
        return viewContent;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        textView = (TextView) viewContent.findViewById(R.id.tv_content3);
        tab_content_list = (MyListView) getActivity().findViewById(R.id.tab_essence_content_listview3);//关联ListView组件,注意getActivity
        super.onActivityCreated(savedInstanceState);
        isPrepared = true;
        if (isFirstLoading) {
            isFirstLoading = false;
        }
        bundle = getArguments();
        news_list = new ArrayList<>();
        if (bundle != null) {
            type = bundle.getString("type");
            title = bundle.getString("title");
            textView.setText(title);
            adapter = new NewsAdapter(getActivity(), handler, R.layout.item_news);
            adapter.setData(news_list);
            tab_content_list.setAdapter(adapter);
            if (!NetAvailable.isConnect(getActivity())) {
                ToastUtils.showToast(getActivity(), "请检查网络链接", Toast.LENGTH_LONG);
            } else {
                TabFragmentAdapter.requestByCategory(page, "10", type, handler);
            }
//            adapter.notifyDataSetChanged();
        }
        //initUI

        tab_content_list.setOnRefreshListener(new MyListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                isRefreshing = true;
                if (!NetAvailable.isNetworkAvailable()) {
                    ToastUtils.showToast(getActivity(), "请检查网络链接", Toast.LENGTH_LONG);
                } else {
                    //    requestByCategory(getRequestParams());
                    TabFragmentAdapter.requestByCategory("1", "10", type, handler);
                }
            }
        });
        tab_content_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), NewsActivity.class);
                intent.putExtra("url", news_list.get(position - 1).getUrl());
                startActivity(intent);
            }
        });

    }

//    private List<JuheResult.ResultBean.DataBean> requestByCategory(String page, String size, String cid) {
//        String url = Config.getApiUrl();
//        RequestParams params = new RequestParams();
//        params.add("key", Config.NEWS_APP_KEY);
//        params.add("page", page);
//        params.add("size", size);
//        params.add("cid", cid);
//        AsyncRequest.ClientGet(url, params, new TextNetWorkCallBack() {
//            @TargetApi(Build.VERSION_CODES.KITKAT)
//            @Override
//            public void onMySuccess(int statusCode, Header[] header, final String result) {
//                try {
//                    JuheResult JuheResult = new Gson().fromJson(result, JuheResult.class);
//                    if ("0".equals(JuheResult.getError_code())) {
////                        news_list.clear();
////                        news_list.addAll(JuheResult.getResult().getData());
////                        adapter.setData(news_list);
////                        tab_content_list.setAdapter(adapter);
//                        Message message = Message.obtain();
//                        message.obj = JuheResult.getResult().getData();
//                        message.what = Config.MESSAGE_WHAT_SET_NEWS;
//                        handler.sendMessage(message);
//                        System.out.println(news_list.toString());
//                        endDialog();
//                        ToastUtils.showToast(getActivity(), "已更新", Toast.LENGTH_LONG);
//                    }
//                } catch (Exception e) {
//                    responseError(e);
//                }
//            }
//
//            @Override
//            public void onMyFailure(int statusCode, Header[] header, String result, Throwable
//                    th) {
//                ToastUtils.showToast(getActivity(), "查询错误，请检查网络", Toast.LENGTH_LONG);
//                endDialog();
//            }
//        });
//    }

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

    @Override
    protected void setlazyLoad() {
        super.setlazyLoad();
        if (!isPrepared || !isVisible) {
            return;
        }
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
            tab_content_list.onRefreshComplete();
            isRefreshing = false;
        }
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isFirstLoading = true;
    }


}
