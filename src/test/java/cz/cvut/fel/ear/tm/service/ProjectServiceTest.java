package cz.cvut.fel.ear.tm.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.junit.jupiter.api.Assertions.*;

class ProjectServiceTest {

    @PersistenceContext
    EntityManager em;

    @Autowired
    private ProjectService projectService;

    @BeforeEach
    void setUp() {
    }

    @Test
    public void addUserToProject_addsUserToProject(){

    }
}