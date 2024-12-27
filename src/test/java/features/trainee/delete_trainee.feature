Feature: Delete trainee
  As a user
  I want to delete a trainee
  So that the trainee profile is removed from the system

  Scenario: Delete trainee successfully
    Given I have a valid trainee ID
    When I call the "deleteTrainee" endpoint with the ID
    Then I should receive a "204 No Content" response
    And the trainee should be deleted from the system

  Scenario: Fail to delete trainee due to internal server error
    Given I have a valid trainee ID
    When I call the "deleteTrainee" endpoint and the service encounters an error
    Then I should receive a "500 Internal Server Error" response
