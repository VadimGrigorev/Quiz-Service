package com.quiz.repository;

import com.quiz.model.Completion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface CompletionRepository extends PagingAndSortingRepository<Completion, Integer> {
    public Page<Completion> findByUserId(Integer UserId, Pageable paging);
}
