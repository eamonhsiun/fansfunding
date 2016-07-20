package com.fansfunding.internal;

/**
 * Created by 13616 on 2016/7/19.
 */
public class SendComment {
    //返回的结果
    private boolean result;

    //返回的状态码
    private int errCode;

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
}
