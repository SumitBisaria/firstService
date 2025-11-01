package com.example.firstService.service;

import java.util.concurrent.ExecutionException;

/**
 * Service is used to provide abstraction for data fetch related task.
 * @author SBisaria
 */
public interface FirstService {

    String fetchDataFromServiceB(int range) ;

}
