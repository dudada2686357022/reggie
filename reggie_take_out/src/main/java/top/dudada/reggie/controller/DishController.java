package top.dudada.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import top.dudada.reggie.common.R;
import top.dudada.reggie.dto.DishDto;
import top.dudada.reggie.entity.Category;
import top.dudada.reggie.entity.Dish;
import top.dudada.reggie.entity.DishFlavor;
import top.dudada.reggie.service.CategoryService;
import top.dudada.reggie.service.DishFlavorService;
import top.dudada.reggie.service.DishService;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
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

    @Autowired
    private RedisTemplate redisTemplate;


    /**
     * 新增菜品
     * @param dishDto
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto){
        dishService.saveWithFlavor(dishDto);

//     修改菜品数据时清除所有菜品的缓存数据
        Set keys = redisTemplate.keys("dish_*");
        redisTemplate.delete(keys);


//        清理某个分类下面的菜品缓存数据
//        String key = "dish_" + dishDto.getCategoryId() + "_1";
//        redisTemplate.delete(keys);



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


    /**
     * 根据ID获取菜品基本信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<DishDto> get(@PathVariable  Long id){
        DishDto dishDto = dishService.getByIdWithFlavor(id);

        return R.success(dishDto);

    }


    /**
     * 修改菜品信息
     * @param dishDto
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody  DishDto dishDto){
      dishService.updateWithFlavor(dishDto);

//     修改菜品数据时清除所有菜品的缓存数据
        Set keys = redisTemplate.keys("dish_*");
        redisTemplate.delete(keys);


//        清理某个分类下面的菜品缓存数据
//        String key = "dish_" + dishDto.getCategoryId() + "_1";
//        redisTemplate.delete(keys);
        return R.success("修改菜品成功");
    }


//    /**
//     * 根据条件查询菜品数据
//     * @param dish
//     * @return
//     */
//    @GetMapping("/list")
//    public R<List<Dish>> list(Dish dish){
//
////        构造查询条件
//        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.eq(dish.getCategoryId()!=null,Dish::getCategoryId,dish.getCategoryId());
////        起售状态为1的菜品
//        queryWrapper.eq(Dish::getStatus,1);
////        添加排序条件
//        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
//
//        List<Dish> list = dishService.list(queryWrapper);
//
//        return R.success(list);
//
//
//    }



    /**
     * 根据条件查询菜品数据
     * @param dish
     * @return
     */
    @GetMapping("/list")
    public R<List<DishDto>> list(Dish dish){

        List<DishDto> dishDtoList=null;
//        动态构造Key
        String key ="dish_"+dish.getCategoryId()+"_"+dish.getStatus();//dish_13964464684684_1
//        先从redis中获取缓存数据
        dishDtoList =(List<DishDto>) redisTemplate.opsForValue().get(key);
//        判断是否存在缓存数据
        if (dishDtoList!=null){
//            以缓存数据
            return R.success(dishDtoList);
        }


//        构造查询条件
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(dish.getCategoryId()!=null,Dish::getCategoryId,dish.getCategoryId());
//        起售状态为1的菜品
        queryWrapper.eq(Dish::getStatus,1);
//        添加排序条件
        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);

        List<Dish> list = dishService.list(queryWrapper);

         dishDtoList = list.stream().map((item) -> {
            DishDto dishDto = new DishDto();

            BeanUtils.copyProperties(item, dishDto);

            Long categoryId = item.getCategoryId();//分类ID
            //根据ID查找分类对象
            Category category = categoryService.getById(categoryId);
            if (category != null) {
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }

//            当前菜品的ID
            Long dishId = item.getId();
            LambdaQueryWrapper<DishFlavor> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(DishFlavor::getDishId, dishId);

            List<DishFlavor> dishFlavorList = dishFlavorService.list(lambdaQueryWrapper);
            dishDto.setFlavors(dishFlavorList);

            return dishDto;
        }).collect(Collectors.toList());


//         如果数据不存在，需要查询数据库，将查询到的菜品数据缓存到Redis中,时间：60min
        redisTemplate.opsForValue().set(key,dishDtoList,60, TimeUnit.MINUTES);


        return R.success(dishDtoList);



    }



































}
