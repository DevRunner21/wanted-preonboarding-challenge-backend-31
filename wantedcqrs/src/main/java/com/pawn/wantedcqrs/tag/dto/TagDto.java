package com.pawn.wantedcqrs.tag.dto;

import com.pawn.wantedcqrs.tag.domain.Tag;
import lombok.Data;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class TagDto {
    private Long id;
    private String name;
    private String slug;

    public static TagDto fromEntity(Tag tag) {
        if (tag == null) return null;

        TagDto dto = new TagDto();
        dto.setId(tag.getId());
        dto.setName(tag.getName());
        dto.setSlug(tag.getSlug());
        return dto;
    }

    public static List<TagDto> fromEntities(List<Tag> tags) {
        if (tags == null) return Collections.emptyList();
        return tags.stream()
                .map(TagDto::fromEntity)
                .collect(Collectors.toList());
    }

}
