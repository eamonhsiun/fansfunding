package com.fansfunding.internal.user;

/**
 * Created by 13616 on 2016/8/10.
 */
public class EnsureFollowUser {
    //返回的结果
    private boolean result;

    //返回的状态码
    private int errCode;

    //是否关注
    private boolean data;

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


    public boolean isData() {
        return data;
    }

    public void setData(boolean data) {
        this.data = data;
    }
}
