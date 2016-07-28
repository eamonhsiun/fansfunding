package com.fansfunding.internal;

import java.io.Serializable;
import java.util.List;

/**
 * Created by RJzz on 2016/7/18.
 */
public class Address implements Serializable{
    //返回的结果
    private boolean result;

    //返回的状态码
    private int errCode;

    //返回的数据
    private List<SingleAddress> data;


    //token
    private String token;

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public int getErrCode() {
        return  errCode;
    }

    public void setErrCode(int errorCode) {
        this. errCode = errorCode;
    }

    public List<SingleAddress> getData() {
        return data;
    }

    public void setData(List<SingleAddress> data) {
        this.data = data;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
    //返回内部类对象
    public SingleAddress dataDetial() {
        return new SingleAddress();
    }
}
