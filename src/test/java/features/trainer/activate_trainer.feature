Feature: Activate trainer
  As a user
  I want to activate a trainer account
  So that the trainer can access the system

  Scenario: Successfully activate a trainer
    Given a valid activate trainer request
    When I call the "activateTrainer" method
    Then I should receive a "200 OK" response

  Scenario: Fail to activate a trainer due to an internal server error
    Given the system encounters an error
    When I call the "activateTrainer" method
    Then I should receive a "500 Internal Server Error" response
