package org.kickmyb.server.photo;

import org.kickmyb.server.task.MTask;

import javax.persistence.*;

@Entity
public class MPhoto {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;

    @Lob    public byte[] blob;
    @Basic  public String contentType;
    @OneToOne
    public MTask task;
}
