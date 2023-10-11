package top.dudada.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.dudada.reggie.entity.Category;
import top.dudada.reggie.mapper.CategoryMapper;
import top.dudada.reggie.service.CategoryService;
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
}
