package org.graduate.savefile.service;

import org.springframework.core.io.Resource;
import org.springframework.lang.NonNull;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.nio.file.Path;
import java.util.stream.Stream;

/**
 * @author : LiuXianghai on 2021/2/23
 * @Created : 2021/02/23 - 19:20
 * @Project : savefile
 */
public interface StorageService {
    /*
        根据用户 ID 来创建最新的上传目录，
        这不仅便于管理， 同时还能更加方便地作为资源加载
     */
    void init(String userId);

    void store(MultipartFile file);

    void store(@NonNull InputStream inputStream,
               @NonNull String fileName);

    Stream<Path> loadAll();

    Path load(String fileName);

    Resource loadAsResource(String fileName);

    void deleteAll();
}
