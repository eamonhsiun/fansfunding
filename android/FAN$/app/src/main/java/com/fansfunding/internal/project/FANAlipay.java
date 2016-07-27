package com.fansfunding.internal.project;

/**
 * Created by 13616 on 2016/7/25.
 */
public class FANAlipay {

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

        //订单编号
        private String orderNo;

        //订单签名
        private String signedOrder;


        public String getOrderNo() {
            return orderNo;
        }

        public void setOrderNo(String orderNo) {
            this.orderNo = orderNo;
        }

        public String getSignedOrder() {
            return signedOrder;
        }

        public void setSignedOrder(String signedOrder) {
            this.signedOrder = signedOrder;
        }
    }
}
