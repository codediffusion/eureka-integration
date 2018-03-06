package serviceb.app;

import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.shared.Application;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.netflix.eureka.EurekaDiscoveryClient;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * Created by rd on 05/03/18.
 */
public class Executor {

    @Autowired
    private EurekaClient eurekaClient;

    private RestTemplate restTemplate;

    public Executor(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String execute(String name){
        Application application = eurekaClient.getApplication("SERVICE-A");

        String homepageUrl = application.getInstances().get(0).getHomePageUrl();
        return restTemplate.getForObject(homepageUrl + "/greeting?name=" + name, String.class);
    }
}
