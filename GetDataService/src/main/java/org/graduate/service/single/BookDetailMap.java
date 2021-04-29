package org.graduate.service.single;

import org.graduate.service.entity.BasicUrl;
import org.graduate.service.entity.BookDetail;
import org.graduate.service.entity.Platform;
import org.graduate.service.tool.GetData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author : LiuXianghai on 2021/3/27
 * @Created : 2021/03/27 - 21:26
 * @Project : GetDataService
 */
@Component
public class BookDetailMap {

    private static final Map<Short, BookDetail> bookDetailMap = new HashMap<>();

    public BookDetailMap(BasicUrl basicUrl) {
        List<Platform> platformList = basicUrl.getPlatformList();
        for (Platform platform : platformList) {
            bookDetailMap.put(platform.getPlatFormId(), platform.getBookDetail());
        }
    }

    public static Map<Short, BookDetail>  getBookDetailMap() {
        return bookDetailMap;
    }
}
