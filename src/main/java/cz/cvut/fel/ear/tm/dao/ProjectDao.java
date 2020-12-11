package cz.cvut.fel.ear.tm.dao;

import cz.cvut.fel.ear.tm.model.Project;
import cz.cvut.fel.ear.tm.model.State;
import cz.cvut.fel.ear.tm.model.Task;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProjectDao extends BaseDao<Project> {
    public ProjectDao() {
        super(Project.class);
    }

    public Project findByName(String projectName){
        return em.createNamedQuery("Project.findByName", Project.class)
                .setParameter("project", projectName)
                .getSingleResult();
    }
}
