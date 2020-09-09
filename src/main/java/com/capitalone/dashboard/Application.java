package com.capitalone.dashboard;

import com.capitalone.dashboard.config.MongoConfig;
import com.capitalone.dashboard.config.RestApiAppConfig;
import com.capitalone.dashboard.config.WebMVCConfig;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

/**
 * Application configuration and bootstrap
 *
 */
@SpringBootApplication
@EnableEncryptableProperties
public class Application extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Application.class, RestApiAppConfig.class, WebMVCConfig.class, MongoConfig.class);
    }
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
