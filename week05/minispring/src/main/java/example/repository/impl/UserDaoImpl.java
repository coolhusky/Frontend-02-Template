package example.repository.impl;

import example.entity.User;
import example.repository.UserDao;
import example.store.UserTable;
import io.github.coolhusky.minispring.annotations.Component;
import io.github.coolhusky.minispring.utils.NumberHelper;

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
