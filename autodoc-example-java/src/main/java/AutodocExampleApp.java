import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.stropa.autodoc.engine.AutodocEngine;
import org.stropa.autodoc.reporters.DummyDockerReporter;
import org.stropa.autodoc.reporters.HostnameReporter;

import java.util.Arrays;


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
        AutodocEngine doc = new AutodocEngine(null);
        doc.writeDocs(Arrays.asList(new HostnameReporter(), new DummyDockerReporter()));
    }

}
