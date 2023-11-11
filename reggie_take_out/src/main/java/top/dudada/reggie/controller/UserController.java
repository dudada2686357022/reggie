package top.dudada.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.dudada.reggie.common.R;
import top.dudada.reggie.entity.User;
import top.dudada.reggie.service.UserService;
import top.dudada.reggie.util.SMSUtils;
import top.dudada.reggie.util.ValidateCodeUtils;

import javax.servlet.http.HttpSession;
import java.util.Map;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;


    /**
     * 发送验证码
     * @param user
     * @param session
     * @return
     */
    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session){
        String phone = user.getPhone();

        if (StringUtils.isNotEmpty(phone)){
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            SMSUtils.sendMessage("瑞吉外卖","",phone,code);
            System.out.println("验证码："+code);
            session.setAttribute(phone,code);
            return R.success("手机验证码发送成功");
        }

        return R.error("短信发送失败");

    }


    /**
     * 手机端用户登入
     * @param map
     * @param session
     * @return
     */
    @PostMapping("/login")
    public R<User> login(@RequestBody Map map ,HttpSession session){
//        获取手机号
        String phone = map.get("phone").toString();
//        验证码
        String code = map.get("code").toString();

//        从Session中获取保存的验证码
        Object codeSession = session.getAttribute(phone);

//        进行验证码比对(页面提交的验证码和Session中保存的验证码进行比对)
        if (codeSession!=null&&codeSession.equals(codeSession)){
//            如果成功比对，说明登入成功
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getPhone,phone);

            User user = userService.getOne(queryWrapper);
            if (user==null){
//                判断当前手机号对应的用户是否为新用户,如果是新用户就自动完成注册
                user = new User();
                user.setPhone(phone);
                user.setStatus(1);
                userService.save(user);
            }
            session.setAttribute("user",user.getId());
            return R.success(user);

        }

        return R.error("登入失败");
    }


































}
