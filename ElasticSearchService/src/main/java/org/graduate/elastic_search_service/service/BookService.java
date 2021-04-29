package org.graduate.elastic_search_service.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.graduate.elastic_search_service.data_entity.Author;
import org.graduate.elastic_search_service.data_entity.Book;
import org.graduate.elastic_search_service.entity.AuthorDocument;
import org.graduate.elastic_search_service.entity.AuthorSimple;
import org.graduate.elastic_search_service.entity.BookDocument;
import org.graduate.elastic_search_service.entity.MyConfiguration;
import org.graduate.elastic_search_service.mapper.AuthorSimpleMapper;
import org.graduate.elastic_search_service.mapper.ImageUrlMapper;
import org.graduate.elastic_search_service.repository.AuthorRepository;
import org.graduate.elastic_search_service.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: liuxianghai
 * Date: 2021/2/11
 * Time: 上午11:48
 */

@Slf4j
@Service(value = "bookService")
public class BookService {
    private final JdbcTemplate jdbcTemplate;

    private final BookRepository bookRepository;

    private final MyConfiguration configuration;

    private final RestHighLevelClient client;

    private final AuthorRepository authorRepository;

    @Resource(name = "JacksonMapper")
    private ObjectMapper mapper;

    @Autowired
    public BookService(BookRepository bookRepository,
                       JdbcTemplate jdbcTemplate,
                       MyConfiguration configuration,
                       @Qualifier("restHighLevelClient") RestHighLevelClient client,
                       AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.jdbcTemplate = jdbcTemplate;
        this.configuration = configuration;
        this.client = client;
        this.authorRepository = authorRepository;
    }

