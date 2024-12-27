Feature: Trainer registration
  As a user
  I want to register a new trainer
  So that the trainer profile is created successfully


  Scenario: Successfully register a new trainer
    Given a valid trainer registration request
    When I call the "registerTrainer" method
    Then I should receive a "201 Created" response
    And the response should contain the registered trainer details

  Scenario: Fail to register a trainer due to invalid details
    Given an invalid trainer registration request
    When I call the "registerTrainer" method
    Then I should receive a "400 Bad Request" response
    And the response should contain validation error messages
