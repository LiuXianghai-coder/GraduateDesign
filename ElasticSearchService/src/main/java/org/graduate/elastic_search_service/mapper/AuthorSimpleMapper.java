package org.graduate.elastic_search_service.mapper;


import org.graduate.elastic_search_service.entity.AuthorSimple;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 * User: liuxianghai
 * Date: 2021/2/11
 * Time: 上午11:46
 */

public class AuthorSimpleMapper implements RowMapper<AuthorSimple> {
    @Override
    public AuthorSimple mapRow(ResultSet resultSet, int i) throws SQLException {
        AuthorSimple author = new AuthorSimple();

        author.setAuthorId((Number) resultSet.getObject("author_id"));
        author.setAuthorName(resultSet.getString("author_name"));

        return author;
    }
}
