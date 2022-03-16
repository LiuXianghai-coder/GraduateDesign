package org.graduate.service.tool;

import com.sun.istack.NotNull;
import org.graduate.service.configuration.JsonObjectConfiguration;
import org.graduate.service.configuration.ParseMessageConfiguration;
import org.graduate.service.constants.Const;
import org.graduate.service.constants.FileHelper;
import org.graduate.service.constants.InfoConstant;
import org.graduate.service.constants.UserAgent;
import org.graduate.service.data.BookHoldingKind;
import org.graduate.service.data.CreatedBook;
import org.graduate.service.data_entity.BookImage;
import org.graduate.service.data_entity.*;
import org.graduate.service.entity.*;
import org.graduate.service.repository.*;
import org.graduate.service.tags.*;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.Date;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.graduate.service.constants.FileHelper.writeToFile;
import static org.graduate.service.constants.FileHelper.writeToFileResult;

/**
 * 使用 application.yml 文件内的 getdata.urls 的 url，
 * 以及 getdata.keyword 组合获得一个 URL， 即可得到一个电商网站的第一次搜索
 * 第一次搜索：用于获得该电商的出版社名称
 * 第二次搜索：获取图书信息、店铺信息、作者信息等
 */
@Import(value = org.graduate.service.configuration.ParseMessageConfiguration.class)
@Service
public class GetData {
    private final Logger logger = LoggerFactory.getLogger(GetData.class);

    private Long count = 0L;

    private final BasicUrl basicUrl;


    // 出版社的 JPA 数据访问接口
    private final PublisherRepository publisherRepository;

    // 书籍基本对象的 JPA 数据访问接口
    private final BookRepository bookRepository;

    // 临时存储解析到的元素列表标签集合的文件对象
    private final File elementsFile;

    // 临时文件的信息对象， 包括文件名， 文件路径等等；
    private final MyData myData;

    // 临时存储字符串数据的文件；
    private final File tempFile;

    // 待处理的 html 文件， 通过预处理、浏览器执行即可得到正常的文档对象
    private final File htmlFile;

    // 存放脚本的临时文件， 用于控制浏览器加载数据的行为, 该文件应当是只读的
    private final File scriptFile;

    // Publisher 对象的属性配置类
    private final PublisherProps props;

    // 自定义的配置属性对象
    private final MyConfiguration myConfiguration;

    // 图书图片信息 JPA 数据访问接口
    private final BookImageRepository bookImageRepository;

    // Kafka 消息中间件模版对象
    private final KafkaTemplate<Object, Object> kafkaTemplate;

    // 解析信息的配置类， 用户加载对应的信息对象 Bean
    private final ParseMessageConfiguration messageConfiguration;

    private final BookKindRepo bookKindRepo;

    private final BookHoldingKindRepo bookHoldingKindRepo;

    private final AuthorRepository authorRepository;

    private final CreatedBookRepository createdBookRepository;

    private final PublisherBookRepository publisherBookRepository;

    private final BookChapterRepository bookChapterRepository;

    private final JsonObjectConfiguration jsonObjectConfiguration;

    /*
        解析 Dom 的结果， 放置于消息接收中，
        用于判断浏览器加载的 Dom 是否已经成功地写入到文件中， 这个状态是由得到的消息确认的。
        初始值为 false， 这是由于初始时时无法获取消息的
     */
    private Boolean isFinished = false;

    private int timeCount = 0; // 轮询处理结果，超过某个阈值将会放弃解析这个路径

    @Autowired
    public GetData(BasicUrl basicUrl, MyData myData, PublisherProps props,
                   MyConfiguration myConfiguration,
                   PublisherRepository publisherRepository,
                   BookRepository bookRepository,
                   BookImageRepository bookImageRepository,
                   AuthorRepository authorRepository,
                   PublisherBookRepository publisherBookRepository,
                   KafkaTemplate<Object, Object> kafkaTemplate,
                   ParseMessageConfiguration messageConfiguration,
                   BookUrlRepo bookUrlRepo,
                   JsContentRepo jsContentRepo,
                   BookKindRepo bookKindRepo,
                   BookHoldingKindRepo bookHoldingKindRepo,
                   CreatedBookRepository createdBookRepository,
                   BookChapterRepository bookChapterRepository,
                   JsonObjectConfiguration jsonObjectConfiguration) throws IOException {
        this.basicUrl = basicUrl;
        this.props = props;
        this.myData = myData;
        this.myConfiguration = myConfiguration;

        this.publisherRepository = publisherRepository;
        this.bookRepository = bookRepository;
        this.bookImageRepository = bookImageRepository;

        this.tempFile = new File(myData.getPath() + myData.getTempFileName());
        this.elementsFile = new File(myData.getPath() + myData.getElementsFileName());
        this.scriptFile = new File(myData.getPath() + myData.getScriptFileName());
        this.htmlFile = new File(myData.getPath() + myData.getHtmlFileName());
        this.bookKindRepo = bookKindRepo;
        this.bookHoldingKindRepo = bookHoldingKindRepo;
        this.authorRepository = authorRepository;
        this.createdBookRepository = createdBookRepository;
        this.publisherBookRepository = publisherBookRepository;
        this.bookChapterRepository = bookChapterRepository;
        this.jsonObjectConfiguration = jsonObjectConfiguration;

        // 如果元素文件不存在， 则创建它
        createNotExistFile(this.elementsFile);

        // 如果临时文件不存在， 则创建它
        createNotExistFile(this.tempFile);

        // 如果控制浏览器的脚本文件不存在， 则创建它
        createNotExistFile(this.scriptFile);

        // 如果 html 文件不存在， 则创建它
        createNotExistFile(this.htmlFile);
        this.kafkaTemplate = kafkaTemplate;
        this.messageConfiguration = messageConfiguration;

        if (0 == this.scriptFile.length()) {
            Optional<JsContent> content = jsContentRepo.findLastJsContent();
            if (content.isEmpty()) {
                throw new IllegalArgumentException("Js Content is null");
            }

            FileHelper.writeToFile(this.scriptFile, content.get().getContent());
        }
    }

    /**
     * 创建一个当前不存在的文件
     *
     * @param file ： 要创建的文件
     * @throws IOException ： 创建文件出现异常时抛出
     */
    private void createNotExistFile(File file) throws IOException {
        if (!file.exists()) {
            logger.info(logger.getName() + " " +
                    file.getName() + " " +
                    InfoConstant.FILE_NOT_EXIST_MESSAGE);
            // 创建新文件
            if (file.createNewFile()) {
                logger.info(logger.getName() + " " + file.getName() + " "
                        + InfoConstant.CREATE_FILE_SUCCESS);
            }
        }
    }

