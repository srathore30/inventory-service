package com.inventory.inventory.utill;

import com.inventory.inventory.Configs.TokenContext;
import com.inventory.inventory.constant.ApiErrorCodes;
import com.inventory.inventory.dto.response.ProductRes;
import com.inventory.inventory.exception.BusinessServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceClient {
    private final RestTemplate restTemplate;
    @Value("${product.getProduct.url}")
    private String productUrl;
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

    public ProductRes getProduct(Long productId) {
        try {
            log.info("While fetching getProduct from product service client");
            String url = productUrl + productId;
            HttpEntity<Void> requestEntity = new HttpEntity<>(createHeaders());
            log.info("Fetch product details with authorization header");
            ResponseEntity<ProductRes> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, ProductRes.class);
            return response.getBody();
        }catch (Exception e){
            log.info("Error occurred: " + e.getMessage());
            throw new BusinessServiceException(ApiErrorCodes.INVALID_INPUT.getErrorCode(), e.getMessage());
        }
    }
}