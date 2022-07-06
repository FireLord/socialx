# SocialX

A [NewsApi.org](https://newsapi.org/) based app. This app uses api call from newsapi site. The data comes in the json format. The app is based on latest MVVM arch. The app uses single activity to load all the fragments in that viewHolder. This method helps us to save memory on android. The app has search functionality too.


## Tech stack

The app went through multiple iterations to achieve what it is today, in coming days Ill move to material Design 3 and use jetpack compose.

The libraries used:

- [AppCompat](https://developer.android.com/jetpack/androidx/releases/appcompat) 
- [Fragments](https://developer.android.com/jetpack/androidx/releases/fragment) 
- [Material Components](https://material.io/develop/android) 
- [ViewPager2](https://developer.android.com/jetpack/androidx/releases/viewpager2) for UI
- [Retrofit](https://github.com/square/retrofit) for getting data from API
- [Room](https://developer.android.com/jetpack/androidx/releases/room) to store data in database
- [RecyclerView](https://developer.android.com/jetpack/androidx/releases/recyclerview) show news in list
- [CardView](https://developer.android.com/reference/androidx/cardview/widget/CardView) to show each news
- [TabLayout](https://developer.android.com/reference/com/google/android/material/tabs/TabLayout)
- [Firebase](https://firebase.google.com/) for login to account from email, google, facebook
- [Glide](https://bumptech.github.io/glide/doc/download-setup.html) for image from json
- [DiffUtil](https://developer.android.com/reference/androidx/recyclerview/widget/DiffUtil)
- [AndroidX Navigation](https://developer.android.com/guide/navigation) for handling navigation events

In order, this is what happens in the app:

1. Load raw resources and parse as JSON
2. Log into the account from email, google or fb
3. Expose as a sorted list through a RecyclerView
4. By pressing 3 dots on the side of search we get logout option
5. ViewModel handle all the device configuration changes

# Unit testing
Unit testing is not performed, as of now I lack experience in that. Will be done in upcoming days.

# License

```
Copyright (c) 2022 Aman Kumar

Permission is hereby granted, free of charge, to any
person obtaining a copy of this software and associated
documentation files (the "Software"), to deal in the
Software without restriction, including without
limitation the rights to use, copy, modify, merge,
publish, distribute, sublicense, and/or sell copies of
the Software, and to permit persons to whom the Software
is furnished to do so, subject to the following
conditions:

The above copyright notice and this permission notice
shall be included in all copies or substantial portions
of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF
ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED
TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT
SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR
IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
DEALINGS IN THE SOFTWARE.
```