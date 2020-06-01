# DemoNoteApp

## Request
Creating an android application by using Kotlin language. App should save user data locally and connect to MySQL web database via using XAMPP server. Application should be able to synchronize data with web database by calling PHP script. 

# 1	Assumptions
ID | Description
-- | --
ASM-001 | App rotation will not be supported in first release.
ASM-002 | Supporting tablet devices and creating multiple layout files will   not be added.
ASM-003 | App will be able to communicate with a database via API
ASM-004 | Users will be provided with a web user interface to use the app in   web base too



# 2	Requirements

ID | Description
-- | --
ASM-001 | App rotation will not be   supported in first release.
ASM-002 | Supporting tablet devices   and creating multiple layout files will not be added.
ASM-003 | App will be able to   communicate with a database via API
ASM-004 | Users will be provided   with a web user interface to use the app in web base too



# 3	Solution

## 3.1	DemoNoteApp
Within Demo Requirement, a new native android application will be created. The main purpose of this demo application is create a sample android app which can have local database to store data internally and can synchronize data to web stored database.

Application will be created in GitHub with 3 main modules which are;
1. Android
2. Web
3. Design

Android module will contain all native android codes, in-app database, application architecture patterns etc.
Similarly, Web module will contain web based development codes, database creation scripts, connection URLs, and API scripts which will be written in PHP language.
And finally, Design module will have web and android pre-screenshots, app navigation flow, UX and UI related drawings, etc.

### 3.1.1	Android Module
A sample app will be created from scratch. Application package name will be “com.enbcreative.demonoteapp”. App will have splash screen which should contain ENB logo for a few seconds then Login Activity will be displayed which should have below characteristics;
 
<img src="https://github.com/ercanduman/DemoNoteApp/blob/master/Docs/images/Example%20Login%20screen.jpg?raw=true" width="300" title="Figure 1: Example Login screen">

User should be presented with login screen as shown above which has two editing fields for Email and Password.
However, “Sign Up” option should be provided for unregistered users as well. Each option should have its own user interface (UI) as shown in below figure.
 
<img src="https://github.com/ercanduman/DemoNoteApp/blob/master/Docs/images/Example%20Sign%20Up%20screen.jpg?raw=true" width="300" title="Figure 2: Example Sign Up screen">

After entering user’s credentials to login screen a progress bar should be displayed to indicate network connection. 
When network connection is successful, a toast message should be displayed to user with content of “Login Success” and a new activity should be started (MainActivity). 
 
<img src="https://github.com/ercanduman/DemoNoteApp/blob/master/Docs/images/Example%20Validation%20of%20Email%20or%20Password.jpg?raw=true" width="300" title="Figure 3: Example Validation of Email or Password">


Main Activity is the main UI which all user interactions will be handled such as Listing all notes, adding / editing existing one or deleting a note.
A rounded add button (+) will be available on main screen for adding new notes. After clicking add button a dialog will be displayed to user which may looks as below. 
 
<img src="https://github.com/ercanduman/DemoNoteApp/blob/master/Docs/images/Example%20Adding%20Note.png?raw=true" width="300" title="Figure 4: Example Adding Note">


Note object will be stored in database with id, content and date columns. All notes in main activity will be listed based on id by descending order.
“Swipe to delete” option will be provided so user can drag the item to LEFT for deleting.
 
<img src="https://github.com/ercanduman/DemoNoteApp/blob/master/Docs/images/Swipe%20to%20Delete%20Feature.png?raw=true" width="300" title="Figure 5: Swipe to Delete Feature">

Room library will be used for storing Notes locally. App will synchronize all data with web in background if network available. Thanks to this feature user will have smooth interactions with app.

#### 3.1.1.1	JSON Format
Since android app connects to web API and gets a response for each request, API response should be in JSON format and have different content for each response Success and Failure responses.
#####  3.1.1.1.1	JSON format - Success Login Response:
```
{
  "error": false,
  "message": "Login successful.",
  "user": {
    "id": 2,
    "username": "test user",
    "email": "test@mail.com",
    "created_at": "2020-05-27",
    "updated_at": "2020-05-27",
    "gender": "male"
  }
}

```
##### 3.1.1.1.2	JSON format - Failure Login Response:
```
{
  "error": true,
  "message": "Invalid email or password."
}
```

