package com.yjtse.lamp.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.yjtse.lamp.Config;
import com.yjtse.lamp.R;
import com.yjtse.lamp.domain.Socket;
import com.yjtse.lamp.utils.ToastUtils;

import java.util.HashMap;
import java.util.List;

public class DeviceAdapter extends BaseAdapter {

    private Context context;
    private List<Socket> data;
    private int aysncItem;
    private Handler handler;
    private LayoutInflater layoutInflater;

    public DeviceAdapter(Context context, Handler handler, int aysncItem) {
        this.context = context;
        this.handler = handler;
        this.aysncItem = aysncItem;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        TextView imageView = null;
        Switch aSwitch = null;
        TextView socketNameView = null;
        TextView socket_status_text = null;
        TextView socket_id_text = null;

        if (convertView == null) {
            convertView = layoutInflater.inflate(aysncItem, null);
            imageView = (TextView) convertView.findViewById(R.id.device_logo);
            socketNameView = (TextView) convertView.findViewById(R.id.device_text);
            socket_status_text = (TextView) convertView.findViewById(R.id.device_status_text);
            socket_id_text = (TextView) convertView.findViewById(R.id.device_id_text);
            aSwitch = (Switch) convertView.findViewById(R.id.device_right_arrow);
            convertView.setTag(new DataWrapper(imageView, socketNameView, socket_status_text, socket_id_text, aSwitch));
        } else {
            DataWrapper dataWrapper = (DataWrapper) convertView.getTag();
            imageView = dataWrapper.imageView;
            socketNameView = dataWrapper.socketNameView;
            aSwitch = dataWrapper.aSwitch;
            socket_status_text = dataWrapper.socket_status_text;
            socket_id_text = dataWrapper.socket_id_text;
        }
        socketNameView.setText(data.get(position).getSocketName());
        socket_id_text.setText(data.get(position).getSocketId());
        socket_status_text.setText(data.get(position).getAvailable());
        imageView.setBackgroundResource(R.drawable.ic_launcher);
        aSwitch.setChecked("1".equals(data.get(position).getStatus()));

        switch (data.get(position).getAvailable()) {
            case "-1":
                socket_status_text.setText("Offline");
                socket_status_text.setTextColor(Color.GRAY);  //灰色
                socket_id_text.setTextColor(Color.GRAY);  //灰色
                break;
            default:
                socket_status_text.setText("Online");
                socket_id_text.setTextColor(context.getResources().getColor(R.color.theme_color));  //灰色
                socket_status_text.setTextColor(context.getResources().getColor(R.color.theme_color));
                break;
        }

        final Switch finalASwitch = aSwitch;
        finalASwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {
                HashMap<String, String> params = new HashMap<>();
//                Socket socket = new Socket();
                if (!"-1".equals(data.get(position).getAvailable())) {
                    Message msg = Message.obtain();
                    msg.arg1 = position;
                    msg.arg2 = isChecked ? 1 : 0;
                    msg.what = Config.MESSAGE_WHAT_UPDATE_DEVICE_STATUS;
                    handler.sendMessage(msg);
                    finalASwitch.setChecked(isChecked);
                } else {
                    /**
                     * 如果设备已经断线了，无论怎么操作都保持关闭状态
                     */
                    ToastUtils.showToast(context, "设备已经离线，请检查设备", Toast.LENGTH_LONG);
                    finalASwitch.setChecked(false);
                }
            }
        });

        return convertView;
    }

    private final class DataWrapper {
        public TextView imageView;
        public TextView socketNameView;
        public TextView socket_status_text;
        public TextView socket_id_text;
        public Switch aSwitch;

        public DataWrapper(TextView imageView, TextView socketNameView, TextView socket_status_text, TextView socket_id_text, Switch aSwitch) {
            this.imageView = imageView;
            this.socketNameView = socketNameView;
            this.socket_status_text = socket_status_text;
            this.socket_id_text = socket_id_text;
            this.aSwitch = aSwitch;
        }
    }


    public List<Socket> getData() {
        return data;
    }

    public void setData(List<Socket> data) {
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

}
