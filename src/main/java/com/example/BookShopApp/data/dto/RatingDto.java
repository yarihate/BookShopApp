package com.example.BookShopApp.data.dto;

public class RatingDto {

    private String slug;
    private int ratingOverallCount;
    private int oneStarCount;
    private int twoStarsCount;
    private int threeStarsCount;
    private int fourStarsCount;
    private int fiveStarsCount;

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public int getRatingOverallCount() {
        return ratingOverallCount;
    }

    public void setRatingOverallCount(int ratingOverallCount) {
        this.ratingOverallCount = ratingOverallCount;
    }

    public int getOneStarCount() {
        return oneStarCount;
    }

    public void setOneStarCount(int oneStarCount) {
        this.oneStarCount = oneStarCount;
    }

    public int getTwoStarsCount() {
        return twoStarsCount;
    }

    public void setTwoStarsCount(int twoStarsCount) {
        this.twoStarsCount = twoStarsCount;
    }

    public int getThreeStarsCount() {
        return threeStarsCount;
    }

    public void setThreeStarsCount(int threeStarsCount) {
        this.threeStarsCount = threeStarsCount;
    }

    public int getFourStarsCount() {
        return fourStarsCount;
    }

    public void setFourStarsCount(int fourStarsCount) {
        this.fourStarsCount = fourStarsCount;
    }

    public int getFiveStarsCount() {
        return fiveStarsCount;
    }

    public void setFiveStarsCount(int fiveStarsCount) {
        this.fiveStarsCount = fiveStarsCount;
    }
}
