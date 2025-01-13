Feature: Registration
  As a user
  I want to register a new trainee
  So that the trainee profile is created successfully

  Scenario: Register trainee successfully
    Given I have "valid trainee registration request"
    When register "trainee"
    Then the response should contain the registered "trainee" username and random password

  Scenario: Successfully register a new trainer
    Given I have "valid trainer registration request"
    When register "trainer"
    Then the response should contain the registered "trainer" username and random password
