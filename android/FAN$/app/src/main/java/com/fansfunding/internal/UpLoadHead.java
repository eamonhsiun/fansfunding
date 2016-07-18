package com.fansfunding.internal;

/**
 * Created by 13616 on 2016/7/12.
 */
public class UpLoadHead {
    //结果
    private boolean result;

    //错误代码
    private int errCode;

    //数据
    private String data;


    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public int getErrCode() {
        return errCode;
    }

    public void setErrCode(int errCode) {
        this.errCode = errCode;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
