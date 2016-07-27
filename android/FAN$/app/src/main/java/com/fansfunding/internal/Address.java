package com.fansfunding.internal;

import java.io.Serializable;
import java.util.List;

/**
 * Created by RJzz on 2016/7/18.
 */
public class Address implements Serializable{
    //返回的结果
    private boolean result;

    //返回的状态码
    private int errCode;

    //返回的数据
    private List<DataDetial> data;


    //token
    private String token;

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public int getErrCode() {
        return  errCode;
    }

    public void setErrCode(int errorCode) {
        this. errCode = errorCode;
    }

    public List<DataDetial> getData() {
        return data;
    }

    public void setData(List<DataDetial> data) {
        this.data = data;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
    public class DataDetial implements Serializable{
        //地址Id
        private int addressId;


        //用户姓名
        private String name;

        //电话
        private String phone;

        //省
        private String province;

        //市
        private String city;

        //地区
        private String district;

        //地址
        private String address;

        //邮编
        private int postCode;

        //是否是默认地址
        private int isDefault;


        public String getAddress() {
            return address;
        }

        public String getName() {
            return name;
        }

        public int getAddressId() {
            return addressId;
        }

        public String getCity() {
            return city;
        }

        public String getDistrict() {
            return district;
        }

        public String getPhone() {
            return phone;
        }

        public int getPost_code() {
            return postCode;
        }

        public String getProvince() {
            return province;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public void setDistrict(String district) {
            this.district = district;
        }

        public void setAddressId(int id) {
            this.addressId = id;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public void setPost_code(int post_code) {
            this.postCode = post_code;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public void setIs_default(int is_default) {
            this.isDefault = is_default;
        }

        public int getIs_default() {
            return this.isDefault;
        }
    }

    //返回内部类对象
    public DataDetial dataDetial() {
        return new DataDetial();
    }
}
