# Introduction
This repository contains a coding challenge app project. It's an Android mobile application called Notflix. Notflix is a movie and music app, but it doesn't let you watch movies nor let you listen to music. It's just an app that displays a list of items obtained from iTunes Search API and shows a detailed view for each item. The project applies MVVM architecture and its UI has a bit of material design with dark mode color palette implementation.

# Objective
Create a master-detail application that contains at least one dependency. This application should display a list of items obtained from iTunes Search API and show a detailed view for each item. The URL you must obtain your data from is as follows:
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
- A date when user previously visited, shown in the list header.
- Save the last screen that the user was on. When the app restores, it should present the user with that screen.

You can save other data. Add any supporting information in the same manner as above.

# Architecture
In the app, I used the MVVM or Model-View-ViewModel architecture which Google strongly advises for developers to use. I also used Service and Repository pattern for data processing and data accessing respectively. I also used Client Service, a type of Service which I use to fetch data from a server. I used Google's Android Jetpack Architecture components to make everything working smoothly in the app like the Lifecycles, LiveData, ViewModel and Room components.

## Model
The *models* folder in the app contains the constants, enums, interfaces and the models. Basically anything that concerns a data that is used within the app is located here. It's a collection of enums, interfaces and classes with fields, getters & setters or sometimes just constant values. The model in the models folder is where the data that we are going to work with is modelled from.

## View
The *views* folder in the app contains the adapters, fragments, activities, custom views as well as observers. I usually create apps with single activity with multiple fragments composition usually just a MainActivity launcher and a bunch of fragments with it. But due to some warnings I'm getting in the manifest to provide at least one activity with ACTION-VIEW intent filter, I now provide two activities. Because the downside if I don't do that then the apps I will make will not be indexable by Google Search which will drive less traffic that means fewer downloads resulting into low profit. And no one wants that.

## ViewModel
The *viewmodels* folder is the view's model. This is the interface between the model and the view of the app. We can bind this to the view itself so that we can instantly update the view when something changed in our view model. I wasn't able to apply data binding in the project but I did use LiveData to communicate with the view and view model for live updates of the views when something changed in view model's properties.

