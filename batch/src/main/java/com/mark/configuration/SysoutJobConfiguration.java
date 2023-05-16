package com.mark.configuration;

import com.mark.incrementer.TimeIncrementer;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class SysoutJobConfiguration {

    private static final String SYSOUT_JOB = "sysoutJob";
    private static final String SYSOUT_STEP = "sysoutStep";
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean(SYSOUT_JOB)
    public Job sysoutJob(
            TimeIncrementer timeIncrementer,
            Step sysoutStep
    ) {
        return jobBuilderFactory.get(SYSOUT_JOB)
                .incrementer(timeIncrementer)
                .start(sysoutStep)
                .build();
    }

    @Bean(SYSOUT_STEP)
    public Step sysoutStep() {
        return stepBuilderFactory.get(SYSOUT_STEP)
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("Hello World");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }
}
