Feature: Deactivate trainee
  As a user
  I want to deactivate a trainee account
  So that the trainee loses access to the system

  Scenario: Deactivate trainee successfully
    Given I have a valid trainee deactivation request
    When I call the "deactivateTrainee" endpoint with the request
    Then I should receive a "200 OK" response
    And the trainee account should be deactivated

  Scenario: Fail to deactivate trainee due to internal server error
    Given I have a valid trainee deactivation request
    When I call the "deactivateTrainee" endpoint and the service encounters an error
    Then I should receive a "500 Internal Server Error" response
