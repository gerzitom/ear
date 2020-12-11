package cz.cvut.fel.ear.tm.dao;

import cz.cvut.fel.ear.tm.model.Comment;
import cz.cvut.fel.ear.tm.model.Task;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CommentDao extends BaseDao<Comment> {
    public CommentDao() {
        super(Comment.class);
    }

    public List<Comment> findByTask(Long taskId){
        return em.createNamedQuery("Comment.findByTask", Comment.class)
                .setParameter("task", taskId)
                .getResultList();
    }

}
