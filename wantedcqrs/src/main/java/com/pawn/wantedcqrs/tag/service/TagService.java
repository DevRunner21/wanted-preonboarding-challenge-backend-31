package com.pawn.wantedcqrs.tag.service;

import com.pawn.wantedcqrs.tag.dto.TagDto;
import com.pawn.wantedcqrs.tag.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TagService {

    private final TagRepository tagRepository;

    public List<TagDto> getTagsBy(List<Long> tagIds) {
        return tagRepository.findTagsByIdIn(tagIds).stream()
                .map(TagDto::fromEntity)
                .collect(Collectors.toList());
    }

}
