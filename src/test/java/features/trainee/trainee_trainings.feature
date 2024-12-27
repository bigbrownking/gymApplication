Feature: Retrieve trainee training list
  As a user
  I want to retrieve the list of trainings assigned to a trainee
  So that I can track their training progress

  Scenario: Retrieve training list successfully
    Given I have a valid trainee username and criteria
    When I call the "getTrainings" endpoint with the username and criteria
    Then I should receive a "200 OK" response
    And the response should contain the list of trainings matching the criteria

  Scenario: Fail to retrieve training list due to internal server error
    Given I have a valid trainee username
    When I call the "getTrainings" endpoint and the service encounters an error
    Then I should receive a "500 Internal Server Error" response
