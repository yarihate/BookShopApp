package com.example.BookShopApp.data.services;

import com.example.BookShopApp.data.dto.GenreDto;
import com.example.BookShopApp.data.model.genre.GenreEntity;
import com.example.BookShopApp.data.repositories.GenreRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class GenreService {
    private GenreRepository genreRepository;

    @Autowired
    public GenreService(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    public List<GenreDto> getGenres() {
        //итоговый список на отдачу
        List<GenreDto> finalGenreDtos = new ArrayList<>(); //size = 0
        //список первого уровня
        List<GenreEntity> firstLevelGenre = genreRepository.findByParentIdIsNull();
        if (firstLevelGenre.isEmpty()) {
            return finalGenreDtos;
        }
        firstLevelGenre.forEach(firstLevGenre -> finalGenreDtos.add(recursiveGenreMapper(firstLevGenre)));
        finalGenreDtos.sort(Comparator.comparingInt(GenreDto::getCount).reversed());
        return finalGenreDtos;
    }

    private GenreDto recursiveGenreMapper(GenreEntity genre) {
        GenreDto genreDto = new GenreDto();
        genreDto.setName(genre.getName());
        genreDto.setSlug(genre.getSlug());
        //create SubGenre List
        List<GenreEntity> subGenreList = new ArrayList<>(genreRepository.findByParentIdIs(genre.getId()));
        if (subGenreList.isEmpty()) {
            genreDto.setCount(genre.getCount());
            return genreDto;
        }
        List<GenreDto> genreDtos = new ArrayList<>();
        subGenreList.forEach(subGenre -> genreDtos.add(recursiveGenreMapper(subGenre)));
        genreDtos.sort(Comparator.comparingInt(GenreDto::getCount).reversed());
        genreDto.setSubGenres(genreDtos);
        //count
        Integer count = genreDtos.stream()
                .map(GenreDto::getCount)
                .map(Optional::ofNullable)
                .reduce(sum)
                .flatMap(Function.identity())
                .orElse(0);
        genreDto.setCount(count + genre.getCount());
        return genreDto;
    }

    private static final BinaryOperator<Optional<Integer>> sum = (a, b) -> a.isPresent() ?
            (b.isPresent() ? a.map(n -> b.get() + n) : a) : b;
}
