package org.example;

import org.example.config.ApplicationConfig;
import org.example.config.StorageConfig;
import org.example.facade.TrainingFacade;
import org.example.models.Trainer;
import org.example.models.TrainingType;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

public class Main {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(StorageConfig.class, ApplicationConfig.class);

        TrainingFacade facade = context.getBean(TrainingFacade.class);

       // System.out.println(facade.getAllTrainers());


        //checking username and password generation
       // System.out.println("------------------------------------------------");
       /* Trainer trainer = new Trainer("David", "Brown", TrainingType.CARDIO.getDisplayName());
        Trainer trainer2 = new Trainer("David", "Brown", TrainingType.CARDIO.getDisplayName());
        facade.createTrainer(trainer);
        facade.createTrainer(trainer2);

        System.out.println(trainer);
        System.out.println(trainer2);*/



        //checking delete operation on trainees

        System.out.println(facade.getAllTrainings());
/*
        System.out.println("-------------------------");
        System.out.println(facade.getAllTrainees());
        facade.deleteTrainee(2L);
        System.out.println("-------------------------");
        System.out.println(facade.getAllTrainees());
        System.out.println("-------------------------");
        System.out.println(facade.getAllTrainings());
*/
        context.close();

    }
}