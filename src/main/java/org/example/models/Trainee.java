package org.example.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Trainee extends User{
    private String dateOfBirth;
    private String address;

    public Trainee(String firstName,
                   String lastName,
                   String dateOfBirth, String address) {
        super(firstName, lastName);
        this.dateOfBirth = dateOfBirth;
        this.address = address;
    }

    @Override
    public String toString() {
        return "Trainee{" +
                "firstName=" + getFirstName() +
                ", lastName=" + getLastName() +
                ", userName=" + getUsername() +
                ", password=" + getPassword() +
                ", isActive=" + isActive() +
                ", userId=" + getUserId() +
                ", dateOfBirth='" + dateOfBirth + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
