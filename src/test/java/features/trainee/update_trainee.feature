Feature: Update trainee details
  As a user
  I want to update trainee details
  So that I can modify their information

  Scenario: Update trainee successfully
    Given I have valid updated trainee details
    When I call the "updateTrainee" endpoint with the updated details
    Then I should receive a "200 OK" response
    And the trainee details should be updated

  Scenario: Fail to update non-existent trainee
    Given I have invalid trainee details
    When I call the "updateTrainee" endpoint
    Then I should receive a "404 Not Found" response

  Scenario: Fail to update trainee due to internal server error
    Given I have valid updated trainee details
    When I call the "updateTrainee" endpoint and the service encounters an error
    Then I should receive a "500 Internal Server Error" response
