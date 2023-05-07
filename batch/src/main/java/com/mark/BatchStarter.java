package com.mark;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableBatchProcessing
@RequiredArgsConstructor
@SpringBootApplication
public class BatchStarter {
    public static void main(String[] args) {
        SpringApplication.exit(SpringApplication.run(BatchStarter.class, args));
    }
}