    /**
     * 更新对应的书籍的文档对象信息， 这里的做法是先删除对应的索引，
     * 再通过 BulkRequest 请求上传对应的数据创建新的索引节点。
     * 这里无需采用异步的操作，因为同一时间只会有请求需要更新文档
     *
     * @return ： 处理的结果，true: 处理成功   false： 处理失败
     */
    public Boolean updateBookDocument() {
        try {
            GetIndexRequest getIndexRequest =
                    new GetIndexRequest(configuration.getBookIndexName());
            /*
                检查当前的索引是否存在，如果存在则删除它，这是为了更新整个索引文档而做的牺牲，
                但是我认为这是值得的。
             */
            if (client.indices().exists(getIndexRequest, RequestOptions.DEFAULT)) {
                // 删除已有的索引文档， 这是为了更新所有的数据
                DeleteIndexRequest deleteRequest =
                        new DeleteIndexRequest(configuration.getBookIndexName());

                if (!deleteIndexByRequest(deleteRequest)) {
                    log.debug("delete book index failed.");
                    return false;
                }
            }

            // 索引 Id 重新开始计数
            long id = 1;

            Iterable<Book> bookIterable = bookRepository.findAll();

            // 提交数据的信息请求对象
            BulkRequest bulkRequest = new BulkRequest();
            for (Book book : bookIterable) {
                long isbn = book.getIsbn();

                // 创建要添加的书籍对象
                BookDocument book1 = new BookDocument();
                book1.setImageUrl(getImageUrlByIsbn(isbn));
                book1.setAuthors(getAuthorsByIsbn(isbn));
                book1.setIsbn(String.valueOf(isbn));
                book1.setBookName(book.getBookName());

                bulkRequest.add(new IndexRequest(configuration.getBookIndexName())
                        .id(String.valueOf(id++))
                        .source(mapper.writeValueAsBytes(book1), XContentType.JSON)
                );
            }

            bulkRequest.timeout(TimeValue.timeValueMinutes(5));
            BulkResponse response = client.bulk(bulkRequest, RequestOptions.DEFAULT);

            if (!response.hasFailures()) {
                log.debug("Upload Book document success.");
            } else {
                log.debug("Upload Book document failed.");
            }

            return true;
        } catch (Exception e) {
            log.debug("Update Book Info failed.");
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 更新对应的作者对应的文档对象信息， 这里的做法是先删除对应的索引，
     * 再通过 BulkRequest 请求上传对应的数据创建新的索引节点。
     * 这里无需采用异步的操作，因为同一时间只会有请求需要更新文档
     *
     * @return ： 处理的结果，true: 处理成功   false： 处理失败
     */
    public Boolean updateAuthorDocument() {
        GetIndexRequest getIndexRequest =
                new GetIndexRequest(configuration.getAuthorIndexName());
        try {
            /*
                检查当前的索引是否存在，如果存在则删除它，这是为了更新整个索引文档而做的牺牲，
                但是我认为这是值得的。
             */
            if (client.indices().exists(getIndexRequest, RequestOptions.DEFAULT)) {
                // 删除已有的索引文档， 这是为了更新所有的数据
                DeleteIndexRequest deleteRequest =
                        new DeleteIndexRequest(configuration.getAuthorIndexName());
                if (!deleteIndexByRequest(deleteRequest)) {
                    log.debug("delete author index failed");
                    return false;
                }
            }

            // 索引 Id 重新开始计数
            long id = 1;

            // 查询所有的作者信息， 遍历获取对应的书籍信息
            Iterable<Author> authors = authorRepository.findAll();

            for (Author author : authors) {
                List<Book> books =
                        bookRepository.getBooksByAuthorId(author.getAuthorId());

                if (0 == books.size()) continue;

                // 提交数据的信息请求对象
                BulkRequest bulkRequest = new BulkRequest();

                for (Book book : books) {
                    Long isbn = book.getIsbn();
                    BookDocument book1 = new BookDocument();
                    book1.setIsbn(String.valueOf(isbn));
                    book1.setBookName(book.getBookName());
                    book1.setAuthors(getAuthorsByIsbn(isbn));
                    book1.setImageUrl(getImageUrlByIsbn(isbn));

                    // 写入的对象， 这个索引是需要按照作者 ID 查找书籍的
                    AuthorDocument authorDocument = new AuthorDocument();
                    authorDocument.setAuthorId(author.getAuthorId());
                    authorDocument.setBook(book1);

                    // 添加到对应的请求中， 是为了添加对应的数据体
                    bulkRequest.add(new IndexRequest(configuration.getAuthorIndexName())
                            .id(String.valueOf(id++))
                            .source(mapper.writeValueAsBytes(authorDocument), XContentType.JSON));
                }

                /*
                    一次性提交存在的所有的数据请求，可以提高效率
                 */
                BulkResponse response = client.bulk(bulkRequest, RequestOptions.DEFAULT);

                if (!response.hasFailures()) {
                    log.debug("Upload author document success.");
                } else {
                    log.debug("Upload author document failed.");
                }
            }

            return true;
        } catch (Exception e) {
            log.debug("Update author document failed.");
            e.printStackTrace();
            return false;
        }
    }

    private String getImageUrlByIsbn(@NonNull Long isbn) {
        /*
            按照书籍的 ISBN 查找对应的图像地址的 SQL 语句。
            这里设置的图像地址只设置一个， 这是为了展示方便以及尽量缩小索引文档的空间。
            这里选取的图像地址时按照最长的匹配来的， 也就是说， 对应书籍的图像地址中，
            地址最长的将会作为这个书籍信息的图像地址
         */
        String selectImageUrlSql = "SELECT max(image_url) AS image_url\n" +
                "FROM book_image\n" +
                "WHERE isbn=?\n" +
                "GROUP BY isbn";

        List<String> urlList = jdbcTemplate.query(selectImageUrlSql, new ImageUrlMapper(), isbn);

        if (0 < urlList.size())
            return urlList.get(0);
        return null;
    }

    private List<AuthorSimple> getAuthorsByIsbn(@NonNull long isbn) {
        /*
            按照书籍 ISBN 查找作者信息列表的 SQL 语句
         */
        String selectAuthorSql = "SELECT cb.author_id AS author_id, author_name\n" +
                "FROM author\n" +
                "         JOIN created_book cb on author.author_id = cb.author_id\n" +
                "         JOIN book b on b.isbn = cb.isbn\n" +
                "WHERE b.isbn=?";


        return jdbcTemplate
                .query(selectAuthorSql, new AuthorSimpleMapper(), isbn);
    }

    private Boolean deleteIndexByRequest(@NonNull DeleteIndexRequest request) {
        try {
            AcknowledgedResponse deleteResponse = client
                    .indices()
                    .delete(request, RequestOptions.DEFAULT);

            if (deleteResponse.isAcknowledged())
                log.debug("DeleteIndexRequest delete book success");
            else
                log.debug("DeleteIndexTemplateRequest delete book failed");
            return true;
        } catch (Exception e) {
            log.debug("Delete index request failed.");
            e.printStackTrace();
            return false;
        }
    }
}