package serviceb.app;

import org.springframework.web.client.RestTemplate;

/**
 * Created by rd on 05/03/18.
 */
public class Executor {


    private RestTemplate restTemplate;

    public Executor(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void execute(String john){



    }
}
