package cz.cvut.fel.ear.tm.dao;

import cz.cvut.fel.ear.tm.model.Comment;
import cz.cvut.fel.ear.tm.model.User;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserDao extends BaseDao<User> {
    public UserDao() {
        super(User.class);
    }

    public User findByUsername(String username) throws EmptyResultDataAccessException {
        return em.createNamedQuery("User.findByUsername", User.class)
                .setParameter("username", username)
                .getSingleResult();
    }
}
