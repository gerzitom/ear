package cz.cvut.fel.ear.tm.service;

import cz.cvut.fel.ear.tm.dao.ProjectDao;
import cz.cvut.fel.ear.tm.dto.project.ProjectDto;
import cz.cvut.fel.ear.tm.dto.project.ProjectReadDto;
import cz.cvut.fel.ear.tm.environment.Generator;
import cz.cvut.fel.ear.tm.exception.AlreadyExistsException;
import cz.cvut.fel.ear.tm.exception.AuthenticationException;
import cz.cvut.fel.ear.tm.exception.NotFoundException;
import cz.cvut.fel.ear.tm.model.Project;
import cz.cvut.fel.ear.tm.model.SecurityUser;
import cz.cvut.fel.ear.tm.model.User;
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

import static cz.cvut.fel.ear.tm.environment.Generator.*;
import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
class ProjectServiceTest {

    private String rawToken;
    private SecurityUser securityUser;

    @PersistenceContext
    EntityManager em;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private MyUserDetailsService userDetailsService;

    @BeforeEach
    void setUp() {
        User user = generateUser();
        em.persist(user);
        securityUser = new SecurityUser(user);
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
        rawToken = generateRawAuthenticationToken(userDetails);
    }

    // Basic CRUD Operations

    @Test
    public void persistProject__projectIsAddedToDatabase(){
        // persist new project
        ProjectDto project = generateProjectDto();
        Long newProjectId = projectService.persist(project, rawToken);
        Project newProject = project.buildNewProject();
        newProject.setId(newProjectId);

        // find project
        Project foundProject = em.find(Project.class, newProjectId);
        assertEquals(newProject, foundProject);
    }

    @Test
    public void findProject__projectCanBeFound(){
        Project project = generateProject();
        em.persist(project);

        // find project
        ProjectReadDto foundProject = projectService.find(project.getId());

        assertEquals(new ProjectReadDto(project), foundProject);
    }

    @Test
    public void findProjectByUsername__findsProject(){
        ProjectDto project = generateProjectDto();
        projectService.persist(project, rawToken);

        ProjectReadDto foundProject = projectService.findByName(project.getName());
        assertEquals(foundProject, foundProject);
    }

    @Test
    public void updateProject__updatesProject(){
        // persist new project
        String updatedName = "Persisted project updated name";
        ProjectDto projectDto = generateProjectDto();
        Long persistedProject = projectService.persist(projectDto, rawToken);

        // update project
        ProjectDto projectDtoUpdated = new ProjectDto();
        projectDtoUpdated.setName(updatedName);
        projectService.update(persistedProject, projectDtoUpdated);

        // assert
        ProjectReadDto updatedProject = projectService.find(persistedProject);
        assertEquals(updatedName, updatedProject.getName());
    }

    @Test
    public void addProjectWithSameName__throwsAlreadyExistsException(){
        String projectName = "Project name";
        ProjectDto projectDto = generateProjectDto();
        projectDto.setName(projectName);
        projectService.persist(projectDto, rawToken);

        assertThrows(AlreadyExistsException.class, () -> {
            ProjectDto nextProjectDto = generateProjectDto();
            nextProjectDto.setName(projectName);
            projectService.persist(nextProjectDto, rawToken);
        });
    }

    @Test
    public void getProject_throwsNotFoundException(){
        assertThrows(NotFoundException.class, () -> {
            Project project = generateProject();
            em.persist(project);

            projectService.find((long)2);
        });
    }

    @Test
    public void getAll_throwsIsRemovedException(){
        //generating User
        User user = generateUser();
        em.persist(user);
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
        String rawToken1 = generateRawAuthenticationToken(userDetails);

        //User is removed
        user.setRemoved(true);
        em.merge(user);

        assertThrows(AuthenticationException.class, () -> {
            Project project = generateProject();
            em.persist(project);

            projectService.getAll(securityUser);
        });
    }

}