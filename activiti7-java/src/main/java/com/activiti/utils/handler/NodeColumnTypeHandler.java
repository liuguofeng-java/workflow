package com.activiti.utils.handler;

import com.activiti.modules.entity.NodeColumnItem;
import com.alibaba.fastjson2.JSON;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * 数据转换
 *
 * @author liuguofeng
 * @date 2023/12/15 22:06
 **/
@MappedTypes({List.class})
@MappedJdbcTypes(JdbcType.VARCHAR)
public class NodeColumnTypeHandler extends BaseTypeHandler<List<NodeColumnItem>> {


    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, List<NodeColumnItem> nodeColumnItems, JdbcType jdbcType) throws SQLException {
        preparedStatement.setString(i, JSON.toJSONString(nodeColumnItems));
    }

    @Override
    public List<NodeColumnItem> getNullableResult(ResultSet resultSet, String s) throws SQLException {
        String str = resultSet.getString(s);
        return JSON.parseArray(str, NodeColumnItem.class);
    }

    @Override
    public List<NodeColumnItem> getNullableResult(ResultSet resultSet, int i) throws SQLException {
        String str = resultSet.getString(i);
        return JSON.parseArray(str, NodeColumnItem.class);
    }

    @Override
    public List<NodeColumnItem> getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        String str = callableStatement.getString(i);
        return JSON.parseArray(str, NodeColumnItem.class);
    }
}