package top.dudada.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;
import top.dudada.reggie.dto.SetmealDto;
import top.dudada.reggie.entity.Setmeal;

@Service
public interface SetmealService extends IService<Setmeal> {

    void saveWithDish(SetmealDto setmealDto);
}
