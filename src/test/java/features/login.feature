Feature: Login
  As a user
  I want to register a new user
  So that the user profile is created successfully

  Scenario: Login successfully
    Given I have "valid login request"
    When login
    Then the response should contain the jwt token

  Scenario: Login with incorrect credentials
    Given I have "valid login request"
    And user doesn't exist
    When login
    Then the response should contain validation error messages
