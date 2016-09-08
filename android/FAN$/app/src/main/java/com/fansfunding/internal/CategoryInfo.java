package com.fansfunding.internal;

import java.util.List;

/**
 * Created by 13616 on 2016/9/6.
 */
public class CategoryInfo {

    /**
     * result : true
     * errCode : 200
     * data : [{"name":"所有","icon":"","description":"所有","id":1},{"name":"美食","icon":"","description":"美食","id":2},{"name":"美妆","icon":"","description":"美妆","id":3},{"name":"摄影","icon":"","description":"摄影","id":4},{"name":"文字","icon":"","description":"文字","id":5},{"name":"穿搭","icon":"","description":"穿搭","id":6},{"name":"视频","icon":"","description":"视频","id":7},{"name":"绘画","icon":"","description":"绘画","id":8}]
     * token : null
     */

    private boolean result;
    private int errCode;
    /**
     * name : 所有
     * icon :
     * description : 所有
     * id : 1
     */

    private List<DataBean> data;

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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        private String name;
        private String icon;
        private String description;
        private int id;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }
}
