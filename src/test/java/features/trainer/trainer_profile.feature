Feature: Retrieve trainer profile
  As a user
  I want to retrieve a trainer's profile by username
  So that I can view their information


  Scenario: Successfully retrieve trainer profile by username
    Given a valid username exists in the system
    When I call the "getTrainerByUsername" method
    Then I should receive a "200 OK" response
    And the response should contain the trainer's profile details

  Scenario: Fail to retrieve trainer profile with an invalid username
    Given a username that does not exist in the system
    When I call the "getTrainerByUsername" method
    Then I should receive a "404 Not Found" response
    And the response should contain an error message
