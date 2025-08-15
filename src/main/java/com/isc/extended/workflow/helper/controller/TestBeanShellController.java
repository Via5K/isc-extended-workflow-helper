package com.isc.extended.workflow.helper.controller;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import bsh.Interpreter;

@RestController
@RequestMapping("/test/beanshell")
public class TestBeanShellController {

    private static final Logger logger = LoggerFactory.getLogger(TestBeanShellController.class);

    @PostMapping("/run")
    public Map<String, Object> runScript(@RequestBody Map<String, String> payload) {
        String script = payload.getOrDefault("code", "");
        logger.info("Received BeanShell script to execute:\n{}", script);

        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", Instant.now().toString());
        response.put("language", "beanshell");

        try {
            Interpreter interpreter = new Interpreter();
            Object result = interpreter.eval(script);

            response.put("data", result != null ? result.toString() : "null");

            logger.info("Execution result: {}", result);
        } catch (Exception e) {
            response.put("data", "Error: " + e.getMessage());
            logger.error("Script execution failed", e);
        }

        return response;
    }
}
