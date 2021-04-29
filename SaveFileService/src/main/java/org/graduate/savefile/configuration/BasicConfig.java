package org.graduate.savefile.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * @author : LiuXianghai on 2021/2/23
 * @Created : 2021/02/23 - 21:46
 * @Project : savefile
 */
@Configuration
public class BasicConfig {
    private final Environment environment;

    @Autowired
    public BasicConfig(Environment environment) {
        this.environment = environment;
    }

    @Bean(name = "hostAddress")
    public String hostAddress() {
        return "39.99.129.90";
    }

    @Bean(name = "serverPort")
    public String serverPort() {
        return environment.getProperty("server.port");
    }

}
