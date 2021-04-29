package org.graduate.usershare.single;

import org.graduate.usershare.data.UserShareCollections;

public enum UserShareCollectSingle {
    DEFAULT_INSTANCE(new UserShareCollections());

    private final UserShareCollections obj;

    UserShareCollectSingle(UserShareCollections obj) {
        this.obj = obj;
    }

    public UserShareCollections getObj() {
        return obj;
    }
}
