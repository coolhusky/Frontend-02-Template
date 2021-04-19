package example.repository;

import example.entity.User;

/**
 * @author jcwang
 */
public interface UserDao {

    User query(Long id);
}
