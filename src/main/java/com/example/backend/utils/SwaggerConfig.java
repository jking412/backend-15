package com.example.backend.utils;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * 配置类，注册web层相关组件
 */

    @Slf4j
    @Configuration
    public class SwaggerConfig {
        @Bean
        public OpenAPI springShopOpenAPI() {
            return new OpenAPI()
                    .info(new Info().title("backend")
                            .description("我的API文档")
                            .version("v1")
                            .license(new License().name("Apache 2.0").url("http://springdoc.org")))
                    .externalDocs(new ExternalDocumentation()
                            .description("外部文档")
                            .url("https://springshop.wiki.github.org/docs"));
        }



}
