package com.yjtse.lamp.fragment;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;
import com.yjtse.lamp.Config;
import com.yjtse.lamp.R;
import com.yjtse.lamp.adapter.NewsAdapter;
import com.yjtse.lamp.asynchttp.TextNetWorkCallBack;
import com.yjtse.lamp.contentview.ContentWidget;
import com.yjtse.lamp.domain.WxSearchResult;
import com.yjtse.lamp.requests.AsyncRequest;
import com.yjtse.lamp.ui.NewsActivity;
import com.yjtse.lamp.ui.TabFragmentActivity;
import com.yjtse.lamp.utils.NetAvailable;
import com.yjtse.lamp.utils.ToastUtils;
import com.yjtse.lamp.widgets.MyListView;

import org.apache.http.Header;

import java.util.ArrayList;
import java.util.List;

import static android.support.v7.widget.StaggeredGridLayoutManager.TAG;

/**
 * Created by janiszhang on 2016/6/6.
 */

public class ContentFragment extends BaseFragment {
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
    /**
     * 变量
     */
    private NewsAdapter adapter;
    private List<WxSearchResult.ResultBean.ListBean> news_list;
    private boolean isRefreshing = false;
    private boolean isPrepared;
    private boolean isFirstLoading = true;  //是否是第一次加载
    private int mType;
    private String mTitle;
    private String category;
    private String type, title;

    public void setType(int mType) {
        this.mType = mType;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    adapter.setData(news_list);
                    adapter.notifyDataSetChanged();
            }
        }
    };

    public static ContentFragment newInstance(String title, String type) {

        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("type", type);
        ContentFragment fragment = new ContentFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        type = getArguments().getString("type");
        title = getArguments().getString("title");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //布局文件中只有一个居中的TextView
        viewContent = inflater.inflate(R.layout.fragment_content, container, false);
        TextView textView = (TextView) viewContent.findViewById(R.id.tv_content);
        textView.setText(this.title);
        isPrepared = true;
        if (isFirstLoading) {
            isFirstLoading = false;
        }
        if (!NetAvailable.isConnect(getActivity())) {
            ToastUtils.showToast(getActivity(), "请检查网络链接", Toast.LENGTH_LONG);
        } else {

        }
        return viewContent;
    }

    @Override
    protected void setlazyLoad() {
        super.setlazyLoad();
        if (!isPrepared || !isVisible) {
            return;
        }
//        requestByCategory("1", "15", type);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        tabActivity = (TabFragmentActivity) getActivity();
        //initUI

        tab_content_list = (MyListView) getActivity().findViewById(R.id.tab_essence_content_listview);//关联ListView组件,注意getActivity
        adapter = new NewsAdapter(getActivity(), handler, R.layout.item_news);
        news_list = new ArrayList<>();
        WxSearchResult.ResultBean.ListBean bean = new WxSearchResult.ResultBean.ListBean();
        bean.setCid("1");
        bean.setPubTime("1");
        bean.setTitle("1");
        bean.setSourceUrl("1");

        news_list.add(0, bean);
        news_list.add(1, bean);
        news_list.add(2, bean);
        adapter.setData(news_list);
        tab_content_list.setAdapter(adapter);


        tab_content_list.setOnRefreshListener(() -> {
            isRefreshing = true;
            if (!NetAvailable.isNetworkAvailable()) {
                ToastUtils.showToast(getActivity(), "请检查网络链接", Toast.LENGTH_LONG);
            } else {
//                requestByCategory(getRequestParams());
                requestByCategory("1", "10", type);
            }
        });
        tab_content_list.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(getActivity(), NewsActivity.class);
            intent.putExtra("url", news_list.get(position - 1).getSourceUrl());
            startActivity(intent);
        });

    }


