package org.graduate.savefile.service;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author : LiuXianghai on 2021/2/23
 * @Created : 2021/02/23 - 20:04
 * @Project : savefile
 */
@ConfigurationProperties("storage")
@Data
public class StorageProperties {
    private String location = "uploadFiles";
}
