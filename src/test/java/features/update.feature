Feature: Update
  As a user
  I want to update trainee details
  So that I can modify their information

  Scenario: Update trainee successfully
    Given I have "valid trainee update request"
    When update "trainee"
    Then the response should contain the updated "trainee" details

  Scenario: Successfully update trainer profile
    Given I have "valid trainer update request"
    When update "trainer"
    Then the response should contain the updated "trainer" details
