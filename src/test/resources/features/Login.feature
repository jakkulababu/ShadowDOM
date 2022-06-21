Feature: Login as different user roles

  #TODO put this test case on jira
  @smoke1
  Scenario Outline: Check user name
    Given Go to Google page
    When User Search "<search>"
    And I click Google Search
    Examples:
      |search  |
    | Hexaware|
    | Google |


  #TODO put this test case on jira
  @smoke1
  Scenario: Check role
    Given Go to Google page
