package serviceb.app;

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
    private DiscoveryClient discoveryClient;

    private RestTemplate restTemplate;

    public Executor(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String execute(String name){
        List<ServiceInstance> serviceInstanceList = discoveryClient.getInstances("SERVICE-A");
        String host = serviceInstanceList.get(0).getHost();
        int port = serviceInstanceList.get(0).getPort();
        String url = host + ":" + port;
        return restTemplate.getForObject(url + "/greeting?name=" + name, String.class);
    }
}
