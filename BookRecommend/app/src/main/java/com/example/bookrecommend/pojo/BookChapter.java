package com.example.bookrecommend.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class BookChapter {
    @JsonProperty("isbn")
    private long isbn;

    @JsonProperty("chapterId")
    private int chapterId;

    @JsonProperty("chapterKind")
    private short chapterKind;

    @JsonProperty("chapterName")
    private String chapterName;
}
