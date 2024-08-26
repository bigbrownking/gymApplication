package org.example.util;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.experimental.UtilityClass;
import org.springframework.core.io.Resource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.Map;

@UtilityClass
public class SettingsDataUtil {
    private final Gson gson = new Gson();

    public <T> Map<Long, T> readJsonToMap(Resource resource, Class<T> clazz) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
            Type mapType = TypeToken.getParameterized(Map.class, Long.class, clazz).getType();
            return gson.fromJson(reader, mapType);
        }
    }
}