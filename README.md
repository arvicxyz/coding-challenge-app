# Introduction
This repository contains a coding challenge project. It's an Android mobile application called Notflix. Notflix is a movie and music app, but it doesn't let you watch movies nor let you listen to music. It's just an app that displays a list of items obtained from iTunes Search API and shows a detailed view for each item. The project applies MVVM architecture and its UI applies a bit of material design with dark mode color pallete implementation.

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

## Architecture
In the app, I used the MVVM or Model-View-ViewModel architecture which Google strongly advises for developers to use. I also used Service and Repository pattern for data processing and data accessing respectively. I also used Client Service, a type of Service which I use to fetch data from a server. I used Google's Android Jetpack Architecture components to make everything working smoothly in the app like the Lifecycles, LiveData, ViewModel and Room components.

### Model
The *models* folder in the app contains the constants, enums, interfaces and the models. Basically anything that concerns a data that is used within the app is located here. It's a collection of enums, interfaces and classes with fields, getters & setters or sometimes just constant values. The model in the models folder is where the data that we are going to work with is modelled from.

### View
The *views* folder in the app contains the adapters, fragments, activities, custom views as well as observers. I usually create apps with single activity multiple fragments composition usually just a MainActivity launcher and a bunch of fragments with it. But due to some warnings I'm getting in the manifest to provide at least one activity with ACTION-VIEW intent filter, I now provide two activities. Because the downside if I don't do that then the apps I will make will not be indexable by Google Search which will drive less traffic that means less downloads resulting into low profit. And no one wants that.

### ViewModel
The *viewmodels* folder is the view's model. This is the interface between the model and the view of the app. We can bind this to the view itself so that we can instantly update the view when something changed in our view model. I wasn't able to apply data binding in the project but I did use LiveData to communicate with the view and view model for live updates of the views when something changed in view model's properties.

