package com.inventory.inventory.utill;

import com.inventory.inventory.Configs.TokenContext;
import com.inventory.inventory.constant.ApiErrorCodes;
import com.inventory.inventory.dto.response.ClientFMCGResponse;
import com.inventory.inventory.dto.response.MemberResponse;
import com.inventory.inventory.exception.BusinessServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Set;

@RequiredArgsConstructor
@Service
@Slf4j
public class ExternalRestService {

    private final RestTemplate restTemplate;
    @Value("${clients.service.url}")
    private String clientServiceUrl;
    @Value("${member.getMember.url}")
    private String getMemberUrl;
    @Value("${clients.getAllIds.url}")
    private String clientIdsServiceUrl;
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
        try {
            log.info("While fetching getClient from external rest service");
            log.info("Get client with id: {}", clientId);
            String url = clientServiceUrl + "/" + clientId;
            log.info("URL: {}", url);
            HttpEntity<Void> requestEntity = new HttpEntity<>(createHeaders());
            log.info("Fetch client details with authorization header");
            ResponseEntity<ClientFMCGResponse> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, ClientFMCGResponse.class);
            return response.getBody();
        }catch (Exception e){
            log.info("Error occurred: " + e.getMessage());
            throw new BusinessServiceException(ApiErrorCodes.INVALID_INPUT.getErrorCode(), e.getMessage());
        }
    }
    public MemberResponse getMemberById(Long memberId) {
        try {
            log.info("While fetching getMemberById from external rest service");
            log.info("Get member with id: {}", memberId);
            String url = getMemberUrl + "/" + memberId;
            log.info("URL: {}", url);
            HttpEntity<Void> requestEntity = new HttpEntity<>(createHeaders());
            log.info("Fetch client details with authorization header");
            ResponseEntity<MemberResponse> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, MemberResponse.class);
            return response.getBody();
        }catch (Exception e){
            log.info("Error occurred: " + e.getMessage());
            throw new BusinessServiceException(ApiErrorCodes.INVALID_INPUT.getErrorCode(), e.getMessage());
        }
    }

    public Set<Long> getClientIdsByMemberId(Long memberId) {
        try {
            log.info("While fetching getClientIdsByMemberId from external rest service");
            String url = clientIdsServiceUrl + "/" + memberId;
            HttpEntity<Void> requestEntity = new HttpEntity<>(createHeaders());
            log.info("Fetch client details with authorization header");
            return restTemplate.exchange(url, HttpMethod.GET, requestEntity, new ParameterizedTypeReference<Set<Long>>() {
            }).getBody();
        }catch (Exception e){
            log.info("Error occurred: " + e.getMessage());
            throw new BusinessServiceException(ApiErrorCodes.INVALID_INPUT.getErrorCode(), e.getMessage());
        }
    }
}
