Feature: Change trainee password
  As a user
  I want to change a trainee's password
  So that their account remains secure

  Scenario: Change password successfully
    Given I have a valid password change request
    When I call the "changePassword" endpoint with the request
    Then I should receive a "200 OK" response
    And the trainee's password should be updated

  Scenario: Fail to change password due to internal server error
    Given I have a valid password change request
    When I call the "changePassword" endpoint and the service encounters an error
    Then I should receive a "500 Internal Server Error" response
