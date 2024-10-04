package org.example.util;

import lombok.experimental.UtilityClass;
import org.apache.commons.text.RandomStringGenerator;

import java.util.Collections;
import java.util.List;

@UtilityClass
public class Generator {

    public String generateUsername(String firstName, String lastName, List<String> existingUsernames) {
        if (existingUsernames == null) {
            existingUsernames = Collections.emptyList();
        }
        String baseUsername = firstName + "." + lastName;
        String username = baseUsername;
        int counter = 1;
        while (existingUsernames.contains(username)) {
            username = baseUsername + counter++;
        }
        return username;
    }
    public String generatePassword() {
        return new RandomStringGenerator.Builder()
                .withinRange('0', 'z')
                .get().generate(10);
    }
}
