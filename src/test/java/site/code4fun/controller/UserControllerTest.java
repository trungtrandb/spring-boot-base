package site.code4fun.controller;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;
import site.code4fun.dto.AccessTokenResponseDTO;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest {

    @LocalServerPort
    private int port;

    private String url;

    @BeforeEach
    public void setUp() {
        url = String.format("http://localhost:%d/auth/login", port);
    }

    @Test
    public void givenEmailAndPassword_whenLogin_thenResponseTokenIsNotNull() throws JSONException {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        JSONObject personJsonObject = new JSONObject();
        personJsonObject.put("email", "trungtrandb@gmail.com");
        personJsonObject.put("password", "123456");

        HttpEntity<String> request =
                new HttpEntity<>(personJsonObject.toString(), headers);

        AccessTokenResponseDTO personResultAsJsonStr =
                restTemplate.postForObject(url, request, AccessTokenResponseDTO.class);

        assert personResultAsJsonStr != null;
        assertNotNull(personResultAsJsonStr.getToken());
    }
}
