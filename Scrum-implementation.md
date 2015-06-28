# Roles

QCRI ownership includes:

1. Product definition and scope
1. Product roadmap
1. System architecture
1. Database schemata
1. User experience and interface design

Specific roles are as follows:

1. Jaideep is the product owner for AIDR/MM and delegates this role in Ji for day-to-day activities.
1. Ji is the product manager for AIDR/MM.
1. Imran is the scientific lead for AIDR/MM.
1. Ji is the Scrum Master and facilitate scrum processes & activities. Manish would help Ji in setting up processes wherever required.

# Meetings & Status Updates

1. We will create a channel on slack for Daily Scrum. The team will provide updates on what they did today, what they are going to do tomorrow, any blockers everyday on this channel
2. There will be status calls every Monday & Thursday. Sprint Planning, Demo & Retrospectives will also be incorporated in these meeting besides regular status updates.

# Scrum Workflow

1. Product Owner creates & prioritizes stories in Backlog.
2. Team clarifies, asks questions and then estimates the stories. Initially while new team members are learning, the experienced team members might help out with sizing of stories.
3. Stories are loaded in to the sprint as per the team capacity in Sprint Planning Meeting
4. Developers START on the stories.
5. When developers commit the code to Github, they will change the status of story to FINISHED.
6. The code is deployed to Test/Staging environment and tested there. Once verified on test/staging environment, the story status is changed to DELIVERED.
7. The code is deployed to production. Product Owner will verify the functionality on production and then Accept / Reject the user stories.
Right now the Test/Staging environment is not available, so stories may need to be delivered on development machine only. This may be fine for now since most of the development would related to Unit Test Cases.

# Story Size

There was discussion around Story Size. We recommend moving away from tight integration between story points & story points and keep them more as suggestive of the effort required. The effort is inclusive of both DEV and QA efforts. For example, we can have following story sizing scheme:

1. Small  - 1 point - Can be delivered within half a day
2. Medium - 2 point - Can be delivered with a day
3. Large - 4 points - Can be delivered within 2-3 days
4. Very Large - 8 points - Can be delivered within a week
The objective should be have to as small or specific stories as possible.

Given that Agile Process works differently for each team, we will keep on tweaking the process to improve it as per needs of our project.

