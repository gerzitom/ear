package cz.cvut.fel.ear.tm.service;

import cz.cvut.fel.ear.tm.dao.CommentDao;
import cz.cvut.fel.ear.tm.dao.TaskDao;
import cz.cvut.fel.ear.tm.model.Comment;
import cz.cvut.fel.ear.tm.model.Task;
import cz.cvut.fel.ear.tm.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class TaskService {

    private final TaskDao dao;

    private final CommentDao commentDao;

    @Autowired
    public TaskService(TaskDao dao, CommentDao commentDao) {
        this.dao = dao;
        this.commentDao = commentDao;
    }

    @Transactional
    public void persist(Task task){
        Objects.requireNonNull(task);
        dao.persist(task);
    }

    @Transactional(readOnly = true)
    public List<Task> findAll(){
        return dao.findAll();
    }

    @Transactional(readOnly = true)
    public Task find(Long id){
        return dao.find(id);
    }

    @Transactional
    public void remove(Task task){
        Objects.requireNonNull(task);
        dao.remove(task);
    }

    @Transactional
    public void update(Task task){
        Objects.requireNonNull(task);
        dao.update(task);
    }

    @Transactional
    public void addComment(Task task, Comment comment){
        // TODO resolve if task automaticly adds to comment as well
        task.addComment(comment);
        commentDao.persist(comment);
        dao.persist(task);
    }

    @Transactional
    public void addUser(Task task, User user){
        Objects.requireNonNull(task);
        Objects.requireNonNull(user);
        task.addUser(user);
        dao.persist(task);
    }
    
    @Transactional
    public void addSubtask(Task parentTask, Task subtask){
        parentTask.addSubtask(subtask);
        dao.persist(subtask);
        dao.update(parentTask);
    }

    @Transactional
    public List<Task> findByProject(Long projectId){
        Objects.requireNonNull(projectId);
        return dao.findByProject(projectId);
    }

    @Transactional
    public List<Task> findByUser(User user){
        Objects.requireNonNull(user);
        return dao.findByUser(user);
    }
}
