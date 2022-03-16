package org.graduate.elastic_search_service.data;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 书籍的章节信息类
 *
 * @author : LiuXianghai on 2020/12/29
 * @Created : 2020/12/29 - 9:30
 */
@Data
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class BookChapter {
    private Number isbn;

    private Number chapterId;

    private Number chapterKind;

    private String chapterName;
}
