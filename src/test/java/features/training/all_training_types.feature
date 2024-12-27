Scenario: Successfully retrieve training types
Given the system has available training types
When I call the "getTrainingTypes" method
Then I should receive a "200 OK" response
And the response should contain a list of training types

Scenario: Fail to retrieve training types due to an internal server error
Given the system encounters an error
When I call the "getTrainingTypes" method
Then I should receive a "500 Internal Server Error" response
