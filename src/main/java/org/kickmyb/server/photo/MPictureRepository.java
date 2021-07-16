package org.kickmyb.server.photo;

import org.kickmyb.server.task.MTask;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MPictureRepository extends PagingAndSortingRepository<MPicture, Long> {
    Optional<MPicture> findByTask(MTask baby);
}
