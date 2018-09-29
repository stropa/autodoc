package org.stropa.autodoc.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.ComponentScan;
import org.stropa.autodoc.engine.AutodocJavaEngine;
import org.stropa.autodoc.org.stropa.autodoc.storage.FileStorage;

import java.util.HashMap;


@SpringBootApplication
@ComponentScan("org.stropa")
public class AutodocExampleApp implements ApplicationContextAware {

    Logger logger = LoggerFactory.getLogger(AutodocExampleApp.class);


    public static void main(String[] args) {
        SpringApplication.run(AutodocExampleApp.class, args);
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        logger.info("Writing structure docs");
        HashMap<String, Object> context = new HashMap<String, Object>();
        context.put("applicationContext", applicationContext);
        AutodocJavaEngine doc = new AutodocJavaEngine(context);
        doc.describeSpringApplication(applicationContext, new FileStorage("structure.log"));
    }
}
