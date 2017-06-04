package com.yjtse.lamp.adapter;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yjtse.lamp.R;
import com.yjtse.lamp.domain.WxSearchResult;

import java.util.List;

public class NewsAdapter extends BaseAdapter {

    private Context context;
    private List<WxSearchResult.ResultBean.ListBean> data;
    private int aysncItem;
    private Handler handler;
    private LayoutInflater layoutInflater;

    public NewsAdapter(Context context, Handler handler, int aysncItem) {
        this.context = context;
        this.handler = handler;
        this.aysncItem = aysncItem;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ImageView item_news_icon;
        TextView item_news_title;
        TextView item_news_author;
        TextView item_news_date;

        if (convertView == null) {
            convertView = layoutInflater.inflate(aysncItem, null);
            item_news_icon = (ImageView) convertView.findViewById(R.id.item_news_icon);
            item_news_title = (TextView) convertView.findViewById(R.id.item_news_title);
            item_news_author = (TextView) convertView.findViewById(R.id.item_news_author);
            item_news_date = (TextView) convertView.findViewById(R.id.item_news_date);
            convertView.setTag(new DataWrapper(item_news_icon, item_news_title, item_news_author, item_news_date));
        } else {
            DataWrapper dataWrapper = (DataWrapper) convertView.getTag();
            item_news_icon = dataWrapper.item_news_icon;
            item_news_title = dataWrapper.item_news_title;
            item_news_author = dataWrapper.item_news_author;
            item_news_date = dataWrapper.item_news_date;
        }
        /*
        绑定数据
         */

        if (!TextUtils.isEmpty(data.get(position).getThumbnails())) {
            String[] thumbnails = data.get(position).getThumbnails().split("$");
            Glide.with(convertView).load(thumbnails[0]).into(item_news_icon);
        } else {
            item_news_icon.setImageDrawable(convertView.getResources().getDrawable(R.drawable.ic_launcher));
        }
        item_news_title.setText(data.get(position).getTitle());
        item_news_author.setText(data.get(position).getSubTitle());
        item_news_date.setText(data.get(position).getPubTime());
        /*
        监听函数
         */
        return convertView;
    }

    public List<WxSearchResult.ResultBean.ListBean> getData() {
        return data;
    }

    public void setData(List<WxSearchResult.ResultBean.ListBean> data) {
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private final class DataWrapper {
        private ImageView item_news_icon;
        private TextView item_news_title;
        private TextView item_news_author;
        private TextView item_news_date;

        private DataWrapper(ImageView item_news_icon, TextView item_news_title, TextView item_news_author, TextView item_news_date) {
            this.item_news_icon = item_news_icon;
            this.item_news_title = item_news_title;
            this.item_news_author = item_news_author;
            this.item_news_date = item_news_date;
        }
    }

}
