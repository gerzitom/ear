package cz.cvut.fel.ear.tm.dao;

import cz.cvut.fel.ear.tm.model.Sprint;
import org.springframework.stereotype.Repository;

@Repository
public class SprintDao extends BaseDao<Sprint> {
    public SprintDao() {
        super(Sprint.class);
    }

    public Sprint getCurrentSprint(Long projectId){
        try{
            return em.createNamedQuery("Sprint.getCurrentSprint", Sprint.class)
                    .setParameter("projectId", projectId)
                    .getSingleResult();
        } catch (Exception e){
            return null;
        }
    }
}
