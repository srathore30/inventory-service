package com.inventory.inventory.utill;

import com.inventory.inventory.Configs.TokenContext;
import com.inventory.inventory.dto.response.ClientFMCGResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
@Service
@Slf4j
public class ExternalRestService {

    private final RestTemplate restTemplate;
    @Value("${clients.service.url}")
    private String clientServiceUrl;
    private HttpHeaders createHeaders() {
        log.info("Helper method to create HTTP headers with the token");
        log.info("Token: {}", TokenContext.getToken());
        String token = TokenContext.getToken();
        HttpHeaders headers = new HttpHeaders();
        if (token != null) {
            log.info("Add token to headers");
            headers.set("Authorization", "Bearer " + token);
        }
        return headers;
    }

    public ClientFMCGResponse getClient(Long clientId) {
        log.info("Get client with id: {}", clientId);
        String url = clientServiceUrl + "/" + clientId;
        log.info("URL: {}", url);
        HttpEntity<Void> requestEntity = new HttpEntity<>(createHeaders());
        log.info("Fetch client details with authorization header");
        ResponseEntity<ClientFMCGResponse> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, ClientFMCGResponse.class);
        return response.getBody();
    }
}
