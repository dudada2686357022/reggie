package top.dudada.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;
import top.dudada.reggie.entity.Orders;

@Service
public interface OrdersService extends IService<Orders> {

    void submit(Orders orders);
}
