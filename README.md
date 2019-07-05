# Introduction
This repository contains a coding challenge project. It's an Android mobile application called Notflix. Notflix is a movie and music app but it doesn't let you watch movie nor let you listen to music. It's just an app that displays a list of items obtained from iTunes Search API and shows a detailed view for each item. The project applies MVVM architecture and its UI applies a bit of material design with dark mode color pallete implementation.

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
- A date when user previously visited, shown in the list header
- Save the last screen that the user was on. When the app restores, it should present the user with that screen.

You can save other data. Add any supporting information in the same manner as above.

## Architecture
In the app, I used the MVVM or Model-View-ViewModel architecture which Google strongly advises for developers to use. I also used Service and Repository pattern for data processing and data accessing respectively. I also used Client Service, a type of Service which I use to fetch data from a server. I used Google's Android Jetpack Architecture components to make everything working smoothly in the app like the Lifecycles, LiveData, ViewModel and Room components.

### Model
The *models* folder in the app contains the constants, enums, interfaces and the model. Basically anything that concerns a data that is used within the app is located here. It's a collection of enums, interfaces and classes with fields and getters & setters or sometimes just constant values.

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
> I would say that a Repository is a type of service that is used for data access. – Ian Ringrose Sep 17 '09 at 18:57
[Source](https://stackoverflow.com/questions/1440096/difference-between-repository-and-service?rq=1)

What this does is pretty much access data locally. This contains the room databases, shared preferences and caching or pretty much all persistence types should be found here. So the distinction between Service, Client Service and Repository is clear with each one having different roles within the architecture of the project. Client Service handles data accessing online while Repository on the other hand handles data accessing offline or locally and the Service will see aid in handling or processing these data.

#### Room Database
The room database in the app only contains one entity with a corresponding data access object class. To differentiate, an entity is different from a model that's why we put it here. An entity is associated with a stored data while a model is associated with the app. Entities have attributes pretty much like in a database table while models have properties. The *kind of data that I persist* in the app includes the given ones (Track Name:String, Artwork Url:String, Price:float, Genre:String and Long Description:String) with the addition of 'trackId' of type 'int' which I used as 'id' in the entity. This is because, I used it to differentiate the items and use it to determine the last screen the user is on. But there are problems in saving the last screen which I will discuss later on.

#### Shared Preference
The shared preference is another persistence that I used to store single type data. Compared to the room database which stores a large quantity of data presented in a table, the shared preference is tasked to store single data with common data type. This is what I used to store the data, 'a date when user previously visited' and the 'last screen that the user was on' of type String and int respectively. The date was stored as string type and will be shown in the header on app open. The last screen the user was on was stored as an id of type integer. Each detail page has a unique ID provided by the API in a json data structure, the home page is determined by the id -1 while the detail pages has its own id. The problem is there are 'tracks', the term I use to identify the objects provided by the API, that has no trackId which in return results into the default 0 and it presents complications and problems along the way so I have to ignore the tracks having and id of 0 to solve the issue. The API in the first place shouldn't have empty track IDs. I could have decided to not display the items with no track IDs but I still did to show all data in the list. It will not just be presented on app open when it is the last screen the user was on.

#### Caching

#### Util
The utils folder is a collection of helper classes that helps you process data. The services folder has similarity with the utils folder, but the distinction is that, a service class can be tied to a model while a util class is a utility or a helper class that helps in the overall application. It fills the roles that the other components doesn't do.

## User Interface
