package com.example.bookrecommend.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.threeten.bp.OffsetDateTime;

import lombok.Data;

@Data
public class UserBookReview {
    @JsonProperty("bookReviewId")
    private long bookReviewId;

    @JsonProperty("isbn")
    private long isbn;

    @JsonProperty("userId")
    private long userId;

    @JsonProperty("writeDate")
    private OffsetDateTime writeDate;

    @JsonProperty("starNum")
    private Integer starNum;

    @JsonProperty("commentNum")
    private Integer commentNum;

    @JsonProperty("content")
    private String content;

    @JsonProperty("reviewHead")
    private String reviewHead;
}
