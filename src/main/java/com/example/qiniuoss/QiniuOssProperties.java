package com.example.qiniuoss;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author freeok
 */
@Data
@ConfigurationProperties(prefix = "qiniu.oss")
public class QiniuOssProperties {

    /**
     * 开启自动装配
     */
    private Boolean enable;

    /**
     * your access key
     */
    private String accessKey;

    /**
     * your secret key
     */
    private String secretKey;

    /**
     * cdn加速域名
     */
    private String domain;

    /**
     * 存储空间名
     */
    private String bucket;

}
