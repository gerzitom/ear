package cz.cvut.fel.ear.tm.service;

import cz.cvut.fel.ear.tm.dao.CommentDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentService {

    private final CommentDao dao;

    @Autowired
    public CommentService(CommentDao dao) {
        this.dao = dao;
    }

}
