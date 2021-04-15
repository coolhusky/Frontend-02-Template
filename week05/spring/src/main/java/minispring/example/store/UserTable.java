package minispring.example.store;

import com.google.common.collect.ImmutableList;
import minispring.example.entity.User;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author jcwang
 */
public class UserTable {
    private static final Map<Long, User> USER_MAP = new ConcurrentHashMap<>(16);
    private static final ImmutableList<String> USER_NAME_LIST = ImmutableList.<String>builder()
            .add("John", "Mark", "Mike", "Peter")
            .build();
    private static final Random RD = new Random();

    static {
        int nameNum = USER_NAME_LIST.size();
        for (int id = 1; id <= 100; id++) {
            User user = new User(id, USER_NAME_LIST.get(RD.nextInt(nameNum)));
            USER_MAP.put((long)id, user);
        }
    }

    public static User getUser(long id) {
        return USER_MAP.get(id);
    }

}
