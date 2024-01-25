package cn.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Spring-Bean工具类
 *
 * @author nackily
 * @since 1.0.0
 */
@Component
public class SpringBeanUtils implements ApplicationContextAware {

    private static ApplicationContext context = null;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringBeanUtils.context = applicationContext;
    }

    @SuppressWarnings("unchecked")
    public static <T> T getBean(String name) {
        if(name == null || context == null) {
            return null;
        } else {
            return (T) context.getBean(name);
        }
    }

    public static <T> T getBean(Class<T> tClass) {
        return context.getBean(tClass);
    }

}
