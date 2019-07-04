# Introduction
This repository contains a coding challenge project. It's an Android mobile application called Notflix. Notflix is a movie and music app but it doesn't let you watch movie nor let you listen to music. It's just an app that displays a list of items obtained from iTunes Search API and shows a detailed view for each item. The project applies MVVM architecture and its UI applies a bit of material design with dark mode color pallete implementation.

# Objective
Create a master-detail application that contains at least one dependency. This application should display a list of items obtained from a iTunes Search API and show a detailed view for each item. The URL you must obtain your data from is as follows:
* https://itunes.apple.com/search?term=star&amp;country=au&amp;media=movie
*iTunes Web Service Documentation:* https://affiliate.itunes.apple.com/resources/documentation/itunes-store-web-service-search-api/#searching

Your list should show the following details from the API:
- Track Name
- Artwork
- Price
- Genre

In addition, the detail view should show Long Description for the given item.

Each row should have a placeholder image if for some reason the URL is unable to be loaded. Images must not be duplicated when scrolling.

Your app should demonstrate ability to save data and reuse it when the user opens the app again. You can choose any persistence mechanism you wish. You are also free to determine what kind of data you want to persist, however:

- This functionality must be very clear in your app
- Your choices should be explained in a README.md or in code comments

Our suggestions:
- A date when user previously visited, shown in the list header
- Save the last screen that the user was on. When the app restores, it should present the user with that screen.

You can save other data. Add any supporting information in the same manner as above.

## Architecture
In the app, I used the MVVM or Model-View-ViewModel architecture which Google strongly advises for developers to use. I also used Service and Repository pattern for app handling processes and data accessing respectively. I also used Client Service, a type of Service which I use to fetch data from the API. I used Google's Android Jetpack Architecture components to make everything working smoothly in the app like the Lifecycles, LiveData, ViewModel and Room components.

### Model
### View
### ViewModel
#### LiveData
### Service Pattern
#### Client Service
### Repository Pattern
#### Room Database
#### Shared Preference
#### Caching
#### Util

## User Interface
