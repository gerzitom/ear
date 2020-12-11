package cz.cvut.fel.ear.tm.service;

import cz.cvut.fel.ear.tm.dao.ProjectDao;
import cz.cvut.fel.ear.tm.exception.AlreadyExistsException;
import cz.cvut.fel.ear.tm.exception.NotFoundException;
import cz.cvut.fel.ear.tm.model.Project;
import cz.cvut.fel.ear.tm.model.Task;
import cz.cvut.fel.ear.tm.model.Team;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;

@Service
public class ProjectService {
    private final ProjectDao dao;

    @Autowired
    public ProjectService(ProjectDao dao) {
        this.dao = dao;
    }

    @Transactional
    public void persist(Project project){
        Objects.requireNonNull(project);
        dao.persist(project);
    }

    @Transactional
    public Project findByName(String projectName){
        return dao.findByName(projectName);
    }

    @Transactional
    public void addTask(Project project, Task task){
        Objects.requireNonNull(project);
        Objects.requireNonNull(task);
        if(!project.taskExists(task.getName())){
            project.getTasks().add(task);
        } else throw new AlreadyExistsException("This task already exists");
    }
}
