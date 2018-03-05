package servicea.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import servicea.controller.ServiceAController;

/**
 * Created by rd on 05/03/18.
 */
@Configuration
@Import(ServiceAController.class)
public class ServiceAAppConfig {
}
