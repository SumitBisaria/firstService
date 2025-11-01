package com.example.firstService.service.impl;

import com.example.firstService.service.FirstService;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * Service implementation class is used to provide definition of each abstraction.
 * @author SBisaria
 */
@Service
public class FirstServiceImpl implements FirstService {

    private final RestTemplate restTemplate;

    // Constructor injection.
    public FirstServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Method is used to fetch records received from another independent service.
     *
     * @param range instance of int
     * @return response represents data received from service call.
     */
    @Override
    public String fetchDataFromServiceB(int range) {
        String response = "";
        Instant start = Instant.now();
        try {
            response = restTemplate.getForObject("http://localhost:8081/api/getRecords", String.class);

            System.out.println("Response received "+ response);
        } catch (Exception e) {
            System.err.println("Exception received.");
            throw new RuntimeException("");
        }
        Instant end = Instant.now();
        long millis = Duration.between(start, end).toMillis();
        System.out.println("Total time taken for calling service B for range " + range + " is " + millis + " millis");
        return response;
    }
}
