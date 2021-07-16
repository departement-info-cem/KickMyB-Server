package org.kickmyb.server.account;

import org.kickmyb.server.task.MTask;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by joris on 15-09-15.
 */

@Entity
public class MUser {

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    public Long id;

    @Column(unique = true)
    public String username;

    @Basic
    public String password;

    // ORM style storage.
    @OneToMany(fetch=FetchType.EAGER)
    public List<MTask> tasks = new ArrayList<>();
}
