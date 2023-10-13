package top.dudada.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;
import top.dudada.reggie.entity.Category;

@Service
public interface CategoryService extends IService<Category>  {

    void remove(Long id);


}
