package org.graduate.book_service.mapper;

import org.graduate.book_service.data.AuthorSimple;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 * User: liuxianghai
 * Date: 2021/4/25
 * Time: 下午2:39
 */

public class AuthorSimpleMapper implements RowMapper<AuthorSimple> {
    @Override
    public AuthorSimple mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
        AuthorSimple authorSimple = new AuthorSimple();

        authorSimple.setAuthorId((Number) rs.getObject("author_id"));
        authorSimple.setAuthorName(rs.getString("author_name"));

        return authorSimple;
    }
}
