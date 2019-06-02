package org.stropa.autodoc.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.stropa.autodoc.spring.AutodocSpringAdapter;


@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
@ComponentScan("org.stropa")
@Configuration
public class AutodocExampleApp {


    public static void main(String[] args) {
        SpringApplication.run(AutodocExampleApp.class, args);
    }


    @Bean
    public AutodocSpringAdapter getAutodocJavaEngine() {
        return new AutodocSpringAdapter();
    }


}
