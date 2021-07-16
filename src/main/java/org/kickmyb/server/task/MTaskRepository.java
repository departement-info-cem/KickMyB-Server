package org.kickmyb.server.task;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MTaskRepository extends PagingAndSortingRepository<MTask, Long> { }
