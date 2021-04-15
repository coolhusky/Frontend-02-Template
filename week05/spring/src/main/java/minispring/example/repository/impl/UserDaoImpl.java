package minispring.example.repository.impl;

import minispring.annotations.Component;
import minispring.example.entity.User;
import minispring.example.repository.UserDao;
import minispring.example.store.UserTable;
import minispring.utils.NumberHelper;

/**
 * @author jcwang
 */
@Component
public class UserDaoImpl implements UserDao {
    @Override
    public User query(Long id) {
        if (NumberHelper.toPrimitive(id) <= 0) {
            return null;
        }
        return UserTable.getUser(id);
    }
}
