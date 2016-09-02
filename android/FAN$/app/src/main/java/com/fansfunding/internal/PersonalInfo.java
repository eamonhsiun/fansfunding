package com.fansfunding.internal;

import java.io.Serializable;

/**
 * Created by 13616 on 2016/7/14.
 */
public class PersonalInfo implements Serializable{

    //返回的结果
    private boolean result;

    //返回的状态码
    private int errCode;

    //返回的数据
    private DataDetial data;

    //token
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

    public void setErrCode(int errCode) {
        this.errCode = errCode;
    }

    public DataDetial getData() {
        return data;
    }

    public void setData(DataDetial data) {
        this.data = data;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }


    public class DataDetial implements Serializable {

        //用户名
        private String name;

        //用户昵称
        private String nickname;

        //用户手机号码
        private String phone;

        //是否网红
        private int is_red;

        //头像url
        private String head;

        //email
        private String email;

        //个人介绍
        private String intro;

        //动态数量
        private int momentNum;

        //粉丝数量
        private int  followerNum;

        //关注数量
        private int followingNum;

        private RealInfo realInfo;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }
        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public int getIs_red() {
            return is_red;
        }

        public void setIs_red(int is_red) {
            this.is_red = is_red;
        }

        public String getHead() {
            return head;
        }

        public void setHead(String head) {
            this.head = head;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }


        public RealInfo getRealInfo() {
            return realInfo;
        }

        public void setRealInfo(RealInfo realInfo) {
            this.realInfo = realInfo;
        }

        public String getIntro() {
            return intro;
        }

        public void setIntro(String intro) {
            this.intro = intro;
        }

        public int getMomentNum() {
            return momentNum;
        }

        public void setMomentNum(int momentNum) {
            this.momentNum = momentNum;
        }

        public int getFollowerNum() {
            return followerNum;
        }

        public void setFollowerNum(int followerNum) {
            this.followerNum = followerNum;
        }

        public int getFollowingNum() {
            return followingNum;
        }

        public void setFollowingNum(int followingNum) {
            this.followingNum = followingNum;
        }


        public class RealInfo implements Serializable{
            //真实姓名
            private String realName;

            //性别
            private byte sex;

            //出生地
            private String birthPlace;

            //出生日期
            private long birthday;

            public String getRealName() {
                return realName;
            }

            public void setRealName(String realName) {
                this.realName = realName;
            }

            public byte getSex() {
                return sex;
            }

            public void setSex(byte sex) {
                this.sex = sex;
            }

            public String getBirthPlace() {
                return birthPlace;
            }

            public void setBirthPlace(String birthPlace) {
                this.birthPlace = birthPlace;
            }

            public long getBirthday() {
                return birthday;
            }

            public void setBirthday(long birthday) {
                this.birthday = birthday;
            }

        }

    }
}
