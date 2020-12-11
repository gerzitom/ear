package cz.cvut.fel.ear.tm.dao;

import cz.cvut.fel.ear.tm.model.User;
import org.springframework.stereotype.Repository;

@Repository
public class UserDao extends BaseDao<User> {
    public UserDao() {
        super(User.class);
    }
}
