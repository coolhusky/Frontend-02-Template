package example.repository.impl;

import example.entity.User;
import example.repository.UserDao;
import example.store.UserTable;
import minispring.annotations.Component;
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
