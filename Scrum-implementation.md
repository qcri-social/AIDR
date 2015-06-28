# Product owner responsibilities

The implementation is delegated to software developers, but the product owner maintains these responsibilities:

1. Product definition and scope
2. Product architecture and design
3. Product roadmap
4. System architecture and database schemas
5. User experience and interface design

# Meetings and status updates

1. We will create a channel on slack for Daily Scrum. The team will provide updates on what they did today, what they are going to do tomorrow, any blockers everyday on this channel.
2. There will be daily scrum calls at 10:00 AST.
3. There will be status calls every Monday & Thursday. Sprint Planning, Demo & Retrospectives will also be incorporated in these meeting besides regular status updates.

# Scrum workflow

1. Product Owner creates & prioritizes stories in Backlog.
2. Team clarifies, asks questions and then estimates the stories. Initially while new team members are learning, the experienced team members might help out with sizing of stories.
3. Stories are loaded in to the sprint as per the team capacity in Sprint Planning Meeting
4. Developers START on the stories.
5. When developers commit the code to the "dev" branch, they will change the status of story to FINISHED.
6. When the code is deployed in the staging server, the story change to DELIVERED.
7. Story owner performs a code review to verify implementation and Reject in case the code is unsatisfactory, amending the acceptance criteria if needed.
8. Story owner verifies the functionality on the staging server and Reject in case the functionality is unsatisfactory, amending the acceptance criteria if needed.
9. Story owner Accepts when satisfied, marking the story as ACCEPTED.
10. Stories that are accepted can be 

# Stories

## Contents

Stories should be small and specific. They have the following contents:

* Clear and informative **headline** indicating what the story is about
* Story **description** of what the story means from the perspective of its target users, e.g.: "As an operator, I want to ..." or "As a system administrator, I want to ..." or "As a developer, I want to ..."
* **Detailed acceptance criteria** indicating each of the elements that will be evaluated when reviewing the story
* More details will be provided **on demand**. It is responsibility of the developers to ask for clarification.

## Points

The effort is inclusive of both DEV and QA efforts. We use this scheme:

1. Small  - 1 point - Can be delivered within half a day
2. Medium - 2 point - Can be delivered with a day
3. Large - 4 points - Can be delivered within 2-3 days
4. Very Large - 8 points - Can be delivered within a week

The objective should be have to as small or specific stories as possible.
