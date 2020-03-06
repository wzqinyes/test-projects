package runner;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

@EnableAutoConfiguration
@EnableDubbo(scanBasePackages = {"dubbo.service"})
public class App {

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
