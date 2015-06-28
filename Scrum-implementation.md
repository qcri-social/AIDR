# Responsibilities

The implementation is delegated to software developers, but the product owner maintains these responsibilities:

1. Product definition and scope
2. Product architecture and design
3. Product roadmap
4. System architecture and database schemas
5. User experience and interface design

# Workflow

## Sprint workflow

1. Product Owner creates & prioritizes stories in Backlog.
2. Team clarifies, asks questions and then estimates the stories. Initially while new team members are learning, the experienced team members might help out with sizing of stories.
3. Stories are loaded in to the sprint as per the team capacity in Sprint Planning Meeting
4. Developers START on the stories.
5. When developers commit the code to the "dev" branch, they will change the status of story to FINISHED.
6. When the code is deployed in the "staging" branch and pushed to the staging server, the story change to DELIVERED.
7. Story owner performs a code review to verify implementation and Reject in case the code is unsatisfactory, amending the acceptance criteria if needed.
8. Story owner verifies the functionality on the staging server and Reject in case the functionality is unsatisfactory, amending the acceptance criteria if needed.
9. Story owner Accepts when satisfied, marking the story as ACCEPTED.
10. A release merges code from the "staging" to the "master" branch

## Meetings and status updates

1. There will be daily scrum calls at 10:00 AST - the team will provide updates on what they did today, what they are going to do tomorrow, any blockers.
2. Additionally, daily updates should be posted to the daily scrum channel: https://aidr.slack.com/messages/daily-scrum/ 
3. There will be status calls every Monday & Thursday. Sprint Planning, Demo & Retrospectives will also be incorporated in these meeting besides regular status updates.

# Stories

## Contents

Stories should be concrete and testable. They have the following contents:

* Clear and informative **headline** indicating what users can do after the story is written, e.g. "Download tweets filtered by date"
* Story **description** of what the feature means from the perspective of its target users, e.g.: "As an operator, I want to ..." or "As a system administrator, I want to ..." or "As a developer, I want to ..."
* **Detailed acceptance criteria** indicating each of the elements that will be evaluated when reviewing the story
* More details will be provided **on demand**. It is responsibility of the developers to ask for clarification when details are missing.

## Points

The effort is inclusive of both DEV and QA efforts. We use this scheme:

1. Small  - 1 point - Can be delivered within half a day
2. Medium - 2 point - Can be delivered with a day
3. Large - 4 points - Can be delivered within 2-3 days
4. Very Large - 8 points - Can be delivered within a week

The objective should be have to as small or specific stories as possible.
