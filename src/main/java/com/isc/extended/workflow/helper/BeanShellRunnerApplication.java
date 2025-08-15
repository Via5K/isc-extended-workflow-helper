// package com.isc.extended.workflow.helper;

// import org.springframework.boot.SpringApplication;
// import org.springframework.boot.autoconfigure.SpringBootApplication;

// @SpringBootApplication
// public class BeanShellRunnerApplication {
//     public static void main(String[] args) {
//         SpringApplication.run(BeanShellRunnerApplication.class, args);
//     }
// }
package com.isc.extended.workflow.helper;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BeanShellRunnerApplication {

    public static void main(String[] args) {
        SpringApplication.run(BeanShellRunnerApplication.class, args);
    }

    @Bean
    CommandLineRunner runOnStart(ApplicationContext ctx) {
        return args -> {
            BeanShellRunner runner = ctx.getBean(BeanShellRunner.class);
            runner.runScript("System.out.println(\"Hello from BeanShell!\");");
        };
    }
}
