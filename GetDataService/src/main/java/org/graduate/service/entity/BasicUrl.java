package org.graduate.service.entity;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Data
@ConfigurationProperties(prefix = "getdata")
public final class BasicUrl {
    private List<Platform> platformList;
}
