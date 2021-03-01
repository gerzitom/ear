package cz.cvut.fel.ear.tm.dao;

import cz.cvut.fel.ear.tm.model.Project;
import cz.cvut.fel.ear.tm.model.State;
import cz.cvut.fel.ear.tm.model.Task;
import cz.cvut.fel.ear.tm.model.User;
import cz.cvut.fel.ear.tm.model.relations.ProjectUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class ProjectDao extends BaseDao<Project> {

    public ProjectDao() {
        super(Project.class);
    }

    public Project findByName(String projectName){
        try{
            Project foundProject = em.createNamedQuery("Project.findByName", Project.class)
                    .setParameter("project", projectName)
                    .getSingleResult();
            return foundProject;
        } catch (Exception e){
            return null;
        }
    }
}
