package org.graduate.savefile.controller;

import lombok.extern.slf4j.Slf4j;
import org.graduate.savefile.entity.BytesMultipartFile;
import org.graduate.savefile.entity.ImageURL;
import org.graduate.savefile.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Iterator;

/**
 * 处理上传的文档信息
 *
 * @author : LiuXianghai on 2021/2/23
 * @Created : 2021/02/23 - 19:15
 * @Project : savefile
 */
@Slf4j
@RestController
@RequestMapping(path = "")
@CrossOrigin(origins = "*")
public class SaveFileController {
    private final StorageService storageService;

    @javax.annotation.Resource(name = "hostAddress")
    private String hostAddress;

    @javax.annotation.Resource(name = "serverPort")
    private String serverPort;

    @Autowired
    public SaveFileController(StorageService storageService) {
        this.storageService = storageService;
    }

    @PostMapping(path = "/upload")
    public ResponseEntity<ImageURL> uploadFile(
            @RequestParam(name = "imageFile") MultipartFile file,
            @RequestParam(name = "userId") String userId) {
        storageService.init(userId);
        storageService.store(file);

        System.out.println("Content-Type: " + file.getContentType());

        String fileUrl = "http://" + hostAddress + ":" + serverPort +
                "/file/" + file.getOriginalFilename() + "?userId=" + userId;

        ImageURL imageURL = new ImageURL();
        imageURL.setImageUrl(fileUrl);

        return ResponseEntity.ok().body(imageURL);
    }


    @GetMapping(path = "/file/{fileName}")
    public ResponseEntity<Resource> getFile(
            @PathVariable(name = "fileName") String fileName,
            @RequestParam(name = "userId") String userId
    ) {
        // 重新加载根目录，这是由于每个用户 ID 都有不同的根目录
        storageService.init(userId);
        Resource file = storageService.loadAsResource(fileName);

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE,
                "image/jpeg; filename=\"" + file.getFilename() + "\"").body(file);
    }
}
