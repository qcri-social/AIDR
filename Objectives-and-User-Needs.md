This document describes the strategic direction of AIDR in terms of objectives and user needs.

# Objectives

The objective of AIDR is to provide a platform to collect and classify in real-time a large amount of user-generated messages during humanitarian crises. The platform applies human intelligence (crowdsourcing) and machine intelligence (machine learning) to perform these tasks. This platform is provided as a service via a website, and as free and open source software.

The main success metrics of AIDR are:

* The number of crises that can be handled simultaneously: AIDR should support tens of crises at the same time.
* The number of messages that can be classified per unit of time: AIDR should support up to hundreds of messages per minute.
* The number of users who can volunteer to help in the classification: AIDR should allow hundreds of volunteers to work simultaneously.

# User Needs

AIDR has two types of users: **operators**, who start and oversee the process of collection and classification, and **labelers**, who contribute human intelligence through labeled examples.

## User Needs: Operators

Operators of AIDR have the following main needs:

* Maintain (Create, Read, Update or Delete) a set of crisis-related collections
 * Create a collection specifying a set of parameters
 * Monitor the state of a collection
 * Modify the parameters of a collection
 * Stop, delete and/or archive a collection
* Maintain (CRUD) for each crisis-related collection, a set of classifiers
 * Create a classifier specifying a set of tags for it, or copy an existing (suggested) classifier
 * Monitor the classification accuracy
 * Modify the tags of the classifier
 * Remove the classifier from the collection
* Download the resulting classified tweets onto a spreadsheet.
* Visualize at a glance the resulting classified tweets.
* Consume the resulting classified tweets into an application through an API.

Users of AIDR are typically members of emergency-response organizations (including both professional/formal organizations and digital volunteer communities), as well as members of the public.

**Operator personas** (fictional characters describing prototypical users):

* Andres, 40, is a manager in a large non-governmental organization based in Europe. He has an undergraduate degree in Economics, and a Master’s degree in government and policy making. His professional background is in non-profit organization management. He lives with his wife and two small children in a large apartment, and often rides his bike to work. In his spare time, he enjoys baking, trail running, and watching movies (comedies are his favorite). Andres’ department is responsible for coordinating relief efforts in the wake of severe crises and mass emergencies. When such events occur, Andres is often deployed to the field to quickly assess the situation, gather data, and provide a report to decision makers and other stakeholders regarding fund and supply allocation. Regarding the data-gathering aspect of his job; to gather details about a given situation, Andres interviews people on the ground, speaks on the phone with stakeholders and other people with local knowledge and knowledge about the event, and also uses several mainstream news media sites, international geophysical organization websites (e.g. USGS; EMSC), and Tweetdeck to monitor both mainstream news and social media.
 When it comes to online tools, Andres uses Twitter and Facebook. What does Andres want from AIDR? He wants summarized information about what is in the social media data about the crisis under question.

* Maggie, 32, works with Andres in the same non-governmental organization based in Europe. She has an undergraduate degree in political science, and began to work at the NGO soon after she graduated. When disasters occur, she often stays in the home office and monitors activities from there during the first few weeks after impact. Her job is to write the MIRA report, and to continually collect data to create later reports that are read by stakeholders.
 Maggie uses MS word, PowerPoint, and excel to organize data and write her reports. She also uses Facebook   and Skype often.
 What does Maggie want from AIDR?
 She wants a summary about social media regarding the disaster event under question. This includes examples of social media posts that contain critical information and/or explain details about the situation. She uses this information to frame both the MIRA and subsequent reports.
 
* Josefina, 30, is a member of a digital volunteer network that creates crisis maps. She has an undergraduate degree in Communications, and is currently one of the leaders among the volunteers who respond to disaster situations by reading and labeling tweets that include information about the situation.  When she’s not responding to disaster situations, she works as a copy editor for a local travel publication. She has lived in the UK her entire life, and spends a lot of time with her close-knit family. In her spare time, she enjoys knitting, kayaking, and reading 19th century literature.
 When a disaster occurs, and Josefina is requested to respond and organize volunteers. In the past 2 years, Josefina has responded to four disasters by collaborating with between 10-20 other volunteers via Skype, and using a Google spreadsheet to label, and organize tweets. Both Josefina and her fellow volunteers have experience with the Ushahidi platform and have used it in previous crises to create crisis maps. These maps show locations of potential points of interest as communicated and/or inferred in tweets. The crisis maps are then provided to the public, and to formal response agencies.
 What does Josefina want from AIDR? Josefina wants AIDR to help her create her crisis map – she wants labeled tweets that she can then populate a map with.

* Maurice, 38, is a journalist, blogger and activist who is interested in how social media can help him quickly gain information about news-worthy events. He travels frequently, but calls Brooklyn, NY home when he’s not globe-trotting for work. In his limited spare time, he likes to bake, play tennis, and read comic books.
 Maurice uses Tweetdeck, LinkedIn, Instagram, Pinterest, Vine, Facebook, Reddit, G+ and Topsy to get the pulse on social media around certain events or developments. This involves finding out about new events, understanding various aspects about the event, and deciding what angle he’ll take in writing about events.
 What does Maurice want from AIDR? Maurice wants either a general picture about the event as it’s represented in social media, or, he may be interested in a specific topic.
 
* Carlos, 40, is a developer of web applications. He is a senior developer at a large international software company. He is an active member of the group “Geeks with a Purpose,” and frequently attends “hacks of kindness” events. When he’s not at work, Carlos enjoys training for triathlons, taking photos, and playing chess.
 When a disaster occurs, one of the tasks Carlos undertakes it to create crisis mash-ups using data from different sources, such as RSS feeds, the AIDR JSON feed, and Google maps.
 What does Carlos want from AIDR?
 He wants categorized data with all the meta data for each tweet, in a JSON feed, in real time, or he may want all of the data for a recent event.
 
* Alejandra, 33, a professional researcher at a research institute.
 Alejandra is interested in questions related to psychological responses to crises. She frequently publishes in academic journals, and typically works with survey data on the order of several thousands to several hundreds of thousands of data points. She performs statistical analyses, and is an expert in data mining tools. She also uses data clustering to find patterns and themes in data.
 What does she want from AIDR? Alejandra wants archived data (labeled and unlabeled) for past crises (the entire data set).

** Only humanitarian topics may be monitored and supported**

## User Needs: Labelers

Labelers provide volunteer work to classify tweets in AIDR. Their main needs are:

* Finding tasks that they can do, being able to focus on crises that motivate them to be volunteers.
* Learning to do that task, by reading instructions and seeing examples.
* Performing the task (i.e. classify tweets).
* Understanding how well they performed the task.
* Seeing the output of AIDR.
* Understanding the effect of their work in the output of AIDR.

Labelers can be anyone, not necessarily members of volunteer organizations.