    /**
     * 解析当前页面的查找书籍的地址
     *
     * @return ：
     * @throws IOException          : 写入文件异常时抛出
     * @throws InterruptedException :
     */
    @Deprecated
    public List<String> parseBookList() throws IOException, InterruptedException {
        List<String> result = new ArrayList<>();
        Random random = new Random();
        for (Platform platform : basicUrl.getPlatformList()) {

            // 查找的 Page 页的索引
            int pageIndex = 0;
            while (true) {
                Pageable pageable = PageRequest.of(pageIndex, props.getPageSize());
                List<Publisher> publishers = publisherRepository.findAll(pageable);
                // 如果查找不到任何 Publisher 对象， 则说明查找到结尾
                if (0 == publishers.size()) break;

                for (Publisher publisher : publishers) {
                    for (String url : platform.getUrls()) {
                        Connection connection = Jsoup
                                .connect(url + publisher.getPublisherName())
                                .userAgent(UserAgent.randomUserAgent());
                        Document document = connection.get();

                        // 书籍基本信息的获取对象
                        BookList bookList = platform.getBookList();

                        Elements elements = document.getAllElements();

                        // 如果使用的是通过输入 URL 参数的方式获取当前页面的最大页面数
                        setBookListMaxPage(bookList, elements);

                        // 按照页面参数获取对象
                        HashMap<String, String> parameter = new HashMap<>();
                        for (int i = 1; i <= bookList.getMaxPage(); ++i) {
                            parameter.put(Const.PAGE_PARAMETER_KEY, String.valueOf(i));
                            connection = Jsoup
                                    .connect(url + publisher.getPublisherName())
                                    .userAgent(UserAgent.randomUserAgent())
                                    .data(parameter);
                            document = connection.get();

                            elements = document.getAllElements();

                            // 解析获得所有的书籍信息商品链接
                            Elements elements1 = parseElements(bookList.getTags(), elements);

                            String href;
                            for (org.jsoup.nodes.Element element : elements1) {
                                href = perfectHref(element.attr("href"));
                                parseBookInfo(href, platform.getBookDetail());
//                                writeToTempFile(href + "\n");
                            }
                        }

                        // 睡眠 3s， 避免反扒取机制
                        TimeUnit.SECONDS.sleep(random.nextInt(20));
                    }

                    // 避免频繁地搜索对应的网站
                    TimeUnit.SECONDS.sleep(random.nextInt(60));
                }

                pageIndex++;
            }
        }

        return result;
    }

    /**
     * 解析获取的文档信息，获取对应的书籍信息， 保存对应的书籍信息
     */
    public void saveBookInfo(String href, BookDetail bookDetail) {
        saveBookInfo(parseBookInfo(href, bookDetail));
    }

