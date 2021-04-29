package org.graduate.book_service.tools;

import com.sun.istack.NotNull;
import org.graduate.book_service.constant.Const;
import org.graduate.book_service.constant.InfoConstant;
import org.graduate.book_service.constant.UserAgent;
import org.graduate.book_service.data.BookUrl;
import org.graduate.book_service.data.Publisher;
import org.graduate.book_service.entity.*;
import org.graduate.book_service.repository.BookUrlRepo;
import org.graduate.book_service.repository.PublisherRepository;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 使用 application.yml 文件内的 getdata.urls 的 url，
 * 以及 getdata.keyword 组合获得一个 URL， 即可得到一个电商网站的第一次搜索
 * 第一次搜索：用于获得该电商的出版社名称
 * 第二次搜索：获取图书信息、店铺信息、作者信息等
 */
@Service
public class SaveBookUrl {
    private final Logger logger = LoggerFactory.getLogger(SaveBookUrl.class);

    private final BasicUrl basicUrl;

    // 用户代理对象， 随机获取一个用户代理
    private final UserAgent userAgent = new UserAgent();

    // 出版社的 JPA 数据访问接口
    private final PublisherRepository publisherRepository;

    // Publisher 对象的属性配置类
    private final PublisherProps props;

    // 自定义的配置属性对象
    private final MyConfiguration myConfiguration;

    private final BookUrlRepo bookUrlRepo;

    private int recordCount = 0;

    @Autowired
    public SaveBookUrl(BasicUrl basicUrl,
                   PublisherProps props,
                   MyConfiguration myConfiguration,
                   PublisherRepository publisherRepository,
                   BookUrlRepo bookUrlRepo) {

        this.basicUrl = basicUrl;
        this.props = props;
        this.myConfiguration = myConfiguration;

        this.publisherRepository = publisherRepository;
        // 书籍基本对象的 JPA 数据访问接口
        this.bookUrlRepo = bookUrlRepo;
    }

    /**
     * 解析当前页面的查找书籍的地址
     */
    public HttpStatus updateAllBookUrls()
            throws IOException, InterruptedException {
        logger.info("Start Update BookUrls.......");
        logger.info(String.valueOf(basicUrl.getPlatformList().size()));
        for (Platform platform : basicUrl.getPlatformList()) {

            // 访问页面的头部信息
            HeadInfo headInfo = new HeadInfo();
            headInfo.setHttpProtocol(platform.getHttpProtocol());

            // 书籍基本信息的获取对象
            BookList bookList = platform.getBookList();

            // 查找的 Page 页的索引
            int pageIndex = 0;
            int maxSize = 0; // 每次最大地址页面爬取 URL 地址
            while (true) {
                Pageable pageable = PageRequest.of(pageIndex, props.getPageSize(),
                        Sort.by("publisherId"));

                List<Publisher> publishers = publisherRepository.findAll(pageable);
                // 如果查找不到任何 Publisher 对象， 则说明查找到结尾
                if (0 == publishers.size()) break;

                for (Publisher publisher : publishers) {
                    for (String url : platform.getUrls()) {
                        logger.debug(logger.getName() + " publisher url: "
                                + url + publisher.getPublisherName());
                        Connection connection = Jsoup
                                .connect(url + publisher.getPublisherName())
                                .userAgent(userAgent.randomUserAgent());
                        Document document = connection.get();

                        Elements elements = document.getAllElements();

                        if (recordCount > Const.PARSE_COUNT) break;

                        maxSize = 0;
                        if (NextPageStrategy.Next == bookList.getNextPageStrategy()) {
                            String href = nextPageHref(document,
                                    platform.getNextPageSearch(),
                                    platform.getBasicUrl());

                            logger.info("Href: " + href);
                            while (null != href) {
                                // 获取对应的页面的信息
                                connection = Jsoup.connect(href)
                                        .userAgent(userAgent.randomUserAgent());
                                document = connection.get();

                                BookUrl bookUrl = new BookUrl();
                                bookUrl.setUrl(href);
                                bookUrl.setHashBookDetail(platform.getPlatFormId());
                                logger.info("BookUrl: " + bookUrl);

                                bookUrlRepo.save(bookUrl);
                                maxSize++;
                                recordCount++;

                                if (maxSize > 200) break;

                                TimeUnit.SECONDS.sleep(2000L);

                                // 更新页面地址
                                href = nextPageHref(document,
                                        platform.getNextPageSearch(),
                                        platform.getBasicUrl());
                            }
                            
                            continue;
                        }

                        // 如果使用的是通过输入 URL 参数的方式获取当前页面的最大页面数
                        setBookListMaxPage(bookList, elements);

                        // 按照页面参数获取对象
                        HashMap<String, String> parameter = new HashMap<>();
                        for (int i = 1; i <= bookList.getMaxPage(); ++i) {
                            parameter.put(Const.PAGE_PARAMETER_KEY, String.valueOf(i));
                            connection = Jsoup
                                    .connect(url + publisher.getPublisherName())
                                    .userAgent(userAgent.randomUserAgent())
                                    .data(parameter);
                            document = connection.get();

                            saveBookUrls(bookList, document, platform.getPlatFormId());
                        }

                        if (recordCount > Const.PARSE_COUNT) break;

                        // 睡眠 3s， 一个可能有效的处理反扒取机制的方案
                        TimeUnit.SECONDS.sleep(3000L);
                    }

                    // 避免频繁地搜索对应的网站
                    TimeUnit.SECONDS.sleep(5000L);
                }

                pageIndex++;
            }
        }
        logger.info("Finish Update BookUrls.......");

        return HttpStatus.OK;
    }

