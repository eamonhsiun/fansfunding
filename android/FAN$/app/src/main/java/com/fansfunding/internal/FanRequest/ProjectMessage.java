package com.fansfunding.internal.FanRequest;

import com.fansfunding.internal.ProjectInfo;

/**
 * Created by 13616 on 2016/7/23.
 */
public class ProjectMessage {
    //返回的结果
    private boolean result;

    //返回的状态码
    private int errCode;

    //返回的数据
    private ProjectInfo data;

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

    public ProjectInfo getData() {
        return data;
    }

    public void setData(ProjectInfo data) {
        this.data = data;
    }
}
