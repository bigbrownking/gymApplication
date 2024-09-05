package org.example.models;

import lombok.Getter;

@Getter
public enum TrainingType {
    CARDIO("Cardio"),
    STRENGTH("Strength"),
    FLEXIBILITY("Flexibility");

    private final String displayName;

    TrainingType(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
