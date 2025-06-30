package com.github.mathbook3948.zzibot.handler;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.apache.ibatis.type.TypeHandler;

import java.sql.*;
import java.util.Arrays;
import java.util.List;

@MappedJdbcTypes(JdbcType.ARRAY)
@MappedTypes(List.class)
public class StringArrayTypeHandler implements TypeHandler<List<String>> {

    @Override
    public void setParameter(PreparedStatement ps, int i, List<String> parameter, JdbcType jdbcType)
            throws SQLException {
        Connection connection = ps.getConnection();
        Array array = connection.createArrayOf("text", parameter.toArray());
        ps.setArray(i, array);
    }

    @Override
    public List<String> getResult(ResultSet rs, String columnName) throws SQLException {
        Array array = rs.getArray(columnName);
        return Arrays.asList((String[]) array.getArray());
    }

    @Override
    public List<String> getResult(ResultSet rs, int columnIndex) throws SQLException {
        Array array = rs.getArray(columnIndex);
        return Arrays.asList((String[]) array.getArray());
    }

    @Override
    public List<String> getResult(CallableStatement cs, int columnIndex) throws SQLException {
        Array array = cs.getArray(columnIndex);
        return Arrays.asList((String[]) array.getArray());
    }
}
