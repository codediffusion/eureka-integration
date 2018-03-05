package serviceb.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import serviceb.app.Executor;

/**
 * Created by rd on 05/03/18.
 */
@Configuration
public class ServiceBAppConfig {

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }


    @Bean
    public Executor executor(RestTemplate restTemplate){
        return new Executor(restTemplate);
    }

}
