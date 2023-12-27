package org.zerock.b01.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi restApi(){
        return GroupedOpenApi.builder()
                .group("API")
                .packagesToScan("org.zerock.b01.controller")            //모든 controller 포함
                .pathsToExclude("/board/*")                             //board로 시작하는 거 제외
                .build();
    }

    @Bean
    public GroupedOpenApi commonApi(){
        return GroupedOpenApi.builder()
                .pathsToMatch("/board/*")
                .group("COMMON API")
                .build();
    }

}
