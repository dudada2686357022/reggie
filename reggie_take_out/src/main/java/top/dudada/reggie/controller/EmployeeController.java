package top.dudada.reggie.controller;



import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Base64Utils;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;
import top.dudada.reggie.common.R;
import top.dudada.reggie.entity.Employee;
import top.dudada.reggie.service.EmployeeService;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

/**
 * 员工后台登入、退出
 */
@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;


    /**
     * 员工登入
     * @param request
     * @param employee
     * @return
     */
    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee){

//        1.将页面提交的密码password进行md5加密处理
        String password = employee.getPassword();
//        DigestUtils：是 Apache Commons Codec 库中的类
//        DigestUtils 类用于安全存储密码、数字签名和验证等领域。
//        这里将给定的字符串 password 转换为 MD5 哈希值
//        password.getBytes()这里将字符串password转换为字节数组
        password= DigestUtils.md5DigestAsHex(password.getBytes());


//        2.根据页面提交的用户名username查询数据库
//        LambdaQueryWrapper 是 MyBatis-Plus 库提供的一个类
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername,employee.getUsername());
        Employee emp = employeeService.getOne(queryWrapper);


//        3.如果没有查询到则返回登入失败结果
        if (emp == null){
            return R.error("登入失败");
        }

//        4.密码比对。如果不一致则返回登入失败的结果
        if (!emp.getPassword().equals(password)){
            return R.error("登入失败");
        }


//        5.查看员工状态，如果为已禁用状态，则返回员工已禁用的结果

        if (emp.getStatus()==0){
            return R.error("账号已禁用");
        }


//        6.登入成功，将员工的ID存入Session并返回登入成功的结果
        request.getSession().setAttribute("employee",emp.getId());

        return R.success(emp);

    }


    /**
     * 员工退出
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request){
        request.getSession().removeAttribute("employee");
        return R.success("退出成功");
    }


    /**
     * 新增员工
     * @param request
     * @param employee
     * @return
     */
    @PostMapping
    public  R<String> save(HttpServletRequest request,@RequestBody Employee employee){
        log.info("新增员工,员工信息:"+employee.toString());

//        设置初始密码123456,需要进行md5加密处理
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));

//        LocalDateTime 是 Java 编程语言中的一个类，
//        具体在 java.time 包中，它在 Java 8 中引入，
//        作为 Java 日期和时间 API（java.time）的一部分。
//        它表示一个没有时区信息的日期和时间。
//        employee.setCreateTime(LocalDateTime.now());
//        employee.setUpdateTime(LocalDateTime.now());


//        获得当前登入用户的ID
//        Long empId =(Long) request.getSession().getAttribute("employee");

//        employee.setCreateUser(empId);
//        employee.setUpdateUser(empId);

        employeeService.save(employee);

        return R.success("新增员工成功");
    }


    /**
     * 员工分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name){
        log.info("page = {},pageSize = {}.name= {}",page,pageSize,name);

//        分页构造器
        Page pageInfo = new Page(page, pageSize);
//        构造条件构造器
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper();

//        添加过条件(name参数不为空时添加)
        queryWrapper.like(StringUtils.isNotEmpty(name),Employee::getName,name);

//        添加排序条件
        queryWrapper.orderByDesc(Employee::getUpdateTime);

//        执行查询
        employeeService.page(pageInfo,queryWrapper);

        return  R.success(pageInfo);



    }


    /**
     * 员工状态修改,员工信息修改
     * @param request
     *
     * @param employee
     * @return
     */
    @PutMapping
    public R<String> update(HttpServletRequest request,@RequestBody Employee employee){
        log.info(employee.toString());

//        Long empId = (Long) request.getSession().getAttribute("employee");
//        employee.setUpdateTime(LocalDateTime.now());
//        employee.setUpdateUser(empId);
        employeeService.updateById(employee);

        return R.success("员工信息修改成功");

    }


    /**
     *根据ID查询员工信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable Long id){
        log.info("根据ID查询员工信息...");
        Employee employee = employeeService.getById(id);
        if (employee!=null){
            return R.success(employee);
        }
        return R.error("未查询到该员工信息");

    }

















}



























































