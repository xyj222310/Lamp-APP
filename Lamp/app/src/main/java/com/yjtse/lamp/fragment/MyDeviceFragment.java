package com.yjtse.lamp.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.yjtse.lamp.R;
import com.yjtse.lamp.ui.TabFragmentActivity;
import com.yjtse.lamp.widgets.MyListView;

public class MyDeviceFragment extends BaseFragment {

    private MyListView fm_device_message_list;//设备列表

    private Button fm_device_message_multiply_setting;

    private TabFragmentActivity tabActivity;
    /**
     * 默认多选，第一次点击改变状态
     */
    private boolean isClickMultiple = true;

    public static MyDeviceFragment instance = null;

    private String gateNumber;

    private Dialog dialog = null;

    private boolean isFirstLoading = true;

    private boolean isRefreshing = false;
    /**
     * 预加载标志，默认值为false，设置为true，表示已经预加载完成，可以加载数据
     */
    private boolean isPrepared;

    private String ip = "";
    private int port = 0;

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
            fm_device_message_list.onRefreshComplete();
            isRefreshing = false;
        }
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_device_message, container, false);
        isPrepared = true;
        return view;
    }

    @Override
    protected void setlazyLoad() {
        super.setlazyLoad();
        if (!isPrepared || !isVisible) {
            return;
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        instance = this;
        fm_device_message_list = (MyListView) getActivity().findViewById(R.id.fm_device_message_list);//关联ListView组件,注意getActivity
        fm_device_message_multiply_setting = (Button) getActivity().findViewById(R.id.fm_device_message_multiply_setting);
        fm_device_message_list.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {

//                    Intent intent = new Intent(getActivity(), ControlActivity.class);
//                    intent.putExtra("GateNumber", gateNumber);//网关号
//                    intent.putExtra("ip", ip);
//                    intent.putExtra("port", port);
//                    intent.putExtra("DeviceSelectedItem", clickedEntity);//将被点击的记录放入DeviceSelectedItem值中，利用Intent机制传递过去
//                    startActivity(intent);
//
            }
        });

        fm_device_message_multiply_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                Bundle bundle = new Bundle();
//                bundle.putSerializable("ChoiceItem", list);
//                bundle.putString("ip", ip);
//                bundle.putInt("port", port);
//                Intent intent = new Intent(getActivity(), UnifiedSettingActivity.class);
//                intent.putExtras(bundle);
//                startActivity(intent);
            }
        });
        tabActivity = (TabFragmentActivity) getActivity();
    }

    @Override
    public void onStart() {
        super.onStart();
        fm_device_message_multiply_setting.setVisibility(View.GONE);
//        adapter.setShowCheckBox(false);
//        adapter.setAllItemDisChecked();
//        adapter.notifyDataSetChanged();
        isClickMultiple = true;
        tabActivity.setActionBarMyDeviceOnClickListener(new TabFragmentActivity.ActionBarMyDeviceOnClickListener() {
            @Override
            public void onMyDeviceFirstBtnClick() {
                if (isClickMultiple) {
//                    if (adapter.getCount() <= 0) { //如果适配器中没有数据，不显示统一设置按钮
//                        return;
//                    }
                    fm_device_message_multiply_setting.setVisibility(View.VISIBLE);
//                    adapter.setShowCheckBox(true);
//                    adapter.setAllItemChecked();
//                    adapter.notifyDataSetChanged();
                } else {
                    fm_device_message_multiply_setting.setVisibility(View.GONE);
//                    adapter.setShowCheckBox(false);
//                    adapter.setAllItemDisChecked();
//                    adapter.notifyDataSetChanged();
                }
                isClickMultiple = !isClickMultiple;
            }
        });
    }
}
