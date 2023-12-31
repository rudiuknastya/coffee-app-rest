package project;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableAsync;
@OpenAPIDefinition(
        info = @Info(title = "Coffee-app Rest Api",description = "Coffee-app Rest Api documentation",version = "v1"),
        servers = {@Server(url = "/Coffee_App_A_Rudiuk_Rest/", description = "Default Server URL")}
)
@SpringBootApplication
@EnableAsync
public class App extends SpringBootServletInitializer {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
