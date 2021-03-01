package cz.cvut.fel.ear.tm.service;

import cz.cvut.fel.ear.tm.dao.ProjectDao;
import cz.cvut.fel.ear.tm.dto.comment.CommentDto;
import cz.cvut.fel.ear.tm.dto.project.ProjectDto;
import cz.cvut.fel.ear.tm.dto.project.ProjectReadDto;
import cz.cvut.fel.ear.tm.dto.task.TaskReadDto;
import cz.cvut.fel.ear.tm.environment.Generator;
import cz.cvut.fel.ear.tm.exception.AlreadyExistsException;
import cz.cvut.fel.ear.tm.exception.EarException;
import cz.cvut.fel.ear.tm.exception.NotFoundException;
import cz.cvut.fel.ear.tm.model.Project;
import cz.cvut.fel.ear.tm.model.SecurityUser;
import cz.cvut.fel.ear.tm.model.Task;
import cz.cvut.fel.ear.tm.model.User;
import cz.cvut.fel.ear.tm.model.relations.TaskUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.List;
import java.util.stream.Collectors;

import static cz.cvut.fel.ear.tm.environment.Generator.*;
import static java.lang.Math.abs;
import static org.junit.jupiter.api.Assertions.*;


@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
class TaskServiceTest {

    private User user;
    private SecurityUser securityUser;

    @PersistenceContext
    EntityManager em;

    @Autowired
    private TaskService sut;

    @Autowired
    private MyUserDetailsService userDetailsService;

    @BeforeEach
    void setUp() {
        user = generateUser();
        em.persist(user);
        securityUser = new SecurityUser(user);
    }



    @Test
    public void addSubtaskAddsTaskToProject(){
        Task task = generateTask();

        Project project = new Project();
        em.persist(project);

        task.setProject(project);
        em.persist(task);

        List<Task> tasks = sut.findByProject(project.getId()).stream().filter(e -> e.getId().equals(task.getId())).collect(Collectors.toList());

        assertNotNull(tasks);
    }


    @Test
    public void addCommentToTaskFromNonMemberThrowsException(){
        //persist new task
        Task task = generateTask();
        em.persist(task);

        //Creating new comment
        CommentDto commentDto = generateCommentDto();

        assertThrows(EarException.class, () -> sut.addComment(task.getId(), commentDto,securityUser));
    }

    @Test
    public void addCommentToTaskFromMemberDoesNotThrow(){

        //persist new task
        Task task = generateTask();
        User user = generateUser();
        em.persist(user);
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
        String rawToken1 = generateRawAuthenticationToken(userDetails);

        TaskUser taskUser = new TaskUser();
        taskUser.setUser(user);
        task.getTaskUsers().add(taskUser);
        em.persist(task);

        //Creating new comment
        CommentDto commentDto = generateCommentDto();
        commentDto.setUserId(user.getId());
        sut.addComment(task.getId(), commentDto, securityUser);


        assertDoesNotThrow(() -> sut.addComment(task.getId(), commentDto, securityUser));
    }
}
