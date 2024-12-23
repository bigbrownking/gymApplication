Feature: Create Trainer
  As a user
  I want to register a new trainer
  So that I can see the trainer in the system


  Scenario: Create a new trainer successfully
    Given I have valid "trainer" details
    When I call the "registerTrainer" service
    Then the trainer should be created with a generate "username" and "password"
