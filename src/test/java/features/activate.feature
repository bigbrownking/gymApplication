Feature: Activate
  As a user
  I want to activate a user account
  So that the user can access the system

  Scenario: Activate trainee successfully
    Given I have "valid activation request"
    When activate "trainee"
    Then the "trainee" account should be activated

  Scenario: Activate trainer successfully
    Given I have "valid activation request"
    When activate "trainer"
    Then the "trainer" account should be activated
