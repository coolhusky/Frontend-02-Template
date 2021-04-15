package minispring.example.service.impl;

import minispring.annotations.Autowired;
import minispring.annotations.Component;
import minispring.example.entity.User;
import minispring.example.repository.UserDao;
import minispring.example.service.UserService;

/**
 * @author jcwang
 */
@Component
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;

    @Override
    public User getUser(Long id) {
        return userDao.query(id);
    }
}
