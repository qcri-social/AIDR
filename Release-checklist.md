Upgrading brings a better product and new functionality to users, but there is also a risk of disrupting users and causing them great frustration. To avoid this, we can follow a release process.

# Before the upgrade

Update the operator's manual to reflect any change in functionality or layout of pages. If we keep the manual up-to-date with minor changes, we save ourselves time and effort of doing big changes, or worse, having to rewrite the manual later.

Notify users of the upcoming downtime at least a couple of days in advance, by an e-mail to aidr-users@googlegroups.com indicating the time and date of the downtime, and the expected duration of it.

* Has the manual been updated with new functionality?
* Has the manual been updated to reflect changed page layouts?
* Have the users been notified of the upcoming downtime?

# After the upgrade

Run the post-deployment "smoke" tests to make sure everything is working fine.

Notify users that the upgrade was completed successfully and let them now of all the changes. Indicate clearly whether (a) no action is required from them, or (b) action is required from them. Ideally upgrades should not require actions from users, but if unavoidable, they need to know which actions.

Use the "release" functionality of GitHub to tag a release and add the release notes there.

* Have all the post-deployment "smoke" tests been passed?
* Have the users been notified of the successful upgrade?
* If action is required from the users, have they been instructed what to do?
* Have the users been notified of the upcoming downtime?
* Is there a release in GitHub with release notes?