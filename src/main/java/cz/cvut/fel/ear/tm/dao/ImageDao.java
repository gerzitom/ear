package cz.cvut.fel.ear.tm.dao;

import cz.cvut.fel.ear.tm.model.Image;
import org.springframework.stereotype.Repository;

@Repository
public class ImageDao extends BaseDao<Image> {
    public ImageDao() {
        super(Image.class);
    }
}
