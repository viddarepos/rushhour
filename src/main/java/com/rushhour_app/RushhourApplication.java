package com.rushhour_app;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Rushhour API", version = "1.0", description = "REST API for Rush Hour application."))
public class RushhourApplication {

    public static void main(String[] args) {
        SpringApplication.run(RushhourApplication.class, args);
    }

}
