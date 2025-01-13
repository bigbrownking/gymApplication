Feature: Delete trainee
  As a user
  I want to delete a trainee
  So that the trainee profile is removed from the system

  Scenario: Delete trainee successfully
    Given I have "valid trainee delete request"
    When delete trainee
    Then the trainee should be deleted from the system

  Scenario: Delete trainee that not exist
    Given I have "valid trainee delete request"
    When delete trainee
    And trainee doesn't exist
    Then delete operation should throw EntityNotFoundException
