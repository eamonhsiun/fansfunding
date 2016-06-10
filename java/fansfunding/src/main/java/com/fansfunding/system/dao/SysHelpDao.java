package com.fansfunding.system.dao;

import com.fansfunding.system.entity.SysHelp;
import java.util.List;

public interface SysHelpDao {
    int deleteByPrimaryKey(Integer id);

    int insert(SysHelp record);

    SysHelp selectByPrimaryKey(Integer id);

    List<SysHelp> selectAll();

    int updateByPrimaryKey(SysHelp record);
}