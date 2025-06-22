package org.alumni.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class TestController {

    // This injects the value of the MongoDB URI property currently being used by the application.
    @Value("${spring.data.mongodb.uri}")
    private String mongoUri;

    /**
     * A public endpoint to check the application's current MongoDB URI configuration.
     */
    @GetMapping("/api/config-check")
    public ResponseEntity<Map<String, String>> getConfig() {
        Map<String, String> config = new HashMap<>();
        config.put("active.mongodb.uri", mongoUri);
        return ResponseEntity.ok(config);
    }
}