package com.mark.incrementer;

import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersIncrementer;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class TimeIncrementer implements JobParametersIncrementer {
    @Override
    public JobParameters getNext(JobParameters parameters) {
        Map<String, JobParameter> param = new HashMap<>(
                Optional.of(parameters.getParameters())
                        .orElseGet(HashMap::new)
        );

        String time = String.valueOf(param.get("jobRunTime"));
        LocalDateTime localDateTime;

        if (time != null) {
            localDateTime = LocalDateTime.from(DateTimeFormatter.ISO_DATE_TIME.parse(time)).plusDays(1);
        } else {
            localDateTime = LocalDateTime.now();
        }

        param.put("jobRunTime", new JobParameter(localDateTime.format(DateTimeFormatter.ISO_DATE_TIME)));
        return new JobParameters(param);
    }
}
