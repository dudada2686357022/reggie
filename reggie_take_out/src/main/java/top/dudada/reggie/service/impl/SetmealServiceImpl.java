package top.dudada.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.dudada.reggie.common.CustomException;
import top.dudada.reggie.dto.SetmealDto;
import top.dudada.reggie.entity.Setmeal;
import top.dudada.reggie.entity.SetmealDish;
import top.dudada.reggie.mapper.SetmealMapper;
import top.dudada.reggie.service.SetmealDishService;
import top.dudada.reggie.service.SetmealService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {


    @Autowired
    private SetmealDishService setmealDishService;

    /**
     * 新增套餐
     * @param setmealDto
     */
    @Override
    @Transactional
    public void saveWithDish(SetmealDto setmealDto) {
        this.save(setmealDto);

        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes.stream().map((item) ->{
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());

//        保存套餐和菜品的关联信息，操作setmeal_dish，执行insert操作
        setmealDishService.saveBatch(setmealDishes);


    }

    /**
     * 删除套餐
     * @param ids
     */
    @Transactional
    @Override
    public void removeWithDish(List<Long> ids) {
//        查询套餐状态，确认是否可删除
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Setmeal::getId,ids);
        queryWrapper.eq(Setmeal::getStatus,1);

        int count = this.count(queryWrapper);
        if (count>0){
//            无法删除，抛出异常
            throw new CustomException("套餐状态为售卖，无法删除");
        }

//        如果可以删除，先删除套餐表中的数据 --setmeal
        this.removeByIds(ids);

        LambdaQueryWrapper<SetmealDish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(SetmealDish::getSetmealId,ids);

//        删除关系表中的数据-----setmeal_dish
        setmealDishService.remove(lambdaQueryWrapper);
    }
























}
