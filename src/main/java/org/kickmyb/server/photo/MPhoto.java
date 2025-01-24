package org.kickmyb.server.photo;

import jakarta.persistence.*;
import org.kickmyb.server.task.MTask;


@Entity
public class MPhoto {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;

    @Lob
    public byte[] blob;
    @Basic  public String contentType;
    @OneToOne
    public MTask task;
}
