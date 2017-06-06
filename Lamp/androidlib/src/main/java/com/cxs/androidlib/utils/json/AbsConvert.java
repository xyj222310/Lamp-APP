package com.cxs.androidlib.utils.json;

/**
 * Created by _CXS
 * Date:2016/5/13
 * Time:11:39
 */
public abstract class AbsConvert<T> {

    abstract T parseData(String result);
}
