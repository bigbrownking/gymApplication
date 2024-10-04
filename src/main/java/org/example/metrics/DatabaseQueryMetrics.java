package org.example.metrics;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Component;

import java.util.concurrent.Callable;


@Component
public class DatabaseQueryMetrics {
    private final Timer queryTimer;

    public DatabaseQueryMetrics(MeterRegistry meterRegistry) {
        this.queryTimer = meterRegistry.timer("database.query.latency", "query", "fetch");
    }
    public void trackQueryDuration(Runnable queryLogic) {
        queryTimer.record(queryLogic);
    }

    public <T> T trackQueryDuration(Callable<T> queryLogic) throws Exception {
        return queryTimer.recordCallable(queryLogic);
    }
}
