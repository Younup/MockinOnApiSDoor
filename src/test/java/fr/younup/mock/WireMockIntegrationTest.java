package fr.younup.mock;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.MountableFile;
import org.wiremock.integrations.testcontainers.WireMockContainer;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class WireMockIntegrationTest {

    @Container
    private static final WireMockContainer wiremockServer = new WireMockContainer("wiremock/wiremock")
            .withCopyFileToContainer(MountableFile.forClasspathResource("wiremock-weatherForecast.json"), "/home/wiremock/mappings/wiremock-weatherForecast.json");

    @LocalServerPort
    int localServerPort;

    @DynamicPropertySource
    static void dynamicProperties(DynamicPropertyRegistry registry) {
        registry.add("weather.api.url", wiremockServer::getBaseUrl);
    }

    @BeforeEach
    void setUp() {
        RestAssured.port = localServerPort;
    }

    @Test
    void testWireMock() {
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
