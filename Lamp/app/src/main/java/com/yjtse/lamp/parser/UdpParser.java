package com.yjtse.lamp.parser;

import com.aircontrol.demo.domain.InsideMachineState;
import com.aircontrol.demo.domain.LocalInsideNumber;
import com.aircontrol.demo.utils.UdpOrderUtil;

/**
 * Created by Silenoff on 2016/11/24.
 */

public class UdpParser {

    /**
     * 解析接收到设置wifi用户名和密码的返回状态
     *
     * @param data
     * @return
     */
    public static boolean parseUdpSetSSID(byte[] data) {
        if (data.length != UdpOrderUtil.getInstance().getFrameLength(UdpOrderUtil.getInstance().type_return_config_ssid)) {
            return false;
        }
        if (data[0] == (byte) 0xAA && data[1] == (byte) 0xB1 && data[2] == (byte) 0xB1 && data[3] == (byte) 0x55) {
            return true;
        }
        return false;
    }

    /**
     * 解析查询内机数量，返回内机数量
     *
     * @param data
     * @return
     */
    public static LocalInsideNumber parseUdpSearch(byte[] data) {
        LocalInsideNumber gate;
        if (data.length != UdpOrderUtil.getInstance().getFrameLength(UdpOrderUtil.getInstance().type_return_number)) {
            return null;
        }
        byte[] check = new byte[data.length - 2];
        System.arraycopy(data, 1, check, 0, check.length);
        if (data[0] == (byte) 0xAA && data[1] == (byte) 0x81 && UdpOrderUtil.getInstance().CheckSum(check, check.length) && data[data.length - 1] == (byte) 0x55) {
            gate = new LocalInsideNumber();
            gate.setNumber((int) data[2]);
            gate.setIdentifier(byteToString(data[3],data[4],data[5],data[6]));
            return gate;
        }
        return null;
    }
                                          //高位                           低位
    public static String byteToString(byte high1, byte high0, byte low1, byte low0) {
        int v0 = (high1 & 0xff) << 24;//&0xff将byte值无差异转成int,避免Java自动类型提升后,会保留高位的符号位
        int v1 = (high0 & 0xff) << 16;
        int v2 = (low1 & 0xff) << 8;
        int v3 = (low0 & 0xff) ;
        int s = v0 | v1 | v2 | v3;
        return String.valueOf(s);
    }

    /**
     * 解析查询内机状态，返回内机状态的结构体，此处
     *
     * @param data
     * @return
     */
    public static InsideMachineState parseUdpSearchInside(byte[] data) {
        InsideMachineState state = null;
        if (data.length != UdpOrderUtil.getInstance().getFrameLength(UdpOrderUtil.getInstance().type_return_inside)) {
            return null;
        }
        byte[] check = new byte[data.length - 2];
        System.arraycopy(data, 1, check, 0, check.length);
        if (data[0] == (byte) 0xAA && data[1] == (byte) 0x91 && UdpOrderUtil.getInstance().CheckSum(check, check.length) && data[data.length - 1] == (byte) 0x55) {
            state = new InsideMachineState();
            state.setInsideNumber(data[2]);
            state.setMode(data[3]);
            state.setWindSpeed(data[4]);
            state.setSetTemperature(data[5]);
            state.setInsideTemperature((byteToShort(data[6],data[7])) / 10.0f);
            state.setInsideHumidity((byteToShort(data[8],data[9])) / 10.0f);
            state.setInsidePM2_5((byteToShort(data[10],data[11])) / 10.0f);
        }
        return state;
    }

    public static short byteToShort(byte high,byte low) {
        short s = 0;
        short s0 = (short) (low & 0xff);// 最低位
        short s1 = (short) (high & 0xff);
        s1 <<= 8;
        s = (short) (s0 | s1);
        return s;
    }


    /**
     * 解析设置内机
     * @param data
     * @return
     */
    public static boolean parseUdpInsideSet(byte[] data) {
        if (data.length != UdpOrderUtil.getInstance().getFrameLength(UdpOrderUtil.getInstance().type_return_set_inside)) {
            return false;
        }
        if (data[0] == (byte) 0xAA && data[1] == (byte) 0xA1 && data[2] == (byte) 0xA1 && data[3] == (byte) 0x55) {
            return true;
        }
        return false;
    }
}
