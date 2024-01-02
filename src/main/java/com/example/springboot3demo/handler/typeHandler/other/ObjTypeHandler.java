package com.example.springboot3demo.handler.typeHandler.other;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@MappedJdbcTypes(JdbcType.VARBINARY)
public abstract class ObjTypeHandler<T> extends BaseTypeHandler<T> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, T parameter, JdbcType jdbcType)
            throws SQLException {
        String content = JSON.toJSONString(parameter);
        ps.setString(i, content);
    }

    @Override
    public T getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return this.getObjByJsonString(rs.getString(columnName));
    }

    @Override
    public T getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return this.getObjByJsonString(rs.getString(columnIndex));
    }

    @Override
    public T getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return this.getObjByJsonString(cs.getString(columnIndex));
    }

    private T getObjByJsonString(String content) {
        return StringUtils.isBlank(content) ? null : JSON.parseObject(content, this.specificType());
    }

    /**
     * 具体类型，由子类提供
     *
     * @return 具体类型
     */
    protected abstract TypeReference<T> specificType();
}

