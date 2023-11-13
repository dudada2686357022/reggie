package top.dudada.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.dudada.reggie.entity.OrderDetail;
import top.dudada.reggie.mapper.OrderDetailMapper;
import top.dudada.reggie.service.OrderDetailService;

@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {
}
