package fr.younup.mock;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.MountableFile;

import java.util.HashMap;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class OpenAPIMockIntegrationTest {

    @Container
    static final GenericContainer<?> openApiMock = new GenericContainer<>("muonsoft/openapi-mock:0.3.9")
            .withExposedPorts(8080)
            .withCopyFileToContainer(MountableFile.forClasspathResource("open-api-mock_interface-contract.yml"), "/tmp/spec.yaml")
            .withEnv(new HashMap<>() {{
                put("OPENAPI_MOCK_SPECIFICATION_URL", "/tmp/spec.yaml");
                put("OPENAPI_MOCK_USE_EXAMPLES", "if_present");
            }});

    @LocalServerPort
    int localServerPort;

    @BeforeAll
    static void beforeAll() {
        // Définition dynamique de la propriété système avec l'URL du mock
        System.setProperty("mock.url", "http://" + openApiMock.getContainerIpAddress() + ":" + openApiMock.getMappedPort(8080));
    }

    @DynamicPropertySource
    static void dynamicProperties(DynamicPropertyRegistry registry) {
        registry.add("weather.api.url", () -> System.getProperty("mock.url"));
    }

    @BeforeEach
    void setUp() {
        RestAssured.port = localServerPort;
    }

    @Test
    void testOpenApiMock() {
        // Envoi de la requête à notre Application de gestion des tâches
        given()
                .contentType("application/json")
                .body("{ \"name\": \"Sample Task\", \"location\": \"Paris\" }")
                .when()
                .post("/api/tasks")
                .then()
                .assertThat()
                .statusCode(201)
                .body(containsString("Sample Task"))
                .body(containsString("sunny"));
    }
}
