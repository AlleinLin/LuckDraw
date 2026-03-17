package io.prizewheel.api.config;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 应用配置类
 * 
 * @author Allein
 * @since 1.0.0
 */
@Configuration
@EnableDubbo
@MapperScan("io.prizewheel.adapters.persistence.mapper")
@ComponentScan(basePackages = {
        "io.prizewheel.core",
        "io.prizewheel.adapters",
        "io.prizewheel.api"
})
public class AppConfig {

}
