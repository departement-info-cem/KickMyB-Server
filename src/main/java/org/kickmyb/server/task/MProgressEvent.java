package org.kickmyb.server.task;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

/**
 * Created by joris on 15-09-15.
 */
@Entity
public class MProgressEvent {

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id     public Long id;
    public int deltaPercentage;
    public int resultPercentage;
    public boolean completed;
    public Date timestamp;

}
