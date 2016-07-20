package com.fansfunding.internal;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by 13616 on 2016/7/19.
 */
public class ProjectDetailReward {
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
        private List<ProjectReward> list;

        public List<ProjectReward> getList() {
            return list;
        }

        public void setList(List<ProjectReward> list) {
            this.list = list;
        }
    }

    public class ProjectReward{

        //回馈id
        private int id;

        //项目id
        private int projectId;

        //标题
        private String title;

        //描述
        private String description;

        //限制条件
        private BigDecimal limitation;

        //图片
        private List<String> images;


        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getProjectId() {
            return projectId;
        }

        public void setProjectId(int projectId) {
            this.projectId = projectId;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public BigDecimal getLimitation() {
            return limitation;
        }

        public void setLimitation(BigDecimal limitation) {
            this.limitation = limitation;
        }

        public  List<String> getImages() {
            return images;
        }

        public void setImages( List<String> images) {
            this.images = images;
        }
    }
}
