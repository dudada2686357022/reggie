package top.dudada.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.dudada.reggie.entity.Setmeal;
import top.dudada.reggie.mapper.SetmealMapper;
import top.dudada.reggie.service.SetmealService;
@Service
@Slf4j
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {
}
