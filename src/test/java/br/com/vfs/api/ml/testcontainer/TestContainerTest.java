package br.com.vfs.api.ml.testcontainer;

import br.com.vfs.api.ml.Application;
import org.junit.ClassRule;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.lang.NonNull;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MySQLContainer;

import java.net.ServerSocket;
import java.util.Collections;

import static org.testcontainers.containers.BindMode.READ_ONLY;

@ContextConfiguration(initializers = TestContainerTest.Initializer.class,
        classes = Application.class)
public abstract class TestContainerTest {
    private static final Integer STUBBY_PORT = 8882;
    private static Integer STUBBY_EXPOSED_PORT;

    static {
        try (final ServerSocket TMP_SERVER = new ServerSocket(0); ) {
            STUBBY_EXPOSED_PORT = TMP_SERVER.getLocalPort();
        } catch (Exception e) {
        }
    }


    @ClassRule
    protected final static MySQLContainer MYSQL = new MySQLContainer();

    @ClassRule
    public static final StubbyContainer STUBBY_CONTAINER =
            new StubbyContainer("sandokandias/stubby4j-docker")
                    .withExposedPorts(STUBBY_PORT)
                    .withEnv("STUBBY_PORT", STUBBY_PORT.toString())
                    .withClasspathResourceMapping("stubby.yml", "/usr/local/stubby.yml", READ_ONLY);

    public static class Initializer implements
            ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues values = TestPropertyValues.of(
                    "spring.datasource.url=" + MYSQL.getJdbcUrl(),
                    "spring.datasource.username="+ MYSQL.getUsername(),
                    "spring.datasource.password="+ MYSQL.getPassword(),
                    String.format("url.invoice=http://localhost:%d/mock/invoice", STUBBY_EXPOSED_PORT),
                    String.format("url.ranking-vendor=http://localhost:%d/mock/ranking-vendor",STUBBY_EXPOSED_PORT));
            values.applyTo(configurableApplicationContext);
        }
    }

    @BeforeAll
    static void setup() {
        MYSQL.start();
        STUBBY_CONTAINER.setPortBindings(
                Collections.singletonList(String.format("%s:%s", STUBBY_EXPOSED_PORT, STUBBY_PORT)));
        STUBBY_CONTAINER.start();
    }

    @AfterAll
    static void tearDown() {
        MYSQL.stop();
        STUBBY_CONTAINER.stop();
    }
}

class StubbyContainer extends GenericContainer<StubbyContainer> {
    public StubbyContainer(@NonNull final String dockerImageName) {
        super(dockerImageName);
    }
}