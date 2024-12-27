Feature: Retrieve trainer training list
  As a user
  I want to retrieve the list of trainings assigned to a trainer
  So that I can track their training progress

Scenario: Successfully retrieve trainings by criteria
Given valid training criteria
When I call the "getTrainings" method
Then I should receive a "200 OK" response
And the response should contain a list of trainings matching the criteria

Scenario: Fail to retrieve trainings due to an internal server error
Given the system encounters an error while fetching trainings
When I call the "getTrainings" method
Then I should receive a "500 Internal Server Error" response
