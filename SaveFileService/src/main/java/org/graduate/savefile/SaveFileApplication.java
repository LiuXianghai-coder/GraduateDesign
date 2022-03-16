package org.graduate.savefile;

import org.graduate.savefile.service.StorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(value = {StorageProperties.class})
public class SaveFileApplication {
    public static void main(String[] args) {
        SpringApplication.run(SaveFileApplication.class, args);
    }

}
