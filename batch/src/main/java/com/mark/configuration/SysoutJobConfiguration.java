package com.mark.configuration;

import com.mark.incrementer.TimeIncrementer;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@RequiredArgsConstructor
public class SysoutJobConfiguration {

    private static final String SYSOUT_JOB = "sysoutJob";
    private static final String SYSOUT_STEP = "sysoutStep";
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean(value = "sysoutTaskExecutor")
    public TaskExecutor threadPoolExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(4);
        executor.setMaxPoolSize(4);
        return executor;
    }

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
    public Step sysoutStep(@Qualifier("sysoutTaskExecutor") TaskExecutor executor) {
        return stepBuilderFactory.get(SYSOUT_STEP)
                .tasklet((contribution, chunkContext) -> {
                    System.out.println(chunkContext.getStepContext().getJobName());
                    System.out.println(chunkContext.getStepContext().getJobParameters());
                    return RepeatStatus.FINISHED;
                })
                .taskExecutor(executor)
                .build();
    }
}
