package org.example.service;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class LoginAttemptService {

    private final Map<String, Integer> attempts = new HashMap<>();
    private final Map<String, LocalDateTime> lockoutTime = new HashMap<>();
    private final int MAX_ATTEMPTS = 3;
    private final long LOCK_TIME_DURATION = 5;

    public void loginSucceeded(String username) {
        attempts.remove(username);
        lockoutTime.remove(username);
    }

    public void loginFailed(String username) {
        int currentAttempts = attempts.getOrDefault(username, 0) + 1;
        attempts.put(username, currentAttempts);

        if (currentAttempts >= MAX_ATTEMPTS) {
            blockUser(username);
        }
    }

    public boolean isBlocked(String username) {
        if (lockoutTime.containsKey(username)) {
            LocalDateTime lockoutUntil = lockoutTime.get(username);
            if (lockoutUntil.isAfter(LocalDateTime.now())) {
                return true;
            } else {
                lockoutTime.remove(username);
                attempts.remove(username);
            }
        }
        return false;
    }

    public void blockUser(String username) {
        lockoutTime.put(username, LocalDateTime.now().plusMinutes(LOCK_TIME_DURATION));
    }

    public int getAttempts(String username) {
        return attempts.getOrDefault(username, 0);
    }
}
