package com.microservices.loggingservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


@RestController
public class LoggingController {

    Logger logger = LoggerFactory.getLogger(LoggingController.class);

    private final LoggingService loggingService;

    public LoggingController(LoggingService loggingService) {
        this.loggingService = loggingService;
    }


    @GetMapping("/log")
    public String listLog() {
        Map<String, String> messages = loggingService.log();
        return messages.values().toString();
    }


    @PostMapping("/log")
    public ResponseEntity<Void> log(@RequestBody Map<String, Object> msg) {

        final String id = (String) msg.get("UUID");
        logger.info("UUID: " + id);
        final String text = (String) msg.get("Text");
        logger.info("Text: " + text);

        loggingService.addToLog(id, text);
        return ResponseEntity.ok().build();
    }
}
