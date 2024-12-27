Feature: Trainee registration
  As a user
  I want to register a new trainee
  So that the trainee profile is created successfully

  Scenario: Register trainee successfully
    Given I have valid trainee details
    When I call the "registerTrainee" endpoint with the trainee details
    Then I should receive a "201 Created" response
    And the trainee should be registered with the provided details

  Scenario: Fail to register trainee due to internal server error
    Given I have valid trainee details
    When I call the "registerTrainee" endpoint and the service encounters an error
    Then I should receive a "500 Internal Server Error" response
