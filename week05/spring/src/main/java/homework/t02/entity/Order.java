package homework.t02.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author jcwang
 */
@Data
@AllArgsConstructor
public class Order {
    private long id;

    @Override
    public String toString() {
        return "Order[" + "id=" + id + "]";
    }
}
