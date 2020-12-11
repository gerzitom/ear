/*package cz.cvut.fel.ear.tm.service;

import cz.cvut.fel.ear.tm.dao.TeamDao;
import cz.cvut.fel.ear.tm.model.Team;
import cz.cvut.fel.ear.tm.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Transactional
@SpringBootTest
public class TeamServiceTest {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private TeamService sut;



    @Test
    public void addUserToTeamUpdatesTeamsUsers(){
        User user = new User();
        user.setUsername("hubalmar");

        Team team = new Team();
        em.persist(team);
        em.persist(user);

        sut.addUser(team, user);
        em.merge(team);
        em.merge(user);

        assertNotNull();


    }
}*/
