package minispring.example.repository;

import minispring.example.entity.User;

/**
 * @author jcwang
 */
public interface UserDao {

    User query(Long id);
}
