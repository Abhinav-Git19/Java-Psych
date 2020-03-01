package com.psych.game.config;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

//This annotation indicates one of the "parts" of Spring to analyzed
@Component
public class ApplicationContextProvider implements ApplicationContextAware {

    private static  ApplicationContext applicationContext;

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }




    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {

        this.applicationContext = context;
    }
}