##### 3.1.1.1.3	JSON format - Success Notes Response:
```
{
  "error": false,
  "message": "Execution successful.",
  "notes": [
    {
      "id": 1,
      "userId": 1,
      "content": "Test note for user id 1",
      "created_at": "2020-05-29 15:08:34",
      "updated_at": "2020-05-29 00:00:00"
    },
    {
      "id": 2,
      "userId": 1,
      "content": "Test note content for user id 1",
      "created_at": "2020-05-29 15:08:34",
      "updated_at": "2020-05-29 00:00:00"
    }
  ]
}

```

##### 3.1.1.1.4	JSON format - Failure Notes Response:
```
{
  "error": true,
  "message": "User has no any notes yet."
}
```


### 3.1.2	Web Module
A free web hosting service should be used to host the web based database. The database is going to be prepared using relational database like MySQL.
Web and mobile app connection is going to be done by using PHP scripts. It is going to be secured via username and password only.

### 3.1.3	Design Module
UX Design.
Before starting the design, we need to focus on the story of using the product.
Who is going to use the product and how he or she is going to use?
According to our product (NoteApp), one of the stories can be like below.
User is going to;
Create an account → add a note → edit the note → copy and paste the note → delete the note → to encrypt the note(optional) 

If you guys want to add something to above story, go-ahead!

UI design,
I prefer to start to design from the application!

-	Journey Map
A journey map is going to help us to see what kind of the screen we need to have (Login, created a note, share, etc.)

I will be discussing it with you when I design the journey map.

-	Prototype
After agreeing with the journey map, I'm going to design a prototype. It's going to help us to see the design feeling of the application.

After agreeing with the prototype, I will continue to work with Ercan on the app part. in the meantime, I will start the process for the web part with Sinan.


3.2	Error Handling
3.2.1	Android: 
User will be obligated to enter password and username to authenticate application.
No network exceptions will be handled for already singed in users. Local data will be displayed while web content will be downloading in background during user interactions.



### 3.3	Usage
Android app calls PHP script (api.php) to perform basic CRUD (Create, Read, Update, Delete) operations. PHP script checks the POST request parameters and connects to MySQL database based on the request.
Web based installation can be used via XAMPP web server for local usages.
To install PHP script and MYSQL database successfully, please follow these steps;
1-	Go to DemoNoteApp\Android\DemoNoteApp\ directory and copy “android_demo” folder and paste it under C:\xampp\htdocs\ directory on your local system.
2-	In web browser open http://localhost/phpmyadmin/ link 
3-	Go to Import section as shown below.	 

<img src="https://github.com/ercanduman/DemoNoteApp/blob/master/Docs/images/XAMPP%20Import%20DB%20script.png?raw=true" width="300" title="Figure 6: Import DB script">

4-	Select “firstdb.sql” script which is available under “android_demo” directory.

To check if PHP script installed successfully, open “http://localhost/android_demo/api.php” link.
 
<img src="https://github.com/ercanduman/DemoNoteApp/blob/master/Docs/images/Test%20PHP%20script%20installation.png?raw=true" width="300" title="Figure 7:  Test PHP script installation">

If Postman is already available on your computer, then you can try Login to test API with below credentials:

Request type	: POST
URL		: http://localhost/android_demo/api.php?enbapicall=login
Email		: test@mail.com
Password	: 123456
 

<img src="https://github.com/ercanduman/DemoNoteApp/blob/master/Docs/images/Testing%20API%20for%20Login%20via%20Postman.png?raw=true" width="300" title="Figure 8: Testing API for Login via Postman">

For more details about solution and **Usage**, please check [Solution Document here](https://github.com/ercanduman/DemoNoteApp/blob/master/Docs/SRS/SRS_ENB_CREATIVE_CR001_DEMO_NOTE_APP_v1.0.0.doc)

#### Helpful links:

- [x] [MVVM Architecture Components](https://codelabs.developers.google.com/codelabs/android-room-with-a-view/#0)
- [x] [Coroutines](https://kotlinlang.org/docs/reference/coroutines/basics.html)
- [x] [Retrofit](https://square.github.io/retrofit/)
- [x] [Room + ViewModel + LiveData + RecyclerView (MVVM)](https://codinginflow.com/tutorials/android/room-viewmodel-livedata-recyclerview-mvvm/part-1-introduction)

Happy coding! :+1: :1st_place_medal:


[![license](https://img.shields.io/github/license/DAVFoundation/captain-n3m0.svg?style=flat-square)](https://github.com/ercanduman/DemoNoteApp/blob/master/LICENSE.md)

DemoNoteApp, Copyright (C) 2020, ENB
