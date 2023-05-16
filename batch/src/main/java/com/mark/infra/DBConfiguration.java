package com.mark.infra;

import org.springframework.batch.core.configuration.annotation.BatchConfigurer;
import org.springframework.boot.autoconfigure.batch.BasicBatchConfigurer;
import org.springframework.boot.autoconfigure.batch.BatchProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionManager;

import javax.sql.DataSource;
import javax.xml.crypto.Data;

@Configuration
public class DBConfiguration {

    @Bean
    @ConfigurationProperties("spring.datasource")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    public PlatformTransactionManager platformTransactionManager(
            DataSource dataSource
    ) {
        return new JdbcTransactionManager(dataSource);
    }

    @Bean
    public BatchConfigurer batchConfigurer(
            BatchProperties properties,
            DataSource dataSource,
            PlatformTransactionManager platformTransactionManager
    ) {
        return new BasicBatchConfigurer(properties, dataSource, null) {
            @Override
            public PlatformTransactionManager getTransactionManager() {
                return platformTransactionManager;
            }
        };
    }

}
