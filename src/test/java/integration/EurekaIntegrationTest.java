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
import org.springframework.test.context.TestPropertySource;
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
@TestPropertySource("classpath:serviceb.properties")
@RunWith(SpringJUnit4ClassRunner.class)
public class EurekaIntegrationTest {

    private int serverPort;

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
        //Start service A.
        ConfigurableApplicationContext ctx = new SpringApplicationBuilder(ServiceA.class)
                .properties("spring.config.location: classpath:servicea.properties", "server.port: 0")
                .build().run();
        serverPort = Integer.valueOf(ctx.getEnvironment().getProperty("local.server.port"));
    }

    @Test
    public void execute_success() throws IllegalAccessException, IOException, InstantiationException {
        String response = executor.execute("John");

        assertNotNull(response);
        assertEquals("Hello, John", response);
    }

}
