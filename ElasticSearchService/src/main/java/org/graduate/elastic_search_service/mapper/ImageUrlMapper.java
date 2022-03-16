package org.graduate.elastic_search_service.mapper;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 * User: liuxianghai
 * Date: 2021/2/12
 * Time: 下午9:54
 */

public class ImageUrlMapper implements RowMapper<String> {
    @Override
    public String mapRow(ResultSet resultSet, int i) throws SQLException {
        return resultSet.getString("image_url");
    }
}
