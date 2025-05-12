package com.pawn.wantedcqrs.review.repository;

import com.pawn.wantedcqrs.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long>, QReviewRepositoryCustom {

}
