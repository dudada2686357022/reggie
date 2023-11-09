package top.dudada.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.dudada.reggie.entity.SetmealDish;
import top.dudada.reggie.mapper.SetmealDishMapper;
import top.dudada.reggie.service.SetmealDishService;
@Service
@Slf4j
public class SetmealDishServiceImpl extends ServiceImpl<SetmealDishMapper, SetmealDish> implements SetmealDishService {
}
