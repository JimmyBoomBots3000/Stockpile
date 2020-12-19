# Stockpile
A simple Android application for inventory.

# Goals
Stockpile is designed to be simple and intuitive, with as shallow a learning curve as possible.  It aims to be serviceable in a professional warehosue, but be 
simple enough for tracking household items.  This application simply tracks how many of a given item are on hand and where they are.
It requires Android API level 22 (5.1, Lollipop) or above.

# Features
Stockpile employs a many-to-many relationship between items and locations, enabling users to store and track items across multiple locations and/or store multiple items per loctation.
Interfaces make extensive use of iconography, making it intuitive and easily localizable.

# Strategies
This application uses the common MVC architecture.  Classes provide structure for database items.  Combined with an implementation of the Room database library, these make up the model.  The view exists as the layouts, and the controller as their many respective activities.  These structures can not only be adapted from this to other projects, but were in fact adapted from other projects for this one.  

# Testing 
Testing has been performed almost exclusively as use case testing, but unit tests are planned for future development using JUnit.

# Challenges
The database was an important design consderation, but was challenging to implement because of its structure.  This still may not be the final design, and ideally I'd like to add tracking for transactional history, possibly as a premium feature, which would require more database tables, and a consderable expansion to all aspects of the current MVC.

# Successes
Intent and extras handling has been arguably the most effectively I've employed new knowledge in this project.  Otherwise, previous experience has been applied toward creating Java code that is modular, readable, clear in purpose, and concisely annotated to guide contributing developers.
