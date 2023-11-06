package top.dudada.reggie.dto;

import lombok.Data;
import top.dudada.reggie.entity.Dish;
import top.dudada.reggie.entity.DishFlavor;

import java.util.ArrayList;
import java.util.List;

@Data
public class DishDto extends Dish {

    private List<DishFlavor> flavors = new ArrayList<>();

    private String categoryName;

    private Integer copies;
}
