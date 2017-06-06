package com.cxs.androidlib.rx.retrofit.subscriber;

import com.cxs.androidlib.rx.retrofit.HttpResult;

import rx.Subscriber;

/**
 * Created by _CXS
 * Date:2016/7/27
 * Time:21:27
 */
public abstract class HttpResultSubscriber<T> extends Subscriber<HttpResult<T>> {

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        if (e != null) {
            e.printStackTrace();
            if (e.getMessage() == null) {
                _onError(new Throwable(e.toString()));
            } else {
                _onError(new Throwable(e.getMessage()));
            }
        } else {
            _onError(new Exception("null message"));
        }
    }

    @Override
    public void onNext(HttpResult<T> t) {

//        if (!t.error)
//            onSuccess(t.results);
//        else
//            _onError(new Throwable("error=true"));
        if (t.ecode.equals("0")){
            onSuccess(t.data);
        }
        else
            _onError(new Throwable(new Throwable()));
    }

    public abstract void onSuccess(T t);

    public abstract void _onError(Throwable e);
}
