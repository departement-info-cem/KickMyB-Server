package org.kickmyb.server.task;

import org.joda.time.DateTime;
import org.kickmyb.server.account.MUser;
import org.kickmyb.server.account.MUserRepository;
import org.kickmyb.transfer.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
@Transactional
public class ServiceTaskImpl implements ServiceTask {

    @Autowired
    MUserRepository repoUser;
    @Autowired MTaskRepository repo;
    @Autowired MProgressEventRepository repoProgressEvent;

    private double percentage(Date start, Date current, Date end){
        if (current.after(end)) return 100.0;
        long total = end.getTime() - start.getTime();
        long spent = current.getTime() - start.getTime();
        double percentage =  100.0 * spent / total;
        percentage = Math.max(percentage, 0);
        percentage = Math.min(percentage, 100);
        System.out.println("Percentage is " + percentage);
        return percentage;
    }

    @Override
    public TaskDetailResponse detail(Long id, MUser user) {
        MTask element = user.tasks.stream().filter(elt -> elt.id == id).findFirst().get();
        TaskDetailResponse response = new TaskDetailResponse();
        response.name = element.name;
        response.id = element.id;
        // calcul le temps écoulé en pourcentage
        response.percentageTimeSpent = percentage(element.creationDate, new Date(), element.deadline);
        // aller chercher le dernier événement de progrès
        response.percentageDone = percentageDone(element);
        response.deadline = element.deadline;
        response.events = new ArrayList<>();
        for (MProgressEvent e : element.events) {
            ProgressEvent transfer = new ProgressEvent();
            transfer.value = e.resultPercentage;
            transfer.timestamp = e.timestamp;
            response.events.add(transfer);
        }
        return response;
    }

    // TODO oublier de valider pour une injection javascript
    // TODO Que se passe-t-il si ce n'est pas transactionnel
    @Override
    public void addOne(AddTaskRequest req, MUser user) throws Existing, Empty, TooShort {
        // valider que c'est non vide
        if (req.name.trim().length() == 0) throw new Empty();
        if (req.name.trim().length() < 2) throw new TooShort();
        // valider si le nom existe déjà
        for (MTask b : user.tasks) {
            if (b.name.equalsIgnoreCase(req.name)) throw new Existing();
        }
        // tout est beau, on crée
        MTask t = new MTask();
        t.name = req.name.trim();
        t.creationDate = DateTime.now().toDate();
        if (req.deadline == null) {
            t.deadline = DateTime.now().plusDays(7).toDate();
        } else {
            t.deadline = req.deadline;
        }
        repo.save(t);
        user.tasks.add(t);
        repoUser.save(user);
    }

    @Override
    public void updateProgress(long taskID, int value) {
        MTask element = repo.findById(taskID).get();
        if (value < 0 ) throw new IllegalArgumentException();
        if (value > 100 ) throw new IllegalArgumentException();
        MProgressEvent pe= new MProgressEvent();
        pe.resultPercentage = value;
        pe.completed = value == 100;
        pe.timestamp = DateTime.now().toDate();
        repoProgressEvent.save(pe);
        element.events.add(pe);
        repo.save(element);
    }

    @Override
    public List<HomeItemResponse> home(Long userID) {
        MUser user = repoUser.findById(userID).get();
        List<HomeItemResponse> res = new ArrayList<>();
        for (MTask t : user.tasks) {
            HomeItemResponse r = new HomeItemResponse();
            r.id = t.id;
            r.percentageDone = percentageDone(t);
            r.deadline = t.deadline;
            r.percentageTimeSpent = percentage(t.creationDate, new Date(), t.deadline);
            r.name = t.name;
            res.add(r);
        }
        return res;
    }

    private int percentageDone(MTask t) {
        return t.events.isEmpty()? 0 : t.events.get(t.events.size()-1).resultPercentage;
    }


    @Override
    public MUser userFromUsername(String username) {
        return repoUser.findByUsername(username).get();
    }

}
