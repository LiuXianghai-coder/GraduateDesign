package org.graduate.book_service.implment;

import org.graduate.book_service.entity.RelateNode;
import org.graduate.book_service.mapper.AuthorSimpleMapper;
import org.graduate.book_service.mapper.BookEntityMapper;
import org.graduate.book_service.data.BookEntity;
import org.graduate.book_service.repository.BookEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: liuxianghai
 * Date: 2021/4/25
 * Time: 下午2:04
 */
@Component
public class BookEntityImpl implements BookEntityService {
    private final static int THRESHOLD = 30; // 查找的数量最大阈值

    private final static int RELATE_THRESHOLD = 5;

    private final JdbcTemplate jdbcTemplate;

    private final static String selectAuthorSql =
            "SELECT cb.author_id, author.author_name " +
            "FROM author " +
            "         JOIN created_book cb on author.author_id = cb.author_id " +
            "         JOIN book b on b.isbn = cb.isbn " +
            "WHERE b.isbn = ?";

    @Autowired
    public BookEntityImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<BookEntity> findThroughBooksByUserId(Long userId) {
        String sql = "SELECT DISTINCT (b.isbn), b.book_name, bi.image_url" +
                     " FROM book b JOIN book_image bi on b.isbn = bi.isbn " +
                    " JOIN user_book_browse ubb on b.isbn = ubb.isbn AND user_id=?";

        List<BookEntity> bookEntities = jdbcTemplate
                .query(sql, new BookEntityMapper(), userId);

        setAuthorList(bookEntities);

        return bookEntities;
    }

    @Override
    public List<BookEntity> findCollectedBooksByUserId(Long userId) {
        String sql = "SELECT DISTINCT (b.isbn), b.book_name, bi.image_url " +
                "FROM book b " +
                "         JOIN book_image bi on b.isbn = bi.isbn " +
                "         JOIN user_book_collection ubc on b.isbn = ubc.isbn " +
                "AND user_id = ? AND ubc.is_collection=true";

        List<BookEntity> bookEntities = jdbcTemplate
                .query(sql, new BookEntityMapper(), userId);

        setAuthorList(bookEntities);

        return bookEntities;
    }

    @Override
    public List<BookEntity> findCommentedBooksByUserId(Long userId) {
        String sql = "WITH commented_book AS ( " +
                "    SELECT " +
                "        b.isbn, " +
                "        b.book_name, " +
                "        bi.image_url, " +
                "        row_number() OVER (PARTITION BY b.isbn) AS rk " +
                "    FROM book AS b " +
                "    JOIN book_image bi on b.isbn = bi.isbn " +
                "    JOIN user_book_comment ubc on b.isbn = ubc.isbn" +
                "    AND ubc.user_id=?" +
                ") " +
                "SELECT isbn, book_name, image_url FROM commented_book WHERE rk=1";

        List<BookEntity> bookEntities = jdbcTemplate
                .query(sql, new BookEntityMapper(), userId);

        setAuthorList(bookEntities);

        return bookEntities;
    }

    @Override
    public List<BookEntity> findStaredBooksByUserId(Long userId) {
        String sql = "SELECT DISTINCT (b.isbn), b.book_name, bi.image_url " +
                "FROM book b " +
                "         JOIN book_image bi on b.isbn = bi.isbn " +
                "         JOIN user_book_star ubc on b.isbn = ubc.isbn " +
                "    AND user_id = ? AND ubc.is_star = true";

        List<BookEntity> bookEntities = jdbcTemplate
                .query(sql, new BookEntityMapper(), userId);

        setAuthorList(bookEntities);

        return bookEntities;
    }

    @Override
    public List<BookEntity> findBooksByRelateNodes(List<RelateNode> nodes) {
        String basicSql = " SELECT DISTINCT (b.isbn), b.book_name, bi.image_url, b.book_score "
                + "FROM book b " +
                "         JOIN book_image bi on b.isbn = bi.isbn " +
                "         JOIN book_holding_kind bhk on b.isbn = bhk.isbn " +
                "AND bhk.book_kind_id=? ";

        StringBuilder sql = new StringBuilder("WITH book_temp AS (\n");

        int size = Math.min(nodes.size(), RELATE_THRESHOLD);
        for (int i = 0; i < size; ++i) {
            if (i == size - 1)
                sql.append(basicSql.replace("?",
                        String.valueOf(nodes.get(i).getKindId())));
            else sql.append(basicSql.replace("?",
                    String.valueOf(nodes.get(i).getKindId())))
                    .append(" \n").append(" UNION ").append("\n");
        }

        sql.append(")\n SELECT * FROM book_temp ORDER BY book_score");

        List<BookEntity> result = jdbcTemplate.query(sql.toString(), new BookEntityMapper());

        setAuthorList(result);

        return result;
    }

    @Override
    public List<BookEntity> findDefaultBooks() {
        String sql = "WITH temp_book AS ( " +
                "    SELECT DISTINCT (b.isbn), b.book_name, bi.image_url, b.book_score " +
                "    FROM book b " +
                "             JOIN book_image bi on b.isbn = bi.isbn " +
                ") " +
                "SELECT * FROM temp_book tb " +
                "ORDER BY tb.book_score DESC " +
                "LIMIT 30";

        List<BookEntity> bookEntities = jdbcTemplate
                .query(sql, new BookEntityMapper());

        setAuthorList(bookEntities);

        return bookEntities;
    }

    private void setAuthorList(List<BookEntity> bookEntities) {
        for (BookEntity obj : bookEntities) {
            obj.setAuthors(jdbcTemplate.query(selectAuthorSql,
                    new AuthorSimpleMapper(), obj.getIsbn()));
        }
    }
}
