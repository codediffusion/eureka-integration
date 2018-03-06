package integration;

import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.shared.Application;
import eureka.EurekaServer;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import servicea.app.ServiceA;
import serviceb.app.Executor;
import serviceb.app.ServiceB;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;


@SpringBootTest(classes = {ServiceA.class}, webEnvironment = RANDOM_PORT)
@TestPropertySource("classpath:servicea.properties")
@RunWith(SpringJUnit4ClassRunner.class)
public class EurekaIntegrationTest {

    private Executor executor;

    private EurekaClient eurekaClient;

    @BeforeClass
    public static void beforeClass(){
        //Start Eureka server.
        new SpringApplicationBuilder(EurekaServer.class)
                .properties("spring.config.location: classpath:eureka-server.properties")
                .build().run();
    }

    @Before
    public void setup() {
        //Start service B.
        ConfigurableApplicationContext ctx = new SpringApplicationBuilder(ServiceB.class)
                .properties("spring.config.location: classpath:serviceb.properties", "server.port: 0")
                .build().run();
        executor = ctx.getBean(Executor.class);
        eurekaClient = ctx.getBean(EurekaClient.class);
    }

    @Test
    public void execute_success() throws IllegalAccessException, IOException, InstantiationException, InterruptedException {

        verifyEurekaRegistration();

        String response = executor.execute("John");

        assertNotNull(response);
        assertEquals("Hello, John", response);
    }

    private void verifyEurekaRegistration() throws InterruptedException {
        Application application = null;
        for(int count = 1; count <= 20; count++){
            if(null != eurekaClient.getApplication("SERVICE-A")){
                application = eurekaClient.getApplication("SERVICE-A");
                break;
            }
            Thread.sleep(2000L);
        }

        if(application == null){
            throw new RuntimeException("Service registration timed out");
        }
    }

}
