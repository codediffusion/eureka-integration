package integration;

import com.jpmc.cib.p3.pcm.client.PcmDataClientServiceImpl;
import com.jpmc.cib.p3.pcm.client.app.App;
import com.jpmc.cib.p3.pcm.client.app.PcmToolsExecutor;
import com.jpmc.cib.p3.pcm.client.config.PcmClientConfig;
import com.jpmc.cib.p3.pcm.client.stub.app.EurekaServerApp;
import com.jpmc.cib.p3.pcm.client.stub.app.StubbedPcmApp;
import com.jpmc.cib.p3.pcm.entity.profile.Profile;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

import static com.jpmc.cib.p3.pcm.constants.ProfileConstants.PCMTOOLS_TEST;
import static com.jpmc.cib.p3.pcm.entity.profile.ProfileType.BRH;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;


@ActiveProfiles({PCMTOOLS_TEST})
@SpringBootTest(classes = {App.class}, webEnvironment = RANDOM_PORT)
@RunWith(SpringJUnit4ClassRunner.class)
public class EurekaIntegrationTest {

    private int serverPort;

    @Autowired
    private PcmClientConfig pcmClientConfig;

    @Autowired
    private PcmToolsExecutor pcmToolsExecutor;

    @Autowired
    private PcmDataClientServiceImpl pcmDataClientService;

    private RestTemplate restTemplate = new RestTemplate();

    @BeforeClass
    public static void beforeClass(){
        //Start Eureka server.
        new SpringApplicationBuilder(EurekaServerApp.class)
                .properties("spring.config.location: classpath:eureka-server.properties")
                .build().run();
    }

    @Before
    public void setup(){
        //Start stubbed PCM application.
        ConfigurableApplicationContext ctx = new SpringApplicationBuilder(StubbedPcmApp.class)
                .properties("spring.config.location: classpath:stubbedPcmApp.properties", "server.port: 0")
                .build().run();
        serverPort = Integer.valueOf(ctx.getEnvironment().getProperty("local.server.port"));
    }

    @Test
    public void pcmToolWithEureka_successNonInteractive() throws IllegalAccessException, IOException, InstantiationException {
        String endpoint = "http://localhost:" + serverPort;

        pcmToolsExecutor.execute(pcmClientConfig, pcmDataClientService);

        Profile profile = restTemplate.getForObject(endpoint + "/api/v1/profile/BRH/PPP.BRH.3450/0", Profile.class);

        assertNotNull(profile);
        assertEquals(BRH, profile.getProfileType());
        assertEquals("PPP.BRH.3450", profile.getProfileId());
        assertEquals(0L, profile.getProfileVersion().longValue());
    }

}
