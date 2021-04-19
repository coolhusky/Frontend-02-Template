package homework.t02.service;

import homework.t02.dao.OrderDao;
import homework.t02.entity.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author jcwang
 */
@Component
public class OrderService {
    @Autowired
    private OrderDao orderDao;

    public Order getOrder(long id) {
        return orderDao.getOrderById(id);
    }
}
