package org.example.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Trainer extends User{
    private String specialization;

    public Trainer(String firstName,
                   String lastName,
                   String specialization) {
        super(firstName, lastName);
        this.specialization = specialization;
    }

    @Override
    public String toString() {
        return "Trainer{" +
                "firstName=" + getFirstName() +
                ", lastName=" + getLastName() +
                ", userName=" + getUsername() +
                ", password=" + getPassword() +
                ", isActive=" + isActive() +
                ", userId=" + getUserId() +
                ", specialization='" + specialization + '\'' +
                '}';
    }
}
