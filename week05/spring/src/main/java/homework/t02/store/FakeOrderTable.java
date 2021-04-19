package homework.t02.store;

import homework.t02.entity.Order;
import lombok.Data;
import lombok.Getter;

/**
 * @author jcwang
 */
@Data
public class FakeOrderTable {
    private String tableName;

    public Order selectAllFromTOrderWhereOrderIdEquals(long id) {
        return new Order(id);
    }
}
