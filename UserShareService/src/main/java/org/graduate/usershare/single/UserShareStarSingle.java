package org.graduate.usershare.single;

import org.graduate.usershare.data.UserShareStar;

/**
 * Created by IntelliJ IDEA.
 * User: liuxianghai
 * Date: 2021/4/5
 * Time: 下午9:16
 */

public enum UserShareStarSingle {
    DEFAULT_INSTANCE(new UserShareStar());

    private final UserShareStar obj;

    UserShareStarSingle(UserShareStar obj) {
        this.obj = obj;
    }

    public UserShareStar getObj() {
        return obj;
    }
}
