package homework.t02.dao;

import example.entity.User;

/**
 * @author jcwang
 */
public class UserDao {
    public User getUser(long id) {
        return new User(1, "Mark");
    }
}
