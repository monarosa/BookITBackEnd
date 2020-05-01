
Feature: Create student
  @api
Scenario: 1. Create student as a team member and verify status code 422
Given authorization token is provided for "teacher"
And user accepts content type as "application/json"
When user sends POST request to "/api/students/student" with following information:
| first-name | last-name | email             | password | role                | campus-location | batch-number | team-name      |
| Les      | McDonald  | lesslee@email.com | 1111     | student-team-member | VA              | 12           | Online_Hackers |
And user verifies that response status code is 403