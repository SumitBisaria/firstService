package com.example.firstService.controller;

import com.example.firstService.service.FirstService;
import com.example.firstService.utils.ErrorUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.*;
import java.util.stream.IntStream;

/**
 * Controller class is used to provide operation related to fetch data.
 * @author SBisaria
 */
@RestController
@RequestMapping("/api")
public class FirstServiceController {

    private final FirstService firstService;

    private final ExecutorService executors = Executors.newCachedThreadPool();

    // Constructor injection
    public FirstServiceController(FirstService firstService) {
        this.firstService = firstService;
    }

    /**
     * Method is used to provide dummy data.
     * @return instance of {@link ResponseEntity<List>}
     * @author SBisaria
     */
    @GetMapping("/fetch")
    public ResponseEntity<List<String>> getData() {
        List<CompletableFuture<String>> futures = new ArrayList<>();
        IntStream.range(0, 10)
                .forEach(range -> futures.add(fetchDataInParallel(range)
                        .exceptionally(ex -> {
                            // Individually track the failure for each inputs.r
                            System.err.println(ErrorUtils.failureMessage(String.valueOf(range), Optional.ofNullable(ex)));
                            return ErrorUtils.failureMessage(String.valueOf(range), Optional.empty());
                        })));

        // Combined all futures(independent) and wait all the to complete in 5 secs.
        final CompletableFuture<Void> combinedFuture = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
        try {
            combinedFuture.get(5, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            System.err.println("Not all request finished within 5 seconds");
        } catch (Exception e) {
            System.err.println("Exception received " + e.getMessage());
        }

        final List<String> response = futures.stream().map(data -> data.getNow("Timeout")).toList();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Method is used to fetch data for each input parallel.
     * @param range instance of int.
     * @return instance of {@link CompletableFuture<String>}
     * @author SBisaria
     */
    private CompletableFuture<String> fetchDataInParallel(int range) {
        // Using cache thread pool API call's for each range are being maintained in parallel.
        return CompletableFuture.supplyAsync(() -> firstService.fetchDataFromServiceB(range), executors);
    }

    /**
     *Method is used to fetch data for each input in parallel within 5 secs.
     * @param range
     * @return
     */
    @Deprecated
    private CompletableFuture<String> fetchDataInParallelPerApiTimeout(int range) {
        return CompletableFuture.supplyAsync(() -> firstService.fetchDataFromServiceB(range), executors)
                .orTimeout(5, TimeUnit.SECONDS) // Sets time out for each api call.
                .exceptionally(ex -> "Time out for the range " + range + " " + ex.getMessage());
    }

}
