package cz.cvut.fel.ear.tm.dao;

import cz.cvut.fel.ear.tm.model.Sprint;
import org.springframework.stereotype.Repository;

@Repository
public class SprintDao extends BaseDao<Sprint> {
    public SprintDao() {
        super(Sprint.class);
    }
}
