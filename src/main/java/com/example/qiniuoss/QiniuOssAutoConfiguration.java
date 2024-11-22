package com.example.qiniuoss;

import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author freeok
 */
@Configuration
@EnableConfigurationProperties(QiniuOssProperties.class)
@ConditionalOnClass(QiniuOssService.class)
@ConditionalOnProperty(prefix = "qiniu.oss", value = "enable", matchIfMissing = true)
public class QiniuOssAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(QiniuOssService.class)
    public QiniuOssService ossUtils() {
        return new QiniuOssService();
    }

    @Bean
    public com.qiniu.storage.Configuration qiniuConfig() {
        // 构造一个带指定 Region 对象的配置类
        com.qiniu.storage.Configuration config = new com.qiniu.storage.Configuration(Region.regionCnEast2());
        // 指定分片上传版本
        config.resumableUploadAPIVersion = com.qiniu.storage.Configuration.ResumableUploadAPIVersion.V2;
        return config;
    }

    /**
     * 认证信息实例
     */
    @Bean
    public Auth auth(QiniuOssProperties qiniuOssProperties) {
        return Auth.create(qiniuOssProperties.getAccessKey(), qiniuOssProperties.getSecretKey());
    }

    /**
     * 构建七牛上传工具实例
     */
    @Bean
    public UploadManager uploadManager() {
        return new UploadManager(qiniuConfig());
    }

    /**
     * 构建七牛空间管理实例
     */
    @Bean
    public BucketManager bucketManager(QiniuOssProperties qiniuOssProperties) {
        return new BucketManager(auth(qiniuOssProperties), qiniuConfig());
    }

}
