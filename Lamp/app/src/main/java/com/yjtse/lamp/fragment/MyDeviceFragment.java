package com.yjtse.lamp.fragment;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.zxing.client.android.CaptureActivity;
import com.loopj.android.http.RequestParams;
import com.yjtse.lamp.Config;
import com.yjtse.lamp.R;
import com.yjtse.lamp.adapter.DeviceAdapter;
import com.yjtse.lamp.asynchttp.TextNetWorkCallBack;
import com.yjtse.lamp.domain.Result;
import com.yjtse.lamp.domain.Socket;
import com.yjtse.lamp.requests.AsyncRequest;
import com.yjtse.lamp.ui.TabFragmentActivity;
import com.yjtse.lamp.utils.NetAvailable;
import com.yjtse.lamp.utils.SharedPreferencesUtil;
import com.yjtse.lamp.utils.ToastUtils;
import com.yjtse.lamp.widgets.MyListView;

import org.apache.http.Header;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class MyDeviceFragment extends BaseFragment {

    public static List<Socket> sockets_list;
    public static MyDeviceFragment instance = null;
    private MyListView fm_device_message_list;//设备列表
    private DeviceAdapter adapter;
    private TabFragmentActivity tabActivity;
    private Dialog dialog = null;

    private boolean isFirstLoading = true;

    private boolean isRefreshing = false;
    /**
     * 预加载标志，默认值为false，设置为true，表示已经预加载完成，可以加载数据
     */
    private boolean isPrepared;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                /*
                更新设备状态
                 */
                case Config.MESSAGE_WHAT_UPDATE_DEVICE_STATUS:
                    RequestParams params = new RequestParams();
                    params.put("socketId", sockets_list.get(msg.arg1).getSocketId());
                    params.put("ownerId", String.valueOf(SharedPreferencesUtil.query(getActivity(), Config.KEY_USERNAME, "String")));
                    params.put("status", String.valueOf(msg.arg2));
                    sendSocketUpdateRequest(Config.getRequestURL(Config.ACTION_DEVICE_UPDATE), params);
                    break;

                case Config.MESSAGE_WHAT_DEC_DEVICE:
                    String ownerId = (String) SharedPreferencesUtil.query(getActivity(), Config.KEY_USERNAME, "String");
//                    if (TextUtils.isEmpty(ownerId)) {
                    RequestParams requestParams = new RequestParams();
                    requestParams.add("socketId", sockets_list.get(msg.arg1 - 1).getSocketId());
                    requestParams.add("ownerId", ownerId);
                    sendSocketDeleteRequest(Config.getRequestURL(Config.ACTION_DEVICE_DELETE), requestParams, msg.arg1 - 1);

                    break;
                case Config.MESSAGE_WHAT_ADD_DEVICE:
                    if (msg.obj != null) {
                        String result = msg.obj.toString();
                        Gson gson = new Gson();
                        try {
                            startDialog("正在添加设备，请耐心等待");
                            Socket socket = gson.fromJson(result, Socket.class);
                            RequestParams params1 = new RequestParams();
                            params1.add("socketId", socket.getSocketId());
                            params1.add("socketName", socket.getSocketName());
                            params1.add("ownerId", (String) SharedPreferencesUtil.query(getActivity(), Config.KEY_USERNAME, "String"));
                            params1.add("status", socket.getStatus());

                            requestAddDevice(Config.getRequestURL(Config.ACTION_ADD_DEVICE),
                                    params1);
                            endDialog();
                        } catch (Exception e) {
                            e.printStackTrace();
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setTitle("Alert");
                            builder.setMessage("请扫描正确的二维码");
                            builder.setPositiveButton("确定", null);
                            builder.create().show();
                        }
                    }
                    break;
            }
        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_device, container, false);
        isPrepared = true;
        if (isFirstLoading) {
            isFirstLoading = false;
            startDialog("正在加载.....");
        }
        if (!NetAvailable.isNetworkAvailable(getActivity())) {
            ToastUtils.showToast(getActivity(), "请检查网络链接", Toast.LENGTH_LONG);
        } else {
            requestAllDevice();
        }
        endDialog();
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        instance = this;
        tabActivity = (TabFragmentActivity) getActivity();
        tabActivity.setActionBarSettingOnClickListener(new TabFragmentActivity.ActionBarSettingOnClickListener() {
            @Override
            public void onMySettingFirstBtnClick() {
                Intent openCameraIntent = new Intent(getActivity(), CaptureActivity.class);
                startActivityForResult(openCameraIntent, 0);
            }
        });

        fm_device_message_list = (MyListView) getActivity().findViewById(R.id.fm_device_message_list);//关联ListView组件,注意getActivity
