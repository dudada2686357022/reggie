package top.dudada.reggie.dto;


import lombok.Data;
import top.dudada.reggie.entity.Setmeal;
import top.dudada.reggie.entity.SetmealDish;

import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
