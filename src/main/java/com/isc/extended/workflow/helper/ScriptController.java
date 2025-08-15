package com.isc.extended.workflow.helper;

import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/scripts")
public class ScriptController {

    private final BeanShellRunner beanShellRunner;

    public ScriptController(BeanShellRunner beanShellRunner) {
        this.beanShellRunner = beanShellRunner;
    }

    @PostMapping
    public ScriptResponse executeScripts(@RequestBody ScriptRequest request) {
        try {
            if (!"beanshell".equalsIgnoreCase(request.getLanguage())) {
                return new ScriptResponse(
                        Instant.now(),
                        null,
                        HttpStatus.BAD_REQUEST.value(),
                        "Unsupported language: " + request.getLanguage(),
                        request.getLanguage()
                );
            }

            Object result = beanShellRunner.runScript(request.getScript());
            return new ScriptResponse(
                    Instant.now(),
                    result,
                    HttpStatus.OK.value(),
                    null,
                    request.getLanguage()
            );
        } catch (Exception e) {
            return new ScriptResponse(
                    Instant.now(),
                    null,
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    e.getMessage(),
                    request.getLanguage()
            );
        }
    }

}
