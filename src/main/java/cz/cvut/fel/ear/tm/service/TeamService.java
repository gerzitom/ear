package cz.cvut.fel.ear.tm.service;

import cz.cvut.fel.ear.tm.dao.TeamDao;
import cz.cvut.fel.ear.tm.dao.UserDao;
import cz.cvut.fel.ear.tm.exception.AlreadyExistsException;
import cz.cvut.fel.ear.tm.model.Team;
import cz.cvut.fel.ear.tm.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class TeamService {

    private final TeamDao teamsDao;

    @Autowired
    public TeamService(TeamDao team) {
        teamsDao = team;
    }

    @Transactional
    public void persist(Team team){
        if(teamsDao.findAll().contains(team)){
            throw new AlreadyExistsException("User with username: " + team.getName() + " already exists");
        } else {
            teamsDao.persist(team);
        }
    }

    @Transactional
    public void addUser(Team team, User user){
        Objects.requireNonNull(user);
        final Optional<User> existing = team.getUsers().stream().filter(it -> it.getId()
                .equals(user.getId())).findAny();
        if (existing.isPresent()) {
            throw new AlreadyExistsException("User with username: " + team.getName() + " is aldready in the team");
        } else {
            team.getUsers().add(user);
        }
    }

    @Transactional
    public List<User> findTeamUsers(Long teamId){
        return teamsDao.find(teamId).getUsers();
    }

    @Transactional
    public void removeUser(Team team, User user){
        Objects.requireNonNull(team);
        Objects.requireNonNull(user);
        team.getUsers().remove(user);
        teamsDao.update(team);
    }
}
