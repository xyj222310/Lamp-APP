package com.yjtse.lamp.read;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cxs.androidlib.imageloader.ImageLoader;
import com.yjtse.lamp.R;

import java.util.List;

/**
 * Created by CXS on 2017/6/1.
 * USE:
 */

public class XianDuAdapter extends BaseQuickAdapter<XianDuItem,BaseViewHolder> {
    public XianDuAdapter(List data) {
        super(R.layout.item_xian_du, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, XianDuItem item) {
        ImageView imageView=helper.getView(R.id.iv_avatar);
        ImageLoader.displayImage(imageView,item.getSourceAvatar());
//        helper.setText(R.id.iv_avatar, item.getSourceAvatar());
        helper.setText(R.id.tv_source, item.getSource());
        helper.setText(R.id.tv_time, item.getTime());
        helper.setText(R.id.tv_title, item.getTitle());
        /*helper.setOnItemSelectedClickListener(R.id.item, new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                WebViewActivity.start(item.getContext(), item.getTitle(), item.getUrl());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/
    }

    @Override
    public void setOnItemClickListener(@Nullable OnItemClickListener listener) {
        super.setOnItemClickListener(listener);

    }
}
