package com.cxs.androidlib.rx.retrofit;

/**
 * Created by _CXS
 * Date:2016/7/27
 * Time:15:57
 */
public class HttpResult<T> {

//    public boolean error;
//    public T results;
    //@SerializedName(value = "results", alternate = {"result"})
    public String ecode;
    public String emsg;
    public T data;
}
