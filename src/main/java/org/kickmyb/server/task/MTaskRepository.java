package org.kickmyb.server.task;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.scheduling.config.Task;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MTaskRepository extends JpaRepository<MTask, Long> {
    }

