package com.krixon.ecosystem.profiling.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface AnswerRepository extends PagingAndSortingRepository<Answer, AnswerId>
{
    Page<Answer> findAllByIdMemberId(String memberId, Pageable pageable);
}
