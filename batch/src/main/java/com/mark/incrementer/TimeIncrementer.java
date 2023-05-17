package com.mark.incrementer;

import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersIncrementer;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
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
        String time = param.get("time") == null ? null : String.valueOf(param.get("time"));

        LocalDate today = (time == null || time.isBlank()) ? LocalDate.now() : LocalDate.from(DateTimeFormatter.ISO_DATE.parse(time));

        param.put("time", new JobParameter(DateTimeFormatter.ISO_DATE.format(today)));

        return new JobParameters(param);
    }
}