//        fm_device_message_multiply_setting = (Button) getActivity().findViewById(R.id.fm_device_message_multiply_setting);
        sockets_list = new ArrayList<>();
        adapter = new DeviceAdapter(getActivity(), handler, R.layout.item_device);
        adapter.setData(sockets_list);
        fm_device_message_list.setAdapter(adapter);
        fm_device_message_list.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
                /**
                 * 详情页面，设置定时
                 */
//                    Intent intent = new Intent(getActivity(), ControlActivity.class);
//                    intent.putExtra("GateNumber", gateNumber);//网关号
//                    intent.putExtra("ip", ip);
//                    intent.putExtra("port", port);
//                    intent.putExtra("DeviceSelectedItem", clickedEntity);//将被点击的记录放入DeviceSelectedItem值中，利用Intent机制传递过去
//                    startActivity(intent);
//
            }
        });

        /**
         * 长按删除监听事件
         */
        fm_device_message_list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Notice");
                builder.setMessage("您将删除此设备？");
                builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Message msg = Message.obtain();
                        msg.arg1 = position;
                        msg.what = Config.MESSAGE_WHAT_DEC_DEVICE;
                        handler.sendMessage(msg);
                    }
                });
                builder.setNegativeButton("no", null);
                builder.create().show();
                return false;
            }
        });

        fm_device_message_list.setOnRefreshListener(new MyListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                isRefreshing = true;
//                startDialog("加载中");
                if (!NetAvailable.isNetworkAvailable(getActivity())) {
                    ToastUtils.showToast(getActivity(), "请检查网络链接", Toast.LENGTH_LONG);
                } else {
                    requestAllDevice();
                }
                endDialog();
            }
        });
        tabActivity.setActionBarMyDeviceOnClickListener(new TabFragmentActivity.ActionBarMyDeviceOnClickListener() {
            @Override
            public void onMyDeviceFirstBtnClick() {
                Intent openCameraIntent = new Intent(getActivity(), CaptureActivity.class);
                startActivityForResult(openCameraIntent, Config.CAMERA_REQUEST_CODE);
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == Config.CAMERA_REQUEST_CODE) {
            Bundle bundle = data.getExtras();
            String scanResult = bundle.getString("result");
            Message msg = Message.obtain();
            msg.what = Config.MESSAGE_WHAT_ADD_DEVICE;
            msg.obj = scanResult;
            handler.sendMessage(msg);
        }
    }

    private void requestAllDevice() {

        String url = Config.getRequestURL(Config.ACTION_GET_ALL_DEVICE);

        RequestParams params = new RequestParams();
        params.add("ownerId", String.valueOf(SharedPreferencesUtil.query(getActivity(), Config.KEY_USERNAME, "String")));

        AsyncRequest.ClientGet(url, params, new TextNetWorkCallBack() {
            @TargetApi(Build.VERSION_CODES.KITKAT)
            @Override
            public void onMySuccess(int statusCode, Header[] header, final String result) {

                final List<Socket> socketList = new Gson().fromJson(result, new TypeToken<List<Socket>>() {
                }.getType());
                if (result != null) {
                    if (socketList != null) {
                        ToastUtils.showToast(getActivity(), "刷新成功", Toast.LENGTH_LONG);
                        sockets_list = new ArrayList<>();
                        sockets_list = socketList;
                        adapter.setData(sockets_list);
                        adapter.notifyDataSetChanged();
                    }
                } else {
                    ToastUtils.showToast(getActivity(), "网络请求失败", Toast.LENGTH_LONG);
                }
                endDialog();
            }

            @Override
            public void onMyFailure(int statusCode, Header[] header, String result, Throwable
                    th) {
                ToastUtils.showToast(getActivity(), "查询错误，请检查网络", Toast.LENGTH_LONG);
                endDialog();
            }
        });


    }

    private void requestAddDevice(final String url, RequestParams params) {
        AsyncRequest.ClientPost(url, params, new TextNetWorkCallBack() {
            @Override
            public void onMySuccess(int statusCode, Header[] header, String result) {
                Gson gson = new Gson();
                final Result<Socket> socketResult = gson.fromJson(result, new TypeToken<Result<Socket>>() {
                }.getType());
                if (socketResult != null) {
                    if (socketResult.isSuccess() && !TextUtils.isEmpty(socketResult.getData().getSocketId())) {
                        /**
                         * 更新一下列表
                         */
                        sockets_list.add(socketResult.getData());
                        adapter.setData(sockets_list);
                        ToastUtils.showToast(getActivity(), "添加设备成功", Toast.LENGTH_LONG);
                        adapter.notifyDataSetChanged();

                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle("Alert");
                        builder.setMessage("添加失败：" + socketResult.getError());
                        builder.setPositiveButton("确定", null);
                        builder.create().show();
                    }
                }
            }

            @Override
            public void onMyFailure(int statusCode, Header[] header, String result, Throwable th) {
                ToastUtils.showToast(getActivity(), "添加失败，请检查设备是否已经被注册？", Toast.LENGTH_LONG);
            }
        });
    }

    private void sendSocketUpdateRequest(final String url, RequestParams params) {
        AsyncRequest.ClientPost(url, params, new TextNetWorkCallBack() {
            @Override
            public void onMySuccess(int statusCode, Header[] header, String result) {
                Gson gson = new Gson();
                final Result result1 = gson.fromJson(result, Result.class);
                if (result1 != null) {
                    if (result1.isSuccess()) {
                        ToastUtils.showToast(getActivity(), "设置成功", Toast.LENGTH_SHORT);
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle("Notice");
                        builder.setMessage("更新失败：" + result1.getError());
                        builder.setPositiveButton("确定", null);
                        builder.create().show();
                    }
                }
            }

            @Override
            public void onMyFailure(int statusCode, Header[] header, String result, Throwable th) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Alert");
                builder.setMessage("网络请求失败！");
                builder.create().show();
            }
        });
    }

    private void sendSocketDeleteRequest(final String url, final RequestParams params, final int position) {
        AsyncRequest.ClientPost(url, params, new TextNetWorkCallBack() {
            @Override
            public void onMySuccess(int statusCode, Header[] header, String result) {
                Gson gson = new Gson();
                Result result1 = gson.fromJson(result, Result.class);
                if (result1.isSuccess()) {
                    ToastUtils.showToast(getActivity(), "成功删除", Toast.LENGTH_LONG);
                    sockets_list.remove(position);
                    adapter.setData(sockets_list);
                    adapter.notifyDataSetChanged();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Notice");
                    builder.setMessage("删除失败：" + result1.getError());
                }
            }

            @Override
            public void onMyFailure(int statusCode, Header[] header, String result, Throwable th) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Notice");
                builder.setMessage("删除失败");
            }
        });


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
            fm_device_message_list.onRefreshComplete();
            isRefreshing = false;
        }
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isFirstLoading = true;
    }

    @Override
    protected void setlazyLoad() {
        super.setlazyLoad();
        if (!isPrepared || !isVisible) {
            return;
        }
    }

}
