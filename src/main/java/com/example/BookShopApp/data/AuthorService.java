package com.example.BookShopApp.data;

import com.example.BookShopApp.data.model.author.AuthorEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AuthorService {

    private final AuthorRepository authorRepository;

    @Autowired
    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public Map<String, List<AuthorEntity>> getAuthorsData() {
        List<AuthorEntity> authors = authorRepository.findAll();
        return authors.stream().collect
                (Collectors.groupingBy(author -> String.valueOf(author.getName().charAt(0))));
    }
}
