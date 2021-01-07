package com.codeup.capstone3dprinting.services;

import com.codeup.capstone3dprinting.ReCaptchaResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class ReCaptchaValidationService {

    @Value("${recaptcha.checkbox.secret.key}")
    private String recaptchaCheckboxSecret;

    @Value("${recaptcha.invisible.secret.key}")
    private String recaptchaInvisibleSecret;

    @Value("${recaptcha.testing.secret.key}")
    private String recaptchaTestingSecret;

    private static final String recaptchaServerURL = "https://www.google.com/recaptcha/api/siteverify";

    public boolean validateCheckboxCaptcha(String captchaResponse) {

        RestTemplate restTemplate = new RestTemplate();

        MultiValueMap<String, String> requestMap = new LinkedMultiValueMap<>();
        requestMap.add("secret", recaptchaTestingSecret);
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

    public boolean validateInvisibleCaptcha(String captchaResponse) {
        RestTemplate restTemplate = new RestTemplate();

        MultiValueMap<String, String> requestMap = new LinkedMultiValueMap<>();
        requestMap.add("secret", recaptchaTestingSecret);
        requestMap.add("response", captchaResponse);

        ReCaptchaResponse apiResponseInvisible = restTemplate.postForObject(recaptchaServerURL, requestMap, ReCaptchaResponse.class);
        if (apiResponseInvisible == null) {
            return false;
        }

        System.out.println("apiResponseInvisible.isSuccess() = " + apiResponseInvisible.isSuccess());
        System.out.println("apiResponseInvisible.getChallenge_ts() = " + apiResponseInvisible.getChallenge_ts());
        System.out.println("apiResponseInvisible.getHostname() = " + apiResponseInvisible.getHostname());
        if (apiResponseInvisible.getErrorCodes() != null) {
            System.out.println("apiResponseInvisible.getErrorCodes()[0] = " + apiResponseInvisible.getErrorCodes()[0]);
        }

        return Boolean.TRUE.equals(apiResponseInvisible.isSuccess());
    }

}
