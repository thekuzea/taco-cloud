package tacos.web;

import org.assertj.core.api.recursive.comparison.RecursiveComparisonConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.web.client.RestTemplate;
import tacos.domain.Ingredient;

import java.util.Collections;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RestTemplateTest {

    private static final RecursiveComparisonConfiguration COMPARISON = RecursiveComparisonConfiguration.builder()
            .withIgnoredFields("id")
            .build();

    @LocalServerPort
    private int port;

    private RestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        restTemplate = new RestTemplateBuilder()
                .rootUri(String.format("http://localhost:%d/data-api", port))
                .build();
    }

    @Test
    void testUriVariablesAsArr() {
        final Ingredient ingredient =
                restTemplate.getForObject("/ingredients/{id}", Ingredient.class, "FLTO");

        assertThat(ingredient).usingRecursiveComparison(COMPARISON)
                .isEqualTo(new Ingredient("FLTO", "Flour Tortilla", Ingredient.Type.WRAP));
    }

    @Test
    void testUriVariablesAsMap() {
        final Map<String, String> urlVariables = Collections.singletonMap("id", "FLTO");

        final Ingredient ingredient =
                restTemplate.getForObject("/ingredients/{id}", Ingredient.class, urlVariables);

        assertThat(ingredient).usingRecursiveComparison(COMPARISON)
                .isEqualTo(new Ingredient("FLTO", "Flour Tortilla", Ingredient.Type.WRAP));
    }
}
