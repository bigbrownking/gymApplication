Feature: Update trainer details
  As a user
  I want to update trainer details
  So that I can modify their information

  Scenario: Successfully update trainer profile
    Given a valid update trainer request
    When I call the "updateTrainer" method
    Then I should receive a "200 OK" response
    And the response should contain the updated trainer details

  Scenario: Fail to update trainer profile with an invalid username
    Given an invalid username in the update trainer request
    When I call the "updateTrainer" method
    Then I should receive a "404 Not Found" response
    And the response should contain an error message
