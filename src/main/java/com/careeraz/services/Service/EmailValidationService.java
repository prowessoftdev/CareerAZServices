package com.careeraz.services.Service;


import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import org.springframework.web.util.UriComponentsBuilder;

@Service
public class EmailValidationService {

    @Value("${mailboxlayer.api.key}")
    private String apiKey;

    private static final String API_URL = "https://apilayer.net/api/check";

    public boolean isEmailValid(String email) {
        try {
            RestTemplate restTemplate = new RestTemplate();

            String url = UriComponentsBuilder.fromPath(API_URL)
                    .queryParam("access_key", apiKey)
                    .queryParam("email", email)
                    .queryParam("smtp", 1)
                    .queryParam("format", 1)
                    .toUriString();

            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

            JSONObject json = new JSONObject(response.getBody());
            boolean formatValid = json.optBoolean("format_valid");
            boolean mxFound = json.optBoolean("mx_found");
            boolean smtpCheck = json.optBoolean("smtp_check");

            // Email is valid only if all checks pass
            return formatValid && mxFound && smtpCheck;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}

