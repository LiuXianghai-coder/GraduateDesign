package org.graduate.savefile.entity;

import org.springframework.web.multipart.MultipartFile;

import java.io.*;

/**
 * @author : LiuXianghai on 2021/3/4
 * @Created : 2021/03/04 - 9:23
 * @Project : savefile
 */
public class BytesMultipartFile implements MultipartFile {
    private final byte[] imgContent;

    private final String originalName;

    private final String contentType;

    public BytesMultipartFile(byte[] imgContent,
                              String originalName,
                              String contentType) {
        this.imgContent = imgContent;
        this.originalName = originalName;
        this.contentType = contentType;
    }

    @Override
    public String getName() {
        return "file";
    }

    @Override
    public String getOriginalFilename() {
        return originalName;
    }

    @Override
    public String getContentType() {
        return contentType;
    }

    @Override
    public boolean isEmpty() {
        return imgContent == null || imgContent.length == 0;
    }

    @Override
    public long getSize() {
        return imgContent.length;
    }

    @Override
    public byte[] getBytes() throws IOException {
        return imgContent;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(imgContent);
    }

    @Override
    public void transferTo(File dest) throws IOException, IllegalStateException {
        new FileOutputStream(dest).write(imgContent);
    }
}
