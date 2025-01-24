package org.kickmyb.server.task;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.util.Date;

/**
 * Created by joris on 15-09-15.
 */
@Entity
public class MProgressEvent {

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    public Long id;
    public int deltaPercentage;
    public int resultPercentage;
    public boolean completed;
    public Date timestamp;

}
