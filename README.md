# 📅 Event Tracking App

An Android application built as part of **CS-360: Mobile Architecture and Programming**.  
This app allows users to **register, log in, create, edit, and delete events**, with optional SMS reminders.

---

## 🚀 Features

- 👤 **User Authentication**: Register & log in securely  
- 📝 **Event Management**: Add, edit, and delete events  
- ⏰ **Alerts**: Set reminders before events  
- 💬 **SMS Notifications**: Option to send reminders via SMS  
- 🎨 **Modern UI**: Clean design with Material components  

---

## 📂 Project Structure

```
app/
 ┣ java/com/example/eventtrackingapp/
 ┃ ┣ AddEditEventActivity.java
 ┃ ┣ DBHelper.java
 ┃ ┣ Event.java
 ┃ ┣ EventAdapter.java
 ┃ ┣ LoginActivity.java
 ┃ ┣ MainActivity.java
 ┃ ┣ RegisterActivity.java
 ┣ res/layout/
 ┃ ┣ activity_add_edit_event.xml
 ┃ ┣ activity_event_list.xml
 ┃ ┣ activity_login.xml
 ┃ ┣ activity_register.xml
 ┃ ┣ grid_item_event.xml
 ┃ ┣ grid_item_layout.xml
 ┃ ┣ activity_settings.xml
 ┣ AndroidManifest.xml
```

---

## 🛠️ Tech Stack

- **Language**: Java ☕  
- **Database**: SQLite 📦  
- **UI**: Android XML layouts 🎨  
- **Libraries**:  
  - RecyclerView  
  - CardView  
  - Material Design Components  

---

## 📸 Screenshots

| Login Screen | Event List | Add Event |
|--------------|------------|-----------|
| ![Login](https://via.placeholder.com/200x400.png?text=Logi) | ![Events](https://via.placeholder.com/200x400.png?text=Event+List) | ![Add](https://via.placeholder.com/200x400.png?text=Add+Event) |

---

## ⚙️ Installation

1. Clone this repository:
   ```bash
   git clone https://github.com/yourusername/event-tracking-app.git
   ```
2. Open the project in **Android Studio**  
3. Sync Gradle and build the project  
4. Run on emulator or physical device  

---

## 🚧 Future Improvements

- 🔔 Push notifications instead of SMS  
- 📍 Location-based event triggers  
- ☁️ Cloud backup & sync  

---
# 📱 Project Three Reflection

## ✨ What I Learned
One thing I have learned from my experience with Project Three is how important it is to carefully align the database structure with the app’s logic. At first, my app was crashing because the database columns didn’t match the event fields. Debugging and fixing this gave me a better understanding of how to maintain consistency across activities, adapters, and database helpers. I also learned how crucial testing is at each step of the development cycle—especially when adding new features like delete functionality or SMS permissions.

## ❓ One Question I Still Have
One question I still have about developing and launching a mobile app is:  
👉 How do professional developers handle scaling an app once it moves from prototype or classroom project to thousands of users on the Google Play Store?  

This question relates to performance, security, and database management—things I want to get better at as I continue to learn.

## 🚀 Applying Learning in the Future
One way I will apply my learning from Project Three in the future is by **building apps iteratively**. I now understand that starting with a simple version, testing, and then adding features step by step leads to a more stable and maintainable app. I will also continue to use version control (like GitHub) to track changes, share my work, and document my learning.

## 📈 Looking Ahead
This project taught me the value of user-centered design, secure coding practices, and clear documentation. I plan to continue improving my mobile development skills and eventually publish my apps to the Google Play Store as part of my professional portfolio. Doing so will give me concrete examples of real-world projects to show to employers and demonstrate my ability to complete a full software development lifecycle.


## 🧑‍💻 Author

- **Ryan Peguero**   
- 🌐 [GitHub Profile](https://github.com/rypeguero)

---
