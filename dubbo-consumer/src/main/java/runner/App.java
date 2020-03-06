package runner;

import dubbo.service.BaseService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;

import java.util.UUID;

@EnableAutoConfiguration
public class App {

    @Reference(version = "${dubbo.service.version:1.0.0}", url = "dubbo://127.0.0.1:12345")
    private BaseService baseService;

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @Bean
    public ApplicationRunner runner() {
        return args -> {
            while (true) {
                System.out.println(baseService.getName());
                baseService.setName(UUID.randomUUID().toString());
                Thread.sleep(1000);
            }

        };
    }
}
