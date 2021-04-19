package example.service;

import example.entity.User;

/**
 * @author jcwang
 */
public interface UserService {

    User getUser(Long id);

    void addUser(User user);
}
