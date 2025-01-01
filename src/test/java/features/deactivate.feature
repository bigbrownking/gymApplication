Feature: Deactivate
  As a user
  I want to deactivate a trainee account
  So that the trainee loses access to the system

  Scenario: Deactivate trainee successfully
    Given I have "valid deactivation request"
    When deactivate "trainee"
    Then the "trainee" account should be deactivated


  Scenario: Successfully deactivate a trainer
    Given I have "valid deactivation request"
    When deactivate "trainer"
    Then the "trainer" account should be deactivated


