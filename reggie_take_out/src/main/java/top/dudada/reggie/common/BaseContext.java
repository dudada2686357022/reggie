package top.dudada.reggie.common;

/**
 * 自定义线程工具类，通过ThreadLocal 设置和获取用户ID
 */
public class BaseContext {
    private  static ThreadLocal<Long> threadLocal =new ThreadLocal<>();

    /**
     * 设置ID
     * @param id
     */
    public static void setCurrentId(Long id){
        threadLocal.set(id);
    }

    /**
     * 获取ID
     * @return
     */
    public static  Long getCurrentId(){
        return threadLocal.get();
    }

}
