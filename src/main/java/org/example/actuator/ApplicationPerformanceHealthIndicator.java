package org.example.actuator;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import io.micrometer.core.instrument.Gauge;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.util.concurrent.TimeUnit;

@Component
public class ApplicationPerformanceHealthIndicator implements HealthIndicator {
    private static final long MAX_MEMORY_USAGE = 500 * 1024 * 1024;
    private static final long MAX_RESPONSE_TIME_THRESHOLD_MS = 2000;
    private static final double MAX_ERROR_RATE_THRESHOLD = 0.05;
    private final Timer responseTimer;
    private final Gauge memoryUsageGauge;
    private final MeterRegistry meterRegistry;

    private long simulatedResponseTime = 1500;
    private double simulatedErrorRate = 0.02;

    public ApplicationPerformanceHealthIndicator(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;

        this.responseTimer = meterRegistry.timer("application.performance.response.time");
        this.memoryUsageGauge = Gauge.builder("application.performance.memory.usage", this::getCurrentMemoryUsage).
                register(meterRegistry);
    }

    @Override
    public Health health() {
        responseTimer.record(simulatedResponseTime, TimeUnit.MILLISECONDS);

        if(isMemoryUsageHigh()){
            return Health.down()
                    .withDetail("reason", "High memory usage")
                    .withDetail("memoryUsed", getCurrentMemoryUsage() + "MB")
                    .build();
        }
        if (isResponseTimeTooHigh()) {
            return Health.down()
                    .withDetail("reason", "Response time exceeds threshold")
                    .withDetail("responseTime", simulatedResponseTime + " ms")
                    .build();
        }
        if (isErrorRateTooHigh()) {
            return Health.down()
                    .withDetail("reason", "High error rate")
                    .withDetail("errorRate", simulatedErrorRate * 100 + " %")
                    .build();
        }
        return Health.up().build();

    }
    private boolean isMemoryUsageHigh() {
        long memoryUsed = getCurrentMemoryUsage();
        return memoryUsed > (MAX_MEMORY_USAGE / (1024 * 1024));
    }

    private boolean isResponseTimeTooHigh() {
        return simulatedResponseTime > MAX_RESPONSE_TIME_THRESHOLD_MS;
    }

    private boolean isErrorRateTooHigh() {
        return simulatedErrorRate > MAX_ERROR_RATE_THRESHOLD;
    }

    private long getCurrentMemoryUsage() {
        MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
        MemoryUsage heapUsage = memoryBean.getHeapMemoryUsage();
        return heapUsage.getUsed() / (1024 * 1024);
    }
}
