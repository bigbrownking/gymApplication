Feature: Create Training
  As a user
  I want to register a new training
  So that I can see the training in the system


  Scenario: Create a new training successfully
    Given I have valid "training" details
    When I call the "registerTraining" service
    Then the training should be successfully created
