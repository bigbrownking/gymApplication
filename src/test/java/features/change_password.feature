Feature: Change password
  As a user
  I want to change a user's password
  So that their account remains secure

  Scenario: Change trainee password successfully
    Given I have "valid change password request"
    When change password "trainee"
    Then the "trainee" password should be updated

  Scenario: Change trainer password successfully
    Given I have "valid change password request"
    When change password "trainer"
    Then the "trainer" password should be updated


