Feature: Retrieve not assigned trainers
  As a user
  I want to retrieve a list of trainers not assigned to a trainee
  So that I can assign trainers to the trainee

  Scenario: Retrieve not assigned trainers successfully
    Given I have a valid trainee username
    When I call the "getNotAssignedTrainers" endpoint with the username
    Then I should receive a "200 OK" response
    And the response should contain a list of trainers not assigned to the trainee

  Scenario: Fail to retrieve not assigned trainers due to internal server error
    Given I have a valid trainee username
    When I call the "getNotAssignedTrainers" endpoint and the service encounters an error
    Then I should receive a "500 Internal Server Error" response
