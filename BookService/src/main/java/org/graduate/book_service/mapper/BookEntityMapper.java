package org.graduate.book_service.mapper;

import org.graduate.book_service.data.BookEntity;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 * User: liuxianghai
 * Date: 2021/4/25
 * Time: 下午2:00
 */

public class BookEntityMapper implements RowMapper<BookEntity> {
    @Override
    public BookEntity mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
        BookEntity obj = new BookEntity();

        obj.setIsbn(rs.getLong("isbn"));
        obj.setBookName(rs.getString("book_name"));
        obj.setImageUrl(rs.getString("image_url"));

        return obj;
    }
}
