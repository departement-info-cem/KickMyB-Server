package org.kickmyb.server.task;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

// Mostly used to have creation method, could use cascading

@Repository
public interface MProgressEventRepository extends PagingAndSortingRepository<MProgressEvent, Long> { }
