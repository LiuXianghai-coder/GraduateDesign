package org.graduate.user_service.entity;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: liuxianghai
 * Date: 2021/4/24
 * Time: 上午10:52
 */

public class SingleObject {
    public static final Set<Book> bookSet = new HashSet<>();

    public static Set<Book> getBookSet() {
        return bookSet;
    }
}
