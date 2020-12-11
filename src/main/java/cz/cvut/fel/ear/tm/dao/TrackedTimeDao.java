package cz.cvut.fel.ear.tm.dao;

import cz.cvut.fel.ear.tm.model.TrackedTime;
import org.springframework.stereotype.Repository;

@Repository
public class TrackedTimeDao extends BaseDao<TrackedTime> {
    public TrackedTimeDao() {
        super(TrackedTime.class);
    }
}
