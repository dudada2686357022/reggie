package top.dudada.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.dudada.reggie.common.R;
import top.dudada.reggie.dto.DishDto;
import top.dudada.reggie.entity.Category;
import top.dudada.reggie.entity.Dish;
import top.dudada.reggie.service.CategoryService;
import top.dudada.reggie.service.DishFlavorService;
import top.dudada.reggie.service.DishService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/dish")
public class DishController {

    @Autowired
    private DishService dishService;

    @Autowired
    private DishFlavorService dishFlavorService;

    @Autowired
    private CategoryService categoryService;


    /**
     * 新增菜品
     * @param dishDto
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto){
        dishService.saveWithFlavor(dishDto);
        return R.success("新增菜品成功");
    }


    /**
     * 菜品分类查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize,String name){

//        分页构造器
        Page<Dish> pageInfo = new Page<>(page, pageSize);
        Page<DishDto> dishDtoPage = new Page<>();

//        条件构造器
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
//        添加过滤条件(模糊查询)
        queryWrapper.like(name!=null,Dish::getName,name);
//       添加排序条件
        queryWrapper.orderByDesc(Dish::getUpdateTime);

        dishService.page(pageInfo,queryWrapper);

//        对象拷贝
        BeanUtils.copyProperties(pageInfo,dishDtoPage,"records");

        List<Dish> records = pageInfo.getRecords();
        List<DishDto> list= records.stream().map((item) ->{
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item,dishDto);
            Long categoryId = item.getCategoryId(); //分类ID
//            根据ID查询分类对象
            Category category = categoryService.getById(categoryId);
           if (category !=null){
               String categoryName = category.getName();
               dishDto.setCategoryName(categoryName);
           }
            return dishDto;

        }).collect(Collectors.toList());

        dishDtoPage.setRecords(list);



        return R.success(dishDtoPage);
    }


































}
