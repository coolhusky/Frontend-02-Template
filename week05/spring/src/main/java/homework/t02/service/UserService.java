package homework.t02.service;

import example.entity.User;
import homework.t02.beanconfig.MyBean;
import homework.t02.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author jcwang
 */
@MyBean
public class UserService {

    @Autowired
    private UserDao userDao;

    public User getUser(long id) {
        return userDao.getUser(id);
    }
}
