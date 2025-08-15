package com.isc.extended.workflow.helper;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/escape")
public class ScriptEscapeController {

    @PostMapping
    public Map<String, Object> escapeJavaCode(@RequestBody String code) {
        Map<String, Object> response = new HashMap<>();
        response.put("escaped", code);
        response.put("length",code.length());

        return response;
    }
}
