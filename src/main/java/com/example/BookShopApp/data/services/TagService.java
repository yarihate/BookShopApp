package com.example.BookShopApp.data.services;

import com.example.BookShopApp.data.model.tag.TagEntity;
import com.example.BookShopApp.data.repositories.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.example.BookShopApp.data.services.TagService.TagSize.*;

@Service
public class TagService {
    private TagRepository tagRepository;

    @Autowired
    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public List<TagEntity> getTagsRateData() {
        return tagRepository.findAllTagsRate();
    }

    public Map<TagEntity, String> createRangedMap(List<TagEntity> tags) {
        Map<TagEntity, String> result = new HashMap<>();
        int max = tags.stream()
                .mapToInt(TagEntity::getRate)
                .max().orElseThrow(NoSuchElementException::new);

        int step = max / 4;
        for (TagEntity tag : tags) {
            int half = max / 2;
            if (tag.getRate() <= max / 2) {
                half /= 2;
                if (tag.getRate() <= half) {
                    result.put(tag, "Tag_" + XS.name().toLowerCase());
                } else {
                    result.put(tag, "Tag_" + SM.name().toLowerCase());
                }
            } else {
                half += step;
                if (tag.getRate() <= half) {
                    result.put(tag, "Tag_" + MD.name().toLowerCase());
                } else {

                }
                result.put(tag, "Tag_" + LG.name().toLowerCase());
            }

        }
        return result;
    }

    private int[][] createRanges(int max) {
        int step = max / 4;
        int[][] ranges = new int[4][2];
        int leftBorder = 0;

        for (int i = 0; i < 4; i++) {
            int[] range = ranges[i];
            for (int j = 0; j < 2; j++) {
                range[j] = leftBorder;
                j++;
                leftBorder += step;
                range[j] = leftBorder;
            }
        }
        return ranges;
    }

    enum TagSize {
        MD,
        LG,
        SM,
        XS;
    }
}
