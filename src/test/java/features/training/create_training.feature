Scenario: Successfully register a new training
Given a valid training registration request
When I call the "addTraining" method
Then I should receive a "201 Created" response

Scenario: Fail to register a training due to an internal server error
Given the system encounters an error
When I call the "addTraining" method
Then I should receive a "500 Internal Server Error" response
