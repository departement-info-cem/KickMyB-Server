package org.kickmyb.server.model;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

/**
 * Created by joris on 15-09-15.
 */
@Entity
public class MEvent {

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id     public Long id;
    public Date timestamp;

}
