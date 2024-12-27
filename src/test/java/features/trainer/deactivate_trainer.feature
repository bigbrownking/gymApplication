Feature: Deactivate trainer
  As a user
  I want to deactivate a trainer account
  So that the trainer loses access to the system


  Scenario: Successfully deactivate a trainer
    Given a valid deactivate trainer request
    When I call the "deactivateTrainer" method
    Then I should receive a "200 OK" response

  Scenario: Fail to deactivate a trainer due to an internal server error
    Given the system encounters an error
    When I call the "deactivateTrainer" method
    Then I should receive a "500 Internal Server Error" response
