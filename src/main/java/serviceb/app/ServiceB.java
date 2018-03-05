package serviceb.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Import;
import serviceb.config.ServiceBAppConfig;

/**
 * Created by rd on 26/02/18.
 */
@SpringBootApplication
@EnableDiscoveryClient
@Import(ServiceBAppConfig.class)
public class ServiceB {

    public static void main(String[] args) {
        SpringApplication.run(ServiceB.class, args);
    }

}
