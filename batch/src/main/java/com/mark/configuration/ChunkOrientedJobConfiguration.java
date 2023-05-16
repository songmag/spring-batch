package com.mark.configuration;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.annotation.AfterWrite;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class ChunkOrientedJobConfiguration {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job chunkOrientedJob(
            Step chunkOrientedStep
    ) {
        return jobBuilderFactory.get("chunkOrientedJob")
                .incrementer(new RunIdIncrementer())
                .start(chunkOrientedStep)
                .build();
    }

    @Bean
    public Step chunkOrientedStep(
            ItemReader<String> itemReader,
            ItemProcessor<String, User> itemProcessor,
            ItemWriter<User> itemWriter
    ) {
        return stepBuilderFactory.get("chunkOrientedStep")
                .<String, User>chunk(2)
                .reader(itemReader)
                .processor(itemProcessor)
                .writer(itemWriter)
                .build();
    }

    @Bean
    public ItemReader<String> itemReader() {
        return new ItemReader<>() {
            String[] args = {"SONG,20", "MARK,33", "READER,50"};
            int index = 0;
            Object lock = new Object();

            @Override
            public String read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
                if (index >= args.length) {
                    return null;
                }
                var result = args[index];
                synchronized (lock) {
                    index += 1;
                }
                return result;
            }
        };
    }

    @Bean
    public ItemProcessor<String, User> itemProcessor() {
        return new ItemProcessor<String, User>() {
            @Override
            public User process(String item) throws Exception {
                String[] userInfo = item.split(",");
                return new User(userInfo[0], Integer.parseInt(userInfo[1]));
            }
        };
    }

    @Bean
    public ItemWriter<User> itemWriter() {
        return new ItemWriter<User>() {
            int count = 0;

            @Override
            public void write(List<? extends User> items) throws Exception {
                System.out.println(items);
            }

            @AfterWrite
            void afterWrite(List<? extends User> items) {
                System.out.println(count);
                count += 1;
            }
        };
    }

    @AllArgsConstructor
    @ToString
    public class User {
        String name;
        int age;
    }
}
