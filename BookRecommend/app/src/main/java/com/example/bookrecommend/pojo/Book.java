package com.example.bookrecommend.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class Book {
    @JsonProperty("isbn")
    private long isbn;

    @JsonProperty("bookName")
    private String bookName;

    @JsonProperty("introduction")
    private String introduction;

    @JsonProperty("bookScore")
    private Short bookScore;
}
