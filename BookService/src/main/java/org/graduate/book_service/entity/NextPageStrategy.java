package org.graduate.book_service.entity;

/**
 * 由于不同平台之间的差异， 每个平台对于数据的翻页都不一样，
 *  比如 京东：使用 JavaScript 实现翻页， 所以只能在 URL 内加入参数进行翻页
 *  而对于当当网， 则使用的是直接的翻页连接。
 *
 * @author : LiuXianghai on 2020/12/29
 * @Created : 2020/12/29 - 16:20
 * @Project : GetDataService
 */
public enum NextPageStrategy {
    Page,
    Next
}
