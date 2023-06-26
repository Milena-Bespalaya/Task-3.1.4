package org.example;

import org.example.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class Logic {
    private static final String URL = "http://94.198.50.185:7081/api/users";
    RestTemplate restTemplate;
    ResponseEntity<User> userResponseEntity;
    HttpHeaders httpHeaders;
    public Logic(){

    }

    @Autowired
    public Logic(RestTemplate restTemplate, HttpHeaders httpHeaders) {
        this.restTemplate = restTemplate;
        this.httpHeaders = httpHeaders;
        this.httpHeaders.set("Cookie",
                String.join(",", restTemplate.headForHeaders(URL).get("Set-Cookie")));
    }
    public String work(){
        return addUser().getBody()+updateUser().getBody()+deleteUser().getBody();

    }

    private List<User> getAllUsers() {
        ResponseEntity<List<User>> response = restTemplate.exchange(URL, HttpMethod.GET, null, new ParameterizedTypeReference<List<User>>() {});
        System.out.println(response.getHeaders());
        return response.getBody();
    }

    private ResponseEntity<String> addUser(){
        User user = new User(3L, "James", "Brown", (byte) 15);
        HttpEntity<User> entity = new HttpEntity<>(user, httpHeaders);
        return restTemplate.postForEntity(URL, entity, String.class);
    }

    private ResponseEntity<String> updateUser(){
        User user = new User(3L, "Thomas", "Shelby", (byte) 5);
        HttpEntity<User> entity = new HttpEntity<>(user, httpHeaders);
        return restTemplate.exchange(URL, HttpMethod.PUT, entity, String.class, 3);
    }

    private ResponseEntity<String> deleteUser(){
        Map<String, Long> uriVariables = new HashMap<String, Long>() {{
            put("id", 3L);
        }};
        HttpEntity<String> entity = new HttpEntity<>(null, httpHeaders);
        return restTemplate.exchange(URL + "/{id}", HttpMethod.DELETE, entity, String.class, uriVariables);
    }




}
