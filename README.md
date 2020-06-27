# PriceQuote

## Description
This project was created with the aim of integrating various architectural components and best practice Android programming skill in the same application.
The application allows you to create and save locally restoration / revaluation quotes for a property. it is mainly composed of two screens:
* the main screen shows the list of quotes already made in a **RecycleView**, by selecting an item from the list it is possible to edit the selected object. Finally a **FloatingButton** positioned in the bottom right part of the screen allows you to create a new quote.
* the second screen allows you to fill in the quote. it is made up of 4 tabs in **TabbedViewPager** that guide you to fill in the quote.

## Screenshot

Here are some screenshots of the application:

![Homepage](https://github.com/GiovanniGianola/PriceQuote/blob/master/screenshot/HomePage.jpg)
![InfoFragment](https://github.com/GiovanniGianola/PriceQuote/blob/master/screenshot/InfoFrag.jpg)
![CategoryFragment](https://github.com/GiovanniGianola/PriceQuote/blob/master/screenshot/CategoryFrag.jpg)
![CustomOptionsFragment](https://github.com/GiovanniGianola/PriceQuote/blob/master/screenshot/CustomOptionsFrag.jpg?raw=true)

## Built with
The application is entirely written in **Kotlin 1.3.72** with **Android Studio 4.0**, developed following the guidelines imposed by the architectural model software **Model-View-ViewModel** ([MVVM](https://en.wikipedia.org/wiki/Model%E2%80%93view%E2%80%93viewmodel)). main libraries used:
* [Room](https://developer.android.com/jetpack/androidx/releases/room) - the quotes made through the applications can be saved in a local database.
* [Navigation](https://developer.android.com/jetpack/androidx/releases/navigation) - a framework for navigating between 'destinations' within an Android application implemented as Fragments or Activities.
* [Recycle View](https://developer.android.com/jetpack/androidx/releases/recyclerview) - Android widget to efficiently show items in list format.
* [Lifecycle](https://developer.android.com/jetpack/androidx/releases/lifecycle) - perform actions in response to a change in the lifecycle status of another component, such as activities and fragments.
* [Coroutines](https://kotlinlang.org/docs/reference/coroutines-overview.html) - Asynchronous or non-blocking programming for database access exploiting Repository design pattern.
* [Moshi](https://github.com/square/moshi) - JSON library for Android to read and interpret configuration files located in the Asset folder.

## Authors

* [Giovanni Gianola]

[Giovanni Gianola]: <https://github.com/GiovanniGianola>
