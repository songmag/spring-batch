package com.mark.configuration;

import com.mark.incrementer.TimeIncrementer;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class TimeJobConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job timeJob(
            Step timeStep,
            TimeIncrementer incrementer
    ) {
        return jobBuilderFactory
                .get("timeJob")
                .incrementer(incrementer)
                .start(timeStep)
                .build();
    }

    @Bean
    @JobScope
    public Step timeStep(
            @Value("#{jobParameters[today]}") String time
    ) {
        return stepBuilderFactory
                .get("timeStep")
                .tasklet(
                        (stepContribution, chunkContext) -> {
                            System.out.println(time);
                            Thread.sleep(10000);
                            if(time.equals("2023-05-15")) {
                                throw new RuntimeException("JOB_FAIL");
                            }
                            return RepeatStatus.FINISHED;
                        }
                )
                .build();
    }
}