## Service Pattern
> A service supplies coordination or other "services" that are required to operate your application. They are very different in that services don't typically know how to access data from persistence, and repositories typically only access data/objects for any services you may have.
[Source](https://stackoverflow.com/questions/1440096/difference-between-repository-and-service?rq=1)

Basically, in the app everything that processes or modifies data modelled from a model class is a service. Service classes can also contain methods that helps the model it corresponds. For example, it could be first letter capitalization checking for User model's property firstName or Account model's username and password login checking and error handling which is usually put inside the model itself. But for me, I put everything inside the service that is tied to the User or Account models in the example, but it still depends on the case scenario. By the way, when I say 'tied to' then it means that the model class has a corresponding service class to it. Although not all models have corresponding service class to it and vise versa.

### Client Service
A client service or server client service in long term, is a class I use to fetch data from a server. Any online transaction that concerns get, post or other methods are handled by this component. This component is usually different from the service, but within the app I put the api folder, which contains client service classes, inside the services folder.

## Repository Pattern
> A repository is essentially a facade for persistence that uses collection style semantics (add, update, remove) to supply access to data/objects. It is a way of decoupling the way you store data/objects from the rest of the application.
[Source](https://stackoverflow.com/questions/1440096/difference-between-repository-and-service?rq=1)

What this does is basically access data locally. This contains the room databases, shared preferences and caching. Pretty much all persistence types are found here. So the distinction between Service, Client Service and Repository is clear with each one having different roles within the architecture of the project. Client Service handles data accessing online while Repository on the other hand handles data accessing offline or locally and the Service will aid in handling or processing these data. The Service doesn't know where the data comes from or where it will be stored. On the other hand, Client Service and Repository doesn't know what are the processes being done to the data before they are stored or after they are retrieved.

### Room Database
The room database in the app only contains one entity with a corresponding data access object class. To differentiate, an entity is different from a model that's why we put it here. An entity is associated with a stored data while a model is associated with the app. Entities have attributes pretty much like in a database table while models have properties. The *kind of data that I persist* in the app includes the given ones (Track Name:String, Artwork Url:String, Price:float, Genre:String and Long Description:String) with the addition of Track ID of type 'int' which I used as primary key 'id' in the entity. This is because, I used it to differentiate the items and use it to determine the last screen the user is on. But there are problems in saving the last screen which I will discuss later on.

### Shared Preference
The shared preference is another persistence that I used to store single data. Compared to the room database which stores a large quantity of data presented in a table, the shared preference is tasked to store single data with basic data types. This is what I used to store the data, *'a date when user previously visited'* and *'last screen that the user was on'* of type String and int respectively. The date was stored as text of type String and will be shown in the header on app open. The last screen the user was on was stored as an id of type int. Each detail page has a unique ID fetched from the API, the home page is tagged with the id -1 while the detail pages has each of their own ids. The problem is there are "tracks", the term I use to identify the objects provided by the API, that has no 'trackId' which in return results into the default 0 and it presents complications and problems along the way, so I have to ignore the tracks having an entity id of 0, and they will not be presented on app open when it is the last screen the user was on to solve the issue. The API in the first place shouldn't have empty 'trackId' values. I could have decided to not display the items with no track IDs at all, but I still did to show all data from the list.

### Caching
Caching is also used in the app to provide data stored in the RAM. As you can see, the last two persistence methods I've discussed deals with storing data in the local storage. Meanwhile, the caching helps in the performance of the app as it stores the data in the RAM before the data will be displayed so that there is no need to fetch the data every time from the online source or local storage and makes everything working smoothly. When you open the app, you will be presented with a splash screen (SplashActivity) which is the second activity I provided that I talked about earlier in the Views section. Anyways, during the splash screen, the app checks if it is online or offline. If it is online, it will call on the Client Service to fetch data from the server and save it locally to the database and also put it in a static variable to cache it. If it is offline, then it will fetch data from the database and cache it as well, but if it doesn't have any data saved in the database then it will present an error dialog that the user is has no internet connection. That should sum up the caching steps and method used in the app and all of it runs in a LiveData so that whenever the ClientService data or the Room Database data or the Cached data changes then it will just reflect to the UI. Also there is no need to cache shared preference data as it is small enough to be called every time it is needed.

## Util
The *utils* folder is a collection of helper classes that helps you process data. The services folder has similarity with the utils folder, but the distinction is that, a service class can be tied to a model while a util class is a utility or a helper class that helps in the overall application. The util classes basically fills the roles that the other components doesn't do. The jack of all trades if you may.

# User Interface
Welcome to the presentation layer, visual part and the part that took the most time in the completion of this coding challenge. (Sorry about that :D). I would say I applied material design through margins, paddings, fonts but overall for me the paper like material layout design was not well presented in the later part of the UI development. At first, I just wanted the app to look plain and simple with white backgrounds and surfaces (the white app screenshots were not taken unfortunately), but then I realized the design was too boring for me. So I wanted to learn the dark theme mode material design guidelines at the same time complete the challenge, so I leaned it and applied it. Well, just the color scheme part of it. :) It was looking okay now, but still I wanted to apply something new, so I thought of implementing this UI (See Guide section below) from the Netflix app, but only the middle part of the screenshot for the details page. While the details page was derived from Netflix app, the list page (home/master page) was derived from Spotify app with the rounded corners (See Guide section below) in the first iterations of the UI and then later on I decided to remove the rounded corners. To cut the long story short, I can't decide on the look of the app and it took too long to finish until I got satisfied and named the app Notflix because it is literally Not Netflix. Yeah! I know, it's so corny. LOL!

## Guides
<img src="https://github.com/arvicxyz/coding-challenge-app/blob/master/screenshots/guide.jpg" alt="master-page-guide" width="200"/> <img src="https://www.techybugz.com/wp-content/uploads/2018/12/Netflix-Download-2.jpg" alt="detail-page-guide" height="300"/>

## Older Screenshots
<img src="https://github.com/arvicxyz/coding-challenge-app/blob/master/screenshots/old1.jpg" alt="old1" width="200"/> <img src="https://github.com/arvicxyz/coding-challenge-app/blob/master/screenshots/old2.jpg" alt="old2" width="200"/>

## Old Screenshots
<img src="https://github.com/arvicxyz/coding-challenge-app/blob/master/screenshots/old3.jpg" alt="old3" width="200"/> <img src="https://github.com/arvicxyz/coding-challenge-app/blob/master/screenshots/old4.jpg" alt="old4" width="200"/> <img src="https://github.com/arvicxyz/coding-challenge-app/blob/master/screenshots/old5.jpg" alt="old5" width="200"/> <img src="https://github.com/arvicxyz/coding-challenge-app/blob/master/screenshots/old6.jpg" alt="old6" width="200"/> <img src="https://github.com/arvicxyz/coding-challenge-app/blob/master/screenshots/old7.jpg" alt="old7" width="200"/> <img src="https://github.com/arvicxyz/coding-challenge-app/blob/master/screenshots/old8.jpg" alt="old8" width="200"/> <img src="https://github.com/arvicxyz/coding-challenge-app/blob/master/screenshots/old9.jpg" alt="old9" width="200"/>

## New Screenshots
<img src="https://github.com/arvicxyz/coding-challenge-app/blob/master/screenshots/new1.jpg" alt="new1" width="200"/> <img src="https://github.com/arvicxyz/coding-challenge-app/blob/master/screenshots/new2.jpg" alt="new2" width="200"/> <img src="https://github.com/arvicxyz/coding-challenge-app/blob/master/screenshots/new3.jpg" alt="new3" width="200"/> <img src="https://github.com/arvicxyz/coding-challenge-app/blob/master/screenshots/new4.jpg" alt="new4" width="200"/> <img src="https://github.com/arvicxyz/coding-challenge-app/blob/master/screenshots/new5.jpg" alt="new5" width="200"/> <img src="https://github.com/arvicxyz/coding-challenge-app/blob/master/screenshots/new6.jpg" alt="new6" width="200"/> <img src="https://github.com/arvicxyz/coding-challenge-app/blob/master/screenshots/new7.jpg" alt="new7" width="200"/> <img src="https://github.com/arvicxyz/coding-challenge-app/blob/master/screenshots/new8.jpg" alt="new8" width="200"/>
