package cz.cvut.fel.ear.tm.service;

import cz.cvut.fel.ear.tm.dao.ProjectDao;
import cz.cvut.fel.ear.tm.dto.comment.CommentDto;
import cz.cvut.fel.ear.tm.dto.project.ProjectDto;
import cz.cvut.fel.ear.tm.dto.project.ProjectReadDto;
import cz.cvut.fel.ear.tm.dto.task.TaskReadDto;
import cz.cvut.fel.ear.tm.dto.trackedtime.TrackedTimeDto;
import cz.cvut.fel.ear.tm.environment.Generator;
import cz.cvut.fel.ear.tm.exception.AlreadyExistsException;
import cz.cvut.fel.ear.tm.exception.EarException;
import cz.cvut.fel.ear.tm.exception.NotFoundException;
import cz.cvut.fel.ear.tm.model.Project;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static cz.cvut.fel.ear.tm.environment.Generator.*;
import static java.lang.Math.abs;
import static org.junit.jupiter.api.Assertions.*;


@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
class TrackedTimeServiceTest {
    private String rawToken;

    private Long userId;

    @PersistenceContext
    EntityManager em;

    @Autowired
    private TrackedTimeService sut;

    @Autowired
    private MyUserDetailsService userDetailsService;

    @BeforeEach
    void setUp() {
        User user = generateUser();
        em.persist(user);
        userId = user.getId();
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
        rawToken = generateRawAuthenticationToken(userDetails);
    }

    @Test
    public void addTrackedTimeWhileTrackedThrowsException(){
        TrackedTimeDto trackedTimeDto = generateTrackedTimeDto();
        trackedTimeDto.setUserId(userId);
        Task task = generateTask();
        em.persist(task);
        trackedTimeDto.setTaskId(task.getId());

        //adding tracked time
        sut.addTrackedTime(trackedTimeDto);

        //generating second trackedTime with the same user
        TrackedTimeDto trackedTimeDto2 = generateTrackedTimeDto();
        trackedTimeDto2.setUserId(userId);
        trackedTimeDto2.setTaskId(task.getId());

        assertThrows(AlreadyExistsException.class, () -> sut.addTrackedTime(trackedTimeDto2));
    }

    @Test
    public void addTrackedTimeAfterTrackedEndedDoesntThrowsException(){
        TrackedTimeDto trackedTimeDto = generateTrackedTimeDto();
        trackedTimeDto.setUserId(userId);
        Task task = generateTask();
        em.persist(task);
        trackedTimeDto.setTaskId(task.getId());

        //adding tracked time
        Long id = sut.addTrackedTime(trackedTimeDto);

        //setting end time to the first tracked time
        trackedTimeDto.setEndTime(LocalDateTime.now());
        sut.update(id, trackedTimeDto);

        //generating second trackedTime with the same user
        TrackedTimeDto trackedTimeDto2 = generateTrackedTimeDto();
        trackedTimeDto2.setUserId(userId);
        trackedTimeDto2.setTaskId(task.getId());

        assertDoesNotThrow(() -> sut.addTrackedTime(trackedTimeDto2));
    }

    @Test
    public void getTaskTrackedTimeBriefReturnsSumOfStoppedTrackedTimesForOneUser(){
        TrackedTimeDto trackedTimeDto = generateTrackedTimeDto();
        trackedTimeDto.setUserId(userId);
        Task task = generateTask();
        em.persist(task);
        trackedTimeDto.setTaskId(task.getId());

        //adding tracked time
        Long id = sut.addTrackedTime(trackedTimeDto);

        //returns 0 when there are no stopped tracked times
        assertEquals(0, sut.getTaskTrackedTimeBrief(task.getId()).size());

        //setting end time to the first tracked time
        trackedTimeDto.setEndTime(LocalDateTime.now());
        sut.update(id, trackedTimeDto);
        assertEquals(1, sut.getTaskTrackedTimeBrief(task.getId()).size());

        //generating second trackedTime with the same user
        TrackedTimeDto trackedTimeDto2 = generateTrackedTimeDto();
        trackedTimeDto2.setUserId(userId);
        trackedTimeDto2.setTaskId(task.getId());

        //adding second tracked time
        Long id2 = sut.addTrackedTime(trackedTimeDto2);

        //setting end time to the second tracked time
        trackedTimeDto2.setEndTime(LocalDateTime.now());
        sut.update(id2, trackedTimeDto2);

        //task still returns 1 tracked time
        assertEquals(1, sut.getTaskTrackedTimeBrief(task.getId()).size());
    }
}
