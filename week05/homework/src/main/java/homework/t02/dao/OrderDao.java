package homework.t02.dao;

import homework.t02.entity.Order;
import homework.t02.store.FakeOrderTable;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author jcwang
 */
public class OrderDao {

    @Autowired
    private FakeOrderTable fakeOrderTable;

    public Order getOrderById(long id) {
        return fakeOrderTable.selectAllFromTOrderWhereOrderIdEquals(id);
    }
}
