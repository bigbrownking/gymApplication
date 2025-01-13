Feature: Not assigned trainers
  As a user
  I want to retrieve a list of trainers not assigned to a trainee
  So that I can assign trainers to the trainee

  Scenario: Retrieve not assigned trainers successfully
    Given I have "valid trainee not assigned trainers"
    When not assigned trainers
    Then the response should contain a list of trainers not assigned to the trainee