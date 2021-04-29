package org.graduate.savefile.service;

/**
 * 存储时出现的异常类
 *
 * @author : LiuXianghai on 2021/2/23
 * @Created : 2021/02/23 - 20:07
 * @Project : savefile
 */
public class StorageException extends RuntimeException {
    public StorageException(String message) {
        super(message);
    }

    public StorageException(String message, Throwable t) {
        super(message, t);
    }
}
