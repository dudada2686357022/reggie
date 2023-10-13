package top.dudada.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.dudada.reggie.common.CustomException;
import top.dudada.reggie.entity.Category;
import top.dudada.reggie.entity.Dish;
import top.dudada.reggie.entity.Setmeal;
import top.dudada.reggie.mapper.CategoryMapper;
import top.dudada.reggie.service.CategoryService;
import top.dudada.reggie.service.DishService;
import top.dudada.reggie.service.SetmealService;

@Service
@Slf4j
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

//    注入DishService
    @Autowired
    private DishService dishService;
//    注入SetmealService
    @Autowired
    private SetmealService setmealService;

    /**
     * 根据ID删除分类,删除之前需要进行判断
     * @param id
     */
    @Override
    public void remove(Long id) {
//               条件构造器
        LambdaQueryWrapper<Dish> dishlambdaQueryWrapper = new LambdaQueryWrapper<>();
//      添加查询条件，根据分类(Category)的ID,在菜品(Dish)中进行查询
        dishlambdaQueryWrapper.eq(Dish::getCategoryId,id);
        int count = dishService.count(dishlambdaQueryWrapper);
//        查询当前分类是否关联了菜品,如果存在关联则抛出一个业务异常
        if (count>0){
            throw new CustomException("分类存在菜品，无法删除该分类");

        }
//               条件构造器
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
//      添加查询条件，根据分类(Category)的ID,在套餐(Setmeal)中进行查询
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId,id);
        int count1 = setmealService.count(setmealLambdaQueryWrapper);
        //        查询当前分类是否关联了套餐,如果存在关联则抛出一个业务异常
        if (count1>0){
            throw new CustomException("分类存在套餐，无法删除该分类");
        }


//        正常删除分类
        super.removeById(id);


    }

}
