package com.example.BookShopApp.data.model.book.file;

import javax.persistence.*;

@Entity
@Table(name = "book_file")
public class BookFileEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(columnDefinition = "VARCHAR(255) NOT NULL")
    private String hash;
    @Column(columnDefinition = "INT NOT NULL")
    private int typeId;
    @Column(columnDefinition = "VARCHAR(255) NOT NULL")
    private String path;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
