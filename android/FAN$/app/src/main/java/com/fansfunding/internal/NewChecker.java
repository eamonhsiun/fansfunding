package com.fansfunding.internal;

/**
 * Created by 13616 on 2016/7/8.
 */
public class NewChecker {
    //返回的结果
    private boolean result;

    //返回的状态码
    private int errCode;

    //返回的数据
    private String data;

    //返回的token，即加密信息
    private String token;

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public int getErrCode() {
        return errCode;
    }

    public void setErrCode(int statusCode) {
        this.errCode = statusCode;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