### Service Pattern
> A service supplies coordination or other "services" that are required to operate your application. They are very different in that services don't typically know how to access data from persistence, and repositories typically only access data/objects for any services you may have.
[Source](https://stackoverflow.com/questions/1440096/difference-between-repository-and-service?rq=1)

Basically, in the app everything that processes or modifies the data tied to a model is a service. You input a parameter base on your models then the service processes it then outputs it to a data or information you can use within the app. Service classes can also contain any methods that aids the model it is tied to. For example, it could be error checking which I usually put the logic in the services instead of putting it inside the model itself, but still it depends on the case scenario. By the way, when I say 'tied to', it means each model class I create then there is a corresponding service class to it. Although not all services are tied to a model.

#### Client Service
A client service or server client service in long term, is a class I use to fetch data from a server. Any online transaction that concerns get, post or others is handled by this component. This component is usually different from the service, but within the app I put the api folder, contains client service classes, inside the services folder.

### Repository Pattern
> A repository is essentially a facade for persistence that uses collection style semantics (add, update, remove) to supply access to data/objects. It is a way of decoupling the way you store data/objects from the rest of the application.
[Source](https://stackoverflow.com/questions/1440096/difference-between-repository-and-service?rq=1)

What this does is pretty much access data locally. This contains the room databases, shared preferences and caching or pretty much all persistence types should be found here. So the distinction between Service, Client Service and Repository is clear with each one having different roles within the architecture of the project. Client Service handles data accessing online while Repository on the other hand handles data accessing offline or locally and the Service will see aid in handling or processing these data.

#### Room Database
The room database in the app only contains one entity with a corresponding data access object class. To differentiate, an entity is different from a model that's why we put it here. An entity is associated with a stored data while a model is associated with the app. Entities have attributes pretty much like in a database table while models have properties. The *kind of data that I persist* in the app includes the given ones (Track Name:String, Artwork Url:String, Price:float, Genre:String and Long Description:String) with the addition of 'trackId' of type 'int' which I used as 'id' in the entity. This is because, I used it to differentiate the items and use it to determine the last screen the user is on. But there are problems in saving the last screen which I will discuss later on.

#### Shared Preference
The shared preference is another persistence that I used to store single type data. Compared to the room database which stores a large quantity of data presented in a table, the shared preference is tasked to store single data with common data type. This is what I used to store the data, 'a date when user previously visited' and the 'last screen that the user was on' of type String and int respectively. The date was stored as string type and will be shown in the header on app open. The last screen the user was on was stored as an id of type integer. Each detail page has a unique ID provided by the API in a json data structure, the home page is determined by the id -1 while the detail pages has its own id. The problem is there are 'tracks', the term I use to identify the objects provided by the API, that has no trackId which in return results into the default 0 and it presents complications and problems along the way so I have to ignore the tracks having and id of 0 to solve the issue. The API in the first place shouldn't have empty track IDs. I could have decided to not display the items with no track IDs but I still did to show all data in the list. It will not just be presented on app open when it is the last screen the user was on.

#### Caching
The caching is also used in the app to provide data stored in the RAM. As you can see, the last two persistence methods I've discussed deals with storing data in the storage locally, while the caching helps in the performance of the app as it stores the data in the RAM so that there is no need to fetch the data everytime from the online source or local storage. When you open the app, you will be presented with a splas screen (SplashActivity) which is the second activity I provided that I talked about earlier in the Views section. Anyways, during the splash screen, the app checks if it is online or offline. If it is online, it will call on the Client Service to fetch data from server and save it locally to the database and also put it in a static variable and cache it. If it is offline, then it will fetch data from the database and cache it as well, but if it doesn't have any data then it will present an error dialog that the user is has no internet connection. Pretty much sums up the caching method, but all of it runs in a LiveData so that whenever the ClientService data or the Room Database data or the Cached data changes then it will just reflect to the UI. Also there is no need to cache shared pref data as it is small enough to be called everytime.

### Util
The *utils* folder is a collection of helper classes that helps you process data. The services folder has similarity with the utils folder, but the distinction is that, a service class can be tied to a model while a util class is a utility or a helper class that helps in the overall application. The util classes basically fills the roles that the other components doesn't do.

## User Interface
Welcome to the presentation layer, visual part and the part that took the most time in the completion of this coding challenge. (Sorry about that:D). I would say I applied material design through margins, paddings, fonts but overall the paper like material layout design was not followed in the later part of the UI development. At first I wanted the app to just be plain and simple with white backgrounds and surfaces everywhere (the white app screenshots were not taken unfortunately), but then I realized it was boring. So I wanted to learn the dark theme mode material design guidelines at the same time complete the challenge so I leaned it and applied it. Well, just the color sheme part of it. :) It was looking okay now, but still I wanted to apply something new so I thought of implementing this UI (See Guide section below) from the Netflix app, but only the middle part of the screenshot for the details page. While the details page was derived from Netflix, the list page (home page/master page) was derived from Spotify with the rounded corners (See Guide section below) in the first iteration and then later on I decided to remove the rounded corners. To cut the long story short, I can't decide on the look of the app and took it a long time and then named the app Notflix because it is Not Netflix. Yeah it's so corny. LOL!

### Guides
<img src="https://github.com/arvicxyz/coding-challenge-app/blob/master/screenshots/guide.jpg" alt="master-page-guide" width="200"/><img src="https://www.techybugz.com/wp-content/uploads/2018/12/Netflix-Download-2.jpg" alt="detail-page-guide" height="400"/>

### Older Screenshots
<img src="https://github.com/arvicxyz/coding-challenge-app/blob/master/screenshots/old1.jpg" alt="old1" width="200"/><img src="https://github.com/arvicxyz/coding-challenge-app/blob/master/screenshots/old2.jpg" alt="old2" width="200"/>

### Old Screenshots
<img src="https://github.com/arvicxyz/coding-challenge-app/blob/master/screenshots/old3.jpg" alt="old3" width="200"/><img src="https://github.com/arvicxyz/coding-challenge-app/blob/master/screenshots/old4.jpg" alt="old4" width="200"/><img src="https://github.com/arvicxyz/coding-challenge-app/blob/master/screenshots/old5.jpg" alt="old5" width="200"/><img src="https://github.com/arvicxyz/coding-challenge-app/blob/master/screenshots/old6.jpg" alt="old6" width="200"/><img src="https://github.com/arvicxyz/coding-challenge-app/blob/master/screenshots/old7.jpg" alt="old7" width="200"/><img src="https://github.com/arvicxyz/coding-challenge-app/blob/master/screenshots/old8.jpg" alt="old8" width="200"/><img src="https://github.com/arvicxyz/coding-challenge-app/blob/master/screenshots/old9.jpg" alt="old9" width="200"/>

### New Screenshots
<img src="https://github.com/arvicxyz/coding-challenge-app/blob/master/screenshots/new1.jpg" alt="new1" width="200"/><img src="https://github.com/arvicxyz/coding-challenge-app/blob/master/screenshots/new2.jpg" alt="new2" width="200"/><img src="https://github.com/arvicxyz/coding-challenge-app/blob/master/screenshots/new3.jpg" alt="new3" width="200"/><img src="https://github.com/arvicxyz/coding-challenge-app/blob/master/screenshots/new4.jpg" alt="new4" width="200"/><img src="https://github.com/arvicxyz/coding-challenge-app/blob/master/screenshots/new5.jpg" alt="new5" width="200"/><img src="https://github.com/arvicxyz/coding-challenge-app/blob/master/screenshots/new6.jpg" alt="new6" width="200"/><img src="https://github.com/arvicxyz/coding-challenge-app/blob/master/screenshots/new7.jpg" alt="new7" width="200"/><img src="https://github.com/arvicxyz/coding-challenge-app/blob/master/screenshots/new8.jpg" alt="new8" width="200"/>
