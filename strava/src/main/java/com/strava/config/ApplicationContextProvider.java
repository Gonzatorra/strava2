package com.strava.config;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.ApplicationContext;

public class ApplicationContextProvider {
    private static final ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

    public static ApplicationContext getContext() {
        return context;
    }
}
