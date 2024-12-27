Feature: Change trainer password
  As a user
  I want to change a trainer's password
  So that their account remains secure

  Scenario: Successfully change trainer password
    Given a valid change password request
    When I call the "changePassword" method
    Then I should receive a "200 OK" response

  Scenario: Fail to change password due to an internal server error
    Given the system encounters an error
    When I call the "changePassword" method
    Then I should receive a "500 Internal Server Error" response
