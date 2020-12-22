package org.kickmyb.server.tasks;

import org.kickmyb.server.exceptions.Existing;
import org.kickmyb.server.model.MTask;
import org.kickmyb.server.model.MUser;
import org.kickmyb.server.model.MEventRepository;
import org.kickmyb.server.model.MTaskRepository;
import org.kickmyb.server.model.MUserRepository;
import org.kickmyb.transfer.AddTaskRequest;
import org.kickmyb.transfer.HomeItemResponse;
import org.kickmyb.transfer.TaskDetailResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
@Transactional
public class ServiceWithDB implements Service{

    @Autowired
    MUserRepository repoUser;
    @Autowired
    MTaskRepository repo;
    @Autowired
    MEventRepository repoBabyEvent;

    @Override
    public TaskDetailResponse detail(Long id, MUser user) {
        MTask baby = user.tasks.stream().filter(elt -> elt.id == id).findFirst().get();
        TaskDetailResponse response = new TaskDetailResponse();
        response.name = baby.name;
        response.id = baby.id;
        return response;
    }

    // TODO oublier de valider le nom du bébé pour une injection javascript
    // TODO faire une page jsp qui affiche les gardiens puis les enfants par gardiens
    // TODO exploser la liste avec injection JS
    @Override
    public void addOne(AddTaskRequest req, MUser user) throws Existing {
        // TODO validation
        if (req.name.trim().length() == 0) throw new IllegalArgumentException();
        // TODO validate no baby with same name
        for (MTask b : user.tasks) {
            if (b.name.toLowerCase().equals(req.name.toLowerCase())) throw new Existing();
        }
        // All is good
        MTask baby = new MTask();
        baby.name = req.name;
        repo.save(baby);
        user.tasks.add(baby);
        repoUser.save(user);
    }

    @Override
    public List<HomeItemResponse> home(Long userID) {
        MUser user = repoUser.findById(userID).get();
        List<HomeItemResponse> res = new ArrayList<>();
        for (MTask t : user.tasks) {
            HomeItemResponse r = new HomeItemResponse();
            r.id = t.id;
            r.percentageDone = 20 + new Random().nextInt(50);
            r.name = t.name;
            res.add(r);
        }
        return res;
    }

    // TODO try to see how to make an injection attack example by directly exposing data from DB
    @Override
    public String index() {
        String res = "<html>";
        res += "<div>Index :</div>";
        for (MUser u: repoUser.findAll()) {
            res += "<div>" + u.username  + "</div>";
        }
        res += "</html>";
        return res;
    }

    @Override
    public MUser userFromUsername(String username) {
        return repoUser.findByUsername(username).get();
    }

}
