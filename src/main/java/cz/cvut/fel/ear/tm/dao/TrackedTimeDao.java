package cz.cvut.fel.ear.tm.dao;

import cz.cvut.fel.ear.tm.model.Project;
import cz.cvut.fel.ear.tm.model.TrackedTime;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TrackedTimeDao extends BaseDao<TrackedTime> {
    public TrackedTimeDao() {
        super(TrackedTime.class);
    }

    public TrackedTime findByUserActiveUse(Long userId){
        try{
            TrackedTime foundTrackedTime = em.createNamedQuery("TrackedTime.findActiveTrackedTimeByUser", TrackedTime.class)
                    .setParameter("user", userId)
                    .getSingleResult();
            return foundTrackedTime;
        } catch (Exception e){
            return null;
        }
    }

    public List<TrackedTime> findByTask(Long taskId){
        return em.createNamedQuery("TrackedTime.getTaskTrackedTimes", TrackedTime.class)
                .setParameter("task", taskId)
                .getResultList();
    }
}
