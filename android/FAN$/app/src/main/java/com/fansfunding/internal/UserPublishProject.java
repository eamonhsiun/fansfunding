package com.fansfunding.internal;

import java.util.List;

/**
 * Created by 13616 on 2016/7/22.
 */
public class UserPublishProject {

    //返回的结果
    private boolean result;

    //返回的状态码
    private int errCode;

    //返回的数据
    private DataDetial data;

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

    public DataDetial getData() {
        return data;
    }

    public void setData(DataDetial data) {
        this.data = data;
    }

    public class DataDetial{
        //项目列表
        private List<ProjectInfo> list;

        public List<ProjectInfo> getList() {
            return list;
        }

        public void setList(List<ProjectInfo> list) {
            this.list = list;
        }
    }

}
