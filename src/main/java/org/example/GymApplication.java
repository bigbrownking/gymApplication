package org.example;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

import java.util.List;

@OpenAPIDefinition(info = @Info(title = "Demo API", version = "1.0"))
@SecurityScheme(
        name = "Bearer Authentication",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
@SpringBootApplication(scanBasePackages = "org.example")
@EnableEurekaServer
public class GymApplication {

    public static void main(String[] args) {
        SpringApplication.run(GymApplication.class, args);
    }

}
