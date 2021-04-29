package org.graduate.service;

import com.sun.istack.NotNull;
import org.graduate.service.constants.Const;
import org.graduate.service.constants.InfoConstant;
import org.graduate.service.data_entity.Author;
import org.graduate.service.data_entity.Book;
import org.graduate.service.data_entity.BookChapter;
import org.graduate.service.data_entity.BookImage;
import org.graduate.service.entity.*;
import org.graduate.service.tags.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.Date;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by IntelliJ IDEA.
 * User: liuxianghai
 * Date: 2021/2/13
 * Time: 下午3:09
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class BookRepoTest {
    private Book getBookObj(Document document, BookDetail bookDetail) throws
            ClassNotFoundException, NoSuchMethodException, InvocationTargetException,
            InstantiationException, IllegalAccessException {
        Book book = new Book();

        // 方法传入参数为空时的处理方法
        if (null == bookDetail) {
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

    private String getISBN(Document document, BookDetail bookDetail)
            throws ClassNotFoundException, NoSuchMethodException,
            IllegalAccessException, InvocationTargetException, InstantiationException {
        // 查找的条件 ISBN 属性对象
        ISBN isbnObj = bookDetail.getIsbn();
        // 解析得到的 ISBN 信息列表， 取第一个为该书籍的 ISBN

        System.out.println("isbn obj: " + isbnObj.toString());
        List<String> isbnList = parseBookProp(document, String.class,
                isbnObj.getTags(), isbnObj.getIsbnRegex(),
                isbnObj.getUseRegex(), false);

        System.out.println("isbn size: " + isbnList.size());

        if (0 == isbnList.size()) return null;

        assert isbnList.size() > 0; // 每本书籍页面至少包含一个ISBN
        return isbnList.get(Const.DEFAULT_GET_INFO_INDEX);
    }

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

    private String trimContent(String content) {
        return content.trim();
    }

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

    private Element parseElement(String selectName) {
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

    private boolean containPoint(String selectName) {
        return selectName.contains("@");
    }

    private void sortTags(List<Tag> tags) {
        tags.sort(Tag::compareTo);
    }

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

    private List<BookImage> getBookImageList(Document document, BookDetail bookDetail)
            throws ClassNotFoundException, NoSuchMethodException,
            InvocationTargetException, InstantiationException, IllegalAccessException {
        // 图书图像信息对象列表
        List<BookImage> result = new ArrayList<>();
        // 方法传入参数为空时的处理方法
        if (null == bookDetail) {
            return result;
        }

        String ISBN = getISBN(document, bookDetail);

        // 查询的 BookImage 对象的条件查找对象
        org.graduate.service.tags.BookImage bookImage = bookDetail.getBookImage();

        List<String> imageUrl = parseBookProp(document, String.class,
                bookImage.getTags(), bookImage.getUrlRegex(),
                bookImage.getUseRegex(), true);

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

    private String perfectHref(String href) {

        // 检测链接是否已经完善的链接地址的正则表达式对象
        Pattern pattern = Pattern.compile(myConfiguration.getHttpPrefixRegex());
        Matcher matcher = pattern.matcher(href);

        // 如果满足要求， 则直接返回原有链接对象
        if (matcher.find()) return href;

        return myConfiguration.getDefaultHttpProtocol() + href;
    }

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

    private void setAuthorIntro(@NotNull Document document,
                                @NotNull BookDetail bookDetail,
                                @NotNull List<Author> authorList)
            throws Exception {
        /*
            如果没有对应的作者信息， 则直接返回
         */
        if (0 == authorList.size()) {
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

    private Boolean isMainTitle(@NotNull String chapterName) {
        Pattern chapterTitle = Pattern.compile("^[【|第]第*[^回|章]+[回|章]】*");
        Matcher matcher = chapterTitle.matcher(chapterName);

        return matcher.find();
    }

    private Boolean isChildTitle(@NotNull String chapterName) {
        Pattern childTitle = Pattern.compile("^\\d+.\\d+　");
        Matcher matcher = childTitle.matcher(chapterName);

        return matcher.find();
    }

    private Boolean isSecondChildTitle(@NotNull String chapterName) {
        Pattern secondChildTitle = Pattern.compile("^\\d+.\\d+.\\d+");
        Matcher matcher = secondChildTitle.matcher(chapterName);

        return matcher.find();
    }

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

    private java.sql.Date getPublisherDate(@NotNull Document document,
                                           @NotNull BookDetail bookDetail)
            throws Exception {
        PublisherDate publisherDate = bookDetail.getPublisherDate();
        // 由于 application.yml 不支持中文, 所以不得已， 只能手动在代码中设置对应的正则表达式了
        publisherDate.setPublisherDateRegex(Const.PUBLISHER_DATE_REGEX);

        List<String> dateList = parseBookProp(document, String.class,
                publisherDate.getTags(), publisherDate.getPublisherDateRegex(),
                publisherDate.getUseRegex(), false);

        // 如果未找到出版日期， 则返回 “1970-01-01” 的 Instant
        if (0 == dateList.size()) return java.sql.Date.valueOf("1970-01-01");

        System.out.println("date: " + dateList.get(0));

        return Date.valueOf(dateList.get(0));
    }

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

    private static long count = 0;

    @Autowired
    private BasicUrl basicUrl;

    @Autowired
    private MyConfiguration myConfiguration;

    @Test
    public void test() throws Exception {
        Document document = Jsoup.parse(new File("/var/tmp/data.html"),
                "UTF-8");

        Platform platform = basicUrl.getPlatformList().get(0);

        BookDetail bookDetail = platform.getBookDetail();

        System.out.println("GetBookObj: " + getBookObj(document, bookDetail));
        System.out.println();
        System.out.println("BookImageList: " +
                Arrays.toString(getBookImageList(document, bookDetail).toArray()));
        System.out.println();
        System.out.println("Authors: " +
                Arrays.toString(getBookAuthors(document, bookDetail).toArray()));
        System.out.println();
        System.out.println("Chapters: " +
                Arrays.toString(getBookChapters(document, bookDetail).toArray()));
        System.out.println();
        System.out.println("publisherName: " + getPublisherName(document, bookDetail));
        System.out.println();
        System.out.println("publisherDate: " + getPublisherDate(document, bookDetail));
        System.out.println();
        System.out.println("bookKind: " + getBookKindName(document, bookDetail));
    }
}
