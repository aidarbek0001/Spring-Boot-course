package kz.springboot.springbootdemo.config;

import kz.springboot.springbootdemo.beans.FirstBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeansConfig {

    @Bean
    public FirstBean firstBean(){
        return new FirstBean();
    }



}
