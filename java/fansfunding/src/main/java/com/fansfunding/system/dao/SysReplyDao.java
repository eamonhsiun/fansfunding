package com.fansfunding.system.dao;

import com.fansfunding.system.entity.SysReply;
import java.util.List;

public interface SysReplyDao {
    int deleteByPrimaryKey(Integer id);

    int insert(SysReply record);

    SysReply selectByPrimaryKey(Integer id);

    List<SysReply> selectAll();

    int updateByPrimaryKey(SysReply record);
}