package org.stropa.autodoc.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.stropa.autodoc.engine.AutodocJavaEngine;


@SpringBootApplication
@ComponentScan("org.stropa")
public class AutodocExampleApp implements InitializingBean {

    Logger logger = LoggerFactory.getLogger(AutodocExampleApp.class);


    public static void main(String[] args) {
        SpringApplication.run(AutodocExampleApp.class, args);
    }



    @Override
    public void afterPropertiesSet() throws Exception {
        logger.info("Writing structure docs");
        AutodocJavaEngine doc = new AutodocJavaEngine(null);
        //doc.writeDocs(Arrays.asList(new HostnameDescriber(), new DummyDockerDescriber()));
    }

}
