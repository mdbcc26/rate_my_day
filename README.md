
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

[Rate My Day, User Testing](https://docs.google.com/document/d/1MgyZ4laGOdWBpFdnYIcJVHsbXaCQ_LvEcbHsUfxK9ys/edit?usp=sharing)

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

--------------------------------------------------------------------------------------------------------------
App Features
--------------------------------------------------------------------------------------------------------------
Rate My Day, is as the name suggests, an app where users are able to rate their day. On opening the app users are prompted to rate the current day and are able to leave a comment. Upon saving they are brought to an overview screen that displays the current month and days they have rated are colored and highlighted according to the chosen theme. Clicking on a day allows users to add a rating if it does not have one, edit or delete their rating if it does have one.
The app can be customized in a multitude of ways in accordance with user preference. Light or dark mode and several themes are available to choose from. Themes range from simple monochromatic gradients to more outlandish options, from among these, every user, including users with partial or total color blindness, should be able to find something they like and can easily parse. 

--------------------------------------------------------------------------------------------------------------
Reflection
--------------------------------------------------------------------------------------------------------------

Matt Dalton.
--------------------------------------------------------------------------------------------------------------
I am glad we chose to work on extending my Mobile Coding project. It relieved the stress of having a functioning app by the end of the first week of CCL, since we already had a baseline. I spent most of my time developing different features and quality of life tweaks. However, when it came to the 6th day of the CCL and with 3ish days left, one has to know when to call it quits and present a version we are happy with as a team. We’ve made a list of to-dos for the future and I’m genuinely considering coming back to this project in my own time. I think Kotlin as a language to program in is still difficult, but I can look through the code, read it and understand it.


Michael Zeller
--------------------------------------------------------------------------------------------------------------
After the previous two CCL’s and the preceding semester I wanted to take CCL3 a bit lighter, while still doing some weird things. I spent most of it working with Datastores and getting theme switching and preferences to work. For the first few days it was a bit of a struggle as I did not feel very comfortable with kotlin and  android studio but eventually I think I managed to get a bit of a hang of it. Because of this, this CCL felt a lot less stressful and while it was better for my mental health, I did enjoy the previous two a lot more in hindsight because of how ambitious and out there my projects were, even if they were much more stressful. 

