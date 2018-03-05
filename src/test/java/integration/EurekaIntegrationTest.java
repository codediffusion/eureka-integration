package integration;

import eureka.EurekaServer;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;
import servicea.app.ServiceA;
import serviceb.app.Executor;
import serviceb.app.ServiceB;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;


@SpringBootTest(classes = {ServiceB.class}, webEnvironment = RANDOM_PORT)
@RunWith(SpringJUnit4ClassRunner.class)
public class EurekaIntegrationTest {

    private int serverPort;

    private RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private Executor executor;

    @BeforeClass
    public static void beforeClass(){
        //Start Eureka server.
        new SpringApplicationBuilder(EurekaServer.class)
                .properties("spring.config.location: classpath:eureka-server.properties")
                .build().run();
    }

    @Before
    public void setup(){
        //Start stubbed PCM application.
        ConfigurableApplicationContext ctx = new SpringApplicationBuilder(ServiceA.class)
                .properties("spring.config.location: classpath:stubbedPcmApp.properties", "server.port: 0")
                .build().run();
        serverPort = Integer.valueOf(ctx.getEnvironment().getProperty("local.server.port"));
    }

    @Test
    public void pcmToolWithEureka_successNonInteractive() throws IllegalAccessException, IOException, InstantiationException {
        String endpoint = "http://localhost:" + serverPort;

        executor.execute("John");

        String response = restTemplate.getForObject(endpoint + "/greeting?name=John", String.class);

        assertNotNull(response);
        assertEquals("Hello, John", response);
    }

}
