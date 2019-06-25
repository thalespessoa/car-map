# car-map

Android application that retrieves a list of cars from server and displays it on a list and on a map.

# Technical considerations
Native android app developed in kotlin 
Min SDK 19
It supports both device orientations (portrait and landscape)
It supports tablets and phone, with different layouts for each one. Split screen for tablets/phone-landscape and separated screens to phone-portrait

# Libraries
Android Architecture Components (LiveData, ViewModel and Data Binding)
Kotlin Coroutines
Google maps
Dagger 2
Retrofit 2
Picasso

# Approach
The app is using a MVVM architecture with 3 layers (Model, View and ViewModel).

# # Model Layer
The main class of model layer is DataRepository. It is an abstraction of all data used by the app. It uses the class NetworkApi to fetch the data from server and provide this data for the rest of the app.
DataRepository is injected by dagger in the ViewModel layer

# # ViewModel Layer
The main class of ViewModel layer is CarsViewModel. This class is responsible for fetching the data from the model layer and generate a view state object (CarsViewState) to be observed as a Livedata from the View Layer. 
The view state generated is unique and immutable. Each action, either from user or from app, generates a new view state object.
CarsViewModel is provided by ViewModelProviders using ViewModelFactory

# # View Layer
This layer is represented by the screens (Activities and Fragments). This layer is responsible for observe the CarsViewState in the CarsViewModel and build the screen according to this view state. Each view always observe only one view state object.
