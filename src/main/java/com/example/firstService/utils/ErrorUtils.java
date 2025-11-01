package com.example.firstService.utils;

import java.util.Optional;

/**
 * Utility class is used to prepare error message based on inputs.
 * @author SBisaria
 */
public class ErrorUtils {

    public static String failureMessage(String input, Optional<Throwable> exOpt) {
        return "Timeout received for input " + input +
                exOpt.map(ex -> " " + ex.getMessage()).orElse("");
    }

}
