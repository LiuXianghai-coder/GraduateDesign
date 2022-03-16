package org.graduate.elastic_search_service.single;

import org.graduate.elastic_search_service.entity.ContentType;
import org.graduate.elastic_search_service.entity.SearchContent;

public enum SingleSearchContent {
    DEFAULT_INSTANCE(ContentType.BookName, "书");

    private final SearchContent searchContent;

    SingleSearchContent(ContentType type, String content) {
        this.searchContent = new SearchContent(type, content);
    }

    public SearchContent getSearchContent() {
        return searchContent;
    }
}
