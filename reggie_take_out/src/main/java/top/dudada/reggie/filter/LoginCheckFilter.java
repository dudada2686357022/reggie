package top.dudada.reggie.filter;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;
import top.dudada.reggie.common.BaseContext;
import top.dudada.reggie.common.R;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登入过滤器
 */
@Slf4j
@WebFilter(filterName = "loginCheckFilter",urlPatterns = "/*")
public class LoginCheckFilter implements Filter {

//  路径匹配器
//    AntPathMatcher 是 Spring 框架核心模块中的一个类，
//    具体位于 org.springframework.util 包中。它用于对字符串执行模式匹配，
//    主要用于使用 Ant 风格的模式匹配和过滤 URL 或文件路径。
    public static final AntPathMatcher PATH_MATCHER =new AntPathMatcher();



    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;


//        1.获取本次请求的URI
        String requestURI = request.getRequestURI();
        log.info("拦截到请求:"+requestURI);

//        2.定义不需要处理的请求路径
        String [] urls=new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "/common/**"

        };


//        3.判断本次请求是否需要处理   如果不需要处理,则直接放行
        boolean check = check(urls, requestURI);
        if (check){
            filterChain.doFilter(request,response);
            return;
        }

//        4.判断登入状态,如果已登入,则直接放行
        if (request.getSession().getAttribute("employee")!=null){

//          从获取session获取用户ID
            Long employeeId =(Long) request.getSession().getAttribute("employee");
//          将获取到的用户ID存入自定义工具类BaseContext中(存到ThreadLocal中)
            BaseContext.setCurrentId(employeeId);

            filterChain.doFilter(request,response);

            return;
        }

//        5.如果未登入则返回未登入结果,通过输出流方式向客户端响应数据
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        return;


    }


    /**
     * 路径匹配，检查本次请求是否需要放行
     * @param urls
     * @param requestURI
     * @return
     */
    public boolean check(String[] urls,String requestURI){
        for (String url: urls) {
            boolean match=PATH_MATCHER.match(url,requestURI);
            if (match){
                return true;
            }
        }
        return false;
    }




}
