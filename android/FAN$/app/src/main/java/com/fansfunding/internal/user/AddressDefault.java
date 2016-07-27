package com.fansfunding.internal.user;

import com.fansfunding.internal.Address;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 13616 on 2016/7/24.
 */
public class AddressDefault {

    //返回的结果
    private boolean result;

    //返回的状态码
    private int errCode;

    //返回的数据
    private Address.DataDetial data;

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

    public Address.DataDetial getData() {
        return data;
    }

    public void setData(Address.DataDetial data) {
        this.data = data;
    }


}