    /**
     * 解析当前链接的图书信息
     *
     * @param href       ： 图书信息链接
     * @param bookDetail : 图书的细节的查找属性， 包括 tags等
     */
    public BookInfo parseBookInfo(String href, BookDetail bookDetail) {
        // 获取当前的方法的方法名
        String methodName = new Object() {}.getClass()
                .getEnclosingMethod().getName();

        // 检查链接是否有效, 如果是无效的， 则直接返回一个新的 BookInfo 对象
        if (!checkHrefValid(href)) {
            logger.info(logger.getName() + " " + methodName + Const.METHOD_STRING
                    + InfoConstant.HREF_FORMAT_ERROR_MESSAGE);
            return new BookInfo();
        }

        BookInfo bookInfo = new BookInfo();
        try {
            logger.info(logger.getName() + " " + methodName + " " + href);
            Connection connection = Jsoup.connect(href)
                    .userAgent(UserAgent.randomUserAgent());

            Document document = connection.get();

            /*
                添加 Dom 加载完成后的自定义执行脚本函数
             */
            document.getElementsByTag("body").first()
                    .attr("onload", "sendData()");

            // 将解析到的 html 内容写入文档中
            writeToFile(this.htmlFile, document.getAllElements().toString());

            // 格式化文档内容， 为没有文档协议的链接添加协议前缀
            if (formatHtmlFile(bookDetail)) {
                logger.info(methodName +
                        " " + InfoConstant.FORMAT_HTML_SUCCESS);
            } else {
                logger.info(methodName +
                        " " + InfoConstant.FORMAT_HTML_FAILED);
                return null;
            }

            /*
                格式化后的 Document 应当再次被读取
             */
            document = getDocumentFromHtmlFile();

            /*
                在获取的文档对象中， 添加 JS 脚本到 head 标签的后面， 控制相关 DOM 的发送
             */
            appendScriptElement(document, getScriptFileContent());

            /*
                再次将 Document 对象写入到 HTML 文件内。
             */
            writeToFile(this.htmlFile, document.select("html").html());

            logger.info(InfoConstant.APPEND_JS_SCRIPT_SUCCESS);

            /*
                发送消息给对应的处理接口， 以得到最终的 DOM 文件
             */
            ParseDomMessage message = messageConfiguration.startParseDomMessage();
            message.setIsHttp(bookDetail.getHttpProtocol().equalsIgnoreCase("http"));
            message.setCount(++count % Long.MAX_VALUE);
            message.setHost("127.0.0.1");

            kafkaTemplate.sendDefault(message);

            // 等待页面的 Dom 保存到指定的文件， 每次等待一秒来尝试是否已经完成了 Dom 解析基本操作
            timeCount = 0;
            while (true) {
                if (timeCount > Const.THRESHOLD) return null;

                String result = processCommand("curl http://127.0.0.1:8084/kafkaMessage");
                if ("200".equalsIgnoreCase(result)) break;
                TimeUnit.SECONDS.sleep(1); // 持续等待直到解析完成
                timeCount++;
            }

            // 重新获取 Document 对象， 因为 Dom 已经被重新写到对应的 element 文件内了。
            document = getDocumentFromElementFile();

            String isbn = getISBN(document, bookDetail);
            // 对于这种貌似违法的书籍，还是不要爬取了 :(
            if (null == isbn) return null;

            bookInfo.setBook(getBookObj(document, bookDetail));
            bookInfo.setBookImages(getBookImageList(document, bookDetail));
            bookInfo.setAuthorList(getBookAuthors(document, bookDetail));
            bookInfo.setBookChapters(getBookChapters(document, bookDetail));
            bookInfo.getBook().setHref(href);

            logger.debug("BookInfo: " + bookInfo);

            // 设置 PublisherBook 对象
            PublishedBook publishedBook = new PublishedBook();

            // 获取出版社的名称
            String publisherName = getPublisherName(document, bookDetail);
            logger.info("PublisherName: " + publisherName);
            Publisher publisher = publisherRepository
                    .findPublisherByPublisherName(publisherName);

            publishedBook.setIsbn(Long.parseLong(
                    Objects.requireNonNull(getISBN(document, bookDetail))));
            // 如果不存在这个出版社， 则添加这个出版社
            if (null == publisher) {
                Publisher obj = new Publisher();
                obj.setPublisherName(publisherName);
                publisherRepository.save(obj);
            }
            // 再次读取数据获得出版社 Id
            publisher = publisherRepository.findPublisherByPublisherName(publisherName);
            publishedBook.setPublisherId(publisher.getPublisherId());
            // 设置出版日期
            publishedBook.setPublishedDate(getPublisherDate(document, bookDetail));

            // 设置出版的书籍的对象
            bookInfo.setPublishedBook(publishedBook);

            // 设置书籍的种类对象
            bookInfo.setBookKindName(getBookKindName(document, bookDetail));

            logger.info("Save Book Object: " + bookInfo);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return bookInfo;
    }

    /*
        Kafka 消息的接受监听器
     */
    @KafkaListener(topics = {Const.TOPIC_NAME}, groupId = Const.GROUP_ID)
    public void handler(String message) {
        // 将消息对象转变为对应的 ParseDomMessage 对象
        ParseDomMessage obj = getParseDomMessageFromMessage(message);

        // 如果转变后的 ParseDomMessage 对象为 null， 则直接返回
        if (null == obj) return;

        logger.info(logger.getName() + " get Message：" + obj);
        /*
            如果消息表示已经完成了 Dom 的解析，
            并且写入了对应的文件， 则设置 isFinished 为 true
            从而继续进行数据的获取
         */
        if (obj.getIsParseEnd()) isFinished = true;
    }

    /**
     * 添加一个脚本元素到 <head><head/> 标签内
     *
     * @param document ：待加入脚本标签的文档对象
     * @param content  ： 加入的 JS 脚本的内容
     */
    private void appendScriptElement(Document document, String content) {
        document.selectFirst("head")
                .appendElement("script")
                .attr("type", "text/javascript")
                .appendChild(new DataNode(content));
    }

    /**
     * 获取对应的 JS 脚本文件的内容，
     * 该内容是用于控制执行的 HTML 向服务端发送解析到的数据
     *
     * @return ： 读取到的 JS 脚本内容
     * @throws Exception ： 当文件不存在或读取文件异常时抛出
     */
    private String getScriptFileContent() throws Exception {
        StringBuilder parseJs = new StringBuilder();
        BufferedReader reader = new BufferedReader(new FileReader(this.scriptFile));
        String line;
        while ((line = reader.readLine()) != null) parseJs.append(line);

        return parseJs.toString();
    }

    /**
     * @param document   : 待解析的文档对象
     * @param bookDetail ： 包含查找对象属性的对象， 用于查找图书对应的属性
     * @return ： 解析数据得到的基本图书信息对象
     * @throws ClassNotFoundException    : parseBookProp 方法异常时抛出
     * @throws NoSuchMethodException     ： parseBookProp 方法异常时抛出
     * @throws InvocationTargetException ： parseBookProp 方法异常时抛出
     * @throws InstantiationException    ： parseBookProp 方法异常时抛出
     * @throws IllegalAccessException    ：parseBookProp 方法异常时抛出
     */
    private Book getBookObj(Document document, BookDetail bookDetail) throws
            ClassNotFoundException, NoSuchMethodException, InvocationTargetException,
            InstantiationException, IllegalAccessException {
        // 当前执行的方法名, 以便显示对应的提示信息
        String methodName = new Object() {
        }.getClass()
                .getEnclosingMethod().getName();

        Book book = new Book();
        // 方法传入参数为空时的处理方法
        if (null == bookDetail) {
            dealParameterObj(methodName, String.valueOf(BookDetail.class));
            return book;
        }

        String isbnInfo = getISBN(document, bookDetail);

        // 如果当前的图书页面没有 ISBN 号， 则不能添加到数据库中， 直接跳过
        if (null == isbnInfo)
            return book;

        // 查找的条件 BookName 信息对象
        BookName bookNameObj = bookDetail.getBookName();
        List<String> bookNameList = parseBookProp(document, String.class,
                bookNameObj.getTags(), bookNameObj.getBookNameRegex(),
                bookNameObj.getUseRegex(), false);

        assert bookNameList.size() > 0; // 每本书籍至少有自己的图书名称
        String bookNameInfo = bookNameList.get(Const.DEFAULT_GET_INFO_INDEX);

        // 查找的图书介绍信息的查找对象
        BookIntro bookIntroObj = bookDetail.getBookIntro();
        List<String> bookIntroList = parseBookProp(document, String.class,
                bookIntroObj.getTags(), bookIntroObj.getIntroRegex(),
                bookIntroObj.getUseRegex(), false);
        String bookIntroInfo = null;
            /*
                有的书籍不一定含有对应的图书介绍信息， 因此这不一定能够找到，
                没有找到的话则设置为 null
             */
        if (0 < bookIntroList.size()) {
            bookIntroInfo = bookIntroList.get(Const.DEFAULT_GET_INFO_INDEX);
        }

        bookNameInfo = Jsoup.parse(bookNameInfo).body().text();

        book.setIsbn(Long.parseLong(isbnInfo));
        book.setBookName(bookNameInfo);
        book.setIntroduction(bookIntroInfo);
        book.setBookScore(Const.DEFAULT_BOOK_SCORE);

        return book;
    }

    /**
     * 获取对应的页面的 ISBN
     *
     * @param document   ： 待解析的文档对象
     * @param bookDetail ： 查询图书信息的查询对象
     * @return ： 解析得到的 ISBN 字符串
     */
    private String getISBN(Document document, BookDetail bookDetail)
            throws ClassNotFoundException, NoSuchMethodException,
            IllegalAccessException, InvocationTargetException, InstantiationException {
        // 查找的条件 ISBN 属性对象
        ISBN isbnObj = bookDetail.getIsbn();
        // 解析得到的 ISBN 信息列表， 取第一个为该书籍的 ISBN

//        logger.info(logger.getName() + " isbn object: " + isbnObj.toString());
        List<String> isbnList = parseBookProp(document, String.class,
                isbnObj.getTags(), isbnObj.getIsbnRegex(),
                isbnObj.getUseRegex(), false);

        if (0 == isbnList.size()) return null;

        assert isbnList.size() > 0; // 每本书籍页面至少包含一个ISBN
        return isbnList.get(Const.DEFAULT_GET_INFO_INDEX);
    }

    /**
     * 获取图书图片地址列表对象
     *
     * @param document   ： 待解析的文档对象
     * @param bookDetail ： 查询的文档条件对象
     * @return ： 解析得到的图书图片信息对象列表
     */
    private List<BookImage> getBookImageList(Document document, BookDetail bookDetail)
            throws ClassNotFoundException, NoSuchMethodException,
            InvocationTargetException, InstantiationException, IllegalAccessException {
        // 图书图像信息对象列表
        List<BookImage> result = new ArrayList<>();
        // 当前执行的方法名, 以便显示对应的提示信息
        String methodName = new Object() {
        }.getClass()
                .getEnclosingMethod().getName();
        // 方法传入参数为空时的处理方法
        if (null == bookDetail) {
            dealParameterObj(methodName, String.valueOf(BookDetail.class));
            return result;
        }

        String ISBN = getISBN(document, bookDetail);

        // 查询的 BookImage 对象的条件查找对象
        org.graduate.service.tags.BookImage bookImage = bookDetail.getBookImage();
        List<String> imageUrl = parseBookProp(document, String.class,
                bookImage.getTags(), bookImage.getUrlRegex(),
                bookImage.getUseRegex(), false);

        /*
            获取当前数据库中的图书图片地址数据的行数， 以此来设置每个对象的 主键值
         */
        long count = bookImageRepository.count();

        /*
            对每个解析到的图像图片地址设置相关的参数， 如：完善 URL 地址、设置主键值
         */
        for (String url : imageUrl) {
            String href = perfectHref(url);
            BookImage obj = new BookImage();

            // 如果解析到的 ISBN 号为空， 则跳过它
            if (null == ISBN) continue;

            // 设置每个属性值
            obj.setIsbn(Long.parseLong(ISBN));
            obj.setImageUrl(href);
            obj.setBookImageId(++count);

            result.add(obj);
        }

        return result;
    }

    /**
     * 处理实参对象为空时的处理方法
     *
     * @param methodName ： 执行的方法的名称
     * @param className  ： 实参的对象的类型
     */
    private void dealParameterObj(String methodName, String className) {
        logger.info(logger.getName() + " " + methodName + " " + className +
                InfoConstant.OBJECT_PARAMETER_IS_NULL_MESSAGE);
        throw new IllegalArgumentException(methodName + " " +
                InfoConstant.OBJECT_PARAMETER_IS_NULL_MESSAGE);
    }

    /**
     * 保存书籍的基本信息
     *
     * @param book ： 待保存的基本图书信息对象
     */
    private void saveBook(Book book) {
        // 获取当前执行方法的方法名, 以便提示信息更加准确
        String methodName = new Object() {
        }.getClass()
                .getEnclosingMethod().getName();
        // 如果保存的对象为空对象， 则跳过它
        if (null == book) {
            logger.info(logger.getName() + " " + methodName + " "
                    + Book.class + InfoConstant.OBJECT_PARAMETER_IS_NULL_MESSAGE);
            return;
        }

        logger.info(logger.getName() + " " + methodName + " save Book Object: " + book);
        bookRepository.save(book);
    }

    /**
     * 保存图片对象列表信息
     *
     * @param bookImageList ： 待保存的图书图片信息对象信息列表
     */
    private void saveBookImages(List<BookImage> bookImageList) {
        // 得到当前执行方法的方法名， 以便打印对应的提示信息
        String methodName = new Object() {
        }.getClass()
                .getEnclosingMethod().getName();

        /*
            如果待保存的对象为空， 则直接抛出对应的异常
         */
        if (null == bookImageList) {
            dealParameterObj(methodName, String.valueOf(BookImage.class));
            return;
        }

        // 依次遍历每个图像信息， 将其保存
        for (BookImage bookImage : bookImageList) {
            // 如果当前的图书的 ISBN 以及图像地址已经存在， 则跳过当前对象
            if (null != bookImageRepository.findBookImageByIsbnAndImageUrl
                    (bookImage.getIsbn(), bookImage.getImageUrl())) {
                continue;
            }

            bookImageRepository.save(bookImage);
        }
    }

    /**
     * 解析获得的文档， 获取书籍的基本信息， 这里的泛型只能是
     * Long、String、Integer等只有一个构造参数的对象，
     * 这是由于爬取数据时的内容应当是这些对象, 但是由于 Long、Integer等其他的数值型对象没有无参构造函数，
     * 因此数据在这里只放在 String 对象中
     *
     * @param document ： 经过爬取网络获得的文档对象
     * @param c        ： 待转换成的对象类型
     * @param tags     ： 查找标签的顺序
     * @param regex    ： 匹配内容的正则表达式， 格式应当为 ***()**， 这是由于内容含有多余的内容导致的
     * @param <T>      ： 泛型函数
     * @return ： 解析得到的图书内容对象
     */
    private <T> List<T> parseBookProp(Document document, Class<T> c,
                                      List<Tag> tags, String regex,
                                      Boolean useRegex, Boolean htmlFlag)
            throws ClassNotFoundException, NoSuchMethodException,
            IllegalAccessException, InvocationTargetException, InstantiationException {
        // 获取所有的标签信息
        Elements elements = document.getAllElements();
        // 将标签按照优先级排序
        sortTags(tags);

        List<T> result = new ArrayList<>();

        // 按照图书查找信息对象的查找标签顺序查找标签
        elements = parseElements(tags, elements);
        Pattern pattern = Pattern.compile(regex);

        /*
            获取泛型类的构造函数， 由于这里是使用的 Long、String、Integer 等简单对象，
            所以这里的构造参数只有一个， 只能使用 String 对象存储数据信息
         */
        Class<T> obj = (Class<T>) Class.forName(c.getName());
        Constructor<T> constructor = obj.getConstructor(c);

        for (org.jsoup.nodes.Element element : elements) {
            T t;
            // 如果不使用正则表达式获取数据信息， 则直接获取标签内的文本数据（经过去空格处理）
            if (!useRegex) {
                if (0 == element.text().trim().length()) continue;

                if (htmlFlag) t = constructor.newInstance(element.html());
                else t = constructor.newInstance(trimContent(element.text()));

                result.add(t);
                continue;
            }

            Matcher matcher = pattern.matcher(element.toString());
            if (matcher.find()) {
                t = constructor.newInstance(matcher.group(1).trim());
                result.add(t);
            }
        }

        return result.stream().distinct().collect(Collectors.toList());
    }

    /**
     * 将解析的数据内容进行空白字符的处理
     *
     * @param content ： 待处理的文本字符串
     * @return ： 经过处理后的文本内容
     */
    private String trimContent(String content) {
        checkStringParameter(content);

        return content.trim();
    }

    /**
     * 将选取元素的标签按照优先级排序
     *
     * @param tags ： 待排序的标签列表
     */
    private void sortTags(List<Tag> tags) {
        tags.sort(Tag::compareTo);
    }

    /**
     * 检查链接是否正确
     *
     * @param href ： 待检测的链接
     * @return ： 是否正确可用
     */
    private boolean checkHrefValid(String href) {
        Pattern pattern = Pattern.compile(myConfiguration.getHrefRegex());
        Matcher matcher = pattern.matcher(href);

        return matcher.find();
    }

    /**
     * 完善相关的图书链接内容。
     * 有的电商， 在商品链接中会省略使用的协议，如 http、https， 所以需要进一步完善才能继续进行爬取
     *
     * @param href ： 待完善的图书的链接地址
     * @return : 经过完善后的链接信息
     */
    private String perfectHref(String href) {
        checkStringParameter(href);

        // 检测链接是否已经完善的链接地址的正则表达式对象
        Pattern pattern = Pattern.compile(myConfiguration.getHttpPrefixRegex());
        Matcher matcher = pattern.matcher(href);

        // 如果满足要求， 则直接返回原有链接对象
        if (matcher.find()) return href;

        return myConfiguration.getDefaultHttpProtocol() + href;
    }

    /**
     * 设置 对应查找页面的最大页面数量
     *
     * @param bookList ： 待设置的 bookList 对象
     * @param elements : 通过抓取得到的页面对象
     */
    private void setBookListMaxPage(BookList bookList, Elements elements) {
        if (NextPageStrategy.Page != bookList.getNextPageStrategy()) return;

        // 如果选用的是向 URL 传入参数进行翻页， 则需要先获取对应页面的最大页数
        if (bookList.getNextPageStrategy() == NextPageStrategy.Page) {
            List<Tag> maxPageTags = bookList.getMaxPageTags();
            assert maxPageTags.size() > 0;
            // 按照优先级对查找页面最大页数进行排序
            maxPageTags.sort(Tag::compareTo);

            /*
                为了获取页面的最大数量， 解析对应的内容， 查找对应的标签， 获取相关的页数内容
             */
            Elements elements1 = parseElements(bookList.getMaxPageTags(), elements);

            // 获取第一个查找到的最大页数信息
            org.jsoup.nodes.Element element = elements1.first();

            bookList.setMaxPage(Integer.parseInt(element.text()));
        }
    }

    /**
     * 通过解析得到的 标签列表， 解析该标签得到出版社信息，
     * 生成出版社信息对象，保存出版社信息
     *
     * @throws Exception ： 爬取网页信息时出现异常则抛出
     */
    public void savePublisher() throws Exception {
        List<String> publisherList = parseUrlToStoreList();
        /*
            解析得到的String格式：<element>text文本</element>
         */
        // 解析每个标签得到的文档对象
        Document document;

        // 出版社对象
        for (String element : publisherList) {
            document = Jsoup.parse(element);
            // 标签文本内容即为需要得到的出版社名称
            String publisherName = document.text();
            logger.info("SavePublisher: " + publisherName);
            savePublisher(publisherName);
        }
    }

    /**
     * 保存新的出版社信息对象
     *
     * @param publisherName ： 解析得到的出版社的名称
     */
    private void savePublisher(String publisherName) {
        // 检测传入的参数是否有效
        checkStringParameter(publisherName);

        Publisher publisher;

        // 根据出版社名称查找是否存在这一出版社

        publisher = publisherRepository
                .findPublisherByPublisherName(publisherName);

        // 如果不为空则说明该出版社已经存在， 不进行操作
        if (null != publisher) return;

        /*
         * 当前存在的数据的总行数， PostgresSQL 没有自增的数据类型，
         * 在这里采用自己生成 ID 的方式手动设置 ID;
         * 使用 JPA 的 CrudRepository 的 count()
         * 方法即可得到数据的总行数
         */
        long count = publisherRepository.count();

        publisher = new Publisher();
        publisher.setPublisherId(++count);
        publisher.setPublisherName(publisherName);
        publisherRepository.save(publisher);
    }

    /**
     * 检测输入的字符串参数是否有效
     * 如果传入的字符串参数为空或者有效长度为 0， 则抛出 IllegalArgumentException
     */
    private void checkStringParameter(String parameter) {
        if (null == parameter || 0 == parameter.trim().length()) {
            throw new IllegalArgumentException(logger.getName() + " "
                    + InfoConstant.STRING_PARAMETER_INVALID_MESSAGE);
        }
    }

    /**
     * 第一次搜索各个品台， 获取各个平台当前存在的出版社
     *
     * @return : 得到的链接
     */
    public List<String> parseUrlToStoreList() throws Exception {
        List<String> result = new ArrayList<>();

        for (Platform platform : basicUrl.getPlatformList()) {
            for (String url : platform.getUrls()) {
                Connection connection = Jsoup.connect(url + platform.getKeyWord())
                        .userAgent(UserAgent.randomUserAgent());

                List<Tag> getElementList = platform.getTags();

                // 按照选取元素的优先级， 对平台对象重新排序元素
                getElementList.sort(Tag::compareTo);

                Document document = connection.get();

                writeToFileResult(elementsFile, writeToElementSwapFile(document.getAllElements()));

                Elements elements = document.getAllElements();
                elements = parseElements(getElementList, elements);

                for (org.jsoup.nodes.Element element : elements) {
                    result.add(element.getAllElements().toString());
                }
            }
        }

        return result;
    }

    /**
     * 依次解析需要解析的元素标签
     *
     * @param getElementList ： 已经按照优先级排好序的标签序列列表
     * @param elements       ： 已经通过连接获得的元素列表
     * @return ： 按照标签顺序依次获取对应的元素
     */
    private Elements parseElements(List<Tag> getElementList, Elements elements) {
        // 按照优先级依次查找对应的对应的标签值
        for (Tag getElement : getElementList) {
            Element element = parseElement(getElement.getSelectName());

            // 如果不是最终的选择标签， 则按照标签属性进行选取
            if (!element.getIsFinalTag()) {
                elements = selectElements(element, elements);

                continue;
            }

            elements = elements.select(element.getTagName());
        }
        return elements;
    }

    /**
     * 通过选取的 Element 对象选择对应的标签
     *
     * @param element  ： 查找 Elements 的 Element 对象
     * @param elements : 查找结果接收对象
     */
    private Elements selectElements(Element element, Elements elements) {
        // 最终的选取标签没有属性值
        if (element.getIsFinalTag()) {
            elements = elements.select(element.getTagName());
            return elements;
        }

        final int attrValueLength = element.getAttrValue().length;

        if (1 == attrValueLength) {
            elements = elements.select(element.getTagName() + "[" + element.getAttrName()
                    + "=" + element.getAttrValue()[0] + "]");
            return elements;
        }

        StringBuilder attrValue = new StringBuilder(element.getTagName() + "[");
        attrValue.append(element.getAttrName()).append("=");
        for (int i = 0; i < attrValueLength; ++i) {
            attrValue.append(element.getAttrValue()[i]).append(" ");
        }
        // 移除最后一位空白字符
        attrValue.deleteCharAt(attrValue.length() - 1);
        attrValue.append("]");

        elements = elements.select(attrValue.toString());

        return elements;
    }

    /**
     * 当前的元素查找序列是否包含 "@" 字符， 如果包含空白字符，
     * 则说明该选择标签是一个选择标签
     * <p>
     * 如果查找的元素的查找序列不包含 "@" 字符， 则说明这是最后一个查找标签，
     * 将查找对应的标签文本内容
     *
     * @param selectName ： 选择的标签信息
     * @return ： 是否包含 "@" 字符
     */
    private boolean containPoint(String selectName) {
        checkParameter("containPoint", selectName);

        return selectName.contains("@");
    }

    /**
     * 将解析到的 Elements 写入到临时文件中
     *
     * @param elements ： 待写入的 elements 数据
     * @return ： true  :  写入正常时返回 true
     * false :  写入出现异常时返回 false
     */
    private boolean writeToElementSwapFile(Elements elements) {
        byte[] bytes = new byte[myData.getBufferSize()];
        int len;
        try {
            OutputStream outputStream = new FileOutputStream(elementsFile);

            InputStream inputStream = new ByteArrayInputStream(elements.toString().getBytes());

            while ((len = inputStream.read(bytes)) > 0) {
                outputStream.write(bytes, 0, len);
            }
            outputStream.close();
            inputStream.close();

            return true;
        } catch (Exception e) {
            logger.info(logger.getName() + " " + e.getLocalizedMessage());
            return false;
        }
    }

    /**
     * 将解析到的图书信息链接内容写入临时文件中
     *
     * @param href ： 写入的字符串内容， 在这里是解析得到的图书的列表链接
     * @return ： 写入结果。true ： 写入成功   false ： 写入失败
     */
    private boolean writeToTempFile(String href) {
        checkStringParameter(href);

        // 临时缓冲字节流
        byte[] bytes = new byte[myData.getBufferSize()];
        int len;
        try {
            // 不断叠加得到的图书的列表
            OutputStream outputStream = new FileOutputStream(this.tempFile, true);
            InputStream inputStream = new ByteArrayInputStream(href.getBytes());

            // 获取输入字节码， 输出到文件中
            while ((len = inputStream.read(bytes)) > 0) {
                outputStream.write(bytes, 0, len);
            }
            outputStream.close();
            inputStream.close();

            return true;
        } catch (IOException e) {
            logger.info(logger.getName() + " " + InfoConstant.WRITE_STRING_TO_FILE_ERROR);
            return false;
        }
    }

    /**
     * 解析选取的标签属性。
     * 标签属性格式：{tag}@{{attrName}={attrValue1}\s{attrValue2}....}
     * {tag}: 标签的名称
     * {attrName}：选取的标签的属性名称
     * {attrValue}：选取的标签的属性值， 使用 空白字符分隔
     *
     * @param selectName ： 选取的标签元素
     * @return ： 解析得到的 org.graduate.service.Entity.Element 对象
     */
    private Element parseElement(String selectName) {
        checkParameter("parseElement", selectName);

        Element element = new Element();

        // 不包含 "@" 字符， 则说明这是一个最终的选择标签，
        // 将直接选择对应的文本信息
        if (!containPoint(selectName)) {
            element.setTagName(selectName);
            element.setIsFinalTag(true);

            return element;
        }

        // 通过选取名的格式，使用 "@" 来分隔标签名和属性
        String[] result = selectName.split("@");

        // 得到的标签应当分隔为 {tag} 和 {attr} 两部分
        if (2 != result.length) {
            logger.info(logger.getName() + "\t" + "select name: " + selectName);
            throw new IllegalArgumentException(selectName + " parseElement method:" +
                    " {tag}@{select rule} split by '@' error " +
                    InfoConstant.SELECT_NAME_FORMAT_INVALID);
        }

        // 按照格式， result的第一部分为标签名
        element.setTagName(result[0]);

        // 标签的属性区分， 获取标签属性名称以及标签属性值
        String[] attrResult = result[1].split("=");

        // 得到的标签应当为 {attrName} 和 {attrValue} 两部分
        if (2 != attrResult.length) {
//            throw new IllegalArgumentException(selectName + "parseElement method: " +
//                    "{attrName}={attrValue} split by '=' error" +
//                    InfoConstant.SELECT_NAME_FORMAT_INVALID);
            /*
                由于这个不包含 = 符号，说明这是一个选择标签属性的选择器，
                应当是选择对应的属性值，同时它也是一个终止标签选择器
            */
            element.setIsFinalTag(true);
            element.setIsSelectAttr(true);
            element.setTagName(result[0]);
            element.setAttrName(result[1]);

            return element;
        }

        // 按照格式， attrResult的第一部分为选取的属性名称
        element.setAttrName(attrResult[0]);
        // attrResult 的第二部分为选取的属性值，使用空白字符区分
        element.setAttrValue(attrResult[1].split("\\s"));
        // 不是最终的选择标签，所以设置最终标记为 "false".
        element.setIsFinalTag(false);

        return element;
    }

    /**
     * 检查方法的参数是否合法：是否为空， 有效长度是否大于 0。
     *
     * @param methodName ： 方法名称
     * @param selectName ： 方法检查的参数
     */
    private void checkParameter(String methodName, String selectName) {
        if (selectName == null || selectName.trim().length() == 0) {
            throw new IllegalArgumentException(methodName + InfoConstant.PARAMETER_VALID_INFO);
        }
    }

    /**
     * 格式化 HTML 文档， 主要是完善那些没有前缀的 URL
     *
     * @return ： 是否格式化 HTML 成功
     * @throws FileNotFoundException : 文件未找到时抛出
     */
    public boolean formatHtmlFile(BookDetail bookDetail) throws FileNotFoundException {
        if (!bookDetail.getUseReplaceFlag()) return true;

        BufferedReader reader = new BufferedReader(new FileReader(this.htmlFile));
        String line;
        try {
            StringBuilder builder = new StringBuilder();
            // 协议长度， 为了检测是否含有前缀， 需要回退相应的步数
            int httpsLength = this.myConfiguration.getSecurityHttpProtocol().length();
            // 默认的前缀协议
//            String defaultProtocol = myConfiguration.getDefaultHttpProtocol();
            String defaultProtocol = bookDetail.getHttpProtocol();
            // 匹配的不包含前缀的 URL的正则表达式
            String regex = bookDetail.getUrlRegex();

            // 匹配不包含协议前缀的 URL 的正则表达式对象
            Pattern pattern = Pattern.compile(regex);
            while ((line = reader.readLine()) != null) {
                Matcher matcher = pattern.matcher(line);

                if (matcher.find()) {
                    line = line.replaceAll(regex, defaultProtocol + regex);
                }

                builder.append(line);
            }

//            writeToFile(elementsFile, builder.toString());
            writeToFile(this.htmlFile, builder.toString());
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * 保存书籍的种类信息以及书籍与种类之间的对应关系
     *
     * @param bookKindNameList ： 待保存的 BookKindName 对象
     */
    private void saveBookKindNameList(@NotNull List<BookKindName> bookKindNameList) {
        /*
            第一次遍历序列，保存未存在的类型名称
         */
        for (BookKindName bookKindName : bookKindNameList) {
            /*
                按名称查找的对象不为 null 则说明存在相关的种类名称
             */
            if (null != bookKindRepo
                    .findBookKindByBookKindName(
                            bookKindName.getKindName()))
                continue;
            org.graduate.service.data.BookKind bookKind =
                    new org.graduate.service.data.BookKind();

            bookKind.setBookKindName(bookKindName.getKindName());

            // 保存相关的书籍种类对象
            bookKindRepo.save(bookKind);
            logger.debug("Save BookKind Object success.");
        }

        /*
            第二次遍历保存相关的书籍的 isbn 号与 书籍种类编号之间的对应
         */
        for (BookKindName bookKindName : bookKindNameList) {
            org.graduate.service.data.BookKind bookKind =
                    bookKindRepo.findBookKindByBookKindName(bookKindName.getKindName());

            Integer bookKindId = bookKind.getBookKindId();
            Long isbn = bookKindName.getIsbn();

            /*
                有可能已经存在相关的对应关系， 因此需要进行检测
             */
            if (null != bookHoldingKindRepo
                    .findBookHoldingKindByBookKindIdAndIsbn(bookKindId, isbn))
                continue;

            BookHoldingKind bookHoldingKind = new BookHoldingKind();
            bookHoldingKind.setBookKindId(bookKindId);
            bookHoldingKind.setIsbn(isbn);

            bookHoldingKindRepo.save(bookHoldingKind);
            logger.debug("Save BookHoldingKind Object success");
        }
    }

    /**
     * 保存 BookInfo 对象
     *
     * @param bookInfo ：待保存的 BookInfo 对象
     * @return ： 保存结果
     */
    private Boolean saveBookInfo(BookInfo bookInfo) {
        if (null == bookInfo) return true; // 忽略保存的 null 对象

        try {
            logger.info("Will save BookInfo Object: " + bookInfo);

            // 保存书籍信息对象
            saveBook(bookInfo.getBook());
            logger.info("Save Book object success.");

            // 将得到的作者信息对象保存
            List<Author> authorList = bookInfo.getAuthorList();
            saveAuthors(authorList);
            logger.info("Save author list success. authors size: " + authorList.size());

            // 将书籍的图像信息保存
            List<BookImage> bookImages = bookInfo.getBookImages();
            saveBookImages(bookImages);
            logger.info("Save BookInfo Object success. bookImages size: " + bookImages);

            /*
               保存书籍的章节信息
             */
            List<BookChapter> bookChapters = bookInfo.getBookChapters();
            saveBookChapters(bookChapters);
            logger.info("Save BookChapter Object success. bookChapter size: " + bookChapters.size());

            // 保存书记的出版信息
            publisherBookRepository.save(bookInfo.getPublishedBook());
            logger.info("Save Publisher Info success");

            // 保存书籍与作者的对应信息
            saveCreatedBookInfo(authorList, bookInfo.getBook().getIsbn());
            logger.info("Save CreatedBook Info success.");

            // 保存书籍的种类信息
            saveBookKindNameList(Collections.singletonList(bookInfo.getBookKindName()));
            logger.info("Save BookKind Info success.");

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 保存对应的书籍与作者的对应信息
     *
     * @param authorList ： 作者信息列表
     * @param isbn       ： 书籍的 isbn 号
     */
    private void saveCreatedBookInfo(@NotNull List<Author> authorList,
                                     @NotNull Long isbn) {
        for (Author author : authorList) {
            // 通过作者名来查找已经存在的作者信息
            Optional<Author> obj = authorRepository
                    .findAuthorByAuthorName(author.getAuthorName());

            if (obj.isEmpty()) continue;

            Author author1 = obj.get();

            // 如果已经有对应的作者书籍对应信息， 则直接跳过
            if (createdBookRepository
                    .findCreatedBookPKByAuthorIdAndIsbn(author1.getAuthorId(), isbn)
                    .isPresent()) continue;

            CreatedBook createdBook = new CreatedBook();

            // 设置对应的属性
            createdBook.setAuthorId(author1.getAuthorId());
            createdBook.setIsbn(isbn);

            createdBookRepository.save(createdBook);
            logger.info("Save CreatedBook object success.");
        }
    }

    /**
     * 保存作者信息列表
     *
     * @param authorList ： 待保存的作者信息列表
     */
    private void saveAuthors(List<Author> authorList) {
        if (null == authorList) return;
        logger.info("AuthorList: " + Arrays.toString(authorList.toArray()));
        for (Author author : authorList) {
            // 如果没有这个作者名的信息， 则保存这个作者
            logger.info("AuthorName: " + author.getAuthorName());
            if (authorRepository
                    .findAuthorByAuthorName(author.getAuthorName())
                    .isEmpty()) {
                authorRepository.save(author);
                logger.info("Save author object: " + author + " success.");
            }
        }
    }

    /**
     * 保存书籍的章节信息列表
     *
     * @param bookChapters : 待保存的 BookChapter 列表
     */
    private void saveBookChapters(List<BookChapter> bookChapters) {
        for (BookChapter bookChapter : bookChapters) {
            // 如果不存在对应的 BookChapter 信息， 则保存它
            if (null != bookChapterRepository.
                    findBookChapterByChapterIdAndIsbn(
                            bookChapter.getChapterId(), bookChapter.getIsbn()))
                continue;
            bookChapterRepository.save(bookChapter);
        }
        logger.info("Save BookChapter List success.");
    }

    /**
     * 获取对应的作者名
     *
     * @param document   ： 待解析的 document 对象
     * @param bookDetail ： 带有对应查找属性的对象
     * @return ： 解析到的 Author 数据对象
     * @throws Exception ：parseBookProp 方法抛出
     */
    private List<Author> getBookAuthors(@NotNull Document document,
                                        @NotNull BookDetail bookDetail)
            throws Exception {
        List<Author> authorList = new ArrayList<>();

        // 查找作者姓名的查找对象
        AuthorName authorName = bookDetail.getAuthorName();

        // 解析到的作者信息列表
        List<String> authorNameList = parseBookProp(document, String.class,
                authorName.getTags(), authorName.getAuthorRegex(),
                authorName.getUseRegex(), false);

        for (String name : authorNameList) {
            Author author = new Author();
            // 设置作者名称
            author.setAuthorName(name);
            authorList.add(author);
        }
        // 设置作者介绍信息
        setAuthorIntro(document, bookDetail, authorList);

        return authorList;
    }

    /**
     * 设置对应的作者对象的介绍信息
     *
     * @param document   ： 待解析的 Document 对象
     * @param bookDetail ： 带有查找属性的 BookDetail 对象， 用于获取对应的查找标签信息
     * @param authorList ： 待设置 的作者对象列表
     * @throws Exception : parseBookProp() 方法出现异常时抛出
     */
    private void setAuthorIntro(@NotNull Document document,
                                @NotNull BookDetail bookDetail,
                                @NotNull List<Author> authorList)
            throws Exception {
        /*
            如果没有对应的作者信息， 则直接返回
         */
        if (0 == authorList.size()) {
            logger.debug("Set Author Intro: authorList size is 0");
            return;
        }

        // 获取对应的作者信息查找对象
        AuthorIntro authorIntro = bookDetail.getAuthorIntro();
        /*
            解析到整个作者介绍的标签内容
         */
        List<String> introList = parseBookProp(document, String.class,
                authorIntro.getTags(), authorIntro.getAuthorIntroRegex(),
                authorIntro.getUseRegex(), false);

        /*
            创建作者与作者介绍的映射
         */
        Map<String, StringBuilder> authorIntroMap = Collections
                .synchronizedMap(new HashMap<>());

        /*
            添加对应的建 —— 作者名称
         */
        for (Author author : authorList) {
            authorIntroMap.put(author.getAuthorName(), new StringBuilder());
        }

        /*
            初始的作者名， 这个代表一个状态， 以切换到不同的作者名的键
         */
        String stateAuthor = authorList.get(0).getAuthorName();

        for (String intro : introList) {
            // 按照 “\n” 换行副分割， 得到每个介绍的信息
            String[] intros = intro.split("\n");
            /*
                检查这个介绍是否包含对应的作者名，
                如果包含这个对应的作者名，
                则将这个介绍注入到这个作者对象中
             */
            for (String introduction : intros) {
                /*
                    依次检查每个作者， 如果有有对应的对象，则放入作者信息中
                 */
                // 如果这个介绍是空白字符， 则跳过
                if (0 == introduction.trim().length()) {
                    continue;
                }

                /*
                    查找对应的状态作者的转换
                 */
                for (Author author : authorList) {
                    if (introduction.contains(author.getAuthorName())) {
                        stateAuthor = author.getAuthorName();
                        break;
                    }
                }

                // 添加到对应的状态作者的键，保存为对应作者的介绍信息
                authorIntroMap.get(stateAuthor).append(introduction).append("\n");
            }
        }

        // 移除每个键的最后一个 “\n” 字符
        for (String key : authorIntroMap.keySet()) {
            StringBuilder builder = authorIntroMap.get(key);
            if (builder.length() > 0) {
                authorIntroMap.get(key)
                        .deleteCharAt(builder.length() - 1);
            }
        }

        // 设置对应作者的介绍信息
        for (Author author : authorList) {
            author.setAuthorIntroduction(authorIntroMap
                    .get(author.getAuthorName())
                    .toString());
        }
    }

    /**
     * 通过对应的 Document 对象获取对应的目录信息
     *
     * @param document   ： 待解析的 Document 对象
     * @param bookDetail ： 包含查找信息的 BookDetail 对象
     * @return ： 解析到的书籍目录信息列表
     * @throws Exception ： 解析 document 出现异常时抛出
     */
    private List<BookChapter> getBookChapters(@NotNull Document document,
                                              @NotNull BookDetail bookDetail)
            throws Exception {
        List<BookChapter> bookChapters = new ArrayList<>();

        // 获取书籍的 isbn
        String isbn = getISBN(document, bookDetail);

        // 书籍目录的查找标签
        BookChapterTag chapterTag = bookDetail.getBookChapterTag();

        // 查找得到的书籍目录信息，以 “\n” 分割
        List<String> chapterList = parseBookProp(document, String.class,
                chapterTag.getTags(), chapterTag.getBookChapterRegex(),
                chapterTag.getUseRegex(), true);

        int chapterId = 1;

        for (String chapter : chapterList) {
            // 按照 "\n" 分割每个目录名
            String[] chapterNames = chapter.split("\n");

            for (String chapterName : chapterNames) {
                BookChapter bookChapter = new BookChapter();
                assert isbn != null;
                bookChapter.setIsbn(Long.parseLong(Objects.requireNonNull(isbn)));
                bookChapter.setChapterId(chapterId++);
                bookChapter.setChapterName(chapterName.replaceAll("<br>", ""));

                // 如果这是一个空的字符串， 则跳过它
                if (0 == chapterName.trim().length())
                    continue;

                // 设置标签的类型，0：未知，1：主标签，2：次标签，3：第二级次标签
                if (isMainTitle(chapterName)) bookChapter.setChapterKind((short) 1);
                else if (isChildTitle(chapterName)) bookChapter.setChapterKind((short) 2);
                else if (isSecondChildTitle(chapterName)) bookChapter.setChapterKind((short) 3);
                else bookChapter.setChapterKind((short) 0);

                bookChapters.add(bookChapter);
            }
        }

        return bookChapters;
    }

    /**
     * 检测当前的目录名是否为主标题
     *
     * @param chapterName ： 待检测的标题名称
     * @return ： 是否是主标题
     */
    private Boolean isMainTitle(@NotNull String chapterName) {
        Pattern chapterTitle = Pattern.compile("[【|第]第*[^回|章]+[回|章]】*");
        Matcher matcher = chapterTitle.matcher(chapterName);

        return matcher.find();
    }

    /**
     * 检测当前的目录名是否是一级子标题
     *
     * @param chapterName ： 待检测标题名称
     * @return ： 是否为一级子标题
     */
    private Boolean isChildTitle(@NotNull String chapterName) {
        Pattern childTitle = Pattern.compile("\\d+.\\d+　");
        Matcher matcher = childTitle.matcher(chapterName);

        return matcher.find();
    }

    /**
     * 检测当前的目录名称是否为二级子标题
     *
     * @param chapterName ： 待检测的标题名称
     * @return ： 是否为二级子标题
     */
    private Boolean isSecondChildTitle(@NotNull String chapterName) {
        Pattern secondChildTitle = Pattern.compile("\\d+.\\d+.\\d+");
        Matcher matcher = secondChildTitle.matcher(chapterName);

        return matcher.find();
    }

    /**
     * 得到执行相关命令之后的相应信息
     *
     * @param command ： 待执行的命令
     * @return ： 执行完成命令之后的字符串响应信息
     */
    private String processCommand(@NotNull String command) {
        try {
            Process exec = Runtime.getRuntime().exec(command);
            InputStream in = exec.getInputStream();

            return readStringFromInputStream(in);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 从输入流中获取字符串序列
     *
     * @param in ： InputStream
     * @return ： 读取到的字符串序列
     * @throws IOException ： 读取时出现异常时抛出
     */
    private String readStringFromInputStream(InputStream in)
            throws IOException {
        StringBuilder builder = new StringBuilder();
        int len;
        byte[] buffer = new byte[1024 * 4];
        while ((len = in.read(buffer)) > 0) {
            String string = new String(buffer, 0, len);
            builder.append(string);
        }
        return builder.toString();
    }

    /**
     * 获取对应的出版社的名称
     *
     * @param document   ： 待解析的 Document 对象
     * @param bookDetail ： 带有查找信息的 BookDetail 对象
     * @return ： null -> 没有找到对应的出版设信息; 查找到的出版社名称
     * @throws Exception ： 解析 Document 对象时出现一场抛出
     */
    private String getPublisherName(@NotNull Document document,
                                    @NotNull BookDetail bookDetail)
            throws Exception {
        PublisherName publisherName = bookDetail.getPublisherName();
        // 由于 application.yml 不支持中文的属性， 不得手动设置对应的正则表达式模式
        publisherName.setPublisherNameRegex(Const.PUBLISHER_NAME_REGEX);

        List<String> publisher = parseBookProp(document, String.class,
                publisherName.getTags(), publisherName.getPublisherNameRegex(),
                publisherName.getUseRegex(), false);

        /*
            如果没有找到对应的出版社的名称， 返回 null
         */
        if (0 == publisher.size()) return null;

        return publisher.get(0);
    }

    /**
     * 从 element 文件中得到 Document 对象
     *
     * @return ： Document 对象
     * @throws IOException ： 解析文件时出现异常抛出
     */
    public Document getDocumentFromElementFile() throws IOException {
        return Jsoup.parse(this.htmlFile, "UTF-8");
    }

    /**
     * 解析得到书籍的出版日期信息
     *
     * @param document   ： 待解析的 Document对象
     * @param bookDetail ： 带有查找信息的 BookDetail 对象
     * @return : 如果不能查找到书籍的出版日期， 则默认返回 “1970-01-01” 的 Instant
     * @throws Exception ： 解析 Document 对象时出现异常时抛出
     */
    private Date getPublisherDate(@NotNull Document document,
                                  @NotNull BookDetail bookDetail)
            throws Exception {
        PublisherDate publisherDate = bookDetail.getPublisherDate();
        // 由于 application.yml 不支持中文, 所以不得已， 只能手动在代码中设置对应的正则表达式了
        publisherDate.setPublisherDateRegex(Const.PUBLISHER_DATE_REGEX);

        List<String> dateList = parseBookProp(document, String.class,
                publisherDate.getTags(), publisherDate.getPublisherDateRegex(),
                publisherDate.getUseRegex(), false);

        // 如果未找到出版日期， 则返回 “1970-01-01” 的 Instant
        if (0 == dateList.size()) return Date.valueOf("1970-01-01");

        return Date.valueOf(dateList.get(0));
    }

    /**
     * 获取对应的 BookKindName 对象列表，
     * 这个对象是包含书籍的 isbn 和对应的种类名称
     *
     * @param document   ： 待解析的 Document 对象
     * @param bookDetail ： 查找对应数据的对象
     * @return ： 解析得到的 BookKindName 对象列表
     * @throws Exception ： 解析 document 对象出现异常时抛出
     */
    private BookKindName getBookKindName(
            @NotNull Document document,
            @NotNull BookDetail bookDetail) throws Exception {
        List<BookKindName> bookKindNameList = new ArrayList<>();

        // 获取书籍种类的查找对象
        BookKind bookKind = bookDetail.getBookKind();

        // 获取对应书籍的 isbn 号
        Long isbn = Long.parseLong(Objects.requireNonNull(
                getISBN(document, bookDetail)));

        // 获取对应书籍的种类名称列表
        List<String> kindNameList = parseBookProp(document, String.class,
                bookKind.getTags(), bookKind.getBookKindRegex(),
                bookKind.getUseRegex(), false);

        BookKindName obj = new BookKindName();
        obj.setIsbn(isbn);
        if (0 == kindNameList.size()) return obj;

        obj.setKindName(kindNameList.get(0));

        return obj;
    }

    /**
     * 从 HTML 文件中解析得到 Document 对象
     *
     * @return ： 从 Html 文件中解析到的 Document 对象
     * @throws IOException ： 解析异常时抛出
     */
    private Document getDocumentFromHtmlFile() throws IOException {
        return Jsoup.parse(this.htmlFile, "UTF-8");
    }

    private ParseDomMessage getParseDomMessageFromMessage(String message) {
        try {
            return jsonObjectConfiguration.jacksonMapper().readValue(message, ParseDomMessage.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
