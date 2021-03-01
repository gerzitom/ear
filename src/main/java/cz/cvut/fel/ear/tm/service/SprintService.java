package cz.cvut.fel.ear.tm.service;

import cz.cvut.fel.ear.tm.dao.SprintDao;
import cz.cvut.fel.ear.tm.dao.TaskDao;
import cz.cvut.fel.ear.tm.dto.project.ProjectReadDto;
import cz.cvut.fel.ear.tm.dto.sprint.SprintDto;
import cz.cvut.fel.ear.tm.dto.sprint.SprintReadDto;
import cz.cvut.fel.ear.tm.exception.AlreadyExistsException;
import cz.cvut.fel.ear.tm.exception.NotFoundException;
import cz.cvut.fel.ear.tm.model.Sprint;
import cz.cvut.fel.ear.tm.model.Task;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class SprintService {

    private SprintDao dao;
    private ProjectService projectService;
    private TaskDao taskDao;

    public SprintService(SprintDao dao, ProjectService projectService, TaskDao taskDao) {
        this.dao = dao;
        this.projectService = projectService;
        this.taskDao = taskDao;
    }

    @Transactional
    public Long persist(SprintDto dto){
        Sprint currentSprint = getCurrentSprint(dto.getProjectId());
        if(currentSprint != null) throw new AlreadyExistsException("Project is currently have opened sprint");
        Sprint sprint = buildFromDto(dto);
        dao.persist(sprint);
        return sprint.getId();
    }

    @Transactional
    public SprintReadDto find(Long sprintId){
        Sprint sprint = dao.find(sprintId);
        if(sprint == null) throw new NotFoundException("Sprint was not found");
        return new SprintReadDto(sprint);
    }

    @Transactional
    public void update(Long sprintId, SprintDto dto){
        Sprint sprint = dao.find(sprintId);
        sprint = dto.update(sprint);
        dao.update(sprint);
    }

    @Transactional
    public void delete(Long sprintId){
        Sprint sprint = dao.find(sprintId);
        dao.remove(sprint);
    }

    @Transactional
    public SprintReadDto findCurrentSprint(Long projectId){
        return new SprintReadDto(getCurrentSprint(projectId));
    }

    @Transactional
    public void addTask(Long sprintId, Long taskId){
        Task task = taskDao.find(taskId);
        Sprint sprint = dao.find(sprintId);
        task.setSprint(sprint);
        taskDao.update(task);
    }

    private Sprint getCurrentSprint(Long projectId){
        ProjectReadDto project = projectService.find(projectId);
        if(project == null) throw new NotFoundException("Project not found");
        Sprint sprint = dao.getCurrentSprint(projectId);
        return sprint;
    }

    private Sprint buildFromDto(SprintDto dto){
        Sprint sprint = new Sprint();
        sprint.setName(dto.getName());
        sprint.setDeadline(dto.getDeadline());
        sprint.setDescription(dto.getDescription());
        sprint.setProject(projectService.getProject(dto.getProjectId()));
        sprint.setClosed(false);
        return sprint;
    }
}
