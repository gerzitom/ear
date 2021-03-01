package cz.cvut.fel.ear.tm.dao;

import cz.cvut.fel.ear.tm.model.State;
import cz.cvut.fel.ear.tm.model.Task;
import cz.cvut.fel.ear.tm.model.User;
import cz.cvut.fel.ear.tm.model.relations.TaskUser;
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

    public List<TaskUser> findByUser(Long userId) {
        return em.createNamedQuery("Task.findByUser", TaskUser.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    public List<Task> findDoneTasks(Long projectId){
        return em.createNamedQuery("Task.findByProjectAndState", Task.class)
                .setParameter("project", projectId)
                .setParameter("state", State.DONE)
                .getResultList();
    }
}
