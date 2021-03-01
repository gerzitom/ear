package cz.cvut.fel.ear.tm.service;

import cz.cvut.fel.ear.tm.dao.ProjectDao;
import cz.cvut.fel.ear.tm.dao.ProjectUserDao;
import cz.cvut.fel.ear.tm.dao.SprintDao;
import cz.cvut.fel.ear.tm.dao.UserDao;
import cz.cvut.fel.ear.tm.dto.project.ProjectDto;
import cz.cvut.fel.ear.tm.dto.project.ProjectReadDto;
import cz.cvut.fel.ear.tm.dto.project.ProjectUserDto;
import cz.cvut.fel.ear.tm.dto.sprint.SprintDto;
import cz.cvut.fel.ear.tm.dto.sprint.SprintReadDto;
import cz.cvut.fel.ear.tm.exception.AlreadyExistsException;
import cz.cvut.fel.ear.tm.exception.AuthenticationException;
import cz.cvut.fel.ear.tm.exception.NotFoundException;
import cz.cvut.fel.ear.tm.exception.ValidationException;
import cz.cvut.fel.ear.tm.model.*;
import cz.cvut.fel.ear.tm.model.relations.ProjectUser;
import cz.cvut.fel.ear.tm.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ProjectService {
    private final ProjectDao dao;
    private final UserDao userDao;
    private final ProjectUserDao projectUserDao;
    private final SprintDao sprintDao;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    public ProjectService(ProjectDao dao, UserDao userDao, ProjectUserDao projectUserDao, SprintDao sprintDao) {
        this.dao = dao;
        this.userDao = userDao;
        this.projectUserDao = projectUserDao;
        this.sprintDao = sprintDao;
    }


    /**
     * Persist project via its DTO.
     * Checks name to not be empty and name of the project does not exists
     * @param project ProjectDto
     * @param username Username, who sets new project
     */
    @Transactional
    public Long persist(ProjectDto project, String username){
        Objects.requireNonNull(project);
        Objects.requireNonNull(project.getName());

        User user = userDao.findByUsername(username);
        if(user == null) throw new AuthenticationException("User not found");

        // name must not be empty
        if(project.getName().isEmpty()) throw new ValidationException("Name of the project can not be empty");

        // project must not exist
        Project foundProject = dao.findByName(project.getName());
        if(foundProject != null) throw new AlreadyExistsException("Project with this name already exists");

        Project newProject = project.buildNewProject();
        dao.persist(newProject);

        // Add user to project
        ProjectUser projectUser = new ProjectUser(user, newProject);
        projectUserDao.persist(projectUser);

        return newProject.getId();
    }

    @Transactional
    public ProjectReadDto find(Long projectId){
        Objects.requireNonNull(projectId);
        Project foundProject = dao.find(projectId);
        if(foundProject == null)
            throw new NotFoundException("Project not found");
        return new ProjectReadDto(foundProject);
    }

    @Transactional
    public Project getProject(Long projectId){
        Objects.requireNonNull(projectId);
        Project project = dao.find(projectId);
        if(project == null)
            throw new NotFoundException("Project not found");
        return project;
    }


    @Transactional
    public List<ProjectReadDto> getAll(SecurityUser user){

        if(user.getRole().equals(Role.PROJECT_MANAGER) || user.getRole().equals(Role.ADMIN)){
            return dao.findAll().stream().map(ProjectReadDto::new).collect(Collectors.toList());
        } else {
            List<ProjectUser> projectUsers = projectUserDao.findByUser(user.getId());
            return projectUsers.stream().map(ProjectUser::getProject).map(ProjectReadDto::new).collect(Collectors.toList());
        }
    }

    @Transactional
    public void update(Long projectId, ProjectDto projectDto){
        Objects.requireNonNull(projectDto);
        Project project = dao.find(projectId);
        if(project == null) throw new NotFoundException("Project does not exist");

        Project updatedProject = projectDto.updateProject(project);
        dao.update(updatedProject);
    }

    @Transactional
    public void deleteProject(Long projectId){
        Project project = dao.find(projectId);
        dao.remove(project);
    }

    @Transactional
    public ProjectReadDto findByName(String projectName){
        return new ProjectReadDto(dao.findByName(projectName));
    }

    @Transactional
    public void addTask(Project project, Task task){
        Objects.requireNonNull(project);
        Objects.requireNonNull(task);
        if(!project.taskExists(task.getName())){
            project.getTasks().add(task);
            dao.update(project);
        } else throw new AlreadyExistsException("This task already exists");
    }

    @Transactional
    public void addUser(ProjectUserDto dto) throws NotFoundException{
        Project project = dao.find(dto.getProject());
        if(project == null) throw new NotFoundException("Project with ID " + dto.getProject() + " not found");
        User user = userDao.find(dto.getUser());
        if(user == null) throw new NotFoundException("User with ID " + dto.getUser() + " not found");


        projectUserDao.addUser(project, user);
    }

    @Transactional
    public void removeUser(Long projectId, Long userId){
        ProjectUser projectUser = this.projectUserDao.find(projectId, userId);
        this.projectUserDao.remove(projectUser);
    }

}
