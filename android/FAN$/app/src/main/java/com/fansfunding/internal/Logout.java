package com.fansfunding.internal;

/**
 * Created by 13616 on 2016/7/8.
 */
public class Logout {
    //返回的结果
    private boolean result;

    //返回的状态码
    private int errCode;

    //返回的数据
    private DataDetial data;

    private String token;
    public String getToken(){
        return token;
    }

    public void setToken(String token){
        this.token=token;
    }
    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public int getErrCode() {
        return errCode;
    }

    public void setErrCode(int errorCode) {
        this.errCode = errorCode;
    }

    public DataDetial getData() {
        return data;
    }

    public void setData(DataDetial data) {
        this.data = data;
    }

    public class DataDetial{

    }

}
