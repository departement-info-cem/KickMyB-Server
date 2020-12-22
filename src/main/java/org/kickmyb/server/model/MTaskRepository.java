package org.kickmyb.server.model;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MTaskRepository extends PagingAndSortingRepository<MTask, Long> { }
