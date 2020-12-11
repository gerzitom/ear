package cz.cvut.fel.ear.tm.dao;

import cz.cvut.fel.ear.tm.model.Task;
import cz.cvut.fel.ear.tm.model.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TaskDao extends BaseDao<Task> {
    public TaskDao() {
        super(Task.class);
    }

    public List<Task> findByProject(Long projectId) {
        return em.createNamedQuery("Task.findByProject", Task.class)
                .setParameter("project", projectId)
                .getResultList();
    }

    public List<Task> findByUser(User user) {
        return em.createNamedQuery("Task.findByUser", Task.class)
                .setParameter("user", user)
                .getResultList();
    }
}
