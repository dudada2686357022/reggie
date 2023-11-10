package top.dudada.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;
import top.dudada.reggie.dto.SetmealDto;
import top.dudada.reggie.entity.Setmeal;

import java.util.List;

@Service
public interface SetmealService extends IService<Setmeal> {

//    新增套餐
    void saveWithDish(SetmealDto setmealDto);

//    删除套餐
    void removeWithDish(List<Long> ids);


}
