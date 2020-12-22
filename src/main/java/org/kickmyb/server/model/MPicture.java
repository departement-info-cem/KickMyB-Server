package org.kickmyb.server.model;

import javax.persistence.*;

@Entity
public class MPicture {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;

    @Lob    public byte[] blob;
    @Basic  public String contentType;
    @OneToOne
    public MTask task;
}
