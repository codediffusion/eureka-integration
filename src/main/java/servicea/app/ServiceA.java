package servicea.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import servicea.config.ServiceAAppConfig;

/**
 * Created by rd on 26/02/18.
 */
@SpringBootApplication
@Import(ServiceAAppConfig.class)
public class ServiceA {

    public static void main(String[] args) {
        SpringApplication.run(ServiceA.class, args);
    }

}
