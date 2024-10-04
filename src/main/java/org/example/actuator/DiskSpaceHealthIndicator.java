package org.example.actuator;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class DiskSpaceHealthIndicator implements HealthIndicator {
    private static final long DISK_SPACE_THRESHOLD_MB = 100;

    @Override
    public Health health() {
        File disk = new File("/");
        long freeSpaceBytes = disk.getFreeSpace();
        long freeSpaceMB = freeSpaceBytes / (1024 * 1024);

        if (freeSpaceMB >= DISK_SPACE_THRESHOLD_MB) {
            return Health.up()
                    .withDetail("free space", freeSpaceMB + " MB")
                    .build();
        } else {
            return Health.down()
                    .withDetail("error", "Low disk space: " + freeSpaceMB + " MB")
                    .build();
        }
    }
}
