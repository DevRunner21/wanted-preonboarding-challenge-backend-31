package com.pawn.wantedcqrs.tag.repository;

import com.pawn.wantedcqrs.tag.domain.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    List<Tag> findTagsByIdIn(Collection<Long> ids);
}
