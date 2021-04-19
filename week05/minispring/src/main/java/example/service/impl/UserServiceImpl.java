package example.service.impl;

import example.entity.User;
import example.repository.AuthDao;
import example.repository.UserDao;
import example.service.PeopleService;
import example.service.UserService;
import io.github.coolhusky.minispring.annotations.Autowired;
import io.github.coolhusky.minispring.annotations.Component;

import java.util.List;

/**
 * @author jcwang
 */
@Component
public class UserServiceImpl implements UserService, PeopleService {
    @Autowired
    private UserDao userDao;
    @Autowired
    private List<AuthDao> authDaos;

    @Override
    public User getUser(Long id) {
        System.out.println("======getUser is invoked...====");
        return userDao.query(id);
    }

    @Override
    public void addUser(User user) {
        System.out.println("add user...");
    }

    @Override
    public void eat() {
        System.out.println("===eat is invoked...=====");
    }
}
