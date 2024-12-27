Feature: Activate trainee
  As a user
  I want to activate a trainee account
  So that the trainee can access the system

  Scenario: Activate trainee successfully
    Given I have a valid trainee activation request
    When I call the "activateTrainee" endpoint with the request
    Then I should receive a "200 OK" response
    And the trainee account should be activated

  Scenario: Fail to activate trainee due to internal server error
    Given I have a valid trainee activation request
    When I call the "activateTrainee" endpoint and the service encounters an error
    Then I should receive a "500 Internal Server Error" response
