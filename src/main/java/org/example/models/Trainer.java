package org.example.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import jakarta.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "trainers")
public class Trainer extends User{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long trainerId;

    @OneToOne
    @JoinColumn(name = "specialization_id")
    private TrainingTypeEntity specialization;

    @OneToMany(mappedBy = "trainer", fetch = FetchType.EAGER)
    private List<Training> trainings;

    public Trainer(String firstName,
                   String lastName,
                   TrainingTypeEntity specialization) {
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
                ", userId=" + getTrainerId() +
                ", specialization='" + specialization + '\'' +
                '}';
    }
}
