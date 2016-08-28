package com.fansfunding.fan.message.db;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 *
 *
 * Created by RJzz on 2016/8/28.
 */

@Table(name = "Notification")
public class NotiifcationTable extends Model {
    @Column(name = "type")
    private int type;
}
