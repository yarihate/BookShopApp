package com.example.BookShopApp.data.dto;

import java.util.List;

public class GenreDto {
    private int id;
    private int parentId;
    private String slug;
    private String name;
    private int count;
    private List<GenreDto> subGenres;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<GenreDto> getSubGenres() {
        return subGenres;
    }

    public void setSubGenres(List<GenreDto> subGenres) {
        this.subGenres = subGenres;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
