package com.example.steamThird.utils.bean;


import com.example.steamThird.common.enumType.ErrorCode;
import com.example.steamThird.exceptionhandler.MessageException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SpringBeanUtils implements ApplicationContextAware {
    protected static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext arg0) throws BeansException {
        if (applicationContext == null) {
            applicationContext = arg0;
        }

    }

    public static Object getBean(String name) {
        //name表示其他要注入的注解name名
        return applicationContext.getBean(name);
    }

    /**
     * 拿到ApplicationContext对象实例后就可以手动获取Bean的注入实例对象
     */
    public static <T> T getBean(Class<T> clazz) {
        try {
            return applicationContext.getBean(clazz);
        } catch (Exception e) {
            log.error("获取bean失败", e);
            throw new MessageException(ErrorCode.Error);
        }
    }
}