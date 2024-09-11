package org.example;

import org.example.config.ApplicationConfig;

import org.example.config.NewConfig;
import org.example.facade.TrainingFacade;
import org.example.models.Trainee;
import org.example.models.Trainer;
import org.example.models.Training;
import org.example.models.TrainingTypeEntity;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

public class Main {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ApplicationConfig.class,
                NewConfig.class);

        TrainingFacade facade = context.getBean(TrainingFacade.class);

        //check for adding trainee

       /* Calendar calendar = Calendar.getInstance();
        calendar.set(2005, Calendar.APRIL, 11, 0, 0, 0); // year, month (0-based), day, hour, minute, second
        Date dateOfBirth = calendar.getTime();



        Trainee trainee = new Trainee();
        trainee.setFirstName("Alisher");
        trainee.setLastName("Khairullin");
        trainee.setDateOfBirth(dateOfBirth);
        trainee.setAddress("Wall Street");
       facade.addTrainee(trainee);
       System.out.println(facade.getAllTrainees());*/


        //check for getting trainee By username
        //Assert.isTrue(facade.getByUsernameTrainee("Alisher.Khairullin").getUsername().equals("Alisher.Khairullin"), "They are not equal!");


        //check for getting trainee By username and password
      /*  Trainee trainee = facade.getByUsernameAndPassword("Alisher.Khairullin", "kCwvKnf090");
        Assert.isTrue(trainee.getUsername().equals("Alisher.Khairullin") && trainee.getPassword().equals("kCwvKnf090"),"It is not him");*/

        //check for update trainee
        /*Trainee trainee = facade.getByUsernameTrainee("Alisher.Khairullin");
        facade.updatePasswordForTrainee(trainee, "newPassword");
        Assert.isTrue(trainee.getPassword().equals("newPassword"), "They are not equal");
        System.out.println(trainee);*/


        //checking delete operation on trainees


        //activate/deactivate
        Trainee trainee = facade.getByUsernameTrainee("emily.davis");
        facade.deactivateTrainee(trainee.getUsername());
        //facade.activateTrainee(trainee.getUsername());


      /*  Trainer trainer = new Trainer();
        trainer.setFirstName("Alisher");
        trainer.setLastName("Khairullin");
        trainer.setSpecialization(new TrainingTypeEntity("flex"));
        facade.createTrainer(trainer);*/
/*
        Training training = new Training();
        Trainee trainee = facade.getByUsernameTrainee("Alisher.Khairullin2");
        Trainer trainer = facade.getByUsernameTrainer("Alisher.Khairullin");
        TrainingTypeEntity trainingType = new TrainingTypeEntity("flex");

        training.setTrainingDate(new Date());
        training.setTrainingName("New Cardio");
        training.setTrainingDuration(90);
        training.setTrainer(trainer);
        training.setTrainee(trainee);
        training.setTrainingType(trainingType);

        facade.createTraining(training);
*/
        //System.out.println(facade.getAllTrainings());
/*



        System.out.println("-------------------------");
        System.out.println(facade.getAllTrainees());
        facade.deleteTrainee(2L);
        System.out.println("-------------------------");
        System.out.println(facade.getAllTrainees());
        System.out.println("-------------------------");
        System.out.println(facade.getAllTrainings());
*/
/*
        String trainerName = "Alisher.Khairullin";
        String traineeName = "Alisher.Khairullin2";
        String dateStr = "2024-09-11 15:04:38.139000";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");
        LocalDateTime givenDate = LocalDateTime.parse(dateStr, formatter);
        LocalDateTime dateBefore = givenDate.minusDays(5);
        LocalDateTime dateAfter = givenDate.plusDays(5);

        Date startDate = Date.from(dateBefore.atZone(ZoneId.systemDefault()).toInstant());
        Date endDate = Date.from(dateAfter.atZone(ZoneId.systemDefault()).toInstant());

        TrainingTypeEntity trainingType = facade.getTrainingType("flex");
        System.out.println("LIST IS : "+facade.getTrainingTrainerListByCriteria(trainerName, null, endDate, traineeName));
        context.close();
*/

       /*String trainee = "jane.smith";

       facade.deleteTrainee(trainee);
*/
    }
}