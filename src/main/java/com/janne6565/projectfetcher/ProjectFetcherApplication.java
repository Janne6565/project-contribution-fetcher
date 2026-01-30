package com.janne6565.projectfetcher;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ProjectFetcherApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProjectFetcherApplication.class, args);
    }

}
