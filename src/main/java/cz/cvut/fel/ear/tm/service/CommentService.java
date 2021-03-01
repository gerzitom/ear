package cz.cvut.fel.ear.tm.service;

import cz.cvut.fel.ear.tm.dao.CommentDao;
import cz.cvut.fel.ear.tm.dao.UserDao;
import cz.cvut.fel.ear.tm.dto.comment.CommentDto;
import cz.cvut.fel.ear.tm.exception.NotFoundException;
import cz.cvut.fel.ear.tm.model.Comment;
import cz.cvut.fel.ear.tm.model.Task;
import cz.cvut.fel.ear.tm.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;

@Service
public class CommentService {

    private final CommentDao dao;

    private final UserDao userDao;

    @Autowired
    public CommentService(CommentDao dao, UserDao userDao) {
        this.dao = dao;
        this.userDao = userDao;
    }

    @Transactional
    public void persist(Comment comment){
        Objects.requireNonNull(comment);
        dao.persist(comment);
    }

    @Transactional
    public void remove(Comment comment){
        Objects.requireNonNull(comment);
        dao.remove(comment);
    }

    @Transactional
    public void update(Comment comment){
        Objects.requireNonNull(comment);
        dao.update(comment);
    }

    /**
     * @param taskId
     * @return Ordered comments
     */
    @Transactional
    public List<Comment> findAll(Long taskId){
        Objects.requireNonNull(taskId);
        return dao.findByTask(taskId);
    }

    public Comment buildFromDto(CommentDto dto) throws NotFoundException{
        Comment comment = new Comment();
        comment.setText(dto.getText());
        comment.setCreated(dto.getCreated());
        User user = userDao.find(dto.getUserId());
        if(user == null) throw new NotFoundException("User not found");
        comment.setUser(userDao.find(dto.getUserId()));
        return comment;
    }
}
