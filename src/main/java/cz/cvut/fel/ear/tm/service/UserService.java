package cz.cvut.fel.ear.tm.service;

import cz.cvut.fel.ear.tm.dao.UserDao;
import cz.cvut.fel.ear.tm.exception.AlreadyExistsException;
import cz.cvut.fel.ear.tm.exception.EarException;
import cz.cvut.fel.ear.tm.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class UserService {
    private final UserDao dao;

    @Autowired
    public UserService(UserDao dao) {
        this.dao = dao;
    }

    @Transactional
    public void persist(User user) throws EarException {
        if(dao.findAll().contains(user)){
            throw new AlreadyExistsException("User with username: " + user.getUsername() + " already exists");
        } else {
            dao.persist(user);
        }
    }

    @Transactional
    public User find(Long id){
        return dao.find(id);
    }

    @Transactional
    public List<User> findAll(){
        return dao.findAll();
    }

    @Transactional
    public void update(User user){
        dao.update(user);
    }
}
