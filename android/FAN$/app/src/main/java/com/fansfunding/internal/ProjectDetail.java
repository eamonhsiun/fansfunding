package com.fansfunding.internal;

/**
 * Created by 13616 on 2016/7/19.
 */
public class ProjectDetail {
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

        //详情页的图片
        private String iamges;

        //项目id
        private int id;

        //项目内容
        private String content;

        //项目影片
        private String video;

        //其他信息

        private String other;

        public String getIamges() {
            return iamges;
        }

        public void setIamges(String iamges) {
            this.iamges = iamges;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getVideo() {
            return video;
        }

        public void setVideo(String video) {
            this.video = video;
        }

        public String getOther() {
            return other;
        }

        public void setOther(String other) {
            this.other = other;
        }
    }
}