//    private void request() {
//        try {
//            String result = "{" +
//                    "\"reason\"" +
//                    ":" +
//                    "\"成功的返回\"" +
//                    "," +
//                    "\"result\"" +
//                    ":{" +
//                    "\"stat\"" +
//                    ":" +
//                    "\"1\"," +
//                    "\"data\"" +
//                    ":" +
//                    "[" +
//                    "{" +
//                    "\"uniquekey\"" +
//                    ":" +
//                    "\"720452099b284695132f1d7cf3c07404\"" +
//                    "," +
//                    "\"title\"" +
//                    ":" +
//                    "\"福特CEO被炒内幕：搞砸了与谷歌的合作，不受硅谷欢迎\"," +
//                    "\"date\"" +
//                    ":" +
//                    "\"2017-06-04 02:22\"" +
//                    "," +
//                    "\"category\"" +
//                    ":" +
//                    "\"科技\"" +
//                    "," +
//                    "\"author_name\"" +
//                    ":" +
//                    "\"每日汽车观察\"" +
//                    "," +
//                    "\"url\"" +
//                    ":" +
//                    "\"http:\\/\\/mini.eastday.com\\/mobile\\/170604022239376.html\"" +
//                    "," +
//                    "\"thumbnail_pic_s\"" +
//                    ":" +
//                    "\"http:\\/\\/07.imgmini.eastday.com\\/mobile\\/20170604\\/20170604022239_5144d12985c086ef2eec25f64c4a6948_5_mwpm_03200403.jpeg\"" +
//                    "," +
//                    "\"thumbnail_pic_s02\"" +
//                    ":" +
//                    "\"http:\\/\\/07.imgmini.eastday.com\\/mobile\\/20170604\\/20170604022239_5144d12985c086ef2eec25f64c4a6948_4_mwpm_03200403.jpeg\"" +
//                    "," +
//                    "\"thumbnail_pic_s03\"" +
//                    ":" +
//                    "\"http:\\/\\/07.imgmini.eastday.com\\/mobile\\/20170604\\/20170604022239_5144d12985c086ef2eec25f64c4a6948_6_mwpm_03200403.jpeg\"" +
//                    "}]}" +
//                    "," +
//                    "\"error_code\"" +
//                    ":" +
//                    "0}";
//            JSONObject object = new JSONObject(result);
//            if (object.getInt("error_code") == 0) {
//                //没有错误
//                JSONObject object2 = object.getJSONObject("result");
//                if ("1".equals(object2.get("stat"))) {
//                    //有数据
//                    final List<News> list = new Gson().fromJson(object2.getJSONArray("data").toString(), new TypeToken<List<News>>() {
//                    }.getType());
//                    news_list = new ArrayList<>();
//                    news_list = list;
//                    adapter.setData(news_list);
//                    adapter.notifyDataSetChanged();
//                    endDialog();
//                    ToastUtils.showToast(getActivity(), "已更新", Toast.LENGTH_LONG);
//
//                } else {
//                    ToastUtils.showToast(getActivity(), object2.get("stat") + ":数据为空", Toast.LENGTH_LONG);
//                }
//            } else {
//                ToastUtils.showToast(getActivity(), object.get("error_code") + ":" + object.get("reason"), Toast.LENGTH_LONG);
//            }
//        } catch (Exception e) {
//            responseError(e);
//        }
//    }

    private void requestByCategory(String page, String size, String cid) {
        String url = Config.getMobSearchApiUrl();
        RequestParams params = new RequestParams();
        params.add("key", Config.MOB_APP_KEY);
        params.add("page", page);
        params.add("size", size);
        params.add("cid", cid);
        AsyncRequest.ClientGet(url, params, new TextNetWorkCallBack() {
            @TargetApi(Build.VERSION_CODES.KITKAT)
            @Override
            public void onMySuccess(int statusCode, Header[] header, final String result) {
                try {
                    WxSearchResult wxSearchResult = new Gson().fromJson(result, WxSearchResult.class);
                    if ("200".equals(wxSearchResult.getRetCode())
                            && "success".equals(wxSearchResult.getMsg())) {
//                        news_list.clear();
                        news_list.addAll(wxSearchResult.getResult().getList());
                        Message message = new Message();
                        message.what = 0;
                        handler.sendMessage(message);
                        Log.i(TAG, "onMySuccess: " + news_list.toString());
                        endDialog();
                        ToastUtils.showToast(getActivity(), "已更新", Toast.LENGTH_LONG);
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
    }

    private String getRequestParams(String mTitle) {

        switch (mTitle) {
            case "科技":
                category = Config.PARAMS_KEJI;
                break;
            case "体育":
                category = Config.PARAMS_TIYU;
                break;
            case "时尚":
                category = Config.PARAMS_SHISHANG;
                break;
            case "娱乐":
                category = Config.PARAMS_YULE;
                break;
            case "国内":
                category = Config.PARAMS_GUONEI;
                break;
            case "国际":
                category = Config.PARAMS_GUOJI;
                break;
            case "社会":
                category = Config.PARAMS_SHEHUI;
                break;
            case "军事":
                category = Config.PARAMS_JUNSHI;
                break;
            case "财经":
                category = Config.PARAMS_CAIJING;
                break;
            default:
                category = "hello";
                break;
        }
        return category;
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
