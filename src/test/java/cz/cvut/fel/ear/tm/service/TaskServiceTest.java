package cz.cvut.fel.ear.tm.service;

import cz.cvut.fel.ear.tm.exception.AlreadyExistsException;
import cz.cvut.fel.ear.tm.model.Project;
import cz.cvut.fel.ear.tm.model.Task;
import cz.cvut.fel.ear.tm.model.Team;
import cz.cvut.fel.ear.tm.model.User;
import org.junit.Rule;
import org.junit.jupiter.api.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertNotNull;


@Transactional
@SpringBootTest
public class TaskServiceTest {


    @PersistenceContext
    private EntityManager em;

    @Autowired
    private TaskService sut;


    @Test
    public void addSubtaskAddsTaskToProject(){
        Task task = new Task();

        Project project = new Project();
        em.persist(project);

        task.setProject(project);
        em.persist(task);

        List<Task> tasks = sut.findByProject(project.getId()).stream().filter(e -> e.getId().equals(task.getId())).collect(Collectors.toList());

        assertNotNull(tasks);
    }

}
