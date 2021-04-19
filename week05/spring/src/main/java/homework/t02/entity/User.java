package homework.t02.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

/**
 * @author jcwang
 */
@Data
@AllArgsConstructor
@ToString
public class User {
    private long id;
    private String name;
}
