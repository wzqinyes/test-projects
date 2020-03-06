package dubbo.service.impl;


import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Value;
import dubbo.service.BaseService;

@Service(version = "${dubbo.service.version:1.0.0}")
public class MyService implements BaseService {

    //The default value of ${dubbo.application.name} is ${spring.application.name}
    @Value("${dubbo.application.name}")
    private String name;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }
}
