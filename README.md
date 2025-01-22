
Read Me & Documentation                                                                                        

--------------------------------------------------------------------------------------------------------------
App Concept
--------------------------------------------------------------------------------------------------------------
Our app started as one of our projects for our Mobile Coding course. The initial app was a bare-bones mood tracker. Users were able to rate their days on a scale of 1-5 stars and see an overview of their rated days throughout the months. Our use case was people wanting to have a nice, clean and simple overview of their mood over a given month.

--------------------------------------------------------------------------------------------------------------
Starting out
--------------------------------------------------------------------------------------------------------------
We had some initial ideas for what we wanted to add to our app however started with a formative user test to determine what people in general would use the app for and what they would expect. The two major features highlighted were the ability to add comments to each day, allowing them to keep better track of why each day got that rating and were not happy with the default and at the time only color palette. We decided these would be our two main additions to the app, comments and changing color themes. Some users pointed out small changes that should be made for quality of life, visibility of information and overall ease of use that were added to our list.

We also wanted to expand our use case by doing this, offering users a light-weight journaling app at the same time.

--------------------------------------------------------------------------------------------------------------
User Test
--------------------------------------------------------------------------------------------------------------
The User-testing was done in three rounds, the first two of which were formative studies, conducted by a group of our peers, talking through their likes, dislikes, suggestions, personal grievances and any improvements. This data was used alongside development and fine tuning the app. The final round of user-testing was summative, involving a SEQ (Single-Easy-Question) and UEQ (User experience questionnaire) design in order to gain some insight into how users rated our app and what they value in such an app.

For a more comprehensive list of our results and testing we have included a link to the data below

https://docs.google.com/document/d/1MgyZ4laGOdWBpFdnYIcJVHsbXaCQ_LvEcbHsUfxK9ys/edit?usp=sharing

--------------------------------------------------------------------------------------------------------------
Development
--------------------------------------------------------------------------------------------------------------
We split up our main features and begun work. Our focus was on the two main features outlined in our user-testing. The first few days of developing were a lot of running in circles trying to figure things out, however eventually we got into a flow and got those main features working. After which we branched out further, Matt handled QoL features such as cleaning up some visual clutter, making certain things more obvious (calender being swipable), changing the landing page. Michael in the meantime, having finished the themes, delved deeper instead to make a functional light and dark mode in addition to themes, allowing users more flexibility in their customization. 

At the beginning of the second week, we did a smaller round of testing. Figuring out what users liked about the changes and if they had imagined something else. From these we mainly got small design tweaks and our users highlighted some visibility issues and much more personal preference, not all of these were implemented due to the conflicting nature of many of them.

Finishing up the majority of the coding and deciding the app was in a version 2.0 state, we performed our third round of testing for quantative data. Some changes have been set for small hotfixes or future development of a version 3.0

--------------------------------------------------------------------------------------------------------------
Database and Preference structure
--------------------------------------------------------------------------------------------------------------
Rate My Day uses both Room and DataStore as database infrastructures.
- Room is used to store all data about each rated day.
- DataStore is used to check what theme and mode is currently being used.
