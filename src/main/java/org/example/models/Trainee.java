package org.example.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "trainees")
public class Trainee extends User{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long traineeId;

    @Column(name = "date_of_birth")
    private LocalDateTime dateOfBirth;

    @Column(name = "address")
    private String address;

    @OneToMany(mappedBy = "trainee")
    private List<Training> trainings;


    public Trainee(String firstName,
                   String lastName,
                   LocalDateTime dateOfBirth, String address) {
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
                ", userId=" + getTraineeId() +
                ", dateOfBirth='" + dateOfBirth + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
