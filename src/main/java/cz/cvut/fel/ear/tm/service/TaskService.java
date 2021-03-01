package cz.cvut.fel.ear.tm.service;

import cz.cvut.fel.ear.tm.dao.*;
import cz.cvut.fel.ear.tm.dto.comment.CommentDto;
import cz.cvut.fel.ear.tm.dto.task.TaskDto;
import cz.cvut.fel.ear.tm.dto.task.TaskReadDto;
import cz.cvut.fel.ear.tm.dto.task.TaskTrackedTimeByUserDto;
import cz.cvut.fel.ear.tm.dto.task.TaskUserDto;
import cz.cvut.fel.ear.tm.exception.AlreadyExistsException;
import cz.cvut.fel.ear.tm.exception.AuthenticationException;
import cz.cvut.fel.ear.tm.exception.EarException;
import cz.cvut.fel.ear.tm.exception.NotFoundException;
import cz.cvut.fel.ear.tm.model.*;
import cz.cvut.fel.ear.tm.model.relations.TaskUser;
import cz.cvut.fel.ear.tm.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class TaskService {

    private final TaskDao dao;
    private final CommentDao commentDao;
    private final ProjectDao projectDao;
    private final UserDao userDao;
    private final TrackedTimeDao trackedTimeDao;
    private final TaskUserDao taskUserDao;
    private final CommentService commentService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    public TaskService(TaskDao dao, CommentDao commentDao, ProjectDao projectDao, UserDao userDao, TrackedTimeDao trackedTimeDao, TaskUserDao taskUserDao, CommentService commentService) {
        this.dao = dao;
        this.commentDao = commentDao;
        this.projectDao = projectDao;
        this.userDao = userDao;
        this.taskUserDao = taskUserDao;
        this.commentService = commentService;
        this.trackedTimeDao = trackedTimeDao;
    }

    /**
     * Adding new task.
     * Project ID required
     * Checks if project for the task exists and if task with same name exists in project.
     * @param taskDto
     */
    @Transactional
    public Long persist(TaskDto taskDto){
        Objects.requireNonNull(taskDto);

        // required project ID
        if(taskDto.getProjectId() == null) throw new EarException("Project ID required");

        // project must exist
        Project foundProject = projectDao.find(taskDto.getProjectId());
        if(foundProject == null) throw new NotFoundException("Project for task not found");

        // Task name must be unique
        List<Task> tasks = dao.findByProject(taskDto.getProjectId());
        Task sameTask = tasks.stream().filter(t -> t.getName().equals(taskDto.getName())).findFirst().orElse(null);
        if(sameTask != null) throw new AlreadyExistsException("Task, that you want to add already exists");

        Task task = buildFromDto(taskDto);
        dao.persist(task);
        return task.getId();
    }

    @Transactional(readOnly = true)
    public List<TaskReadDto> findAll(SecurityUser user){
        List<Task> userTasks = dao.findByUser(user.getId()).stream().map(TaskUser::getTask).collect(Collectors.toList());
        return userTasks.stream().map(TaskReadDto::new).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TaskReadDto find(Long id){
        return new TaskReadDto(dao.find(id));
    }

    @Transactional
    public void remove(Task task){
        Objects.requireNonNull(task);
        dao.remove(task);
    }

    @Transactional
    public void update(Long taskId, TaskDto taskDto){
        Objects.requireNonNull(taskDto);
        Task originalTask = dao.find(taskId);
        Task task = taskDto.updateTask(originalTask);
        dao.update(task);
    }

    @Transactional
    public void removeTask(Long taskId){
        Task task = dao.find(taskId);
        task.getProject().getTasks().remove(task);
        projectDao.update(task.getProject());

        Task parentTask = task.getParentTask();
        if(parentTask != null){
            parentTask.getSubtasks().remove(task);
            dao.update(parentTask);
        }
        task.setParentTask(null);
        task.setSprint(null);
        task.setProject(null);
        this.dao.remove(task);
    }

    @Transactional
    public void addComment(Long taskId, CommentDto commentDto, SecurityUser securityUser) throws EarException{
        Objects.requireNonNull(taskId);
        Objects.requireNonNull(commentDto);
        Objects.requireNonNull(securityUser);

        User user = userDao.find(securityUser.getId());

        // if task contains users
        Task task = dao.find(taskId);
        List<User> users = task.getTaskUsers().stream().map(TaskUser::getUser).collect(Collectors.toList());
        if(!users.contains(user)) throw new EarException("User, that tries to add comment is not part of this task");

        Comment comment = commentService.buildFromDto(commentDto);
        comment.setTask(task);
        commentService.persist(comment);
    }

    @Transactional
    public void removeComment(Long commentId){
        Comment comment = commentDao.find(commentId);
        if(comment == null) throw new NotFoundException("Comment not found");
        commentDao.remove(comment);
    }

    @Transactional
    public void updateComment(Long commentId, CommentDto dto){
        Comment comment = commentDao.find(commentId);
        if(comment == null) throw new NotFoundException("Comment not found");
        comment = dto.update(comment);
        commentDao.update(comment);
    }

    @Transactional
    public void addUser(TaskUserDto dto){
        Objects.requireNonNull(dto);
        TaskUser taskUser = buildFromDto(dto);
        taskUserDao.persist(taskUser);
    }

    /**
     * @deprecated
     * @param taskId
     * @param userId
     */
    @Transactional
    public void removeUser(Long taskId, Long userId){
        TaskUser taskUser = taskUserDao.find(taskId, userId);
        taskUserDao.remove(taskUser);
    }

    @Transactional
    public void removeUser(Long taskId, Long userId, SecurityUser securityUser){
        TaskUser securityTaskUser = taskUserDao.find(taskId, securityUser.getId());
        if(securityTaskUser != null){
            TaskUser taskUser = taskUserDao.find(taskId, userId);
            taskUserDao.remove(taskUser);
        } else throw new NotFoundException("Currently logged user is not member of this task");
    }

    @Transactional
    public void addSubtask(Long parentTaskId, TaskDto subtaskDto){
        Objects.requireNonNull(parentTaskId);
        Objects.requireNonNull(subtaskDto);
        Task parentTask = dao.find(parentTaskId);
        subtaskDto.setProjectId(parentTask.getProject().getId());

        Task childTask = buildFromDto(subtaskDto);
        parentTask.addSubtask(childTask);
        childTask.setParentTask(parentTask);
        dao.persist(childTask);
    }
    
    @Transactional
    public void addSubtask(Task parentTask, Task subtask){
        parentTask.addSubtask(subtask);
        subtask.setParentTask(parentTask);
        dao.persist(subtask);
    }

    @Transactional
    public List<Task> findByProject(Long projectId){
        Objects.requireNonNull(projectId);
        return dao.findByProject(projectId);
    }

    @Transactional
    public List<Task> findByUser(User user){
        Objects.requireNonNull(user);
        return dao.findByUser(user.getId()).stream().map(TaskUser::getTask).collect(Collectors.toList());
    }


    @Transactional
    public List<Task> findDoneTasks(Long projectId){
        Objects.requireNonNull(projectId);
        return dao.findDoneTasks(projectId);
    }


    // rest to entity
    private Task buildFromDto(TaskDto dto){
        Task task = new Task();
        task.setName(dto.getName());
        task.setDescription(dto.getDescription());
        task.setDeadline(dto.getDeadline());
        task.setProject(projectDao.find(dto.getProjectId()));

        return task;
    }

    private TaskUser buildFromDto(TaskUserDto dto){
        Task task = dao.find(dto.getTaskId());
        if(task == null) throw new NotFoundException("Task was not found");
        User user = userDao.find(dto.getUserId());
        if(user == null) throw new NotFoundException("User was not found");


        TaskUser taskUser = new TaskUser();
        taskUser.setTask(task);
        taskUser.setUser(user);
        taskUser.setCreated(dto.getCreated());
        return taskUser;
    }

    private List<TaskTrackedTimeByUserDto> getTrackedTimes(){
        return null;
    }
}
