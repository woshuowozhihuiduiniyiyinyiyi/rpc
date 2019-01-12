package com.hj.basic.rpc.service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author tangj
 * @description
 * @since 2019/1/12 10:07
 */
@Configuration
public class InitialScanner {

    @Bean
    public ServicesSubscribe getServicesSubscribe(){
        return new ServicesSubscribe();
    }
}
