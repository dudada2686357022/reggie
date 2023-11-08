package top.dudada.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;
import top.dudada.reggie.dto.DishDto;
import top.dudada.reggie.entity.Dish;

@Service
public interface DishService extends IService<Dish> {

//    新增菜品，同时插入菜品对应的口味数据，需要操作两张表 dish和dish_flavor
     void saveWithFlavor(DishDto dishDto);

     //根据ID获取菜品基本信息
     DishDto getByIdWithFlavor(Long id);

//     修改菜品
     void  updateWithFlavor(DishDto dishDto);


}
