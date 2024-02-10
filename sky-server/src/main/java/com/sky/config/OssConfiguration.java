package com.sky.config;

/**
 * @author Mark
 * @date 2024/2/10
 */

import com.sky.properties.AliOssProperties;
import com.sky.utils.AliOssUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 阿里云oss图片上传配置类
 */
@Configuration
public class OssConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public AliOssUtil aliOssUtil(AliOssProperties properties){
        return new AliOssUtil(properties.getEndpoint(), properties.getAccessKeyId(),
                properties.getAccessKeySecret(), properties.getBucketName());
    }
}
