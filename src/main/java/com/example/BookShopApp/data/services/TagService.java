package com.example.BookShopApp.data.services;

import com.example.BookShopApp.data.model.tag.TagEntity;
import com.example.BookShopApp.data.repositories.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

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

    enum TagSize {
        MD,
        LG,
        SM,
        XS;
    }
}
