package com.yaromac.springamqptemplate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
public class SpringAmqpTemplateApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringAmqpTemplateApplication.class, args);
    }

}
