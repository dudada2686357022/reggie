package top.dudada.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.dudada.reggie.dto.DishDto;
import top.dudada.reggie.entity.Dish;
import top.dudada.reggie.entity.DishFlavor;
import top.dudada.reggie.mapper.DishMapper;
import top.dudada.reggie.service.DishFlavorService;
import top.dudada.reggie.service.DishService;
import top.dudada.reggie.service.SetmealService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Autowired
    private DishFlavorService dishFlavorService;


    /**
     *新增菜品，同时保存对应的口味数据
     * @param dishDto
     */
    @Transactional
    @Override
    public void saveWithFlavor(DishDto dishDto) {
//        保存菜品的基本信息到菜品表dish
        this.save(dishDto);

        Long dishId = dishDto.getId(); //菜品ID

//        菜品口味
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors.stream().map((item)->{
            item.setDishId(dishId);
            return item;
        }).collect(Collectors.toList());

//        保存菜品口味数据到菜品口味表dish_flavor
        dishFlavorService.saveBatch(flavors);


    }
}
