package org.graduate.elastic_search_service.single;

import org.graduate.elastic_search_service.entity.SearchPage;

/**
 * Created by IntelliJ IDEA.
 * User: liuxianghai
 * Date: 2021/4/27
 * Time: 上午9:44
 */

public enum SingleSearchPage {
    DEFAULT_INSTANCE(0, 20, "bookName");

    private final SearchPage searchPage;

    SingleSearchPage(Integer startLocation, Integer size, String sortCol) {
        this.searchPage = new SearchPage(startLocation, size, sortCol);
    }

    public SearchPage getSearchPage() {
        return searchPage;
    }
}
