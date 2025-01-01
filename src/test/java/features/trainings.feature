Feature: Training list
  As a user
  I want to retrieve the list of trainings assigned to a trainee
  So that I can track their training progress

  Scenario: Retrieve training list successfully
    Given I have "valid trainee trainings request"
    When trainings "trainee"
    Then the response should contain trainings list of "trainee" matching the criteria

  Scenario: Successfully retrieve trainings by criteria
    Given I have "valid trainer trainings request"
    When trainings "trainer"
    Then the response should contain trainings list of "trainer" matching the criteria
