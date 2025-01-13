Feature: Profile
  As a user
  I want to retrieve a trainee's profile by username
  So that I can view their information

  Scenario: Retrieve trainee profile successfully
    Given I have "valid profile request"
    When profile "trainee"
    Then the response should contain the "trainee" profile details

  Scenario: Retrieve trainer profile successfully
    Given I have "valid profile request"
    When profile "trainer"
    Then the response should contain the "trainer" profile details

  Scenario: Fail to retrieve non-existent trainee profile
    Given I have "valid profile request"
    When profile "trainee"
    And "trainee" profile doesn't exist
    Then "trainee" profile operation should throw EntityNotFoundException

  Scenario: Fail to retrieve non-existent trainer profile
    Given I have "valid profile request"
    When profile "trainer"
    And "trainer" profile doesn't exist
    Then "trainer" profile operation should throw EntityNotFoundException
