# Pre-work - ListIt

ListIt is an android app that allows the user to build a ToDO List. The user can add, update and delete items from the list as required. Further, the user can customize 
the item with the following fields.
1) Name
2) Due Date and Time
3) Priority 
4) Memo for custom notes
5) Status

Submitted by: Narayan Jat

Time spent: 20 hours spent in total

## User Stories

The following **required** functionality is completed:

* User can successfully add items to list using Dailog Box detailings all available fields for an item by clicking on the add icon in the toolbar
    * Checks for inavlid names and if the item already exists in the list
* User can edit or delte an item in the list by tapping and holding the item.
    *  A Dialog Screen opens up that allows the user to edit each field
    * An option is displayed to delete the item
    * An option is displayed to mark the item as complete
    * All the changes are reflected in the ToDo List. An image is displayed next to each item indicating its priority or whether it is completed
* User can persist todo items  and retrieve them properly on app restart


The following **optional** features are implemented:

* [ Done ] Persist the todo items [into SQLite](http://guides.codepath.com/android/Persisting-Data-to-the-Device#sqlite) instead of a text file
* [ Done ] Improve style of the todo items in the list [using a custom adapter](http://guides.codepath.com/android/Using-an-ArrayAdapter-with-ListView)
* [ Done ] Add support for completion due dates for todo items 
    * Displayed in a dialog box upon selection (using long click)
* [ Done ] Use a [DialogFragment](http://guides.codepath.com/android/Using-DialogFragment) instead of new Activity for editing items
* [ Done ] Add support for selecting the priority of each todo item (and display in listview item)
* [ Done ] Tweak the style improving the UI / UX, play with colors, images or backgrounds
    *  Can be improved further

The following **additional** features are implemented:

* [ Completed] Allow user to create multiple lists and switch between them as required 
    * Would allow for better organization and user experience
    * Allow the user to create a New List and Clearly mark which list is being displayed [done]
    * Allow the user to delete lists [done]
    * Allow the user to switch between lists [done]
    * Persist Settings in database [done]
* [ BackLog ] Set up reminders for when due date is close

## Video Walkthrough

Here's a walkthrough of implemented user stories:

[Link to GIF Walkthrough](http://i.imgur.com/KZmDBaQ.gif)


GIF created with [LiceCap](http://www.cockos.com/licecap/).

## Project Analysis

As part of your pre-work submission, please reflect on the app and answer the following questions below:

**Question 1:** "What are your reactions to the Android app development platform so far? Compare and contrast Android's approach to layouts and user interfaces in past platforms you've used."

**Answer:** 
I have enjoyed working with Android Studio for app development so far. The UI is easy to use. Especially the ability to render your layouts immediately after a change is very useful. It 
allows for quick prototyping. Also, the fact that the project is pre-structured to separate layouts, color and other resource is very useful.
I find the android’s approach to layouts and user interfaces is similar to that used for web development. However, I do not have enough experience with web development to do a meaningful 
contrast.


**Question 2:** "Take a moment to reflect on the `ArrayAdapter` used in your pre-work. How would you describe an adapter in this context and what is its function in Android? Why do you think the adapter is important? Explain the purpose of the `convertView` in the `getView` method of the `ArrayAdapter`."

Adapter is an important interface in Java which acts as bridge between an AdapterView and the underlying data set for the AdapterView. In our case, we utilize the ArrayAdater which accepts a collection of data objects as the data set and provides the views for a list based user interface widget. In our case, we used an ArrayList as our data set and ListView as the AdapterView. 

 It should be noted that the AdpaterView’s children are determined by the Adapter. Therefore, Adapter is also responsible for creating a view for each item in our data set. In our case, we extended the ArrayAdapter to use a custom view for each item. 

 The convertView paramenter in the getView(…) method is used for recycling containers. It generally points to an older view for reuse. For example, in a case where your ListView can only hold a maximum of 10 items and the user scrolls up to expose the 11th item. The Adapter will make a call to getView(…) to get a view for the new child. However instead of inflating a new view, the view passed as convertView (in this case 1st item that in no longer visible in Listview) can be reused for the contents of the 11th Item in the data set (given that convertView is not null).

## Notes

Describe any challenges encountered while building the app.
I believe the initial setup was a little bit tedious. However after setup, the platform was easy to use and fun to develop on.

## License

    Copyright [yyyy] [name of copyright owner]

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.