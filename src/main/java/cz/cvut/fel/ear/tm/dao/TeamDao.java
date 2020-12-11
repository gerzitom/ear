package cz.cvut.fel.ear.tm.dao;

import cz.cvut.fel.ear.tm.model.Team;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TeamDao extends BaseDao<Team> {
    public TeamDao() {
        super(Team.class);
    }

    public List<Team> findByProject(Long projectId) {
        return em.createNamedQuery("Team.findUser", Team.class)
                .setParameter("project", projectId)
                .getResultList();
    }
}
