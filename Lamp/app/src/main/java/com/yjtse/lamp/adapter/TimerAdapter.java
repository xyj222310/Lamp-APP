package com.yjtse.lamp.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;
import com.yjtse.lamp.Config;
import com.yjtse.lamp.R;
import com.yjtse.lamp.domain.Cron;
import com.yjtse.lamp.utils.NetAvailable;
import com.yjtse.lamp.utils.ToastUtils;

import java.util.Calendar;
import java.util.List;

public class TimerAdapter extends BaseAdapter {

    private Context context;
    private List<Cron> data;
    private int aysncItem;
    private Handler handler;
    private LayoutInflater layoutInflater;

    public TimerAdapter(Context context, Handler handler, int aysncItem) {
        this.context = context;
        this.handler = handler;
        this.aysncItem = aysncItem;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        RadioButton frequence;
        TextView textClock;
        RadioButton statusTobe;
        CheckBox checkBox;
        TextView triggerStatus;

        if (convertView == null) {
            convertView = layoutInflater.inflate(aysncItem, null);
            frequence = (RadioButton) convertView.findViewById(R.id.timer_frequence_radio_button);
            textClock = (TextView) convertView.findViewById(R.id.timer_text_clock);
            statusTobe = (RadioButton) convertView.findViewById(R.id.timer_status_tobe_radio_button);
            checkBox = (CheckBox) convertView.findViewById(R.id.timer_checkbox);
            triggerStatus = (TextView) convertView.findViewById(R.id.item_timer_trigger_status);
            convertView.setTag(new DataWrapper(frequence, textClock, triggerStatus, statusTobe, checkBox));
        } else {
            DataWrapper dataWrapper = (DataWrapper) convertView.getTag();
            frequence = dataWrapper.frequence;
            textClock = dataWrapper.textClock;
            statusTobe = dataWrapper.statusTobe;
            checkBox = dataWrapper.checkBox;
            triggerStatus = dataWrapper.triggerStatus;
        }
        checkBox.setFocusable(false);
        frequence.setFocusable(false);
        statusTobe.setFocusable(false);
        /*
        获取item数据
         */
        String cronValue = data.get(position).getCron();
//        if (CronDateUtils.getCronToDate(cronValue) == null) {
        String[] strings = cronValue.split("\\s+");
        String second = strings[0];
        String minute = strings[1];
        String hour = strings[2];
        String day = strings[3];
        String month = strings[4];
        String week = strings[5];
        String year = strings[6];
        /*
        绑定数据
         */
        statusTobe.setText("届时" + ("1".equals(data.get(position).getStatusTobe()) ? "打开" : "关闭"));
        statusTobe.setChecked("1".equals(data.get(position).getStatusTobe()));
        checkBox.setChecked("1".equals(data.get(position).getAvailable()));
        frequence.setText(("*".equals(month) && "*".equals(day) && "?".equals(week)) ? "每天重复" : "不重复");
        frequence.setChecked("*".equals(month) && "*".equals(day) && "?".equals(week));
        textClock.setText(hour + ":" + minute);
        if ("1".equals(data.get(position).getAvailable())) {
            Calendar calendar = Calendar.getInstance();
            if (Integer.valueOf(hour) == calendar.getTime().getHours()
                    && Integer.valueOf(minute) > calendar.get(Calendar.MINUTE)
                    && Integer.valueOf(minute) - Calendar.getInstance().get(Calendar.MINUTE) < 10) {
                triggerStatus.setText("trigger ready...");
            } else {
                triggerStatus.setText("standing By...");
            }
        } else {
            triggerStatus.setText(".");
        }
        /*
        监听函数
         */
        checkBox.setOnClickListener(v -> {
            if (!NetAvailable.isNetworkAvailable()) {
                ToastUtils.showToast(context, "网络连不上鸟！！！！！", Toast.LENGTH_LONG);
                checkBox.setChecked(!checkBox.isChecked());
            } else {
                String second2 = "00 ";
                String minute2 = minute + " ";
                String hour2 = hour + " ";
                String day2 = "";
                String month2 = month + " ";
                String week2 = "?" + " ";
                String year2 = year + " ";
                Calendar calendar = Calendar.getInstance();
                RequestParams params = new RequestParams();
                Message msg = Message.obtain();
                params.put("id", data.get(position).getId());
                params.put("socketId", data.get(position).getSocketId());
                params.put("ownerId", data.get(position).getOwnerId());
                params.put("statusTobe", data.get(position).getStatusTobe());
                params.put("available", checkBox.isChecked() ? 1 : 0);
                if (checkBox.isChecked() && !frequence.isChecked()) {
                    if (Integer.valueOf(hour) < calendar.getTime().getHours()) {
                        //设定的时间小于当前时间
                        day2 = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH) + 1) + " ";//明天
                        ToastUtils.showToast(v.getContext(), "定时将设置在明天" + hour + ":" + minute, Toast.LENGTH_LONG);
                    }
                    if (Integer.valueOf(hour) > calendar.getTime().getHours()) {
                        //设定的时间大于等于当前时间
                        day2 = calendar.get(Calendar.DAY_OF_MONTH) + " ";//今天
                        ToastUtils.showToast(v.getContext(), "定时将设置在今天" + hour + ":" + minute, Toast.LENGTH_LONG);
                    }
                    if (Integer.valueOf(hour) == calendar.getTime().getHours()) {
                        if (Integer.valueOf(minute) > calendar.get(Calendar.MINUTE)) {
                            //设定的分钟时间大于当前分钟
                            day2 = calendar.get(Calendar.DAY_OF_MONTH) + " ";//今天
                            ToastUtils.showToast(v.getContext(), "定时将设置在今天" + hour + ":" + minute, Toast.LENGTH_LONG);
                        } else {
                            day2 = calendar.get(Calendar.DAY_OF_MONTH) + 1 + " ";//明天
                            ToastUtils.showToast(v.getContext(), "定时将设置在明天" + hour + ":" + minute, Toast.LENGTH_LONG);
                        }
                    }
                    if (Integer.valueOf(hour) == calendar.getTime().getHours()
                            && Integer.valueOf(minute) > calendar.get(Calendar.MINUTE)
                            && Integer.valueOf(minute) - calendar.get(Calendar.MINUTE) < 2) {
                        ToastUtils.showToast(v.getContext(), "请至少设置在2分钟之后" + hour + ":" + minute, Toast.LENGTH_LONG);
                        checkBox.setChecked(!checkBox.isChecked());
                        params = null;
                    } else {
                        params.put("cron", second2 + minute2 + hour2 + day2 + month2 + week2 + year2);
                    }
                } else {
                    params.put("cron", cronValue);
                }
//                msg.arg1 = position;
//                msg.arg2 = checkBox.isChecked() ? 1 : 0;
                msg.obj = params;
                msg.what = Config.MESSAGE_WHAT_UPDATE_TIMER_AVAILABLE;
                handler.sendMessage(msg);
            }
        });
        return convertView;
    }

    public List<Cron> getData() {
        return data;
    }

    public void setData(List<Cron> data) {
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
        public RadioButton frequence;
        public TextView textClock;
        public TextView triggerStatus;
        public RadioButton statusTobe;
        public CheckBox checkBox;

        public DataWrapper(RadioButton frequence, TextView textClock, TextView triggerStatus, RadioButton statusTobe, CheckBox checkBox) {
            this.frequence = frequence;
            this.textClock = textClock;
            this.triggerStatus = triggerStatus;
            this.statusTobe = statusTobe;
            this.checkBox = checkBox;
        }
    }

}
