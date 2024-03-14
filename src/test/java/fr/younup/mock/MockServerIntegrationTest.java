package fr.younup.mock;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockserver.client.MockServerClient;
import org.mockserver.model.MediaType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MockServerContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;
import static org.springframework.http.HttpMethod.GET;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class MockServerIntegrationTest {

    @Container
    private static final MockServerContainer mockServerContainer = new MockServerContainer(DockerImageName.parse("mockserver/mockserver:5.15.0"));
    static MockServerClient mockServerClient;

    @LocalServerPort
    int localServerPort;

    @Value("${weather.api.path}")
    private String apiPath;

    @DynamicPropertySource
    static void dynamicProperties(DynamicPropertyRegistry registry) {
        mockServerClient = new MockServerClient(
                mockServerContainer.getHost(),
                mockServerContainer.getServerPort()
        );
        registry.add("weather.api.url", mockServerContainer::getEndpoint);
    }

    @BeforeEach
    void setUp() {
        RestAssured.port = localServerPort;
        mockServerClient.reset();
    }

    @Test
    void testMockServer() {
        // Configuration des "expectations" dans MockServer pour les requêtes à l'API externe
        mockServerClient.when(
                        request()
                                .withMethod(GET.name())
                                .withPath(apiPath)
                                .withQueryStringParameter("city", "Paris")
                )
                .respond(response()
                        .withStatusCode(201)
                        .withContentType(MediaType.APPLICATION_JSON)
                        .withBody("{ \"temperature\": 25, \"description\": \"sunny\" }")
                );

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
