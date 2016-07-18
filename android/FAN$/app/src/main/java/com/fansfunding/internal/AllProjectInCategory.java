package com.fansfunding.internal;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by 13616 on 2016/7/18.
 */
public class AllProjectInCategory {
    //返回的结果
    private boolean result;

    //返回的状态码
    private int errCode;

    //返回的数据
    private List<DataDetial> data;

    private class DataDetial{

        //项目缩略id
        private int id;

        //项目目录id
        private int categoryId;

        //封面
        private String cover;

        //项目描述
        private String description;

        //详情id
        private int detailId;

        //项目名称
        private String name;

        //发起人
        private String sponsor;

        //截止日期
        private long targetDeadline;

        //筹集金额
        private BigDecimal targetMoney;






    }

}
