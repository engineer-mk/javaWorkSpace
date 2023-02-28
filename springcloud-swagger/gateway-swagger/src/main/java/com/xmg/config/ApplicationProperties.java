package com.xmg.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * 自定义配置项
 *
 * @author makui
 * @created 2023/2/24
 **/
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "application")
@RefreshScope
public class ApplicationProperties {
    /**
     * token头名称
     */
    private String authHeader = "Authorization";

    /**
     * 接口调试token
     */
    private String defaultToken = "";

    /**
     * 无需认证的接口白名单
     */
    private Set<String> whitePath = new HashSet<>();

    /**
     * 接口文档服务
     */
    private Set<String> docServices = new HashSet<>();
}
