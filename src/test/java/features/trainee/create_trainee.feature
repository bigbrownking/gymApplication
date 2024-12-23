Feature: Create Trainee
  As a user
  I want to register a new trainee
  So that I can see the trainee in the system


  Scenario: Create a new trainee successfully
    Given I have valid "trainee" details
    When I call the "registerTrainee" service
    Then the trainee should be created with a generated "username" and "password"
