package org.shamee.common.db.typehandler;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.sql.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;



/**
 * PostgreSQL 数组类型与 Java List<String> 的转换处理器
 * 支持 text[], varchar[] 等字符串数组类型
 *
 * @author shamee
 * @date 2025-07-12
 */
@MappedTypes(List.class)
@MappedJdbcTypes(JdbcType.ARRAY)
public class ArrayToListTypeHandler extends BaseTypeHandler<List<String>> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i,
                                    List<String> parameter, JdbcType jdbcType)
            throws SQLException {
        if (parameter == null) {
            ps.setNull(i, Types.ARRAY);
            return;
        }

        // 创建 PostgreSQL 数组对象
        String[] strings = parameter.toArray(new String[0]);
        Array array = ps.getConnection().createArrayOf("varchar", strings);
        ps.setArray(i, array);
    }

    @Override
    public List<String> getNullableResult(ResultSet rs, String columnName)
            throws SQLException {
        return arrayToList(rs.getArray(columnName));
    }

    @Override
    public List<String> getNullableResult(ResultSet rs, int columnIndex)
            throws SQLException {
        return arrayToList(rs.getArray(columnIndex));
    }

    @Override
    public List<String> getNullableResult(CallableStatement cs, int columnIndex)
            throws SQLException {
        return arrayToList(cs.getArray(columnIndex));
    }

    private List<String> arrayToList(Array array) throws SQLException {
        if (array == null) {
            return Collections.emptyList();
        }

        // 获取数组内容并转换为 List
        Object arrayObject = array.getArray();
        if (arrayObject instanceof String[]) {
            return Arrays.asList((String[]) arrayObject);
        } else if (arrayObject instanceof Object[] objects) {
            // 处理其他可能的对象数组情况
            String[] strings = new String[objects.length];
            for (int i = 0; i < objects.length; i++) {
                strings[i] = objects[i] != null ? objects[i].toString() : null;
            }
            return Arrays.asList(strings);
        }
        return Collections.emptyList();
    }
}