    /**
     * 解析获取的文档信息，获取对应的书籍信息， 保存对应的书籍信息
     * @param bookList ： 书籍列表查找对象
     * @param document ： 待解析的 Document 对象
     */
    private void saveBookUrls(BookList bookList, Document document, Short platFormId) throws InterruptedException {
        Elements elements;
        elements = document.getAllElements();

        // 解析获得所有的书籍信息商品链接
        Elements elements1 = parseElements(bookList.getTags(), elements);

        String href;
        int maxSize = 0;
        for (org.jsoup.nodes.Element element : elements1) {
            href = perfectHref(element.attr("href"));

            BookUrl bookUrl = new BookUrl();
            bookUrl.setUrl(href);
            bookUrl.setHashBookDetail(platFormId);

            logger.info("BokUrl: " + bookUrl.toString());

            bookUrlRepo.save(bookUrl);
            maxSize++;
            recordCount++;

            if (maxSize > 200) break;

            Thread.sleep(2000L);
        }
    }

    /**
     * 获取下一页面的地址
     * @param document ： 待解析的 Document 对象
     * @param nextPageSearch ： 查找元素的对象
     * @param basicUrl ： 该平台的基础路径
     * @return ： 得到的下一页面地址
     */
    private String nextPageHref(@NotNull Document document,
                                @NotNull NextPageSearch nextPageSearch,
                                @NotNull String basicUrl) {
        StringBuilder href = new StringBuilder(basicUrl);

        Elements elements = document.getAllElements();
        elements = parseElements(nextPageSearch.getTags(), elements);

        org.jsoup.nodes.Element element = elements.first();

        String afterHref = element.attr("href");

        if (null == afterHref) return null;

        href.append(afterHref);

        return href.toString();
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
            logger.debug("maxPageTags: " + Arrays.toString(bookList.getMaxPageTags().toArray()));
            logger.debug("elements: " + elements.toString());
            Elements elements1 = parseElements(bookList.getMaxPageTags(), elements);
            logger.debug("elements1: " + elements1.toString());

            // 获取第一个查找到的最大页数信息
            org.jsoup.nodes.Element element = elements1.first();

            bookList.setMaxPage(Integer.parseInt(element.text()));
        }
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
            throw new IllegalArgumentException(selectName + "parseElement method: " +
                    "{attrName}={attrValue} split by '=' error" +
                    InfoConstant.SELECT_NAME_FORMAT_INVALID);
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
}
