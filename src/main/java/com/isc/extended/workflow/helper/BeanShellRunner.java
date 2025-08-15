package com.isc.extended.workflow.helper;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.springframework.stereotype.Component;

import bsh.Interpreter;

@Component
public class BeanShellRunner {

    public Object runScript(String script) throws Exception {
        Interpreter interpreter = new Interpreter();
 

        // Capture System.out
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(baos, true));

        try {
            Object result = interpreter.eval(script);

            if (result != null) {
                // If the script explicitly returned something, return it directly
                return result;
            } else {
                // If no return, fallback to captured System.out
                String printedOutput = baos.toString();
                return printedOutput.isEmpty() ? null : printedOutput.trim();
            }

        } finally {
            // Restore original System.out
            System.setOut(originalOut);
        }
    }
}
