package com.codeup.capstone3dprinting.services;

import com.codeup.capstone3dprinting.ReCaptchaResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class ReCaptchaValidationService {

    @Value("${recaptcha.secret.key}")
    private String recaptchaSecret;

    private static final String recaptchaServerURL = "https://www.google.com/recaptcha/api/siteverify";

    public boolean validateCaptcha(String captchaResponse) {
        RestTemplate restTemplate = new RestTemplate();

        MultiValueMap<String, String> requestMap = new LinkedMultiValueMap<>();
        requestMap.add("secret", recaptchaSecret);
        requestMap.add("response", captchaResponse);

        ReCaptchaResponse apiResponse = restTemplate.postForObject(recaptchaServerURL, requestMap, ReCaptchaResponse.class);
        if (apiResponse == null) {
            return false;
        }

        System.out.println("apiResponse.isSuccess() = " + apiResponse.isSuccess());
        System.out.println("apiResponse.getChallenge_ts() = " + apiResponse.getChallenge_ts());
        System.out.println("apiResponse.getHostname() = " + apiResponse.getHostname());

        return Boolean.TRUE.equals(apiResponse.isSuccess());
    }
}
