package com.mark.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@RequiredArgsConstructor
public class AlertJobConfiguration {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;


    @Bean
    public Job alertJob(
            JobExecutionListener jobAlertListener,
            Step taskletStep
    ) {
        return jobBuilderFactory.get("alertJob")
                .incrementer(new RunIdIncrementer())
                .listener(jobAlertListener)
                .start(taskletStep)
                .build();
    }

    @Bean
    public JobExecutionListener jobAlertListener() {
        return new JobExecutionListener() {
            @Override
            public void beforeJob(JobExecution jobExecution) {
                System.out.println("JOB START");

            }

            @Override
            public void afterJob(JobExecution jobExecution) {
                System.out.println("JOB FINISH");
            }
        };
    }

    @Bean
    public Step taskletStep() {
        return stepBuilderFactory.get("taskletStep")
                .tasklet((stepContribution, chunkContext) -> {
                    System.out.println("TASKLET START");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

}
