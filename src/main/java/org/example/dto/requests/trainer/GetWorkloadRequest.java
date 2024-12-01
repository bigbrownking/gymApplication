package org.example.dto.requests.trainer;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class GetWorkloadRequest {
    private String username;
    private String firstName;
    private String lastName;
    private boolean isActive;
    private LocalDateTime trainingDate;
    private double duration;
    private String action;
}