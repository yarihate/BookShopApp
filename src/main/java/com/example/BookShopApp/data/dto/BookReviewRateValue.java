package com.example.BookShopApp.data.dto;

public class BookReviewRateValue {
    private short value;
    private Integer reviewid;

    public short getValue() {
        return value;
    }

    public void setValue(short value) {
        this.value = value;
    }

    public Integer getReviewid() {
        return reviewid;
    }

    public void setReviewid(Integer reviewid) {
        this.reviewid = reviewid;
    }
}
