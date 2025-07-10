package com.example.journalproject.service;


import com.example.journalproject.api.response.WeatherResponse;
import com.example.journalproject.cache.AppCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@Slf4j
public class WeatherService {

    @Value("${weather.api.key}")
    private String apiKey ;
    @Autowired  // API KEY : LOOK AT THE NOTES : FROM APP CACHE : We have stored the API KEY in DATABASE !
    private AppCache appCache ;


    @Autowired
    private RestTemplate restTemplate ; // 1)


    // 1) GET FUNCTION
    public WeatherResponse getWeather(String city) {
        try {
            String finalApi = appCache.APP_CACHE.get("weather_api").replace("<city>",city).replace("<apiKey>",apiKey) ;
            // restTemplate.exchange(finalApi, HttpMethod.GET, null, WeatherResponse.class) ; // 2)
            ResponseEntity<WeatherResponse> response = restTemplate.exchange(finalApi, HttpMethod.GET, null, WeatherResponse.class) ; // 2)
            if(response.getStatusCode().is2xxSuccessful()) {
                return response.getBody() ;
            } else {
                throw new RuntimeException("Failed to fetch : "+ response.getStatusCode() );
            }
        } catch (Exception e) {
            log.error("FAILED TO FETCH : {}", e.getMessage());
            throw new RuntimeException("Failed to fetch : "+ e) ;
        }
    }



    // 2) POST FUNCTION
    public void postFunction() {
        // (i) API LINK : FROM APP CACHE LOOK AT THE NOTES
        // String finalApi = API.replace("CITY", "Chandigarh").replace("API_KEY", apiKey) ;
        String finalApi = appCache.APP_CACHE.get("weather_api").replace("<city>", "Chandigarh").replace("<apiKey>", apiKey) ;

        // (ii) REQUEST BODY
        String requestBody = "{\n" +
                "  \"username\": \"Joel\",\n" +
                "  \"password\": \"123\"\n" +
                "}" ;

        // (iii) HEADER
        HttpHeaders headers = new HttpHeaders() ;
        headers.setContentType(MediaType.APPLICATION_JSON) ;                // a) application/json
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);      // b) application/x-www-form-urlencoded
                                                                            // From 'a' and 'b' we can choose any one

        // (iv) Creation of PAYLOAD with HEADER
        HttpEntity<String> httpEntity = new HttpEntity<>(requestBody, headers) ;

        ResponseEntity<?> response = restTemplate.exchange(finalApi, HttpMethod.POST, httpEntity , WeatherResponse.class) ;
        return ;
    }
    // 3) PUT FUNCTION : SAME AS POST (just change the HttpMethod from POST to PUT)
    public void putFunction() {
        //         : FROM APP CACHE LOOK AT THE NOTES
        //        String finalApi = API.replace("CITY", "Chandigarh").replace("API_KEY", apiKey) ;
        String finalApi = appCache.APP_CACHE.get("weather_api").replace("<city>", "Chandigarh").replace("<apiKey>", apiKey) ;
        String requestBody = "{\n" +
                "  \"username\": \"Joel\",\n" +
                "  \"password\": \"123\"\n" +
                "}" ;
        HttpHeaders headers = new HttpHeaders() ;
        headers.setContentType(MediaType.APPLICATION_JSON) ;
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<String> httpEntity = new HttpEntity<>(requestBody, headers) ;

        ResponseEntity<?> response = restTemplate.exchange(finalApi, HttpMethod.PUT, httpEntity , WeatherResponse.class) ;
        return ;
    }
    // 4) PATCH FUNCTION : SAME AS POST (just change the HttpMethod from POST to PATCH)
    public void patchFunction() {
        //         : FROM APP CACHE LOOK AT THE NOTES
        //        String finalApi = API.replace("CITY", "Chandigarh").replace("API_KEY", apiKey) ;
        String finalApi = appCache.APP_CACHE.get("weather_api").replace("<city>", "Chandigarh").replace("<apiKey>", apiKey) ;
        String requestBody = "{\n" +
                "  \"username\": \"Joel\",\n" +
                "  \"password\": \"123\"\n" +
                "}" ;
        HttpHeaders headers = new HttpHeaders() ;
        headers.setContentType(MediaType.APPLICATION_JSON) ;
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<String> httpEntity = new HttpEntity<>(requestBody, headers) ;

        ResponseEntity<?> response = restTemplate.exchange(finalApi, HttpMethod.PATCH, httpEntity , WeatherResponse.class) ;
        return ;
    }
    // 5) DELETE FUNCTION : SAME AS POST (just change the HttpMethod from POST to DELETE)
    // NOTE: Delete will take NO-BODY !
    public void deleteFunction() {
        //         : FROM APP CACHE LOOK AT THE NOTES
        //        String finalApi = API.replace("CITY", "Chandigarh").replace("API_KEY", apiKey) ;
        String finalApi = appCache.APP_CACHE.get("weather_api").replace("<city>", "Chandigarh").replace("<apiKey>", apiKey) ;
        HttpHeaders headers = new HttpHeaders() ;
        headers.setContentType(MediaType.APPLICATION_JSON) ;
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<String> httpEntity = new HttpEntity<>(headers) ; // No-body

        ResponseEntity<?> response = restTemplate.exchange(finalApi, HttpMethod.PUT, httpEntity , WeatherResponse.class) ;
        return ;
    }

}


// 1) Rest Template is a by default class in Spring Boot that helps us process Http Requests and helps in bringing us Response.
// 2) Takes 4 parameters :
//         a) The url of the API.
//         b) The method.
//         c) Header : which includes application/json or application/x-www-form-urlencoded
//         d) POJO class (Json object to Plain Old Java Object).
//              MOTIVE : of this pojo class : To convert the Json DESERIALIZE : JSON --> JAVA OBJECT