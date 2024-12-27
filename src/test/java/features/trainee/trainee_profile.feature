Feature: Retrieve trainee profile
  As a user
  I want to retrieve a trainee's profile by username
  So that I can view their information

  Scenario: Retrieve trainee profile successfully
    Given I have a valid trainee username
    When I call the "getTraineeByUsername" endpoint with the username
    Then I should receive a "200 OK" response
    And the response should contain the trainee profile details

  Scenario: Fail to retrieve non-existent trainee profile
    Given I have an invalid trainee username
    When I call the "getTraineeByUsername" endpoint
    Then I should receive a "404 Not Found" response

  Scenario: Fail to retrieve trainee profile due to internal server error
    Given I have a valid trainee username
    When I call the "getTraineeByUsername" endpoint and the service encounters an error
    Then I should receive a "500 Internal Server Error" response
