package com.fansfunding.internal.user;

import com.fansfunding.internal.SingleAddress;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by 13616 on 2016/7/28.
 */
public class OrderDetail {
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


    public class DataDetial {
        //项目id
        private int projectId;

        //项目分类id
        private int categoryId;

        //项目名
        private String projectName;

        //项目回馈id
        private int feedbackId;

        //项目回馈方式名称
        private String feedbackTitle;

        //支付时间
        private long paidTime;

        //总费用
        private BigDecimal totalFee;

        //回馈描述
        private String feedbackDesc;

        //订单状态
        private String orderStatus;

        //订单编号
        private String orderNo;

        //支付宝编号
        private String tradeNo;

        private SingleAddress address;

        //回馈图片
        private List<String> feedbackImages;


        public int getProjectId() {
            return projectId;
        }

        public void setProjectId(int projectId) {
            this.projectId = projectId;
        }

        public int getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(int categoryId) {
            this.categoryId = categoryId;
        }

        public String getProjectName() {
            return projectName;
        }

        public void setProjectName(String projectName) {
            this.projectName = projectName;
        }

        public int getFeedbackId() {
            return feedbackId;
        }

        public void setFeedbackId(int feedbackId) {
            this.feedbackId = feedbackId;
        }

        public String getFeedbackTitle() {
            return feedbackTitle;
        }

        public void setFeedbackTitle(String feedbackTitle) {
            this.feedbackTitle = feedbackTitle;
        }

        public long getPaidTime() {
            return paidTime;
        }

        public void setPaidTime(long paidTime) {
            this.paidTime = paidTime;
        }

        public BigDecimal getTotalFee() {
            return totalFee;
        }

        public void setTotalFee(BigDecimal totalFee) {
            this.totalFee = totalFee;
        }

        public String getFeedbackDesc() {
            return feedbackDesc;
        }

        public void setFeedbackDesc(String feedbackDesc) {
            this.feedbackDesc = feedbackDesc;
        }

        public String getOrderStatus() {
            return orderStatus;
        }

        public void setOrderStatus(String orderStatus) {
            this.orderStatus = orderStatus;
        }


        public String getOrderNo() {
            return orderNo;
        }

        public void setOrderNo(String orderNo) {
            this.orderNo = orderNo;
        }

        public List<String> getFeedbackImages() {
            return feedbackImages;
        }

        public void setFeedbackImages(List<String> feedbackImages) {
            this.feedbackImages = feedbackImages;
        }


        public String getTradeNo() {
            return tradeNo;
        }

        public void setTradeNo(String tradeNo) {
            this.tradeNo = tradeNo;
        }

        public SingleAddress getAddress() {
            return address;
        }

        public void setAddress(SingleAddress address) {
            this.address = address;
        }
    }
}
