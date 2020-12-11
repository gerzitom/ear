package cz.cvut.fel.ear.tm.service;

import cz.cvut.fel.ear.tm.dao.TrackedTimeDao;
import cz.cvut.fel.ear.tm.model.Task;
import cz.cvut.fel.ear.tm.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class TrackedTimeService {
    TrackedTimeDao dao;

    @Autowired
    public TrackedTimeService(TrackedTimeDao dao){
        this.dao = dao;
    }


    @Transactional
    public void startTrack(Task task, User user){

    }

    @Transactional
    public void endTrack(Task task, User user){

    }


}
